package com.leaf.zhsjalpha.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.leaf.zhsjalpha.R;

public class FriendDetailFragment extends DialogFragment {

    public static FriendDetailFragment newInstance(String studentName, String studentNumber, String birthday, String phone, String nation, String sex) {
        FriendDetailFragment fragment = new FriendDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("studentName", studentName);
        bundle.putString("studentNumber", studentNumber);
        bundle.putString("birthday", birthday);
        bundle.putString("phone", phone);
        bundle.putString("nation", nation);
        bundle.putString("sex", sex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_detail_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String studentName = getArguments().getString("studentName");
        String studentNumber = getArguments().getString("studentNumber");
        String birthday = getArguments().getString("birthday");
        String phone = getArguments().getString("phone");
        String nation = getArguments().getString("nation");
        String sex = getArguments().getString("sex");
        ((TextView) view.findViewById(R.id.tv_studentName)).setText(studentName == null ? "" : studentName);
        ((TextView) view.findViewById(R.id.tv_studentNumber)).setText(studentNumber == null ? "" : studentNumber);
        ((TextView) view.findViewById(R.id.tv_birthday)).setText(birthday == null ? "" : birthday);
        ((TextView) view.findViewById(R.id.tv_phone)).setText(phone == null ? "" : phone);
        ((TextView) view.findViewById(R.id.tv_nation)).setText(nation == null ? "" : nation);
        if (sex != null) {
            if (sex.equals("男"))
                ((ImageView) view.findViewById(R.id.iv_sex)).setImageDrawable(getResources().getDrawable(R.drawable.vector_drawable_male));
            else if (sex.equals("女"))
                ((ImageView) view.findViewById(R.id.iv_sex)).setImageDrawable(getResources().getDrawable(R.drawable.vector_drawable_female));
        }
        view.findViewById(R.id.iv_close).setOnClickListener(v -> dismiss());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        super.onResume();
    }
}