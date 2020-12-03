package com.leaf.zhsjalpha.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityMainBinding;
import com.leaf.zhsjalpha.ui.account.AccountFragment;
import com.leaf.zhsjalpha.ui.community.CommunityFragment;
import com.leaf.zhsjalpha.ui.home.HomeFragment;
import com.leaf.zhsjalpha.ui.submit.SubmitFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.widget.FixFragmentNavigator;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;
    NavController navController;
    protected boolean flag = false;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        super.onCreate(savedInstanceState);
        mainActivity = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firstRun();
//        BottomNavigationView navView = findViewById(R.id.nav_view);

//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_account)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
        ToastUtils.getInstance().initToast(this);

        //fragment复用
        //获取页面容器NavHostFragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        //获取导航控制器
        navController = NavHostFragment.findNavController(fragment);
        //创建自定义的Fragment导航器.
        FixFragmentNavigator fixFragmentNavigator = new FixFragmentNavigator(this, fragment.getChildFragmentManager(), fragment.getId());
        //获取导航器提供者
        NavigatorProvider provider = navController.getNavigatorProvider();
        //把自定义的Fragment导航器添加进去
        provider.addNavigator(fixFragmentNavigator);
        //手动创建导航图
        NavGraph navGraph = initNavGraph(provider, fixFragmentNavigator);
        //设置导航图
        navController.setGraph(navGraph);
        //关联底部导航和导航控制器
        NavigationUI.setupWithNavController(binding.navView, navController);
        //底部导航设置点击事件
        binding.navView.setOnNavigationItemSelectedListener(item -> {
            navController.navigate(item.getItemId());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                case R.id.navigation_account:
                case R.id.navigation_community:
                    StatusBar.lightStatusBar(this, false);
                    break;
                case R.id.navigation_submit:
                    StatusBar.lightStatusBar(this, true);
                    break;
            }
            return true;
        });
    }

    private NavGraph initNavGraph(NavigatorProvider provider, FixFragmentNavigator fragmentNavigator) {
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));
        //用自定义的导航器来创建目的地
        FragmentNavigator.Destination destination1 = fragmentNavigator.createDestination();
        destination1.setId(R.id.navigation_home);
        destination1.setClassName(HomeFragment.class.getCanonicalName());
        destination1.setLabel(getResources().getString(R.string.title_home));
        navGraph.addDestination(destination1);

        FragmentNavigator.Destination destination2 = fragmentNavigator.createDestination();
        destination2.setId(R.id.navigation_submit);
        destination2.setClassName(SubmitFragment.class.getCanonicalName());
        destination2.setLabel(getResources().getString(R.string.title_submit));
        navGraph.addDestination(destination2);

        FragmentNavigator.Destination destination3 = fragmentNavigator.createDestination();
        destination3.setId(R.id.navigation_community);
        destination3.setClassName(CommunityFragment.class.getCanonicalName());
        destination3.setLabel(getResources().getString(R.string.title_community));
        navGraph.addDestination(destination3);

        FragmentNavigator.Destination destination4 = fragmentNavigator.createDestination();
        destination4.setId(R.id.navigation_account);
        destination4.setClassName(AccountFragment.class.getCanonicalName());
        destination4.setLabel(getResources().getString(R.string.title_account));
        navGraph.addDestination(destination4);

        navGraph.setStartDestination(R.id.navigation_home);

        return navGraph;
    }

    @Override
    public void onBackPressed() {
        if (flag) {
            finish();
        } else {
            ToastUtils.showToast("再按一次退出APP", Toast.LENGTH_SHORT);
            flag = true;
            new Handler().postDelayed(() -> flag = false, 2000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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