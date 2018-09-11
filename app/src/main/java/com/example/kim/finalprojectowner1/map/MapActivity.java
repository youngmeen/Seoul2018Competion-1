package com.example.kim.finalprojectowner1.map;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.kim.finalprojectowner1.R;
import com.example.kim.finalprojectowner1.fragment.MapFragment;


public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = new MapFragment();
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, mapFragment);
        transaction.commit();
    }

    public void chatting(View view) {
        finish();
    }

    public void map(View view) {
    }
}
