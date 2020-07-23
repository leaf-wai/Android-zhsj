package com.leaf.zhsjalpha.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.leaf.zhsjalpha.R;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;
    protected boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        firstRun();
        BottomNavigationView navView = findViewById(R.id.nav_view);

//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_account)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public void onBackPressed() {
        if (flag) {
            super.onBackPressed();
            System.exit(0);
        } else {
            Snackbar.make(getWindow().getDecorView().findViewById(R.id.nav_host_fragment), "再按一次将退出APP", Snackbar.LENGTH_SHORT).show();
            flag = true;
            new Handler().postDelayed(() -> flag = false, 2000);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    private void firstRun() {
        SharedPreferences.Editor firstRun = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
        SharedPreferences firstBool = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        if ((firstBool.getBoolean("firstRun", true))) {
            firstRun.putBoolean("firstRun", false);
            firstRun.apply();
        }
    }

}