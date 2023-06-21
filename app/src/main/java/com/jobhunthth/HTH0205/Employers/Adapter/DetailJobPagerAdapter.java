package com.jobhunthth.HTH0205.Employers.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jobhunthth.HTH0205.Employers.Fragment.ApplicationListFragment;
import com.jobhunthth.HTH0205.Employers.Fragment.DetailJobsFragment;
import com.jobhunthth.HTH0205.Employers.Fragment.UnapplicationListFragment;

public class DetailJobPagerAdapter extends FragmentStateAdapter {

    private final int numPager;

    public DetailJobPagerAdapter(@NonNull FragmentActivity fragmentActivity, int numPager) {
        super(fragmentActivity);
        this.numPager = numPager;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new DetailJobsFragment();
            case 1: return new ApplicationListFragment();
            case 2: return new UnapplicationListFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return numPager;
    }
}
