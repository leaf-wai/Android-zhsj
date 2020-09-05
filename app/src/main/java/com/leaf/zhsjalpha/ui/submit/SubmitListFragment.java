package com.leaf.zhsjalpha.ui.submit;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.MyWorkAdapter;
import com.leaf.zhsjalpha.adapter.WorkAdapter;
import com.leaf.zhsjalpha.databinding.FragmentSubmitListBinding;
import com.leaf.zhsjalpha.entity.MyWork;
import com.leaf.zhsjalpha.entity.Work;

import java.util.ArrayList;

public class SubmitListFragment extends Fragment {

    private FragmentSubmitListBinding binding;
    private WorkAdapter workAdapter;
    private MyWorkAdapter myWorkAdapter;
    private ArrayList<Work> works = new ArrayList<>();
    private ArrayList<MyWork> myWorks = new ArrayList<>();
    private static final String POSITION = "position";
    private int mPosition;

    public static SubmitListFragment newInstance(int position) {
        SubmitListFragment fragment = new SubmitListFragment();
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
        initWorkData();
        initMyWorkData();
        binding = FragmentSubmitListBinding.inflate(getLayoutInflater());
        workAdapter = new WorkAdapter(works);
        myWorkAdapter = new MyWorkAdapter(myWorks);
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recylcerViewWork.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mPosition == 0) {
            binding.recylcerViewWork.setAdapter(workAdapter);
        } else if (mPosition == 1) {
            binding.recylcerViewWork.setAdapter(myWorkAdapter);
        }

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> binding.swipeRefreshLayout.setRefreshing(false), 500);
        });
    }

    private void initWorkData() {
        Work work = new Work();
        work.setWorkCourse("绘画");
        work.setContent("我的校园生活");
        work.setDeadline("2020-12-31");
        work.setWorkImageID(R.drawable.vector_drawable_logo_zhsj);
        works.add(work);

        work = new Work();
        work.setWorkCourse("绘画");
        work.setContent("我的校园生活");
        work.setDeadline("2020-12-31");
        work.setWorkImageID(R.drawable.vector_drawable_logo_zhsj);
        works.add(work);

        work = new Work();
        work.setWorkCourse("绘画");
        work.setContent("我的校园生活");
        work.setDeadline("2020-12-31");
        work.setWorkImageID(R.drawable.vector_drawable_logo_zhsj);
        works.add(work);

        work = new Work();
        work.setWorkCourse("绘画");
        work.setContent("我的校园生活");
        work.setDeadline("2020-12-31");
        work.setWorkImageID(R.drawable.vector_drawable_logo_zhsj);
        works.add(work);

        work = new Work();
        work.setWorkCourse("绘画");
        work.setContent("我的校园生活");
        work.setDeadline("2020-12-31");
        work.setWorkImageID(R.drawable.vector_drawable_logo_zhsj);
        works.add(work);
    }

    private void initMyWorkData() {
        MyWork myWork = new MyWork();
        myWork.setWorkCourse("绘画");
        myWork.setContent("我的校园生活");
        myWork.setMyWorkName("自己设计的书");
        myWork.setLike(66);
        myWork.setComment(8);
        myWork.setMyWorkContent("这是我设计的书。这是我设计的书。这是我设计的书。这是我设计的书。这是我设计的书。这是我设计的书。这是我设计的书。这是我设计的书。这是我设计的书。这是我设计的书。这是我设计的书。");
        myWork.setWorkImageID(R.drawable.vector_drawable_logo_zhsj);
        myWork.setWorkPicID(R.drawable.myworkpic);
        myWorks.add(myWork);
        myWorks.add(myWork);
        myWorks.add(myWork);
        myWorks.add(myWork);
        myWorks.add(myWork);
    }


}