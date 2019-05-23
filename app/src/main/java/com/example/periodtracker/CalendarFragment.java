package com.example.periodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.provider.CalendarContract;

import java.util.Calendar;

public class CalendarFragment extends Fragment {

    private CalendarView calendar;
    private TextView date0, date1, date2;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View calendarFragment = inflater.inflate(R.layout.activity_calendar, container, false);
        calendar = (CalendarView) calendarFragment.findViewById(R.id.calendar);
        date0 = (TextView) calendarFragment.findViewById(R.id.date0);
        date1 = (TextView) calendarFragment.findViewById(R.id.date1);
        date2 = (TextView) calendarFragment.findViewById(R.id.date2);
        final SharedPreferences data = this.getActivity().getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = "last date: " + dayOfMonth + "." + (month+1) + "." + year;
                date0.setText(date);
                Calendar next = Calendar.getInstance();
                next.set(year, month, dayOfMonth);
                int cycle = data.getInt("cyclelength", 0);
                next.add(Calendar.DAY_OF_MONTH, cycle);
                date1.setText(date1.getText() + " " + next.get(Calendar.DAY_OF_MONTH) + "." + (next.get(Calendar.MONTH)+1) + "." + next.get(Calendar.YEAR));
                //int test = data.getInt("cycle", 0);
                //date1.setText("test" + test);
                Calendar test = Calendar.getInstance();
                test.add(Calendar.DAY_OF_MONTH, 3);
                calendar.setDate((test.getTimeInMillis()));
            }
        });
        return calendarFragment;
    }



}
