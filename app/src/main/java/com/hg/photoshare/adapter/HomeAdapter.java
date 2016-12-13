package com.hg.photoshare.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hg.photoshare.contants.Constant;
import com.hg.photoshare.fragment.HomeFollowFragment;
import com.hg.photoshare.fragment.HomeNewFragment;

import java.util.List;

/**
 * Created by Nart on 25/10/2016.
 */
public class HomeAdapter extends FragmentPagerAdapter {

    public HomeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HomeNewFragment.newInstance();
            case 1:
                return HomeFollowFragment.newInstance();
            default:
                return HomeNewFragment.newInstance();
        }
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "New";
            case 1:
                return "Follow";
        }
        return super.getPageTitle(position);
    }
}
