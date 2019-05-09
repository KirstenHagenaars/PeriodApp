package com.example.periodtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.CalendarView;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.Date;

public class Calendar extends AppCompatActivity {

    private CalendarView calendar;
    private TextView date0, date1, date2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = (CalendarView) findViewById(R.id.calendarView);
        date0 = (TextView) findViewById(R.id.date0);
        date1 = (TextView) findViewById(R.id.date1);
        date2 = (TextView) findViewById(R.id.date2);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = "last date: " + dayOfMonth + "." + (month+1) + "." + year;
                date0.setText(date);
                Date current = calendar.getDate();
                Date next = current;

            }
        });
    }
}
