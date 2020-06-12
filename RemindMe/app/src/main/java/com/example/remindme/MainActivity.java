package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Arraylist for location to show user
        ArrayList<LocationItem> locations = new ArrayList<>();
        locations.add(new LocationItem(R.drawable.ic_house,"cool1","cool2"));
    }
}