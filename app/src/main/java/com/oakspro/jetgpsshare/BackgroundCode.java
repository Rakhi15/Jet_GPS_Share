package com.oakspro.jetgpsshare;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class BackgroundCode  extends BroadcastReceiver {

    String latCord;
    String longCord;
    private LocationManager locationManager;
    private String provider;
    SharedPreferences sharedPreferences;
    String api_upload_cord="https://oakspro.com/projects/project35/deepu/JGS/cord_upload_api.php";

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {


        sharedPreferences=context.getSharedPreferences("MyUser",Context.MODE_PRIVATE);
        //start

        locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean enable=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enable){
            Intent intent_i=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent_i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

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
                //locationBtn.setText(latCord+" , "+longCord);
                Toast.makeText(context, "Getting cords", Toast.LENGTH_SHORT).show();
                String mobile_user=sharedPreferences.getString("mobile", null);
                UploadCordServer(latCord, longCord, mobile_user, context);
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

        SystemClock.sleep(5000);

    }
    private void UploadCordServer(String latCord, String longCord, String mobile_user, Context context) {
        StringRequest uploadReq=new StringRequest(Request.Method.POST, api_upload_cord, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Response: "+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Volley: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> uploadCord=new HashMap<>();
                uploadCord.put("mobile", mobile_user);
                uploadCord.put("lat", latCord);
                uploadCord.put("long", longCord);
                return uploadCord;
            }
        };
        RequestQueue requestQueue_cord= Volley.newRequestQueue(context);
        requestQueue_cord.add(uploadReq);

    }



}
