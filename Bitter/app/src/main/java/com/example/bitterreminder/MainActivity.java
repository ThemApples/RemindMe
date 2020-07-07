package com.example.bitterreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long START_TIME_IN_MILLIS = 36000000;

    private FloatingActionButton hourButton;
    private FloatingActionButton transportButton;
    private FloatingActionButton stopButton;
    private FloatingActionButton plusButton;
    private FloatingActionButton socialButton;
    private FloatingActionButton shoppingButton;
    private FloatingActionButton studyButton;

    private RecyclerView rV;
    private LocationsAdapter rVa;
    private RecyclerView.LayoutManager rvl;

    private String currentDateTimeString;
    private String startTime;
    private  String showDifference;

    float transitionY = 100f;
    private boolean ifMenu = false;
    OvershootInterpolator interpolator = new OvershootInterpolator();

    private TextView ticker;
    private CountDownTimer cdt;
    private String seeTime;
    private boolean timerRunning;

    private String buttonCondition;
    private boolean stopButtonOn;

    private long timerLeftInMillis = START_TIME_IN_MILLIS;

    ArrayList<LocationItem> locations;

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

        locations = new ArrayList<>();
        locations.add(new LocationItem(R.drawable.ic_house, "Location", "Time(Start time - End time)"));
        lData();
        buildRecyclerView();
    }

    public void showMessage(String message){
        Toast.makeText(this, message ,Toast.LENGTH_SHORT).show();
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

    public void buildRecyclerView()
    {
        rV = findViewById(R.id.recyclerView);
        rV.setHasFixedSize(true);
        rvl = new LinearLayoutManager(this);
        rVa = new LocationsAdapter(locations);

        rV.setLayoutManager(rvl);
        rV.setAdapter(rVa);

        rVa.setOnItemClickListener(new LocationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                String sT= locations.get(position).getTextBottom().substring(0,23);
                String eT =locations.get(position).getTextBottom().substring(25,49);
                String dT = locations.get(position).getTextBottom().substring(50,76);

                locations.get(position);

                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you sure you want to delete this entry?")
                        .setMessage("Location: \n"+ "Start Time: "+sT+"\nEnd position: " + eT
                                + "\nDuration: " + dT )
                        .setPositiveButton("Ok",null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button pos = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                pos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Clicked pop up");
                        removeItem(position);
                        sData();
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void insertRecyclerData()
    {
        int position = 1;
        generateTime();
        if(buttonCondition.equals("hour")){
            locations.add(position, new LocationItem(R.drawable.ic_house,"Location", startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
        }
        else if(buttonCondition.equals("transport")){
            locations.add(position, new LocationItem(R.drawable.ic_transport,"Transport Area",startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
        }
        else if(buttonCondition.equals("Shopping")){
            locations.add(position, new LocationItem(R.drawable.ic_shopping_cart,"Shopping",startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
        }
        else if (buttonCondition.equals("Study")) {
            locations.add(position, new LocationItem(R.drawable.ic_study,"Studying",startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
        }else if(buttonCondition.equals("People"))
        {
            locations.add(position, new LocationItem(R.drawable.ic_people,"SocialEvent",startTime + " - " + currentDateTimeString + "(" +showDifference+")"));
        }

        rVa.notifyItemInserted(position);
    }

    public void removeItem(int position){
        locations.remove(position);
        rVa.notifyItemRemoved(position);
    }

    public void generateTime() {
        currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
    }
    public void generateStartTime(){
        startTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
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

    public void whileRunning()
    {
        stopButtonOn = true;
        stopButton.setVisibility(View.VISIBLE);

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
            buttonCondition = "People";
        }
        else if(number == 5)
        {
            buttonCondition = "Study";
        }
    }

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

    //Timer functions
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

    public void resetTimer()
    {
        cdt.cancel();
        timerLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        stopButton.setVisibility(View.INVISIBLE);
    }

    //Button functions pressed
    public void buttonPressedSetup()
    {
        whileRunning();
        generateStartTime();
        StartTimer();
    }

    public void stopButtonSetup()
    {
        stopRunning();
        calculate();
    }

    //Save & loading data when closing the app
    public void sData()
    {
        SharedPreferences sP = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor ed = sP.edit();
        Gson gson = new Gson();
        String json = gson.toJson(locations);
        ed.putString("List", json);
        ed.apply();
    }

    public void lData()
    {
        SharedPreferences sP = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sP.getString("List", null);
        Type ty = new TypeToken<ArrayList<LocationItem>>() {}.getType();
        locations = gson.fromJson(json, ty);

        if(locations == null)
        {
            locations = new ArrayList<>();
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.plus:
                if(ifMenu){
                    closeMenu();
                }
                else{
                    openMenu();
                }
                break;
            case R.id.hourGlass:
                setCondition(1);
                buttonPressedSetup();
                break;
            case R.id.transport:
                setCondition(2);
                buttonPressedSetup();
                break;
            case R.id.studying:
                setCondition(5);
                buttonPressedSetup();
                break;
            case R.id.shopping:
                setCondition(3);
                buttonPressedSetup();
                break;
            case R.id.social:
                setCondition(4);
                buttonPressedSetup();
                break;
            case R.id.stop_button:
                stopButtonSetup();
                insertRecyclerData();
                sData();
                break;

        }
    }
}