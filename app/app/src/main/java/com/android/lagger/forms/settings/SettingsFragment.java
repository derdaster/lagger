package com.android.lagger.forms.settings;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.lagger.R;
import com.android.lagger.gpslocation.GPSService;
import com.gc.materialdesign.views.Slider;
import com.gc.materialdesign.views.Switch;

/**
 * Created by Kubaa on 2015-05-10.
 */
public class SettingsFragment extends Fragment {

    private Context mContext;
    private View parent;
    Slider sliderDistance;
    Slider sliderTime;
    Switch switchGPS;

    public SettingsFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_settings, container, false);

        return parent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        sliderDistance = (Slider) getActivity().findViewById(R.id.sliderDistance);
        int x=GPSService.getMinDistanceChangeForUpdates();
        sliderDistance.setValue(x);
        x=GPSService.getMinTimeBwUpdates();
        sliderTime = (Slider) getActivity().findViewById(R.id.sliderTime);
        sliderTime.setValue(x+100);
        switchGPS = (Switch) getActivity().findViewById(R.id.switchView);
        switchGPS.setChecked(GPSService.isGPSTracking());

        addListenerOnLoginButton();
    }


    class SliderDistanceListener implements Slider.OnValueChangedListener {

        @Override
        public void onValueChanged(int i) {
            GPSService.setMinDistanceChangeForUpdates(i);
        }
    }

    class SliderTimeListener implements Slider.OnValueChangedListener {

        @Override
        public void onValueChanged(int i) {
            GPSService.setMinTimeBwUpdates(i);
        }
    }

    class SwitchGPSListener implements Switch.OnCheckListener {

        @Override
        public void onCheck(boolean b) {
            GPSService.setGPSTracking(b);
        }
    }

    public void addListenerOnLoginButton() {
        sliderDistance.setOnValueChangedListener(new SliderDistanceListener() {
        });
        sliderTime.setOnValueChangedListener(new SliderTimeListener() {
        });

        sw.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(boolean b) {
                if(b == true)
                {

                }
                else
                {

                }
            }
        });
        switchGPS.setOncheckListener(new SwitchGPSListener());
    }


}