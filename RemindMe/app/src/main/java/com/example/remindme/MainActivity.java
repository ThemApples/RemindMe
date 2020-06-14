package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rV;
    private LocationAdapter rVa;
    private RecyclerView.LayoutManager rvL;

    private ArrayList<LocationItem> locations;
    private FloatingActionButton hourButton;
    private FloatingActionButton transportButton;

    private EditText location;
    private EditText time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hourButton = findViewById(R.id.hourGlass);
        hourButton.setOnClickListener(this);

        transportButton = findViewById(R.id.transport);
        transportButton.setOnClickListener(this);

        createLocationList();
        buildRecylerView();

    }

    public void createLocationList()
    {
        //Arraylist for location to show user
        locations = new ArrayList<>();
        locations.add(new LocationItem(R.drawable.ic_house,"cool1","cool2"));
        //Reminder of what it going to look
        locations.add(new LocationItem(R.drawable.ic_house,"Location","Time(Start time - End time)"));
    }

    public void buildRecylerView()
    {
        //Recycler View Setting up
        rV = findViewById(R.id.recycle);
        rV.setHasFixedSize(true);
        rvL = new LinearLayoutManager(this);
        rVa = new LocationAdapter(locations);

        rV.setLayoutManager(rvL);
        rV.setAdapter(rVa);

        rVa.setOnItemClickListener(new LocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                locations.get(position);
                showMessage(position + "pressed!");
                removeItem(position);
            }
        });
    }

    public void showMessage(String message)
    {
        Toast.makeText(this, message ,Toast.LENGTH_SHORT).show();
    }

    public void insertHour()
    {
        int position = 1;
        locations.add(position, new LocationItem(R.drawable.ic_house,"Location area","Location Time"));
        rVa.notifyItemInserted(position);
    }

    public void insertTransport()
    {
        int position = 1;
        locations.add(position, new LocationItem(R.drawable.ic_transport,"Transport Area","Transport Time"));
        rVa.notifyItemInserted(position);
    }

    public void removeItem(int position)
    {
        locations.remove(position);
        rVa.notifyItemRemoved(position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hourGlass:
                showMessage("HourGlassPressed!");
                insertHour();
                break;
            case R.id.transport:
                showMessage("transportButtonPressed");
                insertTransport();
                break;
        }

    }
}