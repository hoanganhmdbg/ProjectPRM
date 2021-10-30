package com.project.prm391.shoesstore.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by lamtu on 2018-03-04.
 */

public class ViewPagerImageSliderAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragments;

    public ViewPagerImageSliderAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
