package com.leaf.zhsjalpha.ui.community;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.OfficialPostAdapter;
import com.leaf.zhsjalpha.databinding.FragmentCommunityListBinding;
import com.leaf.zhsjalpha.entity.OfficialPost;

import java.util.ArrayList;

public class CommunityListFragment extends Fragment {

    private static final String POSITION = "position";
    private FragmentCommunityListBinding binding;
    private OfficialPostAdapter officialPostAdapter;
    private ArrayList<OfficialPost> officialPosts = new ArrayList<>();
    private int mPosition;

    public static CommunityListFragment newInstance(int position) {
        CommunityListFragment fragment = new CommunityListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initOfficialPosts();
        binding = FragmentCommunityListBinding.inflate(getLayoutInflater());
        officialPostAdapter = new OfficialPostAdapter();
        officialPostAdapter.setList(officialPosts);
        officialPostAdapter.setAnimationEnable(true);
        officialPostAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.AlphaIn);
        officialPostAdapter.setAnimationFirstOnly(false);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recylcerViewCommunity.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mPosition == 0) {
            binding.recylcerViewCommunity.setAdapter(officialPostAdapter);
            binding.btnFab.setVisibility(View.GONE);
        } else if (mPosition == 1) {
            binding.recylcerViewCommunity.setAdapter(officialPostAdapter);
        }

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> binding.swipeRefreshLayout.setRefreshing(false), 500);
        });
    }

    private void initOfficialPosts() {
        OfficialPost officialPost = new OfficialPost();
        OfficialPost officialPost1 = new OfficialPost();
        officialPost.setPostContent("户外活动是儿童成长中不可缺少的关键性环节，综合实践平台鼓励儿童多参与户外活动，只有强健的体魄才能赢得未来！");
        officialPost.setPostTime("6 小时前");
        officialPost.setComment(0);
        officialPost.setLike(3);
        officialPosts.add(officialPost);
        officialPost1.setPostContent("综合实践活动是国家义务教育和普通高中课程方案规定的必修课程,与学科课程并列设置,是基础教育课程体系的重要组成部分。综合实践活动课程是一门强调以学生的经验、社会实际和社会需要和问题为核心，以主题的形式对课程资源进行整合的课程，以有效地培养和发展学生解决问题的能力、探究精神和综合实践能力为目的的课程。");
        officialPost1.setPostTime("10 小时前");
        officialPost1.setComment(6);
        officialPost1.setLike(22);
        officialPosts.add(officialPost1);
        officialPosts.add(officialPost1);
        officialPosts.add(officialPost);
    }
}