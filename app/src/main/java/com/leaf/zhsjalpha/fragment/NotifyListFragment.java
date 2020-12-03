package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.NotifyAdapter;
import com.leaf.zhsjalpha.adapter.TeacherNoticeAdapter;
import com.leaf.zhsjalpha.databinding.FragmentNotifyListBinding;
import com.leaf.zhsjalpha.entity.MessageData;
import com.leaf.zhsjalpha.viewmodel.NotifyViewModel;

import org.jetbrains.annotations.NotNull;

public class NotifyListFragment extends Fragment {

    private static final String POSITION = "position";
    private FragmentNotifyListBinding binding;
    private NotifyViewModel mViewModel;
    private NotifyAdapter notifyAdapter;
    private TeacherNoticeAdapter teacherNoticeAdapter;
    private NotifyDetailFragment dialogFragment;
    private int mPosition;

    public static NotifyListFragment newInstance(int position) {
        NotifyListFragment fragment = new NotifyListFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotifyListBinding.inflate(getLayoutInflater());
        if (mPosition == 0) {
            mViewModel = new ViewModelProvider(getActivity()).get(NotifyViewModel.class);
        } else {
            mViewModel = new ViewModelProvider(this).get(NotifyViewModel.class);
        }
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        initAdapter();
        addListener();
        addObserver();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerViewNotify.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mPosition == 0) {
            binding.recyclerViewNotify.setAdapter(teacherNoticeAdapter);
            teacherNoticeAdapter.setEmptyView(R.layout.view_loading);
            mViewModel.getTeacherNotice(null);
        } else {
            binding.recyclerViewNotify.setAdapter(notifyAdapter);
            notifyAdapter.setEmptyView(R.layout.view_loading);
            mViewModel.getMessages(mPosition);
        }
    }

    private void addListener() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mPosition == 0) {
                if (mViewModel.getWeek().getValue() != 0)
                    mViewModel.getTeacherNotice(mViewModel.getWeek().getValue());
                else
                    mViewModel.getTeacherNotice(null);
            } else {
                mViewModel.getMessages(mPosition);
            }
        });
    }

    private void addObserver() {
        mViewModel.getWeek().observe(getViewLifecycleOwner(), integer -> {
            if (mPosition == 0)
                binding.swipeRefreshLayout.setRefreshing(true);
        });

        mViewModel.getLoadingStatus().observe(getViewLifecycleOwner(), integer -> {
            if (integer == 404) {
                if (mPosition == 0) {
                    View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.view_empty, null, false);
                    ((TextView) emptyView.findViewById(R.id.tv_description)).setText("网络加载失败，点击重试");
                    emptyView.findViewById(R.id.ll_empty).setOnClickListener(v -> {
                        mViewModel.getTeacherNotice(mViewModel.getWeek().getValue());
                        teacherNoticeAdapter.setEmptyView(R.layout.view_loading);
                    });
                    teacherNoticeAdapter.setEmptyView(emptyView);
                } else {
                    View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.view_empty, null, false);
                    ((TextView) emptyView.findViewById(R.id.tv_description)).setText("网络加载失败，点击重试");
                    emptyView.findViewById(R.id.ll_empty).setOnClickListener(v -> {
                        mViewModel.getMessages(mPosition);
                        notifyAdapter.setEmptyView(R.layout.view_loading);
                    });
                    notifyAdapter.setEmptyView(emptyView);
                }
            }
        });
        mViewModel.getNotifies().observe(getViewLifecycleOwner(), notifies -> {
            if (binding.swipeRefreshLayout.isRefreshing()) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            if (notifies.size() == 0 && mViewModel.getLoadingStatus().getValue() == 200) {
                notifyAdapter.setList(notifies);
                notifyAdapter.setEmptyView(R.layout.view_empty);
            } else {
                View footView = LayoutInflater.from(getContext()).inflate(R.layout.view_foot, null, false);
                notifyAdapter.setList(notifies);
                notifyAdapter.setOnItemClickListener((adapter, view, position) -> {
                    MessageData messageData = mViewModel.messageDataList.get(position);
                    dialogFragment = new NotifyDetailFragment().newInstance(messageData.getClassName(), messageData.getSenderName(), messageData.getContent(), messageData.getCreateTime());
                    dialogFragment.show(getChildFragmentManager(), "notifyDetail");
                    new Handler().postDelayed(() -> dialogFragment.getDialog().setCanceledOnTouchOutside(false), 200);
                    mViewModel.read(messageData.getId());
                });
                notifyAdapter.setFooterView(footView);
            }
        });
        mViewModel.getTeacherNotices().observe(getViewLifecycleOwner(), notifies -> {
            if (binding.swipeRefreshLayout.isRefreshing()) {
                binding.swipeRefreshLayout.setRefreshing(false);
            }
            if (notifies.size() == 0 && mViewModel.getLoadingStatus().getValue() == 200) {
                teacherNoticeAdapter.setList(notifies);
                teacherNoticeAdapter.setEmptyView(R.layout.view_empty);
            } else {
                View footView = LayoutInflater.from(getContext()).inflate(R.layout.view_foot, null, false);
                teacherNoticeAdapter.setList(notifies);
                teacherNoticeAdapter.setFooterView(footView);
            }
        });
    }

    private void initAdapter() {
        //NotifyAdapter
        notifyAdapter = new NotifyAdapter();
        notifyAdapter.setAnimationEnable(true);
        notifyAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        notifyAdapter.setAnimationFirstOnly(true);
        notifyAdapter.setFooterWithEmptyEnable(false);
        //TeacherNoticeAdapter
        teacherNoticeAdapter = new TeacherNoticeAdapter();
        teacherNoticeAdapter.setAnimationEnable(true);
        teacherNoticeAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        teacherNoticeAdapter.setAnimationFirstOnly(true);
        teacherNoticeAdapter.setFooterWithEmptyEnable(false);
    }

}