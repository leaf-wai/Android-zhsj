package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.NewFriendAdapter;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.FragmentNewFriendBinding;
import com.leaf.zhsjalpha.entity.ApplyFriendData;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.NewFriendViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewFriendFragment extends Fragment {

    private FragmentNewFriendBinding binding;
    private NewFriendViewModel mViewModel;
    private NewFriendAdapter adapter;
    private FriendDetailFragment dialogFragment;

    private Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
            if (response.isSuccessful() && response.body() != null) {
                ToastUtils.showToast(response.body().getDetail(), Toast.LENGTH_SHORT);
                if (response.body().getCode() == 200) {
                    mViewModel.getApplyFriendList();
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
            ToastUtils.showToast("操作失败", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    public static NewFriendFragment newInstance() {
        NewFriendFragment fragment = new NewFriendFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewFriendBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(NewFriendViewModel.class);
        initRecyclerView();
        addObserver();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initRecyclerView() {
        adapter = new NewFriendAdapter(callback);
        adapter.setAnimationEnable(true);
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
        adapter.setAnimationFirstOnly(false);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            ApplyFriendData applyFriendData = mViewModel.applyFriendDataList.get(position);
            dialogFragment = new FriendDetailFragment().newInstance(applyFriendData.getApplyName(), applyFriendData.getStudentIdApply(), applyFriendData.getBirthday(), applyFriendData.getFamilyPhone(), applyFriendData.getNation(), applyFriendData.getSex());
            dialogFragment.show(getChildFragmentManager(), "friendDetail");
            new Handler().postDelayed(() -> dialogFragment.getDialog().setCanceledOnTouchOutside(false), 200);
        });
        binding.recyclerViewNewFriend.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewNewFriend.setAdapter(adapter);
        adapter.setEmptyView(R.layout.view_loading);
    }

    private void addObserver() {
        mViewModel.getApplyFriends().observe(getViewLifecycleOwner(), applyFriends -> {
            if (applyFriends.size() == 0) {
                adapter.setList(applyFriends);
                adapter.setEmptyView(R.layout.view_empty);
            } else {
                adapter.setList(applyFriends);
            }
        });
    }
}