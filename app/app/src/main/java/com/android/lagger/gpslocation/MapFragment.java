
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MapFragment extends Fragment {

    static final LatLng DUBLIN = new LatLng(53.344103999999990000,
            -6.267493699999932000);
    public static HashMap<Integer,Float> markerColors;
    ProgressDialog pDialog;
    List<LatLng> polyz;
    JSONArray array;
    MapView mMapView;
    private GoogleMap googleMap;
    private Context mContext;
    private View parent;
    private List<GPSUser> gpsUsers;
    public MapFragment(Context context){
        mContext = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initalizeMarkerColors();

        //test Data, to delete!!!/////////////////////////////////////////////////////////////////
        gpsUsers=new ArrayList<GPSUser>();
        gpsUsers.add(new GPSUser(50.808236, 19.108781));
        gpsUsers.add(new GPSUser(50.808434, 19.128928));
        gpsUsers.add(new GPSUser(50.825787, 19.126696));
        gpsUsers.add(new GPSUser(50.835979, 19.149356));
        GPSUser tempUser = gpsUsers.get(0);
        tempUser.addGPSPosition(new GPSCoordinates(50.808236, 19.108781));
        tempUser.addGPSPosition(new GPSCoordinates(50.808434, 19.128928));
        tempUser.addGPSPosition(new GPSCoordinates(50.825787, 19.126696));

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

        GPSService gpsService = new GPSService(mContext);
        if(gpsService.canGetLocation()) {
            gpsService.getLatitude();

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(50.811146, 19.092879)).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        new GetDirection().execute();
        showUserMarkers();
        // Perform any camera updates here


        return v;
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

        List<LatLng> poly = new ArrayList<LatLng>();
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
        int i = 0;
        for (GPSUser user : gpsUsers) {
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(user.getActualPositition().getLatitude(), user.getActualPositition().getLongitude())).title("Hello Maps");

            // Changing marker icon
            float x = (float) 2.0;
            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(markerColors.get(i)));

            // adding marker
            googleMap.addMarker(marker);
            if (!user.getPositionList().isEmpty())
                drawUserPath(user);
            i++;
        }
    }

    public void drawUserPath(GPSUser user) {
        List<GPSCoordinates> tempList = user.getPositionList();
        for (int i = 0; i < user.getPositionList().size() - 1; i++) {
            Polyline line = googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(tempList.get(i).getLatitude(), tempList.get(i).getLongitude()), new LatLng(tempList.get(i + 1).getLatitude(), tempList.get(i + 1).getLongitude()))
                    .width(5)
                    .color(Color.BLACK));
        }

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
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url
                        .openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(httpconn.getInputStream()),
                            8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                String jsonOutput = response.toString();

                JSONObject jsonObject = new JSONObject(jsonOutput);

                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route
                JSONObject route = routesArray.getJSONObject(0);

                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                polyz = decodePoly(polyline);

            } catch (Exception e) {

            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            if (polyz != null) {
                for (int i = 0; i < polyz.size() - 1; i++) {
                    LatLng src = polyz.get(i);
                    LatLng dest = polyz.get(i + 1);
                    Polyline line = googleMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(2).color(Color.RED).geodesic(true));

                }
                pDialog.dismiss();
            }

        }
    }
}



