package com.leaf.zhsjalpha.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.AboutActivity;
import com.leaf.zhsjalpha.activity.LoginActivity;
import com.leaf.zhsjalpha.activity.MainActivity;
import com.leaf.zhsjalpha.databinding.FragmentAccountBinding;
import com.youth.banner.util.BannerUtils;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private FragmentAccountBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(this);

        BannerUtils.setBannerRound(binding.userPanel, 20);
        BannerUtils.setBannerRound(binding.LLListPanel, 20);
        BannerUtils.setBannerRound(binding.buttonPanel, 20);

        mViewModel.getGrade().observe(getViewLifecycleOwner(), s -> binding.tvGrade.setText(s));

        mViewModel.getIntegral().observe(getViewLifecycleOwner(), integer -> binding.tvIntegral.setText(String.valueOf(integer)));

        mViewModel.getPost().observe(getViewLifecycleOwner(), integer -> binding.tvIntegral.setText(String.valueOf(integer)));

        mViewModel.getThumbup().observe(getViewLifecycleOwner(), integer -> binding.tvIntegral.setText(String.valueOf(integer)));

        mViewModel.getStudentName().observe(getViewLifecycleOwner(), s -> binding.tvUsername.setText(s));

//        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences userRead = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        if (userRead.getBoolean("hasLogined", false)) {
            binding.btnLogout.setVisibility(View.VISIBLE);
            binding.btnLogin.setVisibility(View.GONE);
            binding.tvGrade.setVisibility(View.VISIBLE);
            mViewModel.loadUserInfo();
        } else {
            binding.btnLogout.setVisibility(View.GONE);
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.tvGrade.setVisibility(View.GONE);
        }

        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        binding.btnLogout.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setMessage("确定退出登录？");
            builder.setNegativeButton("取消", (dialog, which) -> {

            });
            builder.setPositiveButton("确定", (dialog, which) -> {
                mViewModel.Logout();
                Navigation.findNavController(MainActivity.mainActivity, R.id.nav_host_fragment).navigate(R.id.navigation_account);
            });
            builder.show();
        });

        binding.LLAbout.setOnClickListener(v -> startActivity(new Intent(getActivity(), AboutActivity.class)));
    }

}