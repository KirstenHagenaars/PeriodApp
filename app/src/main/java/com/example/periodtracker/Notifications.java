package com.example.periodtracker;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Calendar;

public class Notifications extends Fragment {
    Context current;

    @SuppressLint("ValidFragment")
    public Notifications(Context current) {
        this.current = current;
    }

    public Notifications() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.activity_notifications, container, false);
        //get the current time

        //define the switches
        Switch dailyReminder = (Switch) v.findViewById(R.id.switchdaily);
        Switch periodReminder = (Switch) v.findViewById(R.id.switchperiod);

        //define a listener for every switch
        dailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                if (isChecked)
                {
                    //input daily time
                    //run notification daily
                    Intent intent = new Intent(current, sendNotification.class);
                    intent.setAction("DAILY_NOTIFICATION");
                    PendingIntent broadcast = PendingIntent.getBroadcast(current.getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) current.getSystemService(Context.ALARM_SERVICE);
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    cal.set(Calendar.HOUR_OF_DAY, 17);
                    cal.set(Calendar.MINUTE, 5);

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);

                }
            }
        });

        periodReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", ""+isChecked);
                if (isChecked)
                {
                    Intent intent = new Intent(current, sendNotification.class);
                    intent.setAction("PERIOD_NOTIFICATION");
                    PendingIntent broadcast = PendingIntent.getBroadcast(current.getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) current.getSystemService(Context.ALARM_SERVICE);
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    //get predicted period days
                    cal.set(Calendar.HOUR_OF_DAY, 16);
                    cal.set(Calendar.MINUTE, 30);

                    //alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                }
            }

        });


        return v;
    }

}
