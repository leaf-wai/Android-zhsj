package com.leaf.zhsjalpha.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityScheduleBinding;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.ScheduleData;
import com.leaf.zhsjalpha.entity.WeekInfo;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.TimeUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.ScheduleViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class ScheduleActivity extends AppCompatActivity {
    private int currentWeek;

    private ActivityScheduleBinding binding;
    private ScheduleViewModel scheduleViewModel;

    private Callback<Result<ScheduleData>> callback = new Callback<Result<ScheduleData>>() {
        @Override
        public void onResponse(@NotNull Call<Result<ScheduleData>> call, @NotNull Response<Result<ScheduleData>> response) {
            if (response.isSuccessful()) {
                Result<ScheduleData> result = response.body();
                if (result.getCode() == 200) {
                    List<ScheduleData.StandardBean> standardBeanList = result.getData().getStandard();
                    binding.tvClass1Start.setText(TimeUtils.timeFormat(standardBeanList.get(0).getStartTime()));
                    binding.tvClass2Start.setText(TimeUtils.timeFormat(standardBeanList.get(1).getStartTime()));
                    binding.tvClass3Start.setText(TimeUtils.timeFormat(standardBeanList.get(2).getStartTime()));
                    binding.tvClass4Start.setText(TimeUtils.timeFormat(standardBeanList.get(3).getStartTime()));
                    binding.tvClass5Start.setText(TimeUtils.timeFormat(standardBeanList.get(4).getStartTime()));
                    binding.tvClass6Start.setText(TimeUtils.timeFormat(standardBeanList.get(5).getStartTime()));
                    binding.tvClass7Start.setText(TimeUtils.timeFormat(standardBeanList.get(6).getStartTime()));
                    binding.tvClass8Start.setText(TimeUtils.timeFormat(standardBeanList.get(7).getStartTime()));
                    binding.tvClass1End.setText(TimeUtils.timeFormat(standardBeanList.get(0).getEndTime()));
                    binding.tvClass2End.setText(TimeUtils.timeFormat(standardBeanList.get(1).getEndTime()));
                    binding.tvClass3End.setText(TimeUtils.timeFormat(standardBeanList.get(2).getEndTime()));
                    binding.tvClass4End.setText(TimeUtils.timeFormat(standardBeanList.get(3).getEndTime()));
                    binding.tvClass5End.setText(TimeUtils.timeFormat(standardBeanList.get(4).getEndTime()));
                    binding.tvClass6End.setText(TimeUtils.timeFormat(standardBeanList.get(5).getEndTime()));
                    binding.tvClass7End.setText(TimeUtils.timeFormat(standardBeanList.get(6).getEndTime()));
                    binding.tvClass8End.setText(TimeUtils.timeFormat(standardBeanList.get(7).getEndTime()));
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<Result<ScheduleData>> call, @NotNull Throwable t) {
            ToastUtils.showToast("获取课表信息失败", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    private Callback<Result<DataList<WeekInfo>>> weekCallback = new Callback<Result<DataList<WeekInfo>>>() {
        @Override
        public void onResponse(@NotNull Call<Result<DataList<WeekInfo>>> call, Response<Result<DataList<WeekInfo>>> response) {
            if (response.isSuccessful()) {
                Result<DataList<WeekInfo>> result = response.body();
                if (result.getData().getData() != null) {
                    binding.tvTerm.setText(result.getData().getData().get(0).getAcademicName() + "第" + result.getData().getData().get(0).getTermId() + "学期");
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<Result<DataList<WeekInfo>>> call, @NotNull Throwable t) {
            ToastUtils.showToast("获取学期信息失败", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        scheduleViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);
        setContentView(binding.getRoot());
        ToastUtils.getInstance().initToast(this);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        new Thread(() -> currentWeek = scheduleViewModel.getCurrentWeek()).start();
        scheduleViewModel.getWeekInfo(weekCallback);

        initView();
        addObserver();

        binding.llWeek.setOnClickListener(v -> showDialogWeek());
        binding.FLBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        binding.tvMonth.setText(month + "月");
        switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
            case 0:
                setToday(binding.tvSunday);
                break;
            case 1:
                setToday(binding.tvMonday);
                break;
            case 2:
                setToday(binding.tvTuesday);
                break;
            case 3:
                setToday(binding.tvWednesday);
                break;
            case 4:
                setToday(binding.tvThursday);
                break;
            case 5:
                setToday(binding.tvFriday);
                break;
            case 6:
                setToday(binding.tvSaturday);
                break;
        }
    }

    private void setToday(TextView textView) {
        textView.setBackground(getResources().getDrawable(R.drawable.bg_week_today));
        textView.setTextColor(Color.WHITE);
    }

    private void addObserver() {
        scheduleViewModel.getWeek().observe(this, integer -> {
            if (integer != 0) {
                binding.tvWeek.setText(String.format("第 %d 周", integer));
                scheduleViewModel.getSchedule(integer, callback);
            }

        });
    }

    private void showDialogWeek() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        ArrayList<String> weekItems = new ArrayList<>();
        for (int i = 1; i <= currentWeek; i++) {
            weekItems.add("第" + i + "周");
        }
        String[] items = weekItems.toArray(new String[weekItems.size()]);
        builder.setTitle("选择周次")
                .setSingleChoiceItems(items, scheduleViewModel.getWeek().getValue() - 1, (dialog, which) -> {
                    scheduleViewModel.getWeek().postValue(which + 1);
                    dialog.dismiss();
                })
                .show();
    }
}