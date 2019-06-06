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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class Notifications extends Fragment {
    Context current;
    int hour;
    int min;
    Switch dailyReminder;
    Switch periodReminder;
    Switch periodAdvanceReminder;
    Switch fertileReminder;
    Boolean dailyReminderChecked;
    Boolean periodReminderChecked;
    Boolean periodAdvanceReminderChecked;
    Boolean fertileReminderChecked;
    //TODO: notifications are coming in immediately

    @SuppressLint("ValidFragment")
    public Notifications(Context current, int hour, int min) {
        this.current = current;
        this.hour = hour;
        this.min = min;
        this.dailyReminderChecked = false;
        this.periodReminderChecked = false;
        this.periodAdvanceReminderChecked = false;
        this.fertileReminderChecked = false;
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
        final SharedPreferences data = this.getActivity().getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_notifications, container, false);
        //get the current time

        //define the switches
        this.dailyReminder = (Switch) v.findViewById(R.id.switchdaily);
        this.periodReminder = (Switch) v.findViewById(R.id.switchperiod);
        this.periodAdvanceReminder = (Switch) v.findViewById(R.id.switchperiodadvance);
        this.fertileReminder = (Switch) v.findViewById(R.id.switchfertile);

        //define a listener for every switch
        dailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
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
            }
        });

        periodReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v("Switch State=", "" + isChecked);
                Intent intent = new Intent(current, sendNotification.class);
                intent.setAction("PERIOD_NOTIFICATION");
                PendingIntent broadcast = PendingIntent.getBroadcast(current.getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) current.getSystemService(Context.ALARM_SERVICE);
                if (isChecked && !periodReminderChecked) {
                    periodReminderChecked = true;

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    //TODO get predicted period days
                    Calendar predicted = Statistics.recentDate(data);
                    predicted.add(predicted.DAY_OF_MONTH,  MainActivity.getCyclelength(data));
                    cal.set(Calendar.DAY_OF_MONTH, predicted.DAY_OF_MONTH);
                    cal.set(Calendar.HOUR_OF_DAY, 10);
                    cal.set(Calendar.MINUTE, 45);

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 86400000* MainActivity.getCyclelength(data), broadcast);
                }
                else if (!isChecked)
                {
                    dailyReminderChecked = false;
                    //cancel alarm manager
                    alarmManager.cancel(broadcast);
                }
            }

        });

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
                    //TODO get predicted period days and substract 3 days
                    Calendar predicted = Statistics.recentDate(data);
                    predicted.add(predicted.DAY_OF_MONTH,  MainActivity.getCyclelength(data) - 3);
                    cal.set(Calendar.DAY_OF_MONTH, predicted.DAY_OF_MONTH);
                    cal.set(Calendar.HOUR_OF_DAY, 10);
                    cal.set(Calendar.MINUTE, 45);

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 86400000* MainActivity.getCyclelength(data), broadcast);
                }
                else if (!isChecked)
                {
                    dailyReminderChecked = false;
                    //cancel alarm manager
                    alarmManager.cancel(broadcast);
                }
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
                    //TODO get first date of fertile window
                    Calendar predicted = Statistics.recentDate(data);
                    predicted.add(predicted.DAY_OF_MONTH,  MainActivity.getCyclelength(data)/2 - 4);
                    cal.set(Calendar.DAY_OF_MONTH, predicted.DAY_OF_MONTH);
                    cal.set(Calendar.HOUR_OF_DAY, 10);
                    cal.set(Calendar.MINUTE, 45);

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 86400000* MainActivity.getCyclelength(data), broadcast);
                }
                else if (!isChecked)
                {
                    dailyReminderChecked = false;
                    //cancel alarm manager
                    alarmManager.cancel(broadcast);
                }
            }
        });

        return v;
    }

    public void onPause() {
        Log.v("OnResume", "YEESS");
        this.dailyReminderChecked = dailyReminder.isChecked();
        this.periodReminderChecked = periodReminder.isChecked();
        this.periodAdvanceReminderChecked = periodAdvanceReminder.isChecked();
        this.fertileReminderChecked = fertileReminder.isChecked();
        super.onPause();
    }

    private void setTime(int hour, int min) {
        this.hour = hour;
        this.min = min;
        setDaily(true);
    }

    private void setDaily(Boolean activate)
    {
        Intent intent = new Intent(current, sendNotification.class);
        intent.setAction("DAILY_NOTIFICATION");
        PendingIntent broadcast = PendingIntent.getBroadcast(current.getApplicationContext(), 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) current.getSystemService(Context.ALARM_SERVICE);
        if (activate)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());

            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, min);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);
        }
        else
        {
            alarmManager.cancel(broadcast);
        }

    }

    public class TimePickerDialog extends Dialog {
        public Button ok;    //confirmation button
        private TimePicker timePicker1;


        public TimePickerDialog(@NonNull Context context) {
            super(context);
            this.setContentView(R.layout.timepickerpopup);
            final SharedPreferences data = context.getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = data.edit();
            this.show();
            ok = this.findViewById(R.id.confirm);
            timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //close pop-up
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
