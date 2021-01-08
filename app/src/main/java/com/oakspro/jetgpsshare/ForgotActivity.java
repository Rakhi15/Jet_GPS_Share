package com.oakspro.jetgpsshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ForgotActivity extends AppCompatActivity {

    EditText emailEd, creat_pass, conf_pass, otpEd;
    TextView res_txt;
    Button nextBtn;
    String randomNumber;
    String api_link="https://oakspro.com/projects/project35/deepu/JGS/email_search.php";
    String api_link2="https://oakspro.com/projects/project35/deepu/JGS/update_password.php";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        //set ids

        res_txt=findViewById(R.id.response_text);
        emailEd=findViewById(R.id.user_ed);
        creat_pass=findViewById(R.id.pass_ed);
        conf_pass=findViewById(R.id.conf_pass_ed);
        nextBtn=findViewById(R.id.loginBtn);
        otpEd=findViewById(R.id.otp_ed);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_name=nextBtn.getText().toString();
                if (btn_name.equals("Next")){
                    String email_s=emailEd.getText().toString();

                    if (!TextUtils.isEmpty(email_s)){

                        processOTP(email_s);


                    }else {
                        Toast.makeText(ForgotActivity.this, "Pleae Fill fields", Toast.LENGTH_SHORT).show();
                    }
                }else if (btn_name.equals("Submit")){

                    String otp_entered=otpEd.getText().toString();
                    if (!TextUtils.isEmpty(otp_entered)){

                        if (otp_entered.equals(randomNumber)){

                            otpEd.setEnabled(false);
                            emailEd.setEnabled(false);

                            creat_pass.setVisibility(View.VISIBLE);
                            conf_pass.setVisibility(View.VISIBLE);
                            nextBtn.setText("Update");

                        }else {
                            Toast.makeText(ForgotActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(ForgotActivity.this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show();
                    }

                }
                else if (btn_name.equals("Update")){
                    String email_s=emailEd.getText().toString();

                    String pass1=creat_pass.getText().toString();
                    String pass2=conf_pass.getText().toString();

                    if (pass2.equals(pass1)){

                        updateProcess(email_s, pass1);

                    }else {
                        Toast.makeText(ForgotActivity.this, "Password Not matched", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });




    }

    private void updateProcess(String email_s, String pass1) {

        progressDialog.show();
        StringRequest request2=new StringRequest(Request.Method.POST, api_link2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");

                    if (status.equals("1")){
                        progressDialog.dismiss();
                        Toast.makeText(ForgotActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotActivity.this, LoginActivity.class));
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(ForgotActivity.this, "Password Not Updated", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ForgotActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> updt=new HashMap<>();
                updt.put("email", email_s);
                updt.put("pass", pass1);
                return  updt;
            }
        };
        RequestQueue requestQueue2=Volley.newRequestQueue(this);
        requestQueue2.add(request2);
    }

    private void processOTP(String email_s) {

progressDialog.show();

        Random random=new Random();
        int randomNumber_temp=random.nextInt(999999);
        randomNumber= String.format("%06d", randomNumber_temp);

        Log.i("OTP: ", randomNumber);


        StringRequest request=new StringRequest(Request.Method.POST, api_link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String msg=jsonObject.getString("message");

                    if (status.equals("1")){

                        otpEd.setVisibility(View.VISIBLE);
                        res_txt.setVisibility(View.VISIBLE);
                        nextBtn.setText("Submit");
                        res_txt.setText("OTP Sent to registered Email : "+msg.toString());
                        progressDialog.dismiss();
                    }
                    else {
                        res_txt.setVisibility(View.VISIBLE);
                        res_txt.setText("Email not found : "+msg.toString());
                        Toast.makeText(ForgotActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ForgotActivity.this, "VError: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parma=new HashMap<>();
                parma.put("email", email_s);
                parma.put("otp", randomNumber);
                return parma;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}