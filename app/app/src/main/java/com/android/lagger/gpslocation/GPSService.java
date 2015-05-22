package com.android.lagger.gpslocation;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.android.lagger.model.navigation.SendingPosition;
import com.android.lagger.serverConnection.GsonHelper;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.serverConnection.URL;
import com.android.lagger.settings.State;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class GPSService extends Service implements LocationListener {


    private static int MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private static int MIN_TIME_BW_UPDATES = 1000 * 20 * 1;
    private final Context context;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    private LatLng coordinates;
    private ArrayList<LatLng> coordinatesList;

    public GPSService(Context context) {
        this.context = context;
        getLocation();
        coordinatesList = new ArrayList();
        if (coordinates != null && coordinates.latitude != 0 && coordinates.longitude != 0) {
            sendLocation();
        }
    }

    public static int getMinDistanceChangeForUpdates() {
        return MIN_DISTANCE_CHANGE_FOR_UPDATES;
    }

    public static void setMinDistanceChangeForUpdates(int minDistanceChangeForUpdates) {
        MIN_DISTANCE_CHANGE_FOR_UPDATES = minDistanceChangeForUpdates;
    }

    public static int getMinTimeBwUpdates() {
        return MIN_TIME_BW_UPDATES / 1000;
    }

    public static void setMinTimeBwUpdates(int minTimeBwUpdates) {
        MIN_TIME_BW_UPDATES = minTimeBwUpdates * 1000;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {

                            coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                            coordinatesList.add(coordinates);
                        }
                    }

                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                                coordinatesList.add(coordinates);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSService.this);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {

        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("Ustawienia GPS");

        alertDialog.setMessage("GPS jest wyłączony. Czy chcesz go włączyć?");

        alertDialog.setPositiveButton("Ustawienia", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location arg0) {
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            coordinates = new LatLng(location.getLatitude(), location.getLongitude());
        }
        if (coordinates != null && coordinates.latitude != 0 && coordinates.longitude != 0) {
            sendLocation();
        }
//        Toast.makeText(
//                context,
//                "Twoja pozycja -\nX: " + coordinates.latitude + "\nY: "
//                        + coordinates.longitude + "liczba " + coordinatesList.size(), Toast.LENGTH_LONG).show();

    }

    public void sendLocation() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                Gson gson = new GsonHelper().getGson();
                SendingPosition sendingPosition = new SendingPosition(State.getLoggedUserId(), new Date(), 2, coordinates.latitude, coordinates.longitude);
                String userString = gson.toJson(sendingPosition);
                JsonParser parser = new JsonParser();
                JsonObject userJson = (JsonObject) parser.parse(userString);
                //JsonObject userJson = createGPSJSON(userId,meetingId);
                //TODO refactoring
                return HttpRequest.POST(URL.ADD_POSITION, userJson);
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {

                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    @Override
    public void onProviderDisabled(String arg0) {
        Toast.makeText(context, "Wyłączono GPS",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String arg0) {
        getLocation();
        Toast.makeText(context, "Włączono GPS",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public JsonObject createGPSJSON(String userId, String meetingId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idUser", userId);
        String x = "\\/Date(928142400000+0200)\\/";
        String y = x.substring(0, x.length() - 1);
        y = y.concat(x.substring(x.length() - 1));


        jsonObject.addProperty("dateTime", y);
        jsonObject.addProperty("idMeeting", meetingId);
        Double latVal = new BigDecimal(this.latitude).setScale(7, BigDecimal.ROUND_HALF_UP).doubleValue();
        Double lngVal = new BigDecimal(this.longitude).setScale(7, BigDecimal.ROUND_HALF_UP).doubleValue();
        jsonObject.addProperty("latitude", String.valueOf(latVal));
        jsonObject.addProperty("longitude", String.valueOf(lngVal));
        return jsonObject;
    }
}
