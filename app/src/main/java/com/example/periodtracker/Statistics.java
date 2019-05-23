package com.example.periodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Statistics extends Fragment {


    public Statistics() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View statsFragment = inflater.inflate(R.layout.activity_statistics, container, false);
        final SharedPreferences data = this.getActivity().getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        TextView cyclelength =  statsFragment.findViewById(R.id.cyclelength);
        TextView periodlength = statsFragment.findViewById(R.id.periodlength);
        TextView date = statsFragment.findViewById(R.id.initdate);
        cyclelength.setText(cyclelength.getText()+ " " + getCycleLength(data));
        periodlength.setText(periodlength.getText() + " " + getPeriodLength(data));
        date.setText(date.getText() + " " + getDay(data) + "." + getMonth(data) +"." + getYear(data));
        return statsFragment;
    }

    static public int getCycleLength(SharedPreferences data)
    {
        return data.getInt("cyclelength", 0);
    }

    public int getPeriodLength(SharedPreferences data)
    {
        return data.getInt("periodlength", 0);
    }

    public int getDay(SharedPreferences data)
    {
        return data.getInt("day", 0);
    }
    public int getMonth(SharedPreferences data)
    {
        return ((data.getInt("month", 0))+1);
    }
    public int getYear(SharedPreferences data)
    {
        return data.getInt("year", 0);
    }

}
