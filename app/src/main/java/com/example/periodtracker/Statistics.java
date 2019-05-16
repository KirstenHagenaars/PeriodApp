package com.example.periodtracker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Statistics extends Fragment {

    private int cycle;

    public Statistics() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_statistics, container, false);
    }

    public int getCycleLenght()
    {
        return cycle;
    }

    public void setCycle(int days)
    {
        cycle = days;
    }
}
