package com.example.kim.finalprojectowner1;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.kim.finalprojectowner1.fragment.ChatFragment;
import com.example.kim.finalprojectowner1.fragment.MessageFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder alert_ex;
    ViewPager vp;


    public static final String[] MANDATORY_PERMISSIONS = {
            "android.permission.CAMERA",
            "android.permission.CHANGE_NETWORK_STATE",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.CHANGE_WIFI_STATE",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.RECORD_AUDIO",
            "android.permission.INTERNET",
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.BROADCAST_STICKY",
            "android.permission.READ_PHONE_STATE",
            "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 24) {
            checkPermission(MANDATORY_PERMISSIONS);
        }//권한 주기
        vp = (ViewPager) findViewById(R.id.vp);
        Button btn_first = (Button) findViewById(R.id.btn_first);
        Button btn_second = (Button) findViewById(R.id.btn_second);
        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);
        btn_second.setOnClickListener(movePageListener);
        btn_second.setTag(1);

//        ChatFragment fragment = ChatFragment.newInstance();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.mainActivity_framelayout, fragment);
//        transaction.commit();

    }

    @TargetApi(24)
    private void checkPermission(String[] permissions) {
        requestPermissions(permissions, 100);

    }

    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MessageFragment();// 응급실
                case 1:
                    return new ChatFragment();// 채팅
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }



    @Override
    public void onBackPressed() {
        alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("정말로 종료하시겠습니다.");

        alert_ex.setPositiveButton("취소", (dialogInterface, i) -> {

        });
        alert_ex.setNegativeButton("종료", (dialogInterface, i) -> {
            FirebaseAuth.getInstance().signOut();
            finishAffinity();
        });
        AlertDialog alert = alert_ex.create();
        alert.show();

    }
}


