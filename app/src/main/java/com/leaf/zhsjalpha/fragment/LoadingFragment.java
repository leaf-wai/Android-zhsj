package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.leaf.zhsjalpha.R;

public class LoadingFragment extends DialogFragment {

    public static LoadingFragment newInstance(String title, int color) {
        LoadingFragment fragment = new LoadingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("color", color);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progressbar_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        int color = getArguments().getInt("color");
        ((TextView) view.findViewById(R.id.tv_loading)).setText(title);
        Sprite doubleBounce = new DoubleBounce();
        doubleBounce.setColor(color);
        ((ProgressBar) view.findViewById(R.id.progressBar)).setIndeterminateDrawable(doubleBounce);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = 350;
        params.height = 350;
        getDialog().getWindow().setAttributes(params);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        super.onResume();
    }
}
