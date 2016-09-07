package com.example.android.appusagestatistics;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private android.support.v4.app.FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TotalTimeSocialApps totalTimeSocialApps = new TotalTimeSocialApps();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, totalTimeSocialApps);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.totalTimeOnCall:
                        TotalTimeCall totalTimeCall = new TotalTimeCall();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, totalTimeCall);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.totalTimeOnCabApps:
                        Toast.makeText(getApplicationContext(), "TotalTimeOnCab", Toast.LENGTH_SHORT).show();
                        TotalTimeCabApps totalTimeCabApps =new TotalTimeCabApps();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, totalTimeCabApps);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.totalTimeOnSocialApps:
                        Toast.makeText(getApplicationContext(), "TotalTimeOnSocial", Toast.LENGTH_SHORT).show();
                        TotalTimeSocialApps totalTimeSocialApps = new TotalTimeSocialApps();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, totalTimeSocialApps);
                        fragmentTransaction.commit();
                        return true;
                    case R.id.totalTimeOnMobile:
                        Toast.makeText(getApplicationContext(), "TotalTimeOnMobile", Toast.LENGTH_SHORT).show();
                        TotalTimeOnMobile totalTimeOnMobile4= new TotalTimeOnMobile();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, totalTimeOnMobile4);
                        fragmentTransaction.commit();
                        return true;
                    default:
                        return true;
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }
}
