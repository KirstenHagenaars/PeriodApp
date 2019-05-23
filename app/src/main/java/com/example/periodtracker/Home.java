package com.example.periodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;

/*
"cycleInitial" --> whether or not initialized
"cyclelength"
"periodlength"
"day" --> of last period
"month"
"year"
 */

public class Home extends Fragment {

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = getView();
        if (view != null) {
            init(view);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeFragment = inflater.inflate(R.layout.fragment_home, container, false);
        init(homeFragment);
        return homeFragment;
    }

    private void init(View homeFragment) {
        final SharedPreferences data = this.getActivity().getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        final ProgressBar progress = homeFragment.findViewById(R.id.progressBar);
        Calendar today = Calendar.getInstance();
        Calendar last = Calendar.getInstance();
        last.set(data.getInt("year", 0), (data.getInt("month", 0)), data.getInt("day", 0));
        final int startcycle = data.getInt("cyclelength", 0);
        int calculations = startcycle;
        //Calculations only work once we restart the app...WHY???HELP!!!!
        while (calculations != 0 && !(last.equals(today))) {
            last.add(Calendar.DAY_OF_MONTH, 1);
            calculations--;
        }
        final int calc = calculations;
        progress.setMax(startcycle);
        progress.setProgress(calc);
        TextView test = homeFragment.findViewById(R.id.test);
        test.setText("Test: "+ calculations);

        homeFragment.invalidate();
    }
}
