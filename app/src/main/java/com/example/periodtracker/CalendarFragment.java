package com.example.periodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private CompactCalendarView calendar;
    private TextView date0, date1, date2;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private List<Event> periods;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View calendarFragment = inflater.inflate(R.layout.activity_calendar, container, false);
        calendar = (CompactCalendarView) calendarFragment.findViewById(R.id.calendar);

        date0 = (TextView) calendarFragment.findViewById(R.id.date0);
        date1 = (TextView) calendarFragment.findViewById(R.id.date1);
        date2 = (TextView) calendarFragment.findViewById(R.id.date2);
        final SharedPreferences data = this.getActivity().getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        final Calendar last = Calendar.getInstance();
        last.set(data.getInt("year", 0), data.getInt("month", 0), data.getInt("day", 0));

        calendar.displayOtherMonthDays(true);
        MainActivity.setToolbarMonth(dateFormatForMonth.format(calendar.getFirstDayOfCurrentMonth()));
        calendar.setUseThreeLetterAbbreviation(true);
        Event period = new Event(Color.GREEN, last.getTimeInMillis(), "You will have your period");
        calendar.addEvent(period);

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //TODO check with list whether event is there and show data of event
                //Context context = getContext();

                if(dateClicked.equals(last))
                {
                    Toast.makeText(getContext(), "Date of period", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "You're free!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                MainActivity.setToolbarMonth(dateFormatForMonth.format(calendar.getFirstDayOfCurrentMonth()));
            }
        });
        return calendarFragment;
    }



}
