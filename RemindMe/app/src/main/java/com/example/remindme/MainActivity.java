package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private FloatingActionButton plusButton;
    private FloatingActionButton socialButton;
    private FloatingActionButton shoppingButton;
    private FloatingActionButton studyButton;

    float transitionY = 100f;
    boolean ifMenu = false;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    private String buttonCondition;
    private boolean stopButtonOn;

    private String cl;
    private String currentDateTimeString;
    private String startTime;
    FusedLocationProviderClient flp;

    private boolean tf = false;

    private String seeTime;
    private String showDifference;

    private TextView ticker;
    private CountDownTimer cdt;
    private boolean timerRunning;
    private long timerLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plusButton = findViewById(R.id.plus);
        socialButton = findViewById(R.id.social);
        shoppingButton = findViewById(R.id.shopping);
        studyButton = findViewById(R.id.studying);
        hourButton = findViewById(R.id.hourGlass);
        stopButton = findViewById(R.id.stop_button);
        transportButton = findViewById(R.id.transport);

        plusButton.setOnClickListener(this);
        socialButton.setOnClickListener(this);
        shoppingButton.setOnClickListener(this);
        studyButton.setOnClickListener(this);
        hourButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        transportButton.setOnClickListener(this);

        socialButton.setAlpha(0f);
        shoppingButton.setAlpha(0f);
        studyButton.setAlpha(0f);
        hourButton.setAlpha(0f);
        stopButton.setAlpha(0f);
        transportButton.setAlpha(0f);


        ticker = findViewById(R.id.timer);

        createLocationList();
        buildRecylerView();
    }

    private void openMenu()
    {
        ifMenu = true;

        plusButton.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        socialButton.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        shoppingButton.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        studyButton.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        hourButton.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        stopButton.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        transportButton.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

    }

    private void closeMenu()
    {
        ifMenu = false;

        plusButton.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        socialButton.animate().translationY(transitionY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        shoppingButton.animate().translationY(transitionY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        studyButton.animate().translationY(transitionY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        hourButton.animate().translationY(transitionY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        stopButton.animate().translationY(transitionY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        transportButton.animate().translationY(transitionY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }


    public void createLocationList() {
        //Arraylist for location to show user
        locations = new ArrayList<>();
        //Reminder of what it going to look
        locations.add(new LocationItem(R.drawable.ic_house, "Location", "Time(Start time - End time)"));
    }

    public void trueBoolean()
    {
        tf = true;
    }

    public void popUp()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Content")
                .setPositiveButton("Ok",null)
                .setNegativeButton("Cancel",null)
                .show();

        Button pos = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("Clicked pop up");
                trueBoolean();
                dialog.dismiss();
            }
        });
    }

    public void buildRecylerView() {
        final boolean[] temp = new boolean[1];
        //Recycler View Setting up
        rV = findViewById(R.id.recycle);
        rV.setHasFixedSize(true);
        rvL = new LinearLayoutManager(this);
        rVa = new LocationAdapter(locations);

        rV.setLayoutManager(rvL);
        rV.setAdapter(rVa);

        rVa.setOnItemClickListener(new LocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                locations.get(position);
                showMessage(position + "pressed!");

                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Title")
                        .setMessage("Content")
                        .setPositiveButton("Ok",null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button pos = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                pos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Clicked pop up");
                        removeItem(position);
                        dialog.dismiss();
                    }
                });


                //removeItem(position);
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
        cdt.cancel();
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
        locations.add(position, new LocationItem(R.drawable.ic_transport,"Transport Area",startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
        rVa.notifyItemInserted(position);
    }

    public void insertStudy()
    {
        int position = 1;
        generateTime();
        locations.add(position, new LocationItem(R.drawable.ic_study,"Studying",startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
        rVa.notifyItemInserted(position);
    }

    public void insertShopping()
    {
        int position = 1;
        generateTime();
        locations.add(position, new LocationItem(R.drawable.ic_shopping_cart,"Shopping",startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
        rVa.notifyItemInserted(position);
    }

    public void insertSocial()
    {
        int position = 1;
        generateTime();
        locations.add(position, new LocationItem(R.drawable.ic_people,"SocialEvent",startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
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

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
        seeTime = timeLeftFormatted;
        ticker.setText(timeLeftFormatted);
    }
    public void pauseTimer()
    {
        cdt.cancel();
        timerRunning = false;
    }

    public void calculate() {
        String start = startTime.substring(startTime.length() - 10);
        String end =  currentDateTimeString.substring(currentDateTimeString.length() - 10);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date t1 = null;
        Date t2 = null;

        try {
            t1 = format.parse(start);
            t2 = format.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = t2.getTime() - t1.getTime();
        int hours = (int) (diff/(1000*60*60)) %24;
        int minutes = (int) (diff/(1000*60)) % 60;
        int seconds = (int) (diff/1000) % 60;
        showDifference = hours+" Hours "+minutes+" Minutes " +seconds+ " Seconds " ;
    }

    @SuppressLint("RestrictedApi")
    public void whileRunning()
    {
        stopButtonOn = true;
        stopButton.setVisibility(View.VISIBLE);
        //hourButton.setVisibility(View.INVISIBLE);

        plusButton.setEnabled(!stopButtonOn);
        hourButton.setEnabled(!stopButtonOn);
        transportButton.setEnabled(!stopButtonOn);
        studyButton.setEnabled(!stopButtonOn);
        socialButton.setEnabled(!stopButtonOn);
        shoppingButton.setEnabled(!stopButtonOn);
    }

    @SuppressLint("RestrictedApi")
    public void stopRunning()
    {
        generateTime();
        resetTimer();
        stopButtonOn = false;

        plusButton.setEnabled(!stopButtonOn);
        hourButton.setEnabled(!stopButtonOn);
        transportButton.setEnabled(!stopButtonOn);
        studyButton.setEnabled(!stopButtonOn);
        socialButton.setEnabled(!stopButtonOn);
        shoppingButton.setEnabled(!stopButtonOn);
    }

    public void setCondition(int number)
    {
        if(number == 1){
            buttonCondition = "hour";
        }
        else if(number == 2)
        {
            buttonCondition = "transport";
        }
        else if(number == 3)
        {
            buttonCondition = "Shopping";
        }
        else if(number == 4)
        {
            buttonCondition = "Study";
        }
        else if(number == 5)
        {
            buttonCondition = "People";
        }
    }

    public void filer()
    {
        String fileName = "ok.txt";
        String context = "Hello this is this content of the text file.";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);

        if(getPermission()) {


            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(context.getBytes());
                fos.close();
                showMessage("Saved");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                showMessage("File Not Found");
            } catch (IOException e) {
                e.printStackTrace();
                showMessage("Error Saving");
            }
        }

    }

    public boolean getPermission()
    {
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State","Yes it is writeable");
            return true;
        }
        else {
            return false;
        }
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
            case R.id.plus:
                if(ifMenu){
                    filer();
                    closeMenu();
                }
                else {
                    openMenu();
                }
                break;
            case R.id.hourGlass:
                showMessage("HourGlassPressed!");
                whileRunning();
                generateStartTime();
                setCondition(1);
                StartTimer();
                //locateYou();
                //getLocation();
                break;
            case R.id.transport:
                whileRunning();
                generateStartTime();
                setCondition(2);
                StartTimer();
                break;
            case R.id.shopping:
                whileRunning();
                generateStartTime();
                setCondition(3);
                StartTimer();
                break;
            case R.id.studying:
                whileRunning();
                generateStartTime();
                setCondition(4);
                StartTimer();
                break;
            case R.id.social:
                whileRunning();
                generateStartTime();
                setCondition(5);
                StartTimer();
                break;
            case R.id.stop_button:
                if(buttonCondition.equals("hour"))
                {
                    stopRunning();
                    calculate();
                    insertHour();
                }
                else if(buttonCondition.equals("transport"))
                {
                    stopRunning();
                    calculate();
                    insertTransport();
                }
                else if(buttonCondition.equals("Shopping"))
                {
                    stopRunning();
                    calculate();
                    insertShopping();
                }
                else if(buttonCondition.equals("Study"))
                {
                    stopRunning();
                    calculate();
                    insertStudy();
                }
                else if(buttonCondition.equals("People"))
                {
                    stopRunning();
                    calculate();
                    insertSocial();
                }
                break;
        }

    }
}