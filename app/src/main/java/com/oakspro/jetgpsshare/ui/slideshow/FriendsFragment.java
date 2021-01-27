package com.oakspro.jetgpsshare.ui.slideshow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oakspro.jetgpsshare.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FriendsFragment extends Fragment {


    EditText numberEd;
    Button addBtn, deleteBtn;
    RecyclerView friendsRecycler;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    String api_link="https://oakspro.com/projects/project35/deepu/JGS/add_friends.php";
    String api_link_delete="https://oakspro.com/projects/project35/deepu/JGS/delete_friends.php";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_friends, container, false);
        numberEd=root.findViewById(R.id.mobile_num);
        addBtn=root.findViewById(R.id.add_btn);
        friendsRecycler=root.findViewById(R.id.recylerView32);
        deleteBtn=root.findViewById(R.id.delete_btn);

        sharedPreferences=getActivity().getSharedPreferences("MyUser", Context.MODE_PRIVATE);

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberM=numberEd.getText().toString();
                String userMobile=sharedPreferences.getString("mobile", null);
                if (!TextUtils.isEmpty(numberM) && numberM.length()==10){
                    uploadFriends(numberM, userMobile);
                }else {
                    Toast.makeText(getContext(), "Invalid Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberM=numberEd.getText().toString();
                String userMobile=sharedPreferences.getString("mobile", null);
                if (!TextUtils.isEmpty(numberM) && numberM.length()==10){
                    deleteFriends(numberM, userMobile);
                }else {
                    Toast.makeText(getContext(), "Invalid Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void deleteFriends(String numberM, String userMobile) {
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, api_link_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("jstatus");

                    if (status.equals("1")){
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Volley: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("frnd_num", numberM);
                params.put("user_num", userMobile);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void uploadFriends(String numberM, String userMobile) {

        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, api_link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("jstatus");

                    if (status.equals("1")){
                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Volley: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("frnd_num", numberM);
                params.put("user_num", userMobile);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}