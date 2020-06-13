package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rV;
    private RecyclerView.Adapter rVa;
    private RecyclerView.LayoutManager rvL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Arraylist for location to show user
        ArrayList<LocationItem> locations = new ArrayList<>();
        locations.add(new LocationItem(R.drawable.ic_house,"cool1","cool2"));
        //Reminder of what it going to look
        locations.add(new LocationItem(R.drawable.ic_house,"Location","Time(Start time - End time)"));

        //Recycler View Setting up 
        rV = findViewById(R.id.recycle);
        rV.setHasFixedSize(true);
        rvL = new LinearLayoutManager(this);
        rVa = new LocationAdapter(locations);

        rV.setLayoutManager(rvL);
        rV.setAdapter(rVa);
    }
}