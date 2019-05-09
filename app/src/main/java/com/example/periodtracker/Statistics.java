package com.example.periodtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Statistics extends AppCompatActivity {

    private int cycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
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
