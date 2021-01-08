package com.oakspro.jetgpsshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView logoImg;
    Animation zoom_inAnim, zoom_outAnim;
    private static int SCREEN=9000;
   SharedPreferences sharedPreferences;
    private boolean loginV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logoImg=findViewById(R.id.logo);

        sharedPreferences=getSharedPreferences("MyUser",MODE_PRIVATE);

     loginV=sharedPreferences.getBoolean("loginS",false);

        zoom_inAnim= AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        zoom_outAnim=AnimationUtils.loadAnimation(this, R.anim.zoom_out);

        logoImg.setAnimation(zoom_inAnim); logoImg.setAnimation(zoom_outAnim);
        logoImg.setAnimation(zoom_inAnim); logoImg.setAnimation(zoom_outAnim);
        logoImg.setAnimation(zoom_inAnim); logoImg.setAnimation(zoom_outAnim);
        logoImg.setAnimation(zoom_inAnim); logoImg.setAnimation(zoom_outAnim);
        logoImg.setAnimation(zoom_inAnim); logoImg.setAnimation(zoom_outAnim);
        logoImg.setAnimation(zoom_inAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loginV==true){
                    Intent intent=new Intent(MainActivity.this, Dashboard.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        },SCREEN);


    }
}