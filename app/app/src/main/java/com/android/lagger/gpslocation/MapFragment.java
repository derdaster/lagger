
package com.android.lagger.gpslocation;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.lagger.R;
import com.android.lagger.model.navigation.Position;
import com.android.lagger.serverConnection.GsonHelper;
import com.android.lagger.serverConnection.HttpRequest;
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

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

    public static HashMap<Integer,Float> markerColors;
    private ProgressDialog pDialog;
    private List<LatLng> polyz;
    private MapView mMapView;
    private GoogleMap googleMap;
    private Context mContext;
    private List<GPSUser> gpsUsers;
    private LatLng chosenPositon;

    public MapFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initalizeMarkerColors();

        //test Data, to delete!!!/////////////////////////////////////////////////////////////////
        gpsUsers=new ArrayList();
//        gpsUsers.add(new GPSUser(50.808236, 19.108781));
//        gpsUsers.add(new GPSUser(50.808434, 19.128928));
//        gpsUsers.add(new GPSUser(50.825787, 19.126696));
//        gpsUsers.add(new GPSUser(50.835979, 19.149356));
//        GPSUser tempUser = gpsUsers.get(0);
//        tempUser.addGPSPosition(new LatLng(50.808236, 19.108781));
//        tempUser.addGPSPosition(new LatLng(50.808434, 19.128928));
//        tempUser.addGPSPosition(new LatLng(50.825787, 19.126696));

        // inflat and return the layout
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
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                chosenPositon = latLng;
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
                getLocations();
            }
        });
        GPSService gpsService = new GPSService(this.getActivity().getApplicationContext());
        if(gpsService.canGetLocation()) {
            gpsService.getLatitude();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(gpsService.getLatitude(), gpsService.getLongitude())).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //new GetDirection().execute();
//        showUserMarkers();
        // Perform any camera updates here


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

        for (GPSUser user : gpsUsers) {
            showNamedMarker(user.getActualPositition(), String.valueOf(user.getIdUser()));

            if (!user.getPositionList().isEmpty())
                drawUserPath(user);

        }
    }

    public void showMarker(LatLng latLng) {
        MarkerOptions marker = new MarkerOptions().position(
                latLng).title("Position");

        drawMarker(marker);
    }

    public void showNamedMarker(LatLng latLng, String name) {
        MarkerOptions marker = new MarkerOptions().position(latLng).title(name);

        drawMarker(marker);
    }

    public void drawMarker(MarkerOptions marker) {

        marker.icon(BitmapDescriptorFactory
                .defaultMarker(MarkerColors.getMarkerColor()));

        googleMap.addMarker(marker);
    }
    public void drawUserPath(GPSUser user) {
        List tempList = user.getPositionList();
        for (int i = 0; i < user.getPositionList().size() - 1; i++) {
            googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(((Position) tempList.get(i)).getLatitude(),((Position) tempList.get(i)).getLongitude()), new LatLng(((Position) tempList.get(i+1)).getLatitude(),((Position) tempList.get(i+1)).getLongitude()))
                    .width(5)
                    .color(Color.BLACK));
        }

    }
    public void getLocations(){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... urls) {
                String userId = String.valueOf(1);
                String meetingId = String.valueOf(1);
                JsonObject userJson = createGPSJSON(userId,meetingId);
                //TODO refactoring
                return HttpRequest.POST(com.android.lagger.serverConnection.URL.GET_POSITIONS, userJson);
            }
            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {

            JsonParser parser = new JsonParser();
            JsonObject responseJson = (JsonObject)parser.parse(result);

            JsonArray usersPositions = responseJson.get("usersPositions").getAsJsonArray();


                Gson gson = new GsonHelper().getGson();
                for (JsonElement userPositionJsonElem : usersPositions) {
                    GPSUser user = gson.fromJson(userPositionJsonElem, GPSUser.class);
                    gpsUsers.add(user);
                    JsonObject object=userPositionJsonElem.getAsJsonObject();
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
    public JsonObject createGPSJSON(String userId,String meetingId){
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
    }
}



