package com.uts_pbp_d_9229;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.uts_pbp_d_9229.view.HomeActivity;

public class WelcomeScreen extends AppCompatActivity {
    private String splash, isLogin;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        getSupportActionBar().hide();

        preference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preference.edit();
        splash  = preference.getString("splash", null);
        isLogin = preference.getString("isLogin", null);

        if(splash == null){
            editor.putString("splash", "ya");
            editor.apply();
            splash = preference.getString("splash", null);
        }

        if(isLogin == null){
            editor.putString("isLogin", "tidak");
            editor.apply();
            isLogin = preference.getString("isLogin", null);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLogin.equals("tidak")){
                    startActivity(new Intent(WelcomeScreen.this, LoginActivity.class));
                }else if(splash.equals("ya")) {
                    startActivity(new Intent(WelcomeScreen.this, SplashScreen.class));
                }else{
                    startActivity(new Intent(WelcomeScreen.this, HomeActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}