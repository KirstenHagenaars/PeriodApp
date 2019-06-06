package com.example.periodtracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Statistics extends Fragment {
    Button randomButton;

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
        averageCycle(data);
        cyclelength.setText(cyclelength.getText()+ " " + getCycleLength(data));
        periodlength.setText(periodlength.getText() + " " + getPeriodLength(data));
        Calendar last = Statistics.recentDate(data);
        date.setText(date.getText() + MainActivity.printDate(last));
        System.out.println("WHY" + last.toString());

        final TextView tips = statsFragment.findViewById(R.id.tips);
        final Random rand = new Random();
        randomButton = (Button) statsFragment.findViewById(R.id.button2);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO get random messages
                int randomNr = rand.nextInt(15);
                String text;
                switch (randomNr){
                    case 0: text = "Exercising and stretching loosens up your muscles and eases the pain."; break;
                    case 1: text = "Vitamin B1 or magnesium supplements may reduce cramps, bloating and other PMS symptoms. Dark chocolate is a good source of this."; break;
                    case 2: text = "A warm bath or a heating pad on your belly or back reduces the effects of your menstrual cramps."; break;
                    case 3: text = "Half of tea spoon of ginger powder during the first 3 or 4 days of your period is an effective treatment for painful periods."; break;
                    case 4: text = "Vitamin D may help regulate menstruation."; break;
                    case 5: text = "Half of tea spoon of ginger powder during the first 3 or 4 days of your period is an effective treatment for painful periods."; break;
                    case 6: text = "Caffeine can cause your body to retain more water, resulting in a nasty bloated feeling, and worsen cramps."; break;
                    case 7: text = "Orgasms help alleviate cramps because they send a huge rush of blood to your lower abdomen and release oxytocin, which can help your uterus relax."; break;
                    case 8: text = "Drink more water to replace lost fluids and avoid alcohol which promotes dehydration."; break;
                    case 9: text = "Avoiding trans-fatty acids like fries, cookies, crackers, and refined foods including sugar, bread, and pasta may also help relieve painful periods."; break;
                    case 10: text = "Sipping chamomile tea may help reduce cramps when you menstruate. "; break;
                    case 11: text = "Salmon has the properties of improving and stabilising your hormones and thus resulting in getting rid of menstruation issues."; break;
                    case 12: text = "If your periods are irregular, try almonds, they are rich in fiber and protein which helps to balance your hormones."; break;
                    case 13: text = "Don’t use soaps or vaginal hygiene products, since it can kill the good bacteria, making way for infections."; break;
                    case 14: text = "If you bled your sheet use of very cold water helps to take out blood stains."; break;
                    case 15: text = "Keep track of your periods yourself or use the app. "; break;
                    default: text = "Tap to get a useful tip!"; break;
                }
                tips.setText(text);
            }
        });
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

    public void averageCycle(SharedPreferences data)
    {
        SharedPreferences.Editor editor = data.edit();
        List<Long> diff = new ArrayList<>();
        List<Calendar> dates = sortDates(data);
        for(int i = 0; i < dates.size()-2; i++)
        {
            Calendar c1 = dates.get(i);
            Calendar c2 = dates.get(i+1);
            long difference = (c1.getTimeInMillis()-c2.getTimeInMillis());
            if(difference > 1)
                diff.add(difference);
        }
        int ave = 0;
        for(long d : diff)
        {
            ave += d;
        }
        if(diff.size() >0)
        {
            ave = ave/diff.size();
            editor.putInt("cyclelength", ave);
        }
    }

    public static List<Calendar> sortDates(SharedPreferences data)
    {
        //returns a chronologically sorted list of starting dates of the 10 most recent periods
        List<Calendar> list = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            if(data.contains("day" + i));
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
        /*
        for(Calendar c : list)
            System.out.println("???" + c.toString());
            */
        //for some reason still contains dates with 0 as values but it's sorted, so I guess we could deal with it
        return list;
    }

    public static Calendar recentDate(SharedPreferences data)
    {
        List<Calendar> sorted = sortDates(data);
        Calendar recent = sorted.get(sorted.size()-1);
        System.out.println("!!!" + recent.toString());
        return recent;
    }
}
