package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.leaf.zhsjalpha.R;

public class NotifyDetailFragment extends DialogFragment {
    public static NotifyDetailFragment newInstance(String className, String teacherName, String content, String time) {
        NotifyDetailFragment fragment = new NotifyDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("className", className);
        bundle.putString("teacherName", teacherName);
        bundle.putString("content", content);
        bundle.putString("time", time);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notify_detail_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String className = getArguments().getString("className");
        String teacherName = getArguments().getString("teacherName");
        String content = getArguments().getString("content");
        String time = getArguments().getString("time");
        ((TextView) view.findViewById(R.id.tv_notifyClassName)).setText(className == null ? "" : className);
        ((TextView) view.findViewById(R.id.tv_teacherName)).setText(teacherName == null ? "" : teacherName);
        ((TextView) view.findViewById(R.id.tv_notifyContent)).setText(content == null ? "" : content);
        ((TextView) view.findViewById(R.id.tv_createTime)).setText(time == null ? "" : time);
        view.findViewById(R.id.iv_close).setOnClickListener(v -> dismiss());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        super.onResume();
    }
}
