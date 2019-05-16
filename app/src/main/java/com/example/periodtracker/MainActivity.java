package com.example.periodtracker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    final Fragment home = new Home();
    final Fragment calendar = new Calendar();
    final Fragment statistics = new Statistics();
    final Fragment notifications = new Notifications();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.main_container, calendar, "4").hide(calendar).commit();
        fm.beginTransaction().add(R.id.main_container, notifications, "3").hide(notifications).commit();
        fm.beginTransaction().add(R.id.main_container, statistics, "2").hide(statistics).commit();
        fm.beginTransaction().add(R.id.main_container, home, "1").commit();
        bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(home).commit();
                        active = home;
                        return true;
                    case R.id.navigation_calendar:
                        fm.beginTransaction().hide(active).show(calendar).commit();
                        active = calendar;
                        return true;
                    case R.id.navigation_statistics:
                        fm.beginTransaction().hide(active).show(statistics).commit();
                        active = statistics;
                        return true;
                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(notifications).commit();
                        active = notifications;
                        return true;
                }

                return false;
            }
        });
    }

}
