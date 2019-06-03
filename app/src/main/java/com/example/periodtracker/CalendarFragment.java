package com.example.periodtracker;

import android.content.Context;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.support.annotation.NonNull;

public class CalendarFragment extends Fragment {

    private CompactCalendarView calendar;
    private TextView date0, date1, date2;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private List<Event> periods;
    private Button enterdate;

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View calendarFragment = inflater.inflate(R.layout.activity_calendar, container, false);
        calendar = (CompactCalendarView) calendarFragment.findViewById(R.id.calendar);
        enterdate = (Button) calendarFragment.findViewById(R.id.enterdate);
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

        enterdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PeriodEnter(getActivity());
            }
        });
        return calendarFragment;
    }

    public class PeriodEnter extends Dialog {
        public String bleeding; //level of bleeding, from 0 to 3
        public String cramps;   //level of cramps, from 0 to 3
        public Button ok;    //confirmation button
        public DatePicker datepicker;

        ImageView b0, b1, b2, b3, c0, c1, c2, c3, previousb, currentb, previousc, currentc;

        public PeriodEnter(@NonNull Context context) {
            super(context);
            this.setContentView(R.layout.enterperiodpopup);
            b0 = this.findViewById(R.id.nobleeding);
            b1 = this.findViewById(R.id.lightbleeding);
            b2 = this.findViewById(R.id.mediumbleeding);
            b3 = this.findViewById(R.id.heavybleeding);
            c0 = this.findViewById(R.id.nocramp);
            c1 = this.findViewById(R.id.lightcramp);
            c2 = this.findViewById(R.id.mediumcramp);
            c3 = this.findViewById(R.id.heavycramp);
            previousb = this.findViewById(R.id.nobleeding);
            currentb = this.findViewById(R.id.nobleeding);

            previousc = this.findViewById(R.id.nocramp);
            currentc = this.findViewById(R.id.nocramp);
            datepicker = this.findViewById(R.id.datepicker);
            final SharedPreferences data = context.getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = data.edit();
            this.show();
            ok = this.findViewById(R.id.confirm);
            b0.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    previousb = currentb;
                    currentb = b0;
                    ToggleB();
                }
            });
            b1.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    previousb = currentb;
                    currentb = b1;
                    ToggleB();
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    previousb = currentb;
                    currentb = b2;
                    ToggleB();
                }
            });
            b3.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    previousb = currentb;
                    currentb = b3;
                    ToggleB();
                }
            });
            c0.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    previousc = currentc;
                    currentc = c0;
                    ToggleC();
                }
            });
            c1.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    previousc = currentc;
                    currentc = c1;
                    ToggleC();
                }
            });
            c2.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    previousc = currentc;
                    currentc = c2;
                    ToggleC();
                }
            });
            c3.setOnClickListener(new View.OnClickListener() {
                //@Override
                public void onClick(View v) {
                    previousc = currentc;
                    currentc = c3;
                    ToggleC();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (currentb.getId()){
                        case R.id.nobleeding: bleeding = "0"; break;
                        case R.id.lightbleeding: bleeding = "1"; break;
                        case R.id.mediumbleeding: bleeding = "2"; break;
                        case R.id.heavybleeding: bleeding = "3"; break;
                    }
                    switch (currentc.getId()){
                        case R.id.nocramp: cramps = "0"; break;
                        case R.id.lightcramp: cramps = "1"; break;
                        case R.id.mediumcramp: cramps = "2"; break;
                        case R.id.heavycramp: cramps = "3"; break;
                    }

                    //close pop-up
                    Calendar date = Calendar.getInstance();
                    date.set(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth());
                    MainActivity.savePeriodInList(data, date.toString(), bleeding, cramps);
                    close();
                }
            });
        }
        public void close()
        {
            this.dismiss();
        }
        private void ToggleB()
        {
            previousb.setImageResource(R.drawable.ic_action_name);
            currentb.setImageResource(R.drawable.ic_action_name2);
        }
        private void ToggleC()
        {
            previousc.setImageResource(R.drawable.ic_action_name);
            currentc.setImageResource(R.drawable.ic_action_name2);
        }
    }
}
