package com.example.periodtracker;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.example.periodtracker.MainActivity.pref;

public class Notifications extends Fragment {
    Context current;
    int hour;
    int min;
    Switch dailyReminder;
    Switch periodReminder;
    Switch periodAdvanceReminder;
    Switch fertileReminder;

    //booleans to prevent alarmmanagers from being set again when fragment is reopened
    Boolean dailyReminderChecked;
    Boolean periodReminderChecked;
    Boolean periodAdvanceReminderChecked;
    Boolean fertileReminderChecked;

    @SuppressLint("ValidFragment")
    public Notifications(Context current, int hour, int min, SharedPreferences data) {
        this.current = current;
        this.hour = hour;
        this.min = min;
        //get values of switches from data
        this.dailyReminderChecked = intToBool(data.getInt("dailyReminderChecked", 0));
        this.periodReminderChecked = intToBool(data.getInt("periodReminderChecked", 0));
        this.periodAdvanceReminderChecked = intToBool(data.getInt("periodAdvanceReminderChecked", 0));
        this.fertileReminderChecked = intToBool(data.getInt("fertileReminderChecked", 0));
    }

    public Notifications() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final SharedPreferences data = this.getActivity().getSharedPreferences(pref, Context.MODE_PRIVATE);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_notifications, container, false);

        //define the switches
        this.dailyReminder = (Switch) v.findViewById(R.id.switchdaily);
        this.periodReminder = (Switch) v.findViewById(R.id.switchperiod);
        this.periodAdvanceReminder = (Switch) v.findViewById(R.id.switchperiodadvance);
        this.fertileReminder = (Switch) v.findViewById(R.id.switchfertile);

        //sets the switches
        dailyReminder.setChecked( intToBool(data.getInt("dailyReminderChecked", 0)));
        periodReminder.setChecked( intToBool(data.getInt("periodReminderChecked", 0)));
        periodAdvanceReminder.setChecked( intToBool(data.getInt("periodAdvanceReminderChecked", 0)));
        fertileReminder.setChecked( intToBool(data.getInt("fertileReminderChecked", 0)));

        //define a listener for every switch and set an alarm for the notification
        /*sets a daily notification to remind the user she needs to take the birth
        control pill, the user can select her prefered daily time in a pop-up*/
        dailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !dailyReminderChecked) {
                    //open pop-up to input daily time
                    //run notification daily
                    dailyReminderChecked = true;
                    new TimePickerDialog(getActivity());
                }
                else if (!isChecked)
                {
                    dailyReminderChecked = false;
                    //cancel alarm manager
                    setDaily(false);
                }
                SharedPreferences.Editor editor = data.edit();
                editor.putInt("dailyReminderChecked", boolToInt(dailyReminder.isChecked()));
                editor.commit();
            }
        });

        /*sets a notification at 8 AM on the day the period is predicted to start*/
        periodReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(current, sendNotification.class);
                intent.setAction("PERIOD_NOTIFICATION");
                PendingIntent broadcast = PendingIntent.getBroadcast(current.getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) current.getSystemService(Context.ALARM_SERVICE);
                if (isChecked && !periodReminderChecked) {
                    periodReminderChecked = true;

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    //get the predicted date of the start of the next period
                    Calendar predicted = Statistics.recentDate(data);
                    predicted.add(predicted.DAY_OF_MONTH,  MainActivity.getCyclelength(data));
                    cal.set(Calendar.DAY_OF_MONTH, predicted.DAY_OF_MONTH);
                    cal.set(Calendar.HOUR_OF_DAY, 8);
                    cal.set(Calendar.MINUTE, 0);
                    //repeat the alarm every cycle
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), TimeUnit.DAYS.toMillis(MainActivity.getCyclelength(data)), broadcast);
                }
                else if (!isChecked)
                {
                    periodReminderChecked = false;
                    //cancel alarm manager
                    alarmManager.cancel(broadcast);
                }
                SharedPreferences.Editor editor = data.edit();
                editor.putInt("periodReminderChecked", boolToInt(periodReminder.isChecked()));
                editor.commit();
            }

        });

        /*sets a notification at 8 AM 3 days before the period is predicted to start*/
        periodAdvanceReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
                Intent intent = new Intent(current, sendNotification.class);
                intent.setAction("PERIODADVANCE_NOTIFICATION");
                PendingIntent broadcast = PendingIntent.getBroadcast(current.getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) current.getSystemService(Context.ALARM_SERVICE);
                if (isChecked && !periodAdvanceReminderChecked) {
                    periodAdvanceReminderChecked = true;

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    //get the date the period is predicted to start and substract 3 days
                    Calendar predicted = Statistics.recentDate(data);
                    predicted.add(predicted.DAY_OF_MONTH,  MainActivity.getCyclelength(data) - 3);
                    cal.set(Calendar.DAY_OF_MONTH, predicted.DAY_OF_MONTH);
                    cal.set(Calendar.HOUR_OF_DAY, 8);
                    cal.set(Calendar.MINUTE, 0);
                    //repeat the alarm every cycle
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), TimeUnit.DAYS.toMillis(MainActivity.getCyclelength(data)), broadcast);
                }
                else if (!isChecked)
                {
                    periodAdvanceReminderChecked = false;
                    //cancel alarm manager
                    alarmManager.cancel(broadcast);
                }
                SharedPreferences.Editor editor = data.edit();
                editor.putInt("periodAdvanceReminderChecked", boolToInt(periodAdvanceReminder.isChecked()));
                editor.commit();
            }
        });

        fertileReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
                Intent intent = new Intent(current, sendNotification.class);
                intent.setAction("FERTILE_NOTIFICATION");
                PendingIntent broadcast = PendingIntent.getBroadcast(current.getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) current.getSystemService(Context.ALARM_SERVICE);
                if (isChecked && !fertileReminderChecked) {
                    fertileReminderChecked = true;

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    //get the date the fertile window is predicted to open
                    Calendar predicted = Statistics.recentDate(data);
                    predicted.add(predicted.DAY_OF_MONTH,  MainActivity.getCyclelength(data)/2 - 4);
                    cal.set(Calendar.DAY_OF_MONTH, predicted.DAY_OF_MONTH);
                    cal.set(Calendar.HOUR_OF_DAY, 8);
                    cal.set(Calendar.MINUTE, 0);
                    //repeat the alarm every cycle
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), TimeUnit.DAYS.toMillis(MainActivity.getCyclelength(data)), broadcast);
                }
                else if (!isChecked)
                {
                    fertileReminderChecked = false;
                    //cancel alarm manager
                    alarmManager.cancel(broadcast);
                }
                SharedPreferences.Editor editor = data.edit();
                editor.putInt("fertileReminderChecked", boolToInt(fertileReminder.isChecked()));
                editor.commit();
            }
        });

        return v;
    }

    public void onPause() {
        //save switch states to prevent alarms being initialized again after reopening the fragment
        this.dailyReminderChecked = dailyReminder.isChecked();
        this.periodReminderChecked = periodReminder.isChecked();
        this.periodAdvanceReminderChecked = periodAdvanceReminder.isChecked();
        this.fertileReminderChecked = fertileReminder.isChecked();
        super.onPause();
    }

    private boolean intToBool(int a)
    {
        //converts an integer to a boolean
        if (a>0)
            return true;
        return false;
    }

    private int boolToInt(boolean a)
    {
        //converts a boolean to an integer
        if (a==false)
            return 0;
        return 1;
    }

    private void setTime(int hour, int min) {
        //update the time to the prefered time for the user to get the daily notification
        this.hour = hour;
        this.min = min;
        setDaily(true);
    }

    private void setDaily(Boolean activate)
    {
        //set the daily notification
        Intent intent = new Intent(current, sendNotification.class);
        intent.setAction("DAILY_NOTIFICATION");
        PendingIntent broadcast = PendingIntent.getBroadcast(current.getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) current.getSystemService(Context.ALARM_SERVICE);
        if (activate)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            //give the calendar the time selected with the timepicker
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, min);
            //repeat the alarm every day
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }
        else
        {
            //cancel the daily notification
            alarmManager.cancel(broadcast);
        }

    }

    public class TimePickerDialog extends Dialog {
        //pop-up to select the time the user wants to get a daily notification
        public Button ok;    //confirmation button
        private TimePicker timePicker1;


        public TimePickerDialog(@NonNull Context context) {
            super(context);
            this.setContentView(R.layout.timepickerpopup);
            final SharedPreferences data = context.getSharedPreferences(pref, Context.MODE_PRIVATE);
            this.show();
            ok = this.findViewById(R.id.confirm);
            timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //save the hour and minute selected and close the pop-up
                    setTime(timePicker1.getHour(), timePicker1.getMinute());
                    close();

                }
            });
        }
        public void close()
        {
            this.dismiss();
        }

    }
}
