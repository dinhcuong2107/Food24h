package com.example.food24h.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.food24h.fragment.DrinkFrag;
import com.example.food24h.fragment.FoodFrag;
import com.example.food24h.fragment.OrdFrag;
import com.example.food24h.fragment.SettingFrag;

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FoodFrag();
            case 1:
                return new DrinkFrag();
            case 2:
                return new OrdFrag();
            case 3:
                return new SettingFrag();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
