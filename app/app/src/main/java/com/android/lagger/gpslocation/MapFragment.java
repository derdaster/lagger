package com.android.lagger.gpslocation;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.lagger.R;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.model.navigation.Position;
import com.android.lagger.serverConnection.GsonHelper;
import com.android.lagger.serverConnection.HttpRequest;
import com.android.lagger.settings.State;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

    public static HashMap<Integer, Float> markerColors;
    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;
    private Meeting meeting;
    private ProgressDialog pDialog;
    private List<LatLng> polyz;
    private MapView mMapView;
    private GoogleMap googleMap;
    private Context mContext;
    private List<GPSUser> gpsUsers;
    private LatLng chosenPositon;

    public MapFragment() {
        this.meeting = new Meeting();
    }

    public MapFragment(Meeting meeting) {
        this.meeting = meeting;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initalizeMarkerColors();


        gpsUsers = new ArrayList();

        View v = inflater.inflate(R.layout.fragment_test_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        CameraPosition cameraPosition;
        GPSService gpsService = new GPSService(this.getActivity().getApplicationContext(), meeting.getId());
        if (gpsService.canGetLocation()) {
            gpsService.getLatitude();

            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(gpsService.getLatitude(), gpsService.getLongitude())).zoom(12).build();

        } else {
            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(State.DEFAULT_LATITUDE, State.DEFAULT_LONGITUDE)).zoom(12).build();
        }
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        startTimer();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /* Method to decode polyline points */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public void showUserMarkers() {
        int i = 1;
        for (GPSUser user : gpsUsers) {
            showNamedMarker(user.getActualPositition(), String.valueOf(user.getIdUser() + " za " + user.getArrivalTime() + " min."), i);

            if (!user.getPositionList().isEmpty())
                drawUserPath(user);
            i++;
            if (i > 9)
                i = 1;
        }
        showNamedMarker(new LatLng(meeting.getLatitude(), meeting.getLongitude()), "Spotkanie : " + meeting.getName(), 0);
    }

    public void showMarker(LatLng latLng) {
        MarkerOptions marker = new MarkerOptions().position(
                latLng).title("Position");

        drawMarker(marker, 0);
    }

    public void showNamedMarker(LatLng latLng, String name, int colorNumber) {
        MarkerOptions marker = new MarkerOptions().position(latLng).title(name);

        drawMarker(marker, colorNumber);
    }

    public void drawMarker(MarkerOptions marker, int colorNumber) {

        marker.icon(BitmapDescriptorFactory
                .defaultMarker(MarkerColors.getMarkerColor(colorNumber)));

        if (colorNumber != 0)
            googleMap.addMarker(marker);
        else
            googleMap.addMarker(marker).showInfoWindow();

    }

    public void drawUserPath(GPSUser user) {
        List tempList = user.getPositionList();
        for (int i = 0; i < user.getPositionList().size() - 1; i++) {
            googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(((Position) tempList.get(i)).getLatitude(), ((Position) tempList.get(i)).getLongitude()), new LatLng(((Position) tempList.get(i + 1)).getLatitude(), ((Position) tempList.get(i + 1)).getLongitude()))
                    .width(5)
                    .color(Color.BLACK));
        }

    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        //initialize the TimerTask's job
        initializeTimerTask();
        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 10000); //
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        //get the current timeStamp
                        new AsyncTask<String, Void, String>() {
                            @Override
                            protected String doInBackground(String... urls) {
                                String userIdString = String.valueOf(State.getLoggedUserId());
                                String meetingIdString = String.valueOf(meeting.getId());
                                JsonObject userJson = createGPSJSON(userIdString, meetingIdString);
                                //TODO refactoring
                                return HttpRequest.POST(com.android.lagger.serverConnection.URL.GET_POSITIONS, userJson);
                            }

                            // onPostExecute displays the results of the AsyncTask.
                            @Override
                            protected void onPostExecute(String result) {
                                if (!result.equals("") && !result.isEmpty()) {
                                    JsonParser parser = new JsonParser();
                                    JsonObject responseJson = (JsonObject) parser.parse(result);

                                    JsonArray usersPositions = responseJson.get("usersPositions").getAsJsonArray();

                                    gpsUsers.clear();
                                    Gson gson = new GsonHelper().getGson();
                                    for (JsonElement userPositionJsonElem : usersPositions) {
                                        GPSUser user = gson.fromJson(userPositionJsonElem, GPSUser.class);
                                        gpsUsers.add(user);
                                        JsonObject object = userPositionJsonElem.getAsJsonObject();
                                        JsonArray positions = object.getAsJsonArray("positions");
                                        List tempPositionList = new ArrayList();
                                        for (JsonElement positionJsonElem : positions) {
                                            Position position = gson.fromJson(positionJsonElem, Position.class);
                                            tempPositionList.add(position);
                                        }
                                        user.setPositionList(tempPositionList);
                                    }
                                    showUserMarkers();

                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Błąd: nie ma takiego spotkania ", Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute();
                    }
                });
            }
        };

    }

    public void getLocations() {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                String userIdString = String.valueOf(State.getLoggedUserId());
                String meetingIdString = String.valueOf(meeting.getId());
                JsonObject userJson = createGPSJSON(userIdString, meetingIdString);
                //TODO refactoring
                return HttpRequest.POST(com.android.lagger.serverConnection.URL.GET_POSITIONS, userJson);
            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {

                JsonParser parser = new JsonParser();
                JsonObject responseJson = (JsonObject) parser.parse(result);

                JsonArray usersPositions = responseJson.get("usersPositions").getAsJsonArray();

                gpsUsers.clear();
                Gson gson = new GsonHelper().getGson();
                for (JsonElement userPositionJsonElem : usersPositions) {
                    GPSUser user = gson.fromJson(userPositionJsonElem, GPSUser.class);
                    gpsUsers.add(user);
                    JsonObject object = userPositionJsonElem.getAsJsonObject();
                    JsonArray positions = object.getAsJsonArray("positions");
                    List tempPositionList = new ArrayList();
                    for (JsonElement positionJsonElem : positions) {
                        Position position = gson.fromJson(positionJsonElem, Position.class);
                        tempPositionList.add(position);
                    }
                    user.setPositionList(tempPositionList);
                }

//                for (JsonElement positionJsonElem : positions) {
//                    Position position = gson.fromJson(positionJsonElem, Position.class);
//                    positionList.add(position);
//                }
                showUserMarkers();
            }
        }.execute();
    }

    public JsonObject createGPSJSON(String userId, String meetingId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idUser", userId);
        jsonObject.addProperty("idMeeting", meetingId);
        return jsonObject;
    }

    private void initalizeMarkerColors() {
        markerColors = new HashMap<Integer, Float>();
        markerColors.put(0, (float) 210.0);
        markerColors.put(1, (float) 240.0);
        markerColors.put(2, (float) 180.0);
        markerColors.put(3, (float) 120.0);
        markerColors.put(4, (float) 300.0);
        markerColors.put(5, (float) 30.0);
        markerColors.put(6, (float) 0.0);
        markerColors.put(7, (float) 330.0);
        markerColors.put(8, (float) 270.0);
        markerColors.put(9, (float) 60.0);
    }

    public LatLng getChosenPositon() {
        return chosenPositon;
    }

    public static class MarkerColors {
        private static HashMap<Integer, Float> markerColors;
        private static int counter;

        static {
            counter = -1;
            markerColors = new HashMap<Integer, Float>();
            markerColors.put(0, (float) 210.0);
            markerColors.put(1, (float) 240.0);
            markerColors.put(2, (float) 180.0);
            markerColors.put(3, (float) 120.0);
            markerColors.put(4, (float) 300.0);
            markerColors.put(5, (float) 30.0);
            markerColors.put(6, (float) 0.0);
            markerColors.put(7, (float) 330.0);
            markerColors.put(8, (float) 270.0);
            markerColors.put(9, (float) 60.0);
        }

        public static float getMarkerColor() {

            if (counter < 9)
                counter += 1;
            else
                counter = 0;
            return markerColors.get(counter);
        }

        public static float getMarkerColor(int i) {
            return markerColors.get(i);
        }
    }

    class GetDirection extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading route. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            //pDialog.show();
        }

        protected String doInBackground(String... args) {

            //TODO!!

            //połączenie z internetem musi być
            String stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=Czestochowa&destination=Wroclaw";
            StringBuilder response = new StringBuilder();

            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection httpconn = null;

            try {
                if (url != null) {
                    httpconn = (HttpURLConnection) url
                            .openConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (httpconn != null) {
                    if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader input = new BufferedReader(
                                new InputStreamReader(httpconn.getInputStream()),
                                8192);
                        String strLine;

                        while ((strLine = input.readLine()) != null) {
                            response.append(strLine);
                        }
                        input.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String jsonOutput = response.toString();

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(jsonOutput);


                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route
                JSONObject route = routesArray.getJSONObject(0);

                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                polyz = decodePoly(polyline);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            if (polyz != null) {
                for (int i = 0; i < polyz.size() - 1; i++) {
                    LatLng src = polyz.get(i);
                    LatLng dest = polyz.get(i + 1);
                    googleMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(2).color(Color.RED).geodesic(true));

                }
                pDialog.dismiss();
            }

        }
    }
}



