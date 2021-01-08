package com.oakspro.jetgpsshare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ShareFragment extends Fragment {

    Button locationBtn;
    String latCord;
    String longCord;
    private LocationManager locationManager;
    private String provider;
    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_share, container, false);
        locationBtn=(Button)root.findViewById(R.id.btn1);


        //start

        locationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean enable=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enable){
            Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        Criteria criteria=new Criteria();
        provider=locationManager.getBestProvider(criteria, false);

        @SuppressLint("MissingPermission") Location location=locationManager.getLastKnownLocation(provider);

        if (location!=null){

        }else {
            latCord="Failed";
            longCord="Failed";
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                float lat=(float) (location.getLatitude());
                float lon=(float) (location.getLongitude());
                latCord=String.valueOf(lat);
                longCord=String.valueOf(lon);
                locationBtn.setText(latCord+" , "+longCord);
                Log.i("LOCATION: ", latCord+longCord);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });

        //end

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
     //   locationManager.requestLocationUpdates(provider, 400,1, new Lo);
    }

    @Override
    public void onPause() {
        super.onPause();
     //   locationManager.removeUpdates();
    }

}