package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.FragmentDeclareBinding;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.DeclareViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclareFragment extends Fragment {

    private DeclareViewModel mViewModel;
    private FragmentDeclareBinding binding;
    private LoadingFragment loadingFragment;
    private String[] classItem = null;
    private int selectedClass = -1;

    private final Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, Response<User> response) {
            Log.d("aaa", "onResponse: " + response.code() + response.body());
            if (response.isSuccessful() && response.body() != null) {
                if (loadingFragment.getDialog().isShowing())
                    new Handler().postDelayed(() -> loadingFragment.dismiss(), 200);
                ToastUtils.showToast(getContext(), response.body().getDetail());
            }
        }

        @Override
        public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
            if (loadingFragment.getDialog().isShowing())
                new Handler().postDelayed(() -> loadingFragment.dismiss(), 200);
            Log.d("aaa", "onFailure: " + t.getMessage());
            ToastUtils.showToast(getContext(), "网络请求失败！请重试");
        }
    };

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
        loadingFragment = new LoadingFragment().newInstance("加载中…", getResources().getColor(R.color.colorPrimary));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingFragment.show(getChildFragmentManager(), "loading");
        initACTV();
        addListener();
        addObserver();
    }

    private void initACTV() {
        mViewModel.getCurrencyType();
        mViewModel.getStudentClass();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_actv_item, mViewModel.getItems().getValue());
        binding.actvType.setAdapter(adapter);
    }

    private void addObserver() {
        mViewModel.getItems().observe(getViewLifecycleOwner(), strings -> {
            if (loadingFragment.getDialog().isShowing())
                new Handler().postDelayed(() -> loadingFragment.dismiss(), 200);
        });

        mViewModel.getLoadingStatus().observe(getViewLifecycleOwner(), integer -> {
            if (integer == 404) {
                if (loadingFragment.getDialog().isShowing())
                    new Handler().postDelayed(() -> loadingFragment.dismiss(), 200);
            }
        });

        mViewModel.getClassItem().observe(getViewLifecycleOwner(), strings -> {
            if (strings.size() == 0) {
                binding.tvClass.setText("暂无班级，快去选课吧~");
                binding.llClass.setClickable(false);
                binding.btnDeclare.setEnabled(false);
            } else {
                binding.tvClass.setText("选择班级");
                binding.llClass.setClickable(true);
                classItem = strings.toArray(new String[strings.size()]);
                binding.btnDeclare.setEnabled(true);
            }
        });
    }

    private void addListener() {
        binding.actvType.setOnItemClickListener((parent, view1, position, id) -> {
            mViewModel.getSubcurrencyId().setValue(mViewModel.currencyTypeList.get(position).getSubcurrencyId());
            mViewModel.getCurrencyId().setValue(mViewModel.currencyTypeList.get(position).getCurrencyId());
        });

        binding.srbScore.setOnRatingChangeListener((ratingBar, rating, fromUser) -> {
            mViewModel.getScore().setValue((int) rating);
        });

        binding.btnDeclare.setOnClickListener(v -> {
            loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.colorPrimary));
            loadingFragment.show(getChildFragmentManager(), "submit");
            mViewModel.postDeclare(String.valueOf(binding.etContent.getText()), callback);
        });

        binding.llClass.setOnClickListener(v -> {
            if (classItem != null)
                showDialogSelectClass();
        });
    }

    private void showDialogSelectClass() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("请选择班级");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(classItem, selectedClass, (dialog, which) -> {
            selectedClass = which;
            mViewModel.getClassId().postValue(mViewModel.courseDataList.get(which).getClassId());
            binding.tvClass.setText(classItem[which]);
            dialog.dismiss();
        });
        builder.show();
    }
}