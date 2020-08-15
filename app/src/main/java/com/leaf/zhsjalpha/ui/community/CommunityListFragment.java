package com.leaf.zhsjalpha.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.leaf.zhsjalpha.databinding.FragmentCommunityListBinding;

public class CommunityListFragment extends Fragment {

    private static final String POSITION = "position";
    private FragmentCommunityListBinding binding;
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
        binding = FragmentCommunityListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.recylcerViewWork.setLayoutManager(new LinearLayoutManager(getContext()));
//        if (mPosition == 0) {
//            binding.recylcerViewWork.setAdapter(workAdapter);
//        } else if (mPosition == 1) {
//            binding.recylcerViewWork.setAdapter(myWorkAdapter);
//        }

    }
}