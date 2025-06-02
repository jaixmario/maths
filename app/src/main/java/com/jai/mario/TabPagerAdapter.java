package com.jai.mario;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jai.mario.fragments.SquareFragment;
import com.jai.mario.fragments.CubeFragment;

public class TabPagerAdapter extends FragmentStateAdapter {

    public TabPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new SquareFragment();
        } else {
            return new CubeFragment(); // placeholder, create CubeFragment later
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Two tabs: Square and Cube
    }
}