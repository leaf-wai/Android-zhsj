package com.leaf.zhsjalpha.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.leaf.zhsjalpha.activity.FeedbackActivity;
import com.leaf.zhsjalpha.activity.FriendsActivity;
import com.leaf.zhsjalpha.activity.LoginActivity;
import com.leaf.zhsjalpha.activity.ModifyPwdActivity;
import com.leaf.zhsjalpha.activity.MyInfoActivity;
import com.leaf.zhsjalpha.activity.MyOrderActivity;
import com.leaf.zhsjalpha.activity.MyTeamActivity;
import com.leaf.zhsjalpha.databinding.FragmentAccountBinding;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private FragmentAccountBinding binding;
    private final View.OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.LL_info:
                if (mViewModel.getLogin().getValue()) {
                    startActivity(new Intent(getActivity(), MyInfoActivity.class));
                } else {
                    ToastUtils.showToast(getContext(), "请先登录综合实践平台", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.LL_about:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.LL_my_order:
                if (mViewModel.getLogin().getValue()) {
                    startActivity(new Intent(getActivity(), MyOrderActivity.class));
                } else {
                    ToastUtils.showToast(getContext(), "请先登录综合实践平台", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.LL_feedback:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.LL_modifyPwd:
                if (mViewModel.getLogin().getValue()) {
                    startActivity(new Intent(getActivity(), ModifyPwdActivity.class));
                } else {
                    ToastUtils.showToast(getContext(), "请先登录综合实践平台", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.LL_manage_friends:
                if (mViewModel.getLogin().getValue()) {
                    startActivity(new Intent(getActivity(), FriendsActivity.class));
                } else {
                    ToastUtils.showToast(getContext(), "请先登录综合实践平台", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.LL_my_team:
                if (mViewModel.getLogin().getValue()) {
                    startActivity(new Intent(getActivity(), MyTeamActivity.class));
                } else {
                    ToastUtils.showToast(getContext(), "请先登录综合实践平台", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(this);

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(getActivity())));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListener();
        addObserver();
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    private void addListener() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            mViewModel.setUserInfo();
            new Handler().postDelayed(() -> binding.swipeRefreshLayout.setRefreshing(false), 500);
        });

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

        binding.LLAbout.setOnClickListener(listener);
        binding.LLMyOrder.setOnClickListener(listener);
        binding.LLInfo.setOnClickListener(listener);
        binding.LLFeedback.setOnClickListener(listener);
        binding.LLManageFriends.setOnClickListener(listener);
        binding.LLMyTeam.setOnClickListener(listener);
        binding.LLModifyPwd.setOnClickListener(listener);
    }

    private void addObserver() {
        mViewModel.getGrade().observe(getViewLifecycleOwner(), s -> binding.tvGrade.setText(s));

        mViewModel.getIntegral().observe(getViewLifecycleOwner(), integer -> binding.tvIntegral.setText(String.valueOf(integer)));

        mViewModel.getPost().observe(getViewLifecycleOwner(), integer -> binding.tvIntegral.setText(String.valueOf(integer)));

        mViewModel.getThumbup().observe(getViewLifecycleOwner(), integer -> binding.tvIntegral.setText(String.valueOf(integer)));

        mViewModel.getStudentName().observe(getViewLifecycleOwner(), s -> binding.tvUsername.setText(s));

        mViewModel.getSchool().observe(getViewLifecycleOwner(), s -> binding.tvSchool.setText(s));

        mViewModel.getLogin().observe(getViewLifecycleOwner(), aBoolean -> {
            if (!aBoolean) {
                binding.swipeRefreshLayout.setEnabled(false);
                mViewModel.getStudentName().setValue("未登录");
                mViewModel.getGrade().setValue("未知年级");
                mViewModel.getIntegral().setValue(0);
                mViewModel.getPost().setValue(0);
                mViewModel.getThumbup().setValue(0);
            } else {
                binding.swipeRefreshLayout.setEnabled(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.loadLoginState();
        if (!mViewModel.Login.getValue()) {
            binding.swipeRefreshLayout.setEnabled(false);
        }
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