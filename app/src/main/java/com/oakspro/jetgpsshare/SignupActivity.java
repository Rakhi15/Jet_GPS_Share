package com.oakspro.jetgpsshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText nameEd, mobileEd, passEd, emailEd;
    Button signupBtn;
    ProgressDialog progressDialog;
    String api_link="https://oakspro.com/projects/project35/deepu/JGS/register_api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        nameEd=findViewById(R.id.name_ed);
        mobileEd=findViewById(R.id.user_ed);
        passEd=findViewById(R.id.pass_ed);
        signupBtn=findViewById(R.id.signupBtn);
        emailEd=findViewById(R.id.email_ed);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_s=nameEd.getText().toString();
                String phone_s=mobileEd.getText().toString();
                String pass_s=passEd.getText().toString();
                String email_s=emailEd.getText().toString();

                if (!TextUtils.isEmpty(name_s) && !TextUtils.isEmpty(phone_s) && !TextUtils.isEmpty(pass_s) && !TextUtils.isEmpty(email_s)){
                    uploadData(name_s, phone_s, pass_s, email_s);
                }
                else {
                    Toast.makeText(SignupActivity.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void uploadData(String name_s, String phone_s, String pass_s, String email_s) {

        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, api_link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String jStatus=jsonObject.getString("jstatus");

                    if (jStatus.equals("1")){
                        progressDialog.dismiss();
                        Toast.makeText(SignupActivity.this, "Signup Succes", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SignupActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupActivity.this, "Volley Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("name", name_s);
                params.put("mobile",phone_s);
                params.put("password", pass_s);
                params.put("email", email_s);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    public void openLoginLink(View view) {
        Intent intent=new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }


}