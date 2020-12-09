package com.leaf.zhsjalpha.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.leaf.zhsjalpha.entity.Schedule;
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
//                    String JsonData = JsonUtils.getJson(getApplicationContext(), "ScheduleTest.json");
//                    ScheduleData scheduleData=JsonUtils.parseScheduleData(JsonData);
//                    List<ScheduleData.StandardBean> standardBeanList = scheduleData.getStandard();
//                    List<Schedule> scheduleList = scheduleData.getSchedule();
                    List<ScheduleData.StandardBean> standardBeanList = result.getData().getStandard();
                    List<Schedule> scheduleList = result.getData().getSchedule();
                    binding.tvClass1Start.setText(TimeUtils.timeFormat(standardBeanList.get(0).getStartTime()));
                    binding.tvClass2Start.setText(TimeUtils.timeFormat(standardBeanList.get(1).getStartTime()));
                    binding.tvClass3Start.setText(TimeUtils.timeFormat(standardBeanList.get(2).getStartTime()));
                    binding.tvClass4Start.setText(TimeUtils.timeFormat(standardBeanList.get(3).getStartTime()));
                    binding.tvClass5Start.setText(TimeUtils.timeFormat(standardBeanList.get(4).getStartTime()));
                    binding.tvClass6Start.setText(TimeUtils.timeFormat(standardBeanList.get(5).getStartTime()));
                    binding.tvClass7Start.setText(TimeUtils.timeFormat(standardBeanList.get(6).getStartTime()));
                    binding.tvClass8Start.setText(TimeUtils.timeFormat(standardBeanList.get(7).getStartTime()));
                    binding.tvClass9Start.setText(TimeUtils.timeFormat(standardBeanList.get(8).getStartTime()));
                    binding.tvClass10Start.setText(TimeUtils.timeFormat(standardBeanList.get(9).getStartTime()));
                    binding.tvClass1End.setText(TimeUtils.timeFormat(standardBeanList.get(0).getEndTime()));
                    binding.tvClass2End.setText(TimeUtils.timeFormat(standardBeanList.get(1).getEndTime()));
                    binding.tvClass3End.setText(TimeUtils.timeFormat(standardBeanList.get(2).getEndTime()));
                    binding.tvClass4End.setText(TimeUtils.timeFormat(standardBeanList.get(3).getEndTime()));
                    binding.tvClass5End.setText(TimeUtils.timeFormat(standardBeanList.get(4).getEndTime()));
                    binding.tvClass6End.setText(TimeUtils.timeFormat(standardBeanList.get(5).getEndTime()));
                    binding.tvClass7End.setText(TimeUtils.timeFormat(standardBeanList.get(6).getEndTime()));
                    binding.tvClass8End.setText(TimeUtils.timeFormat(standardBeanList.get(7).getEndTime()));
                    binding.tvClass9End.setText(TimeUtils.timeFormat(standardBeanList.get(8).getEndTime()));
                    binding.tvClass10End.setText(TimeUtils.timeFormat(standardBeanList.get(9).getEndTime()));

                    for (Schedule schedule : scheduleList) {
                        for (String courseTimeId : schedule.getCourseTimeId()) {
                            switch (courseTimeId) {
                                case "1020":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11020.setVisibility(View.VISIBLE);
                                            binding.tv11020.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21020.setVisibility(View.VISIBLE);
                                            binding.tv21020.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31020.setVisibility(View.VISIBLE);
                                            binding.tv31020.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41020.setVisibility(View.VISIBLE);
                                            binding.tv41020.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51020.setVisibility(View.VISIBLE);
                                            binding.tv51020.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61020.setVisibility(View.VISIBLE);
                                            binding.tv61020.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71020.setVisibility(View.VISIBLE);
                                            binding.tv71020.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                                case "1006":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11006.setVisibility(View.VISIBLE);
                                            binding.tv11006.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21006.setVisibility(View.VISIBLE);
                                            binding.tv21006.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31006.setVisibility(View.VISIBLE);
                                            binding.tv31006.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41006.setVisibility(View.VISIBLE);
                                            binding.tv41006.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51006.setVisibility(View.VISIBLE);
                                            binding.tv51006.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61006.setVisibility(View.VISIBLE);
                                            binding.tv61006.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71006.setVisibility(View.VISIBLE);
                                            binding.tv71006.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                                case "1026":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11026.setVisibility(View.VISIBLE);
                                            binding.tv11026.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21026.setVisibility(View.VISIBLE);
                                            binding.tv21026.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31026.setVisibility(View.VISIBLE);
                                            binding.tv31026.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41026.setVisibility(View.VISIBLE);
                                            binding.tv41026.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51026.setVisibility(View.VISIBLE);
                                            binding.tv51026.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61026.setVisibility(View.VISIBLE);
                                            binding.tv61026.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71026.setVisibility(View.VISIBLE);
                                            binding.tv71026.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                                case "1025":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11025.setVisibility(View.VISIBLE);
                                            binding.tv11025.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21025.setVisibility(View.VISIBLE);
                                            binding.tv21025.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31025.setVisibility(View.VISIBLE);
                                            binding.tv31025.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41025.setVisibility(View.VISIBLE);
                                            binding.tv41025.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51025.setVisibility(View.VISIBLE);
                                            binding.tv51025.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61025.setVisibility(View.VISIBLE);
                                            binding.tv61025.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71025.setVisibility(View.VISIBLE);
                                            binding.tv71025.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                                case "1007":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11007.setVisibility(View.VISIBLE);
                                            binding.tv11007.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21007.setVisibility(View.VISIBLE);
                                            binding.tv21007.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31007.setVisibility(View.VISIBLE);
                                            binding.tv31007.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41007.setVisibility(View.VISIBLE);
                                            binding.tv41007.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51007.setVisibility(View.VISIBLE);
                                            binding.tv51007.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61007.setVisibility(View.VISIBLE);
                                            binding.tv61007.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71007.setVisibility(View.VISIBLE);
                                            binding.tv71007.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                                case "1028":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11028.setVisibility(View.VISIBLE);
                                            binding.tv11028.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21028.setVisibility(View.VISIBLE);
                                            binding.tv21028.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31028.setVisibility(View.VISIBLE);
                                            binding.tv31028.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41028.setVisibility(View.VISIBLE);
                                            binding.tv41028.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51028.setVisibility(View.VISIBLE);
                                            binding.tv51028.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61028.setVisibility(View.VISIBLE);
                                            binding.tv61028.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71028.setVisibility(View.VISIBLE);
                                            binding.tv71028.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                                case "1027":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11027.setVisibility(View.VISIBLE);
                                            binding.tv11027.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21027.setVisibility(View.VISIBLE);
                                            binding.tv21027.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31027.setVisibility(View.VISIBLE);
                                            binding.tv31027.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41027.setVisibility(View.VISIBLE);
                                            binding.tv41027.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51027.setVisibility(View.VISIBLE);
                                            binding.tv51027.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61027.setVisibility(View.VISIBLE);
                                            binding.tv61027.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71027.setVisibility(View.VISIBLE);
                                            binding.tv71027.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                                case "1013":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11013.setVisibility(View.VISIBLE);
                                            binding.tv11013.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21013.setVisibility(View.VISIBLE);
                                            binding.tv21013.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31013.setVisibility(View.VISIBLE);
                                            binding.tv31013.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41013.setVisibility(View.VISIBLE);
                                            binding.tv41013.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51013.setVisibility(View.VISIBLE);
                                            binding.tv51013.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61013.setVisibility(View.VISIBLE);
                                            binding.tv61013.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71013.setVisibility(View.VISIBLE);
                                            binding.tv71013.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                                case "1017":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11017.setVisibility(View.VISIBLE);
                                            binding.tv11017.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21017.setVisibility(View.VISIBLE);
                                            binding.tv21017.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31017.setVisibility(View.VISIBLE);
                                            binding.tv31017.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41017.setVisibility(View.VISIBLE);
                                            binding.tv41017.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51017.setVisibility(View.VISIBLE);
                                            binding.tv51017.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61017.setVisibility(View.VISIBLE);
                                            binding.tv61017.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71017.setVisibility(View.VISIBLE);
                                            binding.tv71017.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                                case "1018":
                                    switch (schedule.getWeekDay()) {
                                        case "1":
                                            binding.tv11018.setVisibility(View.VISIBLE);
                                            binding.tv11018.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "2":
                                            binding.tv21018.setVisibility(View.VISIBLE);
                                            binding.tv21018.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "3":
                                            binding.tv31018.setVisibility(View.VISIBLE);
                                            binding.tv31018.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "4":
                                            binding.tv41018.setVisibility(View.VISIBLE);
                                            binding.tv41018.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "5":
                                            binding.tv51018.setVisibility(View.VISIBLE);
                                            binding.tv51018.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "6":
                                            binding.tv61018.setVisibility(View.VISIBLE);
                                            binding.tv61018.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                        case "7":
                                            binding.tv71018.setVisibility(View.VISIBLE);
                                            binding.tv71018.setText(schedule.getCourseName() + " " + schedule.getClassroomName());
                                            break;
                                    }
                                    break;
                            }
                        }
                    }
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