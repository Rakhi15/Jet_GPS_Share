package com.oakspro.jetgpsshare;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MapFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String userMobile=null;
    String api_call_check="https://oakspro.com/projects/project35/deepu/JGS/check_friends.php";
    ProgressDialog progressDialog;
    String friend_status=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        sharedPreferences=getActivity().getSharedPreferences("MyUser", Context.MODE_PRIVATE);
        userMobile=sharedPreferences.getString("mobile", null);
        View root= inflater.inflate(R.layout.fragment_map, container, false);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();

                final Dialog d = new Dialog(getActivity());

                d.setTitle("MESSAGE");
                d.setContentView(R.layout.customdialoglayout);
                d.setCancelable(false);
                final Button getlocation = d.findViewById(R.id.getlocation);
                final Button cancel= d.findViewById(R.id.cancel);
                final EditText friendmobile=d.findViewById(R.id.friendmobile);

                getlocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String friendmobilenum=friendmobile.getText().toString();
                        if(friendmobilenum.length()==10){
                            checkMobileFriend(friendmobilenum, userMobile);
                            d.dismiss();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        d.dismiss();

                    }
                });
                try {
                    d.show();
                }
                catch (Exception e){
                    Log.i("info",e.getMessage());
                }
            }
        });
        return root;
    }

    private void checkMobileFriend(String friendmobilenum, String userMobile) {
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, api_call_check, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("success");
                    friend_status=jsonObject.getString("friend_status");


                    if (status.equals("success")){

                        if (friend_status.equals("1")){
                            Toast.makeText(getContext(), "Found Friend....Happy Sharing", Toast.LENGTH_SHORT).show();
                            String friend_lat_cord=jsonObject.getString("lat_cord");
                            String friend_long_cord=jsonObject.getString("long_cord");
                            Toast.makeText(getContext(), "Lat: "+friend_lat_cord+" Long: "+friend_long_cord, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }else {
                            Toast.makeText(getContext(), "You are unable to access the user location", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }


                    }
                    if (status.equals("failed")){
                        Log.i("response of zero: ", status.toString());
                        progressDialog.dismiss();

                        Toast.makeText(getContext(), "Mobile Not Found", Toast.LENGTH_SHORT).show();

                       final Dialog d1 = new Dialog(getActivity());

                        d1.setTitle("MESSAGE");
                        d1.setContentView(R.layout.shareappdialog);
                        d1.setCancelable(false);
                        final Button sharebtn = d1.findViewById(R.id.share);
                        final Button cancelbtn= d1.findViewById(R.id.cancelshare);

                        sharebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sharetoWhatsapp(friendmobilenum);
                                d1.dismiss();
                            }
                        });
                        cancelbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d1.dismiss();
                            }
                        });
                        d1.show();




                    }else {
                        Toast.makeText(getContext(), "Error Code", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> checkpar=new HashMap<>();
                checkpar.put("user_mobile", userMobile);
                checkpar.put("friend_mobile", friendmobilenum);
                return checkpar;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);


    }

    private void sharetoWhatsapp(String friendmobilenum) {
        boolean isInstalled=appInstalled("com.whatsapp");
        String app_link="Please Download our JetGPSShare to find your friends \n https://oakspro.com/projects/project35/deepu/JGS/download/JetGPSShare.apk";
        if (isInstalled){
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+91"+friendmobilenum+ "&text="+app_link));
            startActivity(intent);
        }else {
            Toast.makeText(getContext(), "Please Install Whatsapp First", Toast.LENGTH_SHORT).show();
        }


    }
    private boolean appInstalled(String package_name){
        PackageManager packageManager=getActivity().getPackageManager();
        boolean app_install;
        try {
            packageManager.getPackageInfo(package_name, PackageManager.GET_ACTIVITIES);
            app_install=true;
        }catch (PackageManager.NameNotFoundException e){
            app_install=false;
        }
        return app_install;
    }
}