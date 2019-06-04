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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.support.annotation.NonNull;

public class CalendarFragment extends Fragment {

    private CompactCalendarView calendar;
    private TextView date0, date1, date2;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
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
        final Calendar last = MainActivity.getDate(data, MainActivity.getIndex(data)-1);

        calendar.displayOtherMonthDays(true);
        MainActivity.setToolbarMonth(dateFormatForMonth.format(calendar.getFirstDayOfCurrentMonth()));
        calendar.setUseThreeLetterAbbreviation(true);
        //Event period = new Event(Color.GREEN, last.getTimeInMillis(), "You will have your period");
        //calendar.addEvent(period);
        List<Event> periods = enterPastEvents(data);
        for(Event e : periods)
        {
            calendar.addEvent(e);
        }

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

    public List<Event> enterPastEvents(SharedPreferences data)
    {
        List<Event> pastperiod = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            Calendar date = MainActivity.getDate(data, i);
            pastperiod.add(new Event(Color.GREEN, date.getTimeInMillis(), "You had your period"));
        }
        //add first days of periods, 5 periods into the future
        //get recent period and average cycle length
        Calendar dateRecentPeriod = MainActivity.getDate(data, MainActivity.getIndex(data)-1);
        pastperiod.add(new Event(Color.YELLOW, dateRecentPeriod.getTimeInMillis(), "Your recent have your period"));
        //TODO something goes wrong with the marking, its marking in the past?
        int cycleLength = 86400000* MainActivity.getCyclelength(data); //cyclelength in milliseconds
        for(int i = 1; i <= 5; i++)
        {
            pastperiod.add(new Event(Color.RED, dateRecentPeriod.getTimeInMillis()+(i*cycleLength), "You will have your period"));
        }
        return pastperiod;
    }

    public class PeriodEnter extends Dialog {
        public int bleeding; //level of bleeding, from 0 to 3
        public int cramps;   //level of cramps, from 0 to 3
        public Button ok;    //confirmation button
        public DatePicker datepicker;

        //pop-up for entering data about a chosen date
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
            //Adjust drawables as icons are selected and store the selected values for bleeding and cramps
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
                        case R.id.nobleeding: bleeding = 0; break;
                        case R.id.lightbleeding: bleeding = 1; break;
                        case R.id.mediumbleeding: bleeding = 2; break;
                        case R.id.heavybleeding: bleeding = 3; break;
                    }
                    switch (currentc.getId()){
                        case R.id.nocramp: cramps = 0; break;
                        case R.id.lightcramp: cramps = 1; break;
                        case R.id.mediumcramp: cramps = 2; break;
                        case R.id.heavycramp: cramps = 3; break;
                    }

                    //close pop-up
                    MainActivity.savePeriodInList(data, datepicker.getDayOfMonth(), datepicker.getMonth(), datepicker.getYear(), bleeding, cramps);
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
            //takes care of levels of bleeding being selected or deselected
            previousb.setImageResource(getCorrespondingImage(previousb, false));
            currentb.setImageResource(getCorrespondingImage(currentb, true));
        }
        private void ToggleC()
        {
            //takes care of levels of cramps being selected or deselected
            previousc.setImageResource(getCorrespondingImage(previousc, false));
            currentc.setImageResource(getCorrespondingImage(currentc, true));
        }

        private int getCorrespondingImage(ImageView a, boolean current)
        {
            //returns the drawable ImageView a should change to
            switch (a.getId()){
                case R.id.nocramp: if (current)return R.drawable.circle1; return R.drawable.face1;
                case R.id.lightcramp: if (current)return R.drawable.circle2; return R.drawable.face2;
                case R.id.mediumcramp: if (current)return R.drawable.circle3; return R.drawable.face3;
                case R.id.heavycramp: if (current)return R.drawable.circle4; return R.drawable.face4;
                case R.id.nobleeding: if (current)return R.drawable.blood1circle; return R.drawable.blood1;
                case R.id.lightbleeding: if (current)return R.drawable.blood2circle; return R.drawable.blood2;
                case R.id.mediumbleeding: if (current)return R.drawable.blood3circle; return R.drawable.blood3;
                case R.id.heavybleeding: if (current)return R.drawable.blood4circle; return R.drawable.blood4;
                default: return R.drawable.face4;

            }

        }

    }
}
