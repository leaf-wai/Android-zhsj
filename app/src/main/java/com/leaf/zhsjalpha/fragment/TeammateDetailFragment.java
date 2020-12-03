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

public class TeammateDetailFragment extends DialogFragment {
    public static TeammateDetailFragment newInstance(String userName, String userIdCard, String userRace, Integer gradeId, String tel,String wx, String sex) {
        TeammateDetailFragment fragment = new TeammateDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putString("userIdCard", userIdCard);
        bundle.putString("userRace", userRace);
        bundle.putInt("gradeId", gradeId);
        bundle.putString("tel", tel);
        bundle.putString("wx", wx);
        bundle.putString("sex", sex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.teammate_detail_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String userName = getArguments().getString("userName");
        String userIdCard = getArguments().getString("userIdCard");
        String userRace = getArguments().getString("userRace");
        String tel = getArguments().getString("tel");
        String wx = getArguments().getString("wx");
        String sex = getArguments().getString("sex");
        String gradeId = String.valueOf(getArguments().getInt("gradeId"));
        ((TextView) view.findViewById(R.id.tv_studentName)).setText(userName == null ? "" : userName);
        ((TextView) view.findViewById(R.id.tv_idcard)).setText(userIdCard == null ? "" : userIdCard);
        ((TextView) view.findViewById(R.id.tv_nation)).setText(userRace == null ? "" : userRace);
        ((TextView) view.findViewById(R.id.tv_tel)).setText(tel == null ? "" : tel);
        ((TextView) view.findViewById(R.id.tv_wechat)).setText(wx == null ? "" : wx);
        ((TextView) view.findViewById(R.id.tv_grade)).setText(gradeId == null ? "" : gradeId);
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
