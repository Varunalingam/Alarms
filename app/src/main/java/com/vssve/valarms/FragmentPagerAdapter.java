package com.vssve.valarms;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {

    ArrayList<Fragment> Fragments;
    String[] Titles = {"Stopwatch","Alarms","Timer"};

    public FragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        Fragments = new ArrayList<>();
        Fragments.add(new StopwatchFragment());
        Fragments.add(new AlarmsFragment());
        Fragments.add(new TimerFragment());

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return Fragments.get(position);
    }

    @Override
    public int getCount() {
        return Fragments.size();
    }
}
