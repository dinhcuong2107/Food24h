package com.example.food24h;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.food24h.adapter.ViewPageAdapter;
import com.example.food24h.fragment.DrinkFrag;
import com.example.food24h.fragment.FoodFrag;
import com.example.food24h.fragment.OrdFrag;
import com.example.food24h.fragment.SettingFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {
    String key_user="",androidID="";
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidID = android.provider.Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        // nhận data từ intent
        key_user =  getIntent().getStringExtra("key_user");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        load_view_pager();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_food:
                        viewPager.setCurrentItem(0);
                        Intent intent0 = new Intent(MainActivity.this, FoodFrag.class);
                        intent0.putExtra("key_user",key_user);
                        Toast.makeText(MainActivity.this,""+item.getTitle(),Toast.LENGTH_LONG).show();
                        break;
                    case R.id.menu_drink:
                        viewPager.setCurrentItem(1);
                        Intent intent1 = new Intent(MainActivity.this, DrinkFrag.class);
                        intent1.putExtra("key_user",key_user);
                        Toast.makeText(MainActivity.this,""+item.getTitle(),Toast.LENGTH_LONG).show();
                        break;
                    case R.id.menu_ord:
                        viewPager.setCurrentItem(2);
                        Intent intent2 = new Intent(MainActivity.this, OrdFrag.class);
                        intent2.putExtra("key_user",key_user);
                        Toast.makeText(MainActivity.this,""+item.getTitle(),Toast.LENGTH_LONG).show();
                        break;
                    case R.id.menu_setting:
                        viewPager.setCurrentItem(3);
                        Intent intent3 = new Intent(MainActivity.this, SettingFrag.class);
                        intent3.putExtra("key_user",key_user);
                        Toast.makeText(MainActivity.this,""+item.getTitle(),Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }

    private void load_view_pager() {
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_food).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_drink).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_ord).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_setting).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}