package com.leaf.zhsjalpha.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.FragmentCreateTeamBinding;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.TeamViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateTeamFragment extends DialogFragment {

    private FragmentCreateTeamBinding binding;
    private TeamViewModel teamViewModel;
    private LoadingFragment loadingFragment;
    private final Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
            if (response.isSuccessful() && response.body() != null) {
                loadingFragment.dismiss();
                ToastUtils.showToast(getContext(), response.body().getDetail());
                if (response.body().getCode() == 200) {
                    slideToDown(binding.getRoot());
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
            loadingFragment.dismiss();
            ToastUtils.showToast(getContext(), "创建小队失败，请稍后重试！");
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateTeamBinding.inflate(getLayoutInflater());
        teamViewModel = new ViewModelProvider(getActivity()).get(TeamViewModel.class);
        loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.colorPrimary));
        slideToUp(binding.getRoot());
        binding.etContactName.setText(teamViewModel.userName);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListener();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_create_team, null);
        Dialog dialog = new Dialog(getActivity(), 0);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                slideToDown(binding.getRoot());
                return true;
            } else
                return false;
        });
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, getScreenHeight(getActivity()));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.0f;
        window.setAttributes(layoutParams);
        return dialog;
    }

    private void addListener() {
        binding.FLBack.setOnClickListener(v -> {
            slideToDown(binding.getRoot());
        });

        binding.FLSubmit.setOnClickListener(v -> {
            loadingFragment.show(getChildFragmentManager(), "submit");
            String teamName = String.valueOf(binding.etTeamName.getText());
            String teamType = String.valueOf(binding.etGrade.getText());
            String parentMen = String.valueOf(binding.etParentMale.getText());
            String parentWomen = String.valueOf(binding.etParentFemale.getText());
            String tel = String.valueOf(binding.etPhone.getText());
            String userIdCard = String.valueOf(binding.etIdcard.getText());
            String userRace = String.valueOf(binding.etNation.getText());
            String wx = String.valueOf(binding.etWechat.getText());
            String isCoach;
            if (binding.ckCoach.isChecked())
                isCoach = "1";
            else
                isCoach = "0";
            teamViewModel.createTeam(callback, teamName, teamType, parentMen, parentWomen, "1", isCoach, tel, userIdCard, userRace, wx);
        });
    }

    private void slideToUp(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
        slide.setDuration(400);
        slide.setFillEnabled(true);
        slide.setFillAfter(true);
        view.startAnimation(slide);
    }

    public void slideToDown(View view) {
        Animation slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

        slide.setDuration(400);
        slide.setFillEnabled(true);
        slide.setFillAfter(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }
}
