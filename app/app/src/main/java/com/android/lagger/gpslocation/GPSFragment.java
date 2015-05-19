package com.android.lagger.gpslocation;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.lagger.R;

/**
 * Created by Kubaa on 2015-03-20.
 */
public class GPSFragment extends Fragment implements LocationListener {

    private Context mContext;
    private View parent;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    GPSService gpsService;
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider = "";
    Button btnShowLocation;

    public GPSFragment(Context context) {
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_gps, container, false);

        btnShowLocation = (Button) parent.findViewById(R.id.get_location);

        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gpsService = new GPSService(mContext);

                if (gpsService.canGetLocation()) {
                    double latitude = gpsService.getLatitude();
                    double longitude = gpsService.getLongitude();

                    Toast.makeText(
                            mContext,
                            "Twoja pozycja -\nX: " + latitude + "\nY: "
                                    + longitude, Toast.LENGTH_LONG).show();
                } else {
                    gpsService.showSettingsAlert();
                }
            }
        });

        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        latituteField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mContext, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mContext, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }
}
