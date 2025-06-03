package com.jai.mario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jai.mario.fragments.HomeFragment;
import com.jai.mario.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("firstRun", true)) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);

        String username = prefs.getString("username", "User");
        Toast.makeText(this, "Welcome back, " + username + "!", Toast.LENGTH_LONG).show();

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return 2;
            }

            @Override
            public Fragment createFragment(int position) {
                if (position == 0) return new HomeFragment();
                else return new SettingsFragment();
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Home");
            else tab.setText("Settings");
        }).attach();
    }
}
