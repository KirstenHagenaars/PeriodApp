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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
        System.out.println("FUUCK" + MainActivity.printDate(MainActivity.getDate(data, MainActivity.getIndex(data)-1)));
        cyclelength.setText(cyclelength.getText()+ " " + getCycleLength(data));
        periodlength.setText(periodlength.getText() + " " + getPeriodLength(data));
        Calendar last = MainActivity.getDate(data, MainActivity.getIndex(data)-1);
        date.setText(date.getText() + MainActivity.printDate(last));
        System.out.println("WHY" + last.toString());
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

    public int averageCycle(SharedPreferences data)
    {
        long[] diff = new long[9];
        List<Calendar> dates = sortDates(data);
        for(int i = 0; i < 9; i++)
        {
            Calendar c1 = dates.get(i);
            Calendar c2 = dates.get(i+1);
            diff[i]=(c1.getTimeInMillis()-c2.getTimeInMillis());
        }
        OptionalDouble difference = Arrays.stream(diff).average();
        return (int) Math.round(difference.getAsDouble());
    }

    public static List<Calendar> sortDates(SharedPreferences data)
    {
        //returns a chronologically sorted list of starting dates of the 10 most recent periods
        List<Calendar> list = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            if(data.contains("index" + i));
                list.add(MainActivity.getDate(data, i));
        }
        list.sort(new Comparator<Calendar>() {
            @Override
            public int compare(Calendar o1, Calendar o2) {
                if (o1.before(o2))
                        return -1;
                else if(o1.after(o2))
                    return 1;
                return 0;//then they are equal
            }
        });
        return list;
    }

    public static Calendar recentDate(SharedPreferences data)
    {
        List<Calendar> sorted = sortDates(data);
        Calendar recent = sorted.get(sorted.size()-1);
        return recent;
    }
}
