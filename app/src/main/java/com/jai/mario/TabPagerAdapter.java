package com.jai.mario;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jai.mario.fragments.SquareFragment;
import com.jai.mario.fragments.CubeFragment;
import com.jai.mario.fragments.LcmHcfFragment;

public class TabPagerAdapter extends FragmentStateAdapter {

    public TabPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new SquareFragment();
            case 1: return new CubeFragment();
            case 2: return new LcmHcfFragment();
            default: return new SquareFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; 
    }
}