package com.leaf.zhsjalpha.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.ActivityAddTeammateBinding;
import com.leaf.zhsjalpha.entity.StudentData;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.AddTeammateViewModel;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class AddTeammateActivity extends AppCompatActivity {

    private String teamId;

    private OptionsPickerView pvTeammate;
    private ActivityAddTeammateBinding binding;
    private AddTeammateViewModel addTeammateViewModel;
    private LoadingFragment loadingFragment;
    private Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
            if (response.isSuccessful()) {
                loadingFragment.dismiss();
                ToastUtils.showToast(response.body().getDetail(), Toast.LENGTH_SHORT);
                if (response.body().getCode() == 200) {
                    finish();
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
            loadingFragment.dismiss();
            ToastUtils.showToast("添加队员失败，请稍后重试！", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.top_in, R.anim.bottom_out);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityAddTeammateBinding.inflate(getLayoutInflater());
        addTeammateViewModel = new ViewModelProvider(this).get(AddTeammateViewModel.class);
        loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.colorPrimary));
        setContentView(binding.getRoot());
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.cvTeammate.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.bg_location));
        teamId = getIntent().getStringExtra("teamId");

        initPickerView();
        addListener();
    }

    private void addListener() {
        binding.FLBack.setOnClickListener(v -> finish());
        binding.LLTeammate.setOnClickListener(v -> {
            if (addTeammateViewModel.classList.size() != 0 && addTeammateViewModel.classStudentList.size() != 0)
                pvTeammate.setPicker(addTeammateViewModel.classList, addTeammateViewModel.classStudentList);
            pvTeammate.show();
        });
        binding.FLSubmit.setOnClickListener(v -> {
            loadingFragment.show(getSupportFragmentManager(), "submit");
            String tel = String.valueOf(binding.etPhone.getText());
            String userIdCard = String.valueOf(binding.etIdcard.getText());
            String userRace = String.valueOf(binding.etNation.getText());
            String wx = String.valueOf(binding.etWechat.getText());
            String isCoach;
            if (binding.ckCoach.isChecked())
                isCoach = "1";
            else
                isCoach = "0";
            addTeammateViewModel.addTeammate(callback, teamId, isCoach, "0", tel, userIdCard, userRace, wx);
        });
    }

    private void initPickerView() {
        pvTeammate = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            binding.tvTeammate.setText(addTeammateViewModel.classStudentList.get(options1).get(options2));
            StudentData studentData = addTeammateViewModel.classStudentDataList.get(options1).get(options2);
            addTeammateViewModel.userId = studentData.getStudentId();
            addTeammateViewModel.userName = studentData.getName();
            addTeammateViewModel.sex = studentData.getSex();
            addTeammateViewModel.gradeId = studentData.getGradeId();
        })
                .setTitleText("选择队员")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setSubmitColor(Color.parseColor("#1ab394"))
                .setCancelColor(Color.GRAY)
                .setContentTextSize(20)
                .build();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
    }
}