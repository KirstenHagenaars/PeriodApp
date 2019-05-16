package com.example.periodtracker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.Date;

public class Calendar extends Fragment {

    private CalendarView calendar;
    private TextView date0, date1, date2;

    public Calendar() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = (CalendarView) getView().findViewById(R.id.calendarView);
        date0 = (TextView) getView().findViewById(R.id.date0);
        date1 = (TextView) getView().findViewById(R.id.date1);
        date2 = (TextView) getView().findViewById(R.id.date2);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = "last date: " + dayOfMonth + "." + (month+1) + "." + year;
                date0.setText(date);
                //Date current = (java.util.Date) calendar.getDate();
                //Date current = calendar.getDate();
                //Date next = current;

            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_calendar, container, false);
    }



}
