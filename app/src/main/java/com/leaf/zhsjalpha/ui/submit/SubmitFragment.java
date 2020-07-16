package com.leaf.zhsjalpha.ui.submit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.leaf.zhsjalpha.R;

public class SubmitFragment extends Fragment {

    private SubmitViewModel submitViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        submitViewModel =
                new ViewModelProvider(this).get(SubmitViewModel.class);
        View root = inflater.inflate(R.layout.fragment_submit, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        submitViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}