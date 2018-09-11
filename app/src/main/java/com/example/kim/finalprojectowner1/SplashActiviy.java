package com.example.kim.finalprojectowner1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActiviy extends AppCompatActivity {
    LinearLayout linearLayout;
    FirebaseRemoteConfig firebaseRemoteConfig;
    SharedPreferences pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);

        boolean first = pref.getBoolean("isFirst", false);
        if(first==false){
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();
            //앱 최초 실행시 하고 싶은 작업
            startActivity(new Intent(SplashActiviy.this, Instruction.class));

        }else{
            Log.d("Is first Time?", "not first");
        }

        linearLayout = (LinearLayout) findViewById(R.id.splash_layout_linearlayout);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        firebaseRemoteConfig.setConfigSettings(configSettings);

        firebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        firebaseRemoteConfig.activateFetched();
                    } else {

                    }
                    displayWelcomeMessage();
                });

    }

    private void displayWelcomeMessage() {
        String splash_background = firebaseRemoteConfig.getString("splash_background");
        boolean caps = firebaseRemoteConfig.getBoolean("splash_message_caps");
        String splash_message = firebaseRemoteConfig.getString("splash_message");
        //remote config 객체를 Firebase에서 가져온다.

        if (caps) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(splash_message).setPositiveButton("확인", (dialogInterface, i) -> {
                finish();//경고창을 뛰우며 확인을 누르면 종료됨
            });
            builder.create().show();

        } else {
            Intent intent = new Intent(SplashActiviy.this, LoginActivity.class);
            startActivity(intent);

        }
    }
}
