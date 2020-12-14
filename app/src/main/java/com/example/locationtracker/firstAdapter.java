package com.example.locationtracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class firstAdapter extends FragmentPagerAdapter {


    firstFragment page1, page2;
    //This is the fragments for the first thing user see when they open the app setting variables
    public firstAdapter(@NonNull FragmentManager fm) {
        super(fm);
        page1 = new firstFragment();
        page1.setCurrent(1);
        page2 = new firstFragment();
        page2.setCurrent(2);
    }

    @NonNull
    @Override
    //Returning which page we are in
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return page1;

            case 1:
                return page2;
        }
        return null;
    }
//How many pages are there
    @Override
    public int getCount() {
        return 2;
    }


}
