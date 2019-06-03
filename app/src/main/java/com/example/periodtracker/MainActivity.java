package com.example.periodtracker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static android.app.PendingIntent.getActivity;
/*
"cycleInitial" --> whether or not initialized
"cyclelength"
"periodlength"
"day" --> of last period
"month"
"year"
 */

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    final Fragment home = new Home();
    final Fragment calendar = new CalendarFragment();
    final Fragment statistics = new Statistics();
    final Fragment notifications = new Notifications();
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
        String initial = "cycleInitial";

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.appbar);
        setActionBar(toolbar);
        fm.beginTransaction().add(R.id.main_container, calendar, "4").hide(calendar).commit();
        fm.beginTransaction().add(R.id.main_container, notifications, "3").hide(notifications).commit();
        fm.beginTransaction().add(R.id.main_container, statistics, "2").hide(statistics).commit();
        fm.beginTransaction().add(R.id.main_container, home, "1").commit();
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(home).detach(home).attach(home).commit();
                        toolbar.setTitle("Home");
                        toolbar.setSubtitle("");
                        active = home;
                        return true;
                    case R.id.navigation_calendar:
                        fm.beginTransaction().hide(active).show(calendar).commit();
                        fm.beginTransaction().detach(calendar).attach(calendar).commit();
                        toolbar.setTitle("Calendar");
                        active = calendar;
                        return true;
                    case R.id.navigation_statistics:
                        fm.beginTransaction().hide(active).show(statistics).detach(statistics).attach(statistics).commit();
                        toolbar.setTitle("Statistics");
                        toolbar.setSubtitle("");
                        active = statistics;
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(notifications).commit();
                        fm.beginTransaction().detach(notifications).attach(notifications).commit();
                        toolbar.setTitle("Notifications");
                        toolbar.setSubtitle("");
                        active = notifications;
                        return true;
                }
                return false;
            }
        });

        if (!data.contains(initial)||data.getInt("cyclelength", 0)==0 || data.getInt("periodlength", 0)==0) {
            Dialog initializing = new InitialDialog(MainActivity.this);
            editor.putInt(initial, 1).apply();
        }
    }

    public static void savePeriodInList(SharedPreferences data, String date, String bleeding, String cramps)
    {
        Set<String> init = new HashSet<>();
        SharedPreferences.Editor editor = data.edit();

        Set<String> periods = data.getStringSet("periods", init);
        periods.add(Calendar.getInstance().toString());
        periods.add(bleeding);
        periods.add(cramps);
        editor.putStringSet("periods", periods);
        editor.apply();
    }

    public void setCycleInitial ( int c)
    {
        SharedPreferences data = getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("cyclelength", c);
        editor.commit();
    }

    public void setPeriodInitial(int d)
    {
        SharedPreferences data = getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("periodlength", d);
        editor.commit();
    }

    public void setDate(int day, int month, int year)
    {
        SharedPreferences data = getSharedPreferences(pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("day", day).commit();
        editor.putInt("month", month).commit();
        editor.putInt("year", year).commit();
    }

    public static void setToolbarMonth(String navigation)
    {
        toolbar.setSubtitle(navigation);
    }

    public class InitialDialog extends Dialog {
        public EditText cycle, period;
        public DatePicker datePicker;
        public Button ok;
        public InitialDialog(@NonNull Context context) {
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
                    MainActivity.this.setCycleInitial(getCycle());
                    MainActivity.this.setPeriodInitial(getPeriod());
                    MainActivity.this.setDate(datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());
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
