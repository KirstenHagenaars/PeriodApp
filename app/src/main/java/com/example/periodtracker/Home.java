package com.example.periodtracker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/*
"cycleInitial" --> whether or not initialized
"cyclelength"
"periodlength"
"day" --> of last period
"month"
"year"
"periods" stringset/hashset of all periods
 */

public class Home extends Fragment {
    Button dataButton;

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
        TextView test = homeFragment.findViewById(R.id.days);
        test.setText(calculations + "");


        dataButton = (Button) homeFragment.findViewById(R.id.button);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open pop-up
                new InitialDialog(getActivity());

            }
        });


        homeFragment.invalidate();
    }

    public class InitialDialog extends Dialog {
        public String Bleeding; //level of bleeding, from 0 to 3
        public String Cramps;   //level of cramps, from 0 to 3
        public Button ok;    //confirmation button

        ImageView b0, b1, b2, b3, c0, c1, c2, c3, previousb, currentb, previousc, currentc;

        public InitialDialog(@NonNull Context context) {
            super(context);
            this.setContentView(R.layout.enterdatapopup);
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
                        case R.id.nobleeding: Bleeding = "0"; break;
                        case R.id.lightbleeding: Bleeding = "1"; break;
                        case R.id.mediumbleeding: Bleeding = "2"; break;
                        case R.id.heavybleeding: Bleeding = "3"; break;
                    }
                    switch (currentc.getId()){
                        case R.id.nocramp: Cramps = "0"; break;
                        case R.id.lightcramp: Cramps = "1"; break;
                        case R.id.mediumcramp: Cramps = "2"; break;
                        case R.id.heavycramp: Cramps = "3"; break;
                    }

                    //close pop-up
                    MainActivity.savePeriodInList(data, Calendar.getInstance().toString(), Bleeding, Cramps);
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
