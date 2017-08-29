package com.rulzurlibrary;

/**
 * Created by max on 8/28/17.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ScanFragment tab1 = new ScanFragment();
                return tab1;
            case 1:
                SearchFragment tab2 = new SearchFragment();
                return tab2;
            case 2:
                CollectionFragment tab3 = new CollectionFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
