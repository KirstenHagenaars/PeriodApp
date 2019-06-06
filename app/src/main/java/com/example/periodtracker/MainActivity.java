package com.example.periodtracker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static android.app.PendingIntent.getActivity;
/*
"cycleInitial" --> whether or not initialized
"cyclelength"
"periodlength"
"day" --> of last period
"month"
"year"
"periods"
 */

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    final Fragment home = new Home();
    final Fragment calendar = new CalendarFragment();
    final Fragment statistics = new Statistics();
    final Fragment notifications = new Notifications(this, 21,0);
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = statistics;
    static final String pref = "data";
    static Toolbar toolbar;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences data = getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        String index = "index";

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.appbar);
        setActionBar(toolbar);

        //The initialization of our bottom navigation bar, using the fragmentmanager fm
        fm.beginTransaction().add(R.id.main_container, calendar, "4").hide(calendar).commit();
        fm.beginTransaction().add(R.id.main_container, notifications, "3").hide(notifications).commit();
        fm.beginTransaction().add(R.id.main_container, statistics, "2").hide(statistics).commit();
        fm.beginTransaction().add(R.id.main_container, home, "1").commit();
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        //A listener to handle clicks on the navigation bar and change between fragments
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(home).detach(home).attach(home).commit();
                        toolbar.setTitle("Home");
                        toolbar.setSubtitle("");
                        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
                        toolbar.setTitleTextAppearance(MainActivity.this,R.style.navigation);
                        active = home;
                        return true;
                    case R.id.navigation_calendar:
                        fm.beginTransaction().hide(active).show(calendar).commit();
                        fm.beginTransaction().detach(calendar).attach(calendar).commit();
                        toolbar.setTitle("Calendar");
                        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
                        toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));
                        toolbar.setSubtitleTextAppearance(MainActivity.this,R.style.navigation);
                        toolbar.setTitleTextAppearance(MainActivity.this,R.style.navigation);
                        active = calendar;
                        return true;
                    case R.id.navigation_statistics:
                        fm.beginTransaction().hide(active).show(statistics).detach(statistics).attach(statistics).commit();
                        toolbar.setTitle("Statistics");
                        toolbar.setSubtitle("");
                        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
                        toolbar.setTitleTextAppearance(MainActivity.this,R.style.navigation);
                        active = statistics;
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(notifications).commit();
                        fm.beginTransaction().detach(notifications).attach(notifications).commit();
                        toolbar.setTitle("Notifications");
                        toolbar.setSubtitle("");
                        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
                        toolbar.setTitleTextAppearance(MainActivity.this,R.style.navigation);
                        active = notifications;
                        return true;
                }
                return false;
            }
        });

        /*If the user opens the app for the first time, a pop-up appears where the user enters
        //her average cycle length and period length and the date of the start of the most recent
        period. These will serve as starting values*/
        if (!data.contains(index)||data.getInt("cyclelength", 0)==0 || data.getInt("periodlength", 0)==0) {
            new InitialDialog(MainActivity.this);
            editor.putInt("index", 1);
            editor.apply();
        }
    }

    public static int getCyclelength(SharedPreferences data)
    {
        //retrieves the cycle length from the SharedPreferences
        return data.getInt("cyclelength", 28);
    }

    public static int getPeriodlength(SharedPreferences data)
    {
        //retrieves the cycle length from the SharedPreferences
        return data.getInt("periodlength", 5);
    }

    public static boolean lastDayOfMonth(int day, int month)
    {
        return true; //TODO
    }

    public static Boolean firstDayOfPeriod(SharedPreferences data, int day, int month, int year, boolean init)
    {
        Calendar last = Statistics.recentDate(data);
        Calendar saving = Calendar.getInstance();
        saving.set(year, month, day);
        long millis = saving.getTimeInMillis()-last.getTimeInMillis();
        int diff = (int) TimeUnit.MILLISECONDS.toDays(millis);
        int cyclelength = getCyclelength(data);
        if(diff >= cyclelength/2 || init)
            return true;
        else
            return false;
    }

    //When we save the date we save the month as it comes. So January will be 0, February 1 and so on
    public static void savePeriodInList(SharedPreferences data, int day, int month, int year, int bleeding, int cramps, boolean init)
    {
        if (firstDayOfPeriod(data, day, month, year, init))
        {
            SharedPreferences.Editor editor = data.edit();

            editor.putInt("day" + getIndex(data), day).apply();
            editor.putInt("month" + getIndex(data), month).apply();
            editor.putInt("year" + getIndex(data), year).apply();
            editor.putInt("bleeding" + getIndex(data), bleeding).apply();
            editor.putInt("cramps" + getIndex(data), cramps).apply();
            incrementIndex(data);
        }

    }

    public static int getIndex(SharedPreferences data){
        return data.getInt("index", 0);
    }

    public static void incrementIndex(SharedPreferences data){
        SharedPreferences.Editor editor = data.edit();
        if(getIndex(data)+1 > 10)//so we always ony´ly save the 10 most recent events
            editor.putInt("index", 0);
        else
            editor.putInt("index", getIndex(data)+1);
        editor.apply();
    }

    //When we just want to retrieve a date we also leave the month as it is, so starting with Jan 0 going to Dec 11
    public static Calendar getDate(SharedPreferences data, int index)
    {
        Calendar date = Calendar.getInstance();
        date.set(data.getInt("year" + index, 0), data.getInt("month" + index, 0), data.getInt("day" + index, 0));
        return date;
    }

    //only once we print dates we care about the month and SimpleDateFormat changes Jan 0 to Jan 1.
    public static String printDate(Calendar date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date.getTime());
    }

    public void setCycleInitial (int c)
    {
        //stores the initial cycle length in SharedPreferences data
        SharedPreferences data = getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("cyclelength", c);
        editor.commit();
    }

    public void setPeriodInitial(int d)
    {
        //stores the initial period length in SharedPreferences data
        SharedPreferences data = getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("periodlength", d);
        editor.commit();
    }

    public static void setToolbarMonth(String navigation)
    {
        toolbar.setSubtitle(navigation);
    }

    public class InitialDialog extends Dialog {
        //pop-up that appears when the app is opened for the first time, requests the user to enter data
        public EditText cycle, period;
        public DatePicker datePicker;
        public Button ok;
        public InitialDialog(@NonNull final Context context) {
            super(context);
            this.setContentView(R.layout.initializationpopup);
            this.show();
            cycle = this.findViewById(R.id.length);
            period = this.findViewById(R.id.period);
            datePicker = this.findViewById(R.id.datepicker);
            ok = this.findViewById(R.id.confirm);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.this.savePeriodInList(context.getSharedPreferences(pref, Context.MODE_PRIVATE), datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear(), 0, 0, true);
                    MainActivity.this.setCycleInitial(getCycle());
                    MainActivity.this.setPeriodInitial(getPeriod());
                    InitialDialog.this.dismiss();

                    bottomNavigation.setSelectedItemId(bottomNavigation.getSelectedItemId());
                    MainActivity.this.findViewById(R.id.main_container).invalidate();
                }
            });
        }

        public int getCycle(){
            String value = cycle.getText().toString();
            return Integer.parseInt(value);
        }

        public int getPeriod(){
            return Integer.parseInt(period.getText().toString());
        }
    }

}
