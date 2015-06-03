package com.android.lagger.forms.meetings;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.model.entities.Meeting;
import com.android.lagger.model.entities.User;
import com.android.lagger.settings.Parser;
import com.android.lagger.settings.State;
import com.android.lagger.tasks.AcceptMeetingTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Kubaa on 2015-04-08.
 */
public class ViewMeetingFragment extends Fragment {
    private View parent;
    private Context mContext;
    private FloatingActionButton btnAccept;
    private FloatingActionButton btnEdit;
    private FloatingActionButton btnRefuse;

    private TextView labelWhen;
    private TextView labelOrganizer;
    private TextView labelMeetingName;
    private TextView labelWhere;

    private Boolean isReadOnly;
    private Boolean isOrganizer;
    private Meeting meeting;
    private User user;

    private MapView mMapView;
    private GoogleMap googleMap;

    FragmentTransaction fragmentTransaction;

    public ViewMeetingFragment() {

    }

    public ViewMeetingFragment(Context mContext, Boolean isReadOnly) {
        this.mContext = mContext;
        this.isReadOnly = isReadOnly;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_view_meeting, container, false);
        btnAccept = (FloatingActionButton) parent.findViewById(R.id.btnAcceptMeeting);
        btnRefuse = (FloatingActionButton) parent.findViewById(R.id.btnRefuseMeeting);
        btnEdit = (FloatingActionButton) parent.findViewById(R.id.btnEditMeeting);

        addListenersForBtns();
        if (isReadOnly) {
            btnAccept.setVisibility(View.INVISIBLE);
            btnRefuse.setVisibility(View.INVISIBLE);
        }
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


    }

    private void setChosenLocationOnMap() {
        LatLng latLng = new LatLng(State.DEFAULT_LATITUDE, State.DEFAULT_LONGITUDE);
        if (meeting.getLatitude() != Double.MIN_VALUE && meeting.getLongitude() != Double.MIN_VALUE) {
            latLng = new LatLng(meeting.getLatitude(), meeting.getLongitude());
        }
        setMarkerOnMap(latLng);
    }

    private void setMarkerOnMap(LatLng latLng) {
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
        CameraPosition cameraPosition;
        cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(12).build();


        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        // Placing a marker on the touched position
        googleMap.addMarker(markerOptions);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        labelMeetingName = (TextView) getView().findViewById(R.id.tvMeetingName);
        labelWhen = (TextView) getView().findViewById(R.id.labelWhen);
        labelOrganizer = (TextView) getView().findViewById(R.id.labelOrganizer);
        labelWhere = (TextView) getView().findViewById(R.id.labelWhere);

        meeting = insertMeetingDetails();
        setBtnsVisibilityForUser(meeting.getOrganizer());
        addMap(savedInstanceState);
        setChosenLocationOnMap();
    }

    private Meeting insertMeetingDetails() {
        Bundle extras = getArguments();
        Meeting meeting = extras.getParcelable("meeting");
        user = meeting.getOrganizer();

        labelMeetingName.setText(meeting.getName());
        labelWhen.setText(Parser.parseDate(meeting.getStartTime()));
        labelWhere.setText(meeting.getLocationName());
        labelOrganizer.setText(user.getLogin());

        return meeting;
    }

    private void setBtnsVisibilityForUser(final User user){
        isOrganizer = State.getLoggedUserId() == user.getId();
        if (!isOrganizer) {
            btnEdit.setVisibility(View.INVISIBLE);
        }
        else{
            btnAccept.setVisibility(View.INVISIBLE);
            btnRefuse.setVisibility(View.INVISIBLE);
        }
    }

    public void addListenersForBtns() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptMeetingTask.acceptMeeting(meeting.getId(), true, mContext);
                replaceWithFragment(new MeetingListFragment(mContext));
            }
        });

        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptMeetingTask.acceptMeeting(meeting.getId(), false, mContext);
                replaceWithFragment(new MeetingListFragment(mContext));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceWithFragment(new MeetingWhenFragment(mContext, meeting, true));
            }
        });
    }

    private void replaceWithFragment(Fragment fragment){
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container_body, fragment).commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
