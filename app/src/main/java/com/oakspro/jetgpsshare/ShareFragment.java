package com.oakspro.jetgpsshare;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.Task;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ShareFragment extends Fragment {

    Button locationBtn;
    private static int timer_per=2000;


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        locationBtn = (Button) root.findViewById(R.id.btn1);

        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(getContext(), BackgroundCode.class);
                //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("Background");

                PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(), 0, intent, 0);
                try {
                    pendingIntent.send(getContext(), 0, intent);
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer=new Timer();
        long delay=0;
        long time_interval=1*1000;

        timer.scheduleAtFixedRate(task, delay, time_interval);


        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return root;

    }

}