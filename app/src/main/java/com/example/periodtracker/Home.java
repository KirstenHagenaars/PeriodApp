package com.example.periodtracker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;


public class Home extends Fragment {
    Button dataButton;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        //keep initializing the view
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

    private void init(View homeFragment){
        //set the view of the homefragment
        final SharedPreferences data = this.getActivity().getSharedPreferences(MainActivity.pref, Context.MODE_PRIVATE);
        final ProgressBar progress = homeFragment.findViewById(R.id.progressBar);
        Calendar today = Calendar.getInstance();
        Calendar last = Statistics.recentDate(data);
        final int startcycle = data.getInt("cyclelength", 0);

        int calculations = startcycle;
        //Calculations sometimes don't work
        while (calculations != 0 && !last.equals(today)) {
            last.add(Calendar.DAY_OF_MONTH, 1);
            calculations--;
        }
        //set the progressbar
        progress.setMax(startcycle);
        progress.setProgress(calculations);
        TextView test = homeFragment.findViewById(R.id.days);
        test.setText(calculations + "");


        dataButton = (Button) homeFragment.findViewById(R.id.button);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open pop-up for entering data
                new InitialDialog(getActivity());

            }
        });


        homeFragment.invalidate();
    }

    public class InitialDialog extends Dialog {
        public int bleeding; //level of bleeding, from 0 to 3
        public int cramps;   //level of cramps, from 0 to 3
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
            //Adjust drawables as icons are selected and store the selected values for bleeding and cramps
            b0.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    previousb = currentb;
                    currentb = b0;
                    ToggleB();
                }
            });
            b1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    previousb = currentb;
                    currentb = b1;
                    ToggleB();
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    previousb = currentb;
                    currentb = b2;
                    ToggleB();
                }
            });
            b3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    previousb = currentb;
                    currentb = b3;
                    ToggleB();
                }
            });
            c0.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    previousc = currentc;
                    currentc = c0;
                    ToggleC();
                }
            });
            c1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    previousc = currentc;
                    currentc = c1;
                    ToggleC();
                }
            });
            c2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    previousc = currentc;
                    currentc = c2;
                    ToggleC();
                }
            });
            c3.setOnClickListener(new View.OnClickListener() {
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
                    Calendar today = Calendar.getInstance();
                    System.out.println("?" + today.get(Calendar.DAY_OF_MONTH) +"-"+ today.get(Calendar.MONTH) +"-"+ today.get(Calendar.YEAR));
                    MainActivity.savePeriodInList(data, today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.MONTH), today.get(Calendar.YEAR), bleeding, cramps, false);
                    close();

                }
            });
        }

        public void close()
        {
            Home.this.init(getView());
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
