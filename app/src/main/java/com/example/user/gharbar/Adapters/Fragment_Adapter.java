package com.example.user.gharbar.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.user.gharbar.BlankFragment;
import com.example.user.gharbar.NumberFragment;

/**
 * Created by pablo on 27/10/17.
 */

public class Fragment_Adapter extends FragmentPagerAdapter {
    private String tabtitles[]=new String[]{"Description","Photos"};
    public Fragment_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position ==0)
            return new NumberFragment();
        else
            return  new BlankFragment();

    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
