package com.hg.photoshare.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hg.photoshare.data.TutorialData;
import com.hg.photoshare.fragment.ToturialFragment;
import com.hg.photoshare.fragment.TutorialPagerFragment;

/**
 * Created by Nart on 25/10/2016.
 */
public class TutorialAdapter extends FragmentPagerAdapter {
    int numOfPage = 4;
    TutorialData tutorialData;

    public TutorialAdapter(FragmentManager fm, TutorialData data) {
        super(fm);
        tutorialData = data;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TutorialPagerFragment.newInstance(tutorialData.tutorial.get(0), tutorialData.user);
            case 1:
                return TutorialPagerFragment.newInstance(tutorialData.tutorial.get(1), tutorialData.user);
            case 2:
                return TutorialPagerFragment.newInstance(tutorialData.tutorial.get(2), tutorialData.user);
            case 3:
                return TutorialPagerFragment.newInstance(tutorialData.tutorial.get(3), tutorialData.user);
            default:
                return TutorialPagerFragment.newInstance(tutorialData.tutorial.get(0), tutorialData.user);
        }
    }

    @Override
    public int getCount() {
        return numOfPage;
    }
}
