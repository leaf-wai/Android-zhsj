package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.FragmentDeclareBinding;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.DeclareViewModel;

import org.jetbrains.annotations.NotNull;

public class DeclareFragment extends Fragment {

    private DeclareViewModel mViewModel;
    private FragmentDeclareBinding binding;
    private AlertDialog dialog;
    private AlertDialog dialog_declare;
    private View progressbar;
    private View progressbar_declare;

    public static DeclareFragment newInstance() {
        DeclareFragment fragment = new DeclareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDeclareBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(DeclareViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();
        initACTV();
        addListener();
        addObserver();
    }

    private void initACTV() {
        mViewModel.getCurrencyType();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_actv_item, mViewModel.getItems().getValue());
        binding.actvType.setAdapter(adapter);
    }

    private void addObserver() {
        mViewModel.getItems().observe(getViewLifecycleOwner(), strings -> {
            if (strings.size() == 0) {
                dialog.show();
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = 350;
                params.height = 400;
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            } else {
                if (dialog.isShowing())
                    new Handler().postDelayed(() -> dialog.dismiss(), 200);
            }
        });

        mViewModel.getLoadingStatus().observe(getViewLifecycleOwner(), integer -> {
            if (integer == 404) {
                if (dialog.isShowing())
                    new Handler().postDelayed(() -> dialog.dismiss(), 200);
            }
        });

        mViewModel.getDeclareStatus().observe(getViewLifecycleOwner(), integer -> {
            if (dialog_declare.isShowing())
                new Handler().postDelayed(() -> dialog.dismiss(), 200);
            if (integer == 200) {
                ToastUtils.showToast("自主申报成功", Toast.LENGTH_SHORT);
            } else if (integer == 404) {
                ToastUtils.showToast("网络请求失败！请重试", Toast.LENGTH_SHORT);
            }
        });
    }

    private void addListener() {
        binding.actvType.setOnItemClickListener((parent, view1, position, id) -> mViewModel.getSubcurrencyId().setValue(mViewModel.currencyTypeList.get(position).getSubcurrencyId()));
        binding.srbScore.setOnRatingChangeListener((ratingBar, rating, fromUser) -> {
            Log.d("aaa", "rating: " + rating);
            mViewModel.getScore().setValue((int) rating);
            Log.d("aaa", "score: " + mViewModel.getScore().getValue());
        });

        binding.btnDeclare.setOnClickListener(v -> {
            dialog_declare.show();
            mViewModel.postDeclare("", String.valueOf(binding.etContent.getText()), "-1", mViewModel.getCurrencyId().getValue(), mViewModel.getScore().getValue(), mViewModel.getSubcurrencyId().getValue(), 1);
        });
    }

    private void initDialog() {
        initProgressBar();
        initProgressBarDeclare();
        dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setView(progressbar);
        dialog_declare = new AlertDialog.Builder(getContext()).create();
        dialog_declare.setView(progressbar_declare);
    }

    private void initProgressBar() {
        progressbar = LayoutInflater.from(getContext()).inflate(R.layout.progressbar_layout, null, false);
        Sprite doubleBounce = new DoubleBounce();
        doubleBounce.setColor(getResources().getColor(R.color.colorPrimary));
        ProgressBar progressBar = progressbar.findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(doubleBounce);
        TextView tvLoading = progressbar.findViewById(R.id.tv_loading);
        tvLoading.setText("加载中…");
    }

    private void initProgressBarDeclare() {
        progressbar_declare = LayoutInflater.from(getContext()).inflate(R.layout.progressbar_layout, null, false);
        Sprite doubleBounce = new DoubleBounce();
        doubleBounce.setColor(getResources().getColor(R.color.colorPrimary));
        ProgressBar progressBar = progressbar_declare.findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(doubleBounce);
        TextView tvLoading = progressbar_declare.findViewById(R.id.tv_loading);
        tvLoading.setText("正在提交…");
    }
}