package com.leaf.zhsjalpha.ui.submit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.leaf.zhsjalpha.R;

public class SubmitListFragment extends Fragment {

    private static final String POSITION = "position";

    private int mPosition;

    public SubmitListFragment() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_submit_list, container, false);
    }
}