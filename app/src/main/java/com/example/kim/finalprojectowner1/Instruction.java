package com.example.kim.finalprojectowner1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.kim.finalprojectowner1.Adapter.ViewPagerAdapter;


import java.util.ArrayList;
import java.util.List;

import steelkiwi.com.library.view.IndicatorView;

public class Instruction extends AppCompatActivity {
    List<Integer> lstImages=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        lstImages.add(R.drawable.emergency);
        lstImages.add(R.drawable.emergency);
        lstImages.add(R.drawable.emergency);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(lstImages,this);
        ViewPager viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        IndicatorView indicatorView=(IndicatorView)findViewById(R.id.indicatorView);
        indicatorView.attachViewPager(viewPager);




    }

    public void finish(View view) {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);

        startActivity(intent);
        finish();
    }
}