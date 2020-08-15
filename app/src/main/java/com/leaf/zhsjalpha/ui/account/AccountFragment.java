package com.leaf.zhsjalpha.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.AboutActivity;
import com.leaf.zhsjalpha.activity.LoginActivity;
import com.leaf.zhsjalpha.activity.MyOrderActivity;
import com.leaf.zhsjalpha.databinding.FragmentAccountBinding;
import com.leaf.zhsjalpha.utils.StatusBar;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class AccountFragment extends Fragment {

    private static String TAG = "aaa";
    private AccountViewModel mViewModel;
    private FragmentAccountBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(this);

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(getActivity())));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        mViewModel.getGrade().observe(getViewLifecycleOwner(), s -> binding.tvGrade.setText(s));

        mViewModel.getIntegral().observe(getViewLifecycleOwner(), integer -> binding.tvIntegral.setText(String.valueOf(integer)));

        mViewModel.getPost().observe(getViewLifecycleOwner(), integer -> binding.tvIntegral.setText(String.valueOf(integer)));

        mViewModel.getThumbup().observe(getViewLifecycleOwner(), integer -> binding.tvIntegral.setText(String.valueOf(integer)));

        mViewModel.getStudentName().observe(getViewLifecycleOwner(), s -> binding.tvUsername.setText(s));

        mViewModel.getSchool().observe(getViewLifecycleOwner(), s -> binding.tvSchool.setText(s));

        mViewModel.getStudentNo().observe(getViewLifecycleOwner(), integer -> binding.tvStudentNumber.setText(String.valueOf(integer)));

        mViewModel.getLogin().observe(getViewLifecycleOwner(), aBoolean -> {
            if (!aBoolean) {
                binding.swipeRefreshLayout.setEnabled(false);
            } else {
                binding.swipeRefreshLayout.setEnabled(true);
            }
        });
//        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mViewModel.setUserInfo();
            new Handler().postDelayed(() -> binding.swipeRefreshLayout.setRefreshing(false), 500);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListener();
    }

    private void addListener() {
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        binding.btnLogout.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle("确定退出登录？");
            builder.setNegativeButton("取消", (dialog, which) -> {

            });
            builder.setPositiveButton("确定", (dialog, which) -> {
                mViewModel.Logout();
            });
            builder.show();
        });

        binding.LLAbout.setOnClickListener(v -> startActivity(new Intent(getActivity(), AboutActivity.class)));
        binding.LLMyOrder.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyOrderActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        StatusBar.lightStatusBar(getActivity(), false);
        mViewModel.loadLoginState();
        if (!mViewModel.Login.getValue()) {
            binding.swipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBar.lightStatusBar(getActivity(), false);
            mViewModel.loadLoginState();
            if (!mViewModel.Login.getValue()) {
                binding.swipeRefreshLayout.setEnabled(false);
            }
        }
    }
}