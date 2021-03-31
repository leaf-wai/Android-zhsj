package com.leaf.zhsjalpha.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityRadarBinding;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.viewmodel.RadarViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class RadarActivity extends AppCompatActivity {
    private ActivityRadarBinding binding;
    private RadarViewModel radarViewModel;
    private String[] classItem = null;
    private int selectedClass = 0;
    private int currentWeek;
    private final View.OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.ll_week:
                showDialogWeek();
                break;
            case R.id.ll_class:
                showDialogSelectClass();
                break;
            case R.id.FL_back:
                onBackPressed();
                break;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_radar);
        radarViewModel = new ViewModelProvider(this).get(RadarViewModel.class);
        setContentView(binding.getRoot());
        new Thread(() -> currentWeek = radarViewModel.getCurrentWeek()).start();
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));

        initRadarView();
        addObserver();
        binding.llWeek.setOnClickListener(listener);
        binding.llClass.setOnClickListener(listener);
        binding.FLBack.setOnClickListener(listener);
    }

    private void initRadarView() {
        binding.radarChart.getLegend().setEnabled(false);
        binding.radarChart.getDescription().setEnabled(false);
        binding.radarChart.setNoDataText("正在载入数据……");
        binding.radarChart.setNoDataTextColor(getResources().getColor(R.color.colorPrimary));
        binding.radarChart.setWebLineWidth(1f);
        binding.radarChart.setWebColor(getResources().getColor(R.color.gray3));
        binding.radarChart.setWebLineWidthInner(1f);
        binding.radarChart.setWebColorInner(getResources().getColor(R.color.gray3));
        binding.radarChart.setWebAlpha(100);
        binding.radarChart.setRotationEnabled(false);
    }

    private void setRadarData(RadarData radarData, List<String> strings) {
        Typeface miprobold = Typeface.createFromAsset(getAssets(), "fonts/miprobold.ttf");
        XAxis xAxis = binding.radarChart.getXAxis();
        xAxis.setTextSize(14f);
        xAxis.setTextColor(getResources().getColor(R.color.textBlack));
        xAxis.setTypeface(miprobold);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return strings.get((int) value % strings.size());
            }
        });

        YAxis yAxis = binding.radarChart.getYAxis();
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(80);
        yAxis.setDrawLabels(false);

        binding.radarChart.setData(radarData);
        binding.radarChart.invalidate();
        binding.radarChart.animateY(1400, Easing.Linear);
    }

    private void addObserver() {
        radarViewModel.getRadarData().observe(this, radarData -> {
            if (radarData != null) {
                setRadarData(radarData, radarViewModel.getRadarLabel().getValue());
                setScore();
            }
        });

        radarViewModel.getClassItem().observe(this, strings -> {
            if (strings.size() == 0) {
                radarViewModel.getClassName().postValue("暂无班级");
                binding.radarChart.setNoDataText("暂无数据");
                binding.ivArrowDown.setVisibility(View.GONE);
                binding.llClass.setClickable(false);
                binding.llData.setVisibility(View.INVISIBLE);
            } else {
                binding.ivArrowDown.setVisibility(View.VISIBLE);
                binding.llClass.setClickable(true);
                binding.llData.setVisibility(View.VISIBLE);
                classItem = strings.toArray(new String[0]);
            }
        });

        radarViewModel.getClassId().observe(this, s -> {
            if (!s.equals("") && radarViewModel.getClassItem().getValue().size() != 0) {
                binding.llEmpty.setVisibility(View.GONE);
                radarViewModel.getRadarData(s, radarViewModel.getWeek().getValue());
            }
        });

        radarViewModel.getWeek().observe(this, integer -> {
            binding.tvWeek.setText("第 " + integer + " 周");
            if (radarViewModel.getClassItem().getValue().size() != 0) {
                binding.llEmpty.setVisibility(View.GONE);
                radarViewModel.getRadarData(radarViewModel.getClassId().getValue(), integer);
            } else
                binding.llEmpty.setVisibility(View.VISIBLE);
        });
        radarViewModel.getClassName().observe(this, string -> binding.tvClass.setText(string));
    }

    private void showDialogWeek() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        ArrayList<String> weekItems = new ArrayList<>();
        for (int i = 1; i <= currentWeek; i++) {
            weekItems.add("第" + i + "周");
        }
        String[] items = weekItems.toArray(new String[0]);
        builder.setTitle("选择周次")
                .setSingleChoiceItems(items, radarViewModel.getWeek().getValue() - 1, (dialog, which) -> {
                    radarViewModel.getWeek().postValue(which + 1);
                    dialog.dismiss();
                })
                .show();
    }

    private void showDialogSelectClass() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("请选择班级");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(classItem, selectedClass, (dialog, which) -> {
            selectedClass = which;
            radarViewModel.getClassId().postValue(radarViewModel.courseDataList.get(which).getClassId());
            radarViewModel.getClassName().postValue(classItem[which]);
            dialog.dismiss();
        });
        builder.show();
    }

    private void setScore() {
        binding.label1.setText(radarViewModel.statisticList.get(0).getName());
        binding.label2.setText(radarViewModel.statisticList.get(1).getName());
        binding.label3.setText(radarViewModel.statisticList.get(2).getName());
        binding.label4.setText(radarViewModel.statisticList.get(3).getName());
        binding.label5.setText(radarViewModel.statisticList.get(4).getName());
        binding.label6.setText(radarViewModel.statisticList.get(5).getName());
        binding.score1.setText(String.valueOf(radarViewModel.statisticList.get(0).getNum()));
        binding.score2.setText(String.valueOf(radarViewModel.statisticList.get(1).getNum()));
        binding.score3.setText(String.valueOf(radarViewModel.statisticList.get(2).getNum()));
        binding.score4.setText(String.valueOf(radarViewModel.statisticList.get(3).getNum()));
        binding.score5.setText(String.valueOf(radarViewModel.statisticList.get(4).getNum()));
        binding.score6.setText(String.valueOf(radarViewModel.statisticList.get(5).getNum()));
    }
}