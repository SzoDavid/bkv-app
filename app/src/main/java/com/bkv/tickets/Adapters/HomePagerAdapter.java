package com.bkv.tickets.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bkv.tickets.Fragments.ProfileFragment;
import com.bkv.tickets.Fragments.ReservationsFragment;
import com.bkv.tickets.Fragments.SearchFragment;

public class HomePagerAdapter extends FragmentStateAdapter {
    private static final int NUM_TABS = 3;

    public HomePagerAdapter(FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SearchFragment();
            case 1:
                return new ReservationsFragment();
            default:
                return new ProfileFragment();

        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}
