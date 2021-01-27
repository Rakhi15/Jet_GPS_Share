package com.oakspro.jetgpsshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText passwordEd, userEd;
    Button loginBtn;
    ProgressDialog progressDialog;
    String api_link="https://oakspro.com/projects/project35/deepu/JGS/login_api.php";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myedit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences=getSharedPreferences("MyUser",MODE_PRIVATE);
        myedit=sharedPreferences.edit();

        //set ids

        passwordEd=findViewById(R.id.pass_ed);
        userEd=findViewById(R.id.user_ed);
        loginBtn=findViewById(R.id.loginBtn);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);




        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String umobile=userEd.getText().toString();
                String upass=passwordEd.getText().toString();

                if (!TextUtils.isEmpty(umobile) && !TextUtils.isEmpty(upass)){

                    VerifyData(umobile, upass);
                }else {
                    Toast.makeText(LoginActivity.this, "Fill all feilds", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void VerifyData(String umobile, String upass) {

        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, api_link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");

                    if (status.equals("1")){

                        String name=jsonObject.getString("name");
                        String mobile=jsonObject.getString("mobile");
                        String email=jsonObject.getString("email");

                        Toast.makeText(LoginActivity.this, "Login Success: "+name, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this, Dashboard.class);
                        myedit.putString("name", name);
                        myedit.putString("mobile", mobile);
                        myedit.putString("email", email);
                        myedit.putBoolean("loginS", true);
                        myedit.commit();
                        startActivity(intent);
                        progressDialog.dismiss();

                    }
                    else if (status.equals("0")){
                        Toast.makeText(LoginActivity.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                    else {
                        Toast.makeText(LoginActivity.this, "incorrect mobile", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Exec: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "VolleyError: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parmas=new HashMap<>();
                parmas.put("mobile", umobile);
                parmas.put("password", upass);
                return parmas;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void openSignupLink(View view) {

        Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void openForgot(View view) {
        Intent intent=new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(intent);
    }
}