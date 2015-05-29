package com.android.lagger.forms.meetings;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.lagger.R;
import com.android.lagger.gpslocation.GPSService;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.settings.State;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class MeetingWhereFragment extends Fragment {
    FloatingActionButton leftBtn;
    FloatingActionButton rightBtn;
    FragmentTransaction fragmentTransaction;
    private View parent;
    private Context mContext;
    private ViewPager parentPager;
    private MapView mMapView;
    private GoogleMap googleMap;
    private LatLng chosenPosition;
    private Meeting meeting;
    private Boolean isEditMode;

    private EditText locationEditTxt;

    public MeetingWhereFragment(Context context, Meeting meeting, Boolean isEditMode) {
        mContext = context;
        this.meeting = meeting;
        this.isEditMode = isEditMode;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_meeting_where, container, false);
        initialize();
        addListeners();
        addMap(savedInstanceState);
        setChoosenLocationOnMap();

        return parent;
    }

    private void addMap(Bundle savedInstanceState) {
        mMapView = (MapView) parent.findViewById(R.id.mapView);
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
                setMarkerOnMap(latLng);
            }
        });
        CameraPosition cameraPosition;
        GPSService gpsService = new GPSService(mContext);
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
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //new GetDirection().execute();
        // Perform any camera updates here
    }

    private void setChoosenLocationOnMap() {
        LatLng latLng = new LatLng(State.DEFAULT_LATITUDE, State.DEFAULT_LONGITUDE);
        if (meeting.getLatitude() != Double.MIN_VALUE && meeting.getLongitude() != Double.MIN_VALUE) {
            latLng = new LatLng(meeting.getLatitude(), meeting.getLongitude());
        }
        setMarkerOnMap(latLng);
    }

    private void setMarkerOnMap(LatLng latLng) {
        chosenPosition = latLng;
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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
    }

    private void initialize() {
        locationEditTxt = (EditText) parent.findViewById(R.id.editTextLocation);
        if (meeting != null) {
            locationEditTxt.setText(meeting.getLocationName());
        }
        leftBtn = (FloatingActionButton) parent.findViewById(R.id.btnLeftPager);
        rightBtn = (FloatingActionButton) parent.findViewById(R.id.btnRightPager);
    }

    public void addListeners() {

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataAndReplaceToFragment(new MeetingWhenFragment(mContext, meeting, isEditMode));
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataAndReplaceToFragment(new MeetingWhoFragment(mContext, meeting, isEditMode));
            }
        });
    }

    private void updateDataAndReplaceToFragment(Fragment nextFragment) {
        updateMeetingData();

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container_body, nextFragment).commit();

    }

    private void updateMeetingData() {
        meeting.setLocationName(locationEditTxt.getText().toString());
        meeting.setLatitude(chosenPosition.latitude);
        meeting.setLongitude(chosenPosition.longitude);
    }

}