package com.datalabor.soporte.arke.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.datalabor.soporte.arke.R;
import com.datalabor.soporte.arke.common;

public class SplashActivity extends AppCompatActivity {

    private Long duration = 3000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        scheduleSplashScreen();

    }

    private void scheduleSplashScreen()
    {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!checkUser()) {
                    startActivity(new Intent(getApplicationContext(), login.class));
                    finish();
                }

                else
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }





            }
        }, duration); //3000 L = 3 detik


    }

    private boolean checkUser()
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences( this );

        Integer user_id = sharedPref.getInt(common.VAR_USER_ID, 0);

        if (user_id !=0 ) return true;
        else return  false;
    }


}
