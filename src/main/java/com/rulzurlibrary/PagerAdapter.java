package com.rulzurlibrary;

/**
 * Created by max on 8/28/17.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new ScanFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new CollectionFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
