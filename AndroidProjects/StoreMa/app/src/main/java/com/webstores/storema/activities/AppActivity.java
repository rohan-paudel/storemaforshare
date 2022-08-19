package com.webstores.storema.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.webstores.storema.R;
import com.webstores.storema.fragments.DashboardFragment;
import com.webstores.storema.fragments.ItemsFragment;
import com.webstores.storema.fragments.OrdersFragment;
import com.webstores.storema.fragments.SettingsFragment;

public class AppActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private Fragment fragment;
    private DashboardFragment dashboardFragment;
    private ItemsFragment itemsFragment;
    private OrdersFragment ordersFragment;
    private SettingsFragment settingsFragment;

    private long backPressedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);


        fragment = null;
        dashboardFragment = new DashboardFragment();
        itemsFragment = new ItemsFragment();
        ordersFragment = new OrdersFragment();
        settingsFragment = new SettingsFragment();


        navigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, dashboardFragment).commit();

        Thread FragmentSelector = new Thread(new Runnable() {
            @Override
            public void run() {
                navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if(item.getItemId() == R.id.nav_dashboard){
                            fragment = dashboardFragment;


                        } else if(item.getItemId() == R.id.nav_items){
                            fragment = itemsFragment;
                        } else if(item.getItemId() == R.id.nav_orders){
                            fragment = ordersFragment;
                        } else if(item.getItemId() == R.id.nav_settings){
                            fragment = settingsFragment;
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                        item.setChecked(true);


                        return false;
                    }
                });
            }
        });
        FragmentSelector.start();



    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000> System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press back again to exit app", Toast.LENGTH_SHORT).show();
        }

        backPressedTime =  System.currentTimeMillis();





    }
}