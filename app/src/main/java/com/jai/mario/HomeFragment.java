package com.jai.mario.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jai.mario.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager2 viewPager = view.findViewById(R.id.viewPagerInner);
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutInner);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
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
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Square"); break;
                case 1: tab.setText("Cube"); break;
                case 2: tab.setText("LCM / HCF"); break;
            }
        }).attach();

        return view;
    }
}