package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long START_TIME_IN_MILLIS = 36000000;
    private RecyclerView rV;
    private LocationAdapter rVa;
    private RecyclerView.LayoutManager rvL;

    private ArrayList<LocationItem> locations;
    private FloatingActionButton hourButton;
    private FloatingActionButton transportButton;
    private FloatingActionButton stopButton;

    private String cl;
    private String currentDateTimeString;
    private String startTime;
    FusedLocationProviderClient flp;

    private String seeTime;
    private int hour=0;
    private int minute=0;
    private int second=0;
    private String showDifference;

    private TextView ticker;
    private CountDownTimer cdt;
    private boolean timerRunning;
    private long timerLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hourButton = findViewById(R.id.hourGlass);
        hourButton.setOnClickListener(this);

        stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(this);

        transportButton = findViewById(R.id.transport);
        transportButton.setOnClickListener(this);

        ticker = findViewById(R.id.timer);

        createLocationList();
        buildRecylerView();
    }

    public void createLocationList() {
        //Arraylist for location to show user
        locations = new ArrayList<>();
        locations.add(new LocationItem(R.drawable.ic_house, "cool1", "cool2"));
        //Reminder of what it going to look
        locations.add(new LocationItem(R.drawable.ic_house, "Location", "Time(Start time - End time)"));
    }

    public void buildRecylerView() {
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

    public void generateTime() {
        currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
    }
    public void generateStartTime(){
        startTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
    }

    public void locateYou() {
        flp = LocationServices.getFusedLocationProviderClient(this);
    }


    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        flp.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //location time
                Location lo = task.getResult();
                if (lo != null) {
                    try {
                        //Use geoCoder
                        Geocoder geo = new Geocoder(MainActivity.this,
                                Locale.getDefault());
                        List<Address> address = geo.getFromLocation(
                                lo.getLatitude(), lo.getLongitude(), 1
                        );
                        cl = address.get(0).toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message ,Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("RestrictedApi")
    public void resetTimer()
    {
        timerLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        stopButton.setVisibility(View.INVISIBLE);
    }
    public void insertHour(){
        int position = 1;
        generateTime();
        locations.add(position, new LocationItem(R.drawable.ic_house,"Location", startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
        rVa.notifyItemInserted(position);
    }

    public void insertTransport(){
        int position = 1;
        generateTime();
        locations.add(position, new LocationItem(R.drawable.ic_transport,"Transport Area",currentDateTimeString));
        rVa.notifyItemInserted(position);
    }

    public void removeItem(int position){
        locations.remove(position);
        rVa.notifyItemRemoved(position);
    }

    public void StartTimer()
    {
        cdt = new CountDownTimer(timerLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        timerRunning = true;
    }

    public void updateCountDownText(){
        int hours = (int) (timerLeftInMillis/(1000*60*60)) %24;
        int minutes = (int) (timerLeftInMillis/(1000*60)) % 60;
        int seconds = (int) (timerLeftInMillis/1000) % 60;

        //loop to show how long you were in the building for
        if(seconds >=0){
            second++;
            if(second == 60)
            {
                second = 0;
                minute++;
                if(minute == 60)
                {
                    minute = 0;
                    hour++;
                }
            }
        }

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
        seeTime = timeLeftFormatted;
        ticker.setText(timeLeftFormatted);
    }
    public void pauseTimer()
    {
        cdt.cancel();
        timerRunning = false;
    }

    public void timeDifference()
    {
        showDifference = hour + " Hours " + minute + " Minutes " + second+ " Seconds";
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        //Check  permission
        if(ActivityCompat.checkSelfPermission(MainActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //getLocation();
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
        switch (v.getId()){
            case R.id.hourGlass:
                showMessage("HourGlassPressed!");
                stopButton.setVisibility(View.VISIBLE);
                generateStartTime();
                //locateYou();
                //getLocation();
                if(timerRunning) {
                    pauseTimer();
                    hourButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                    timeDifference();
                    showMessage(showDifference);
                    insertHour();
                }else {
                    StartTimer();
                    hourButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                }
                //hourButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_house));
                //insertHour();
                break;
            case R.id.transport:
                showMessage("transportButtonPressed");
                stopButton.setVisibility(View.VISIBLE);
                if(timerRunning) {
                    pauseTimer();
                    hourButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                }else {
                    StartTimer();
                    hourButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                }
                //insertTransport();
                break;
            case R.id.stop_button:
                resetTimer();
                break;
        }

    }
}