package com.leaf.zhsjalpha.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.flyco.tablayout.listener.OnTabSelectListener;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.databinding.ActivityWorkWallBinding;
import com.leaf.zhsjalpha.entity.CourseData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.fragment.PostListFragment;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.WorkWallViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class WorkWallActivity extends AppCompatActivity implements OnTabSelectListener {

    private List<CourseData> courseDataList = new ArrayList<>();
    private List<String> classItemList = new ArrayList<>();

    private ActivityWorkWallBinding binding;
    private WorkWallViewModel workWallViewModel;
    private Callback<Result<DataList<CourseData>>> callback = new Callback<Result<DataList<CourseData>>>() {
        @Override
        public void onResponse(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Response<Result<DataList<CourseData>>> response) {
            if (response.isSuccessful() && response.body() != null) {
                Result<DataList<CourseData>> result = response.body();
                if (result.getData().getData().size() != 0) {
                    courseDataList = result.getData().getData();
                    for (CourseData courseData : courseDataList) {
                        classItemList.add(courseData.getClassName());
                    }
                    initTabLayout(classItemList);
                } else {
                    ToastUtils.showToast("你还没有班级！", Toast.LENGTH_SHORT);
                    finish();
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<Result<DataList<CourseData>>> call, @NotNull Throwable t) {
            ToastUtils.showToast("获取班级信息失败", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityWorkWallBinding.inflate(getLayoutInflater());
        workWallViewModel = new ViewModelProvider(this).get(WorkWallViewModel.class);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ToastUtils.getInstance().initToast(this);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));

        initToolbar();
        workWallViewModel.getStudentClass(callback);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void initTabLayout(List<String> classItemList) {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        String[] mTitles = classItemList.toArray(new String[classItemList.size()]);
        for (CourseData courseData : courseDataList) {
            mFragments.add(PostListFragment.newInstance(courseData.getClassId()));
        }
        binding.tlWorkWall.setViewPager(binding.vpWorkWall, mTitles, this, mFragments);
        binding.tlWorkWall.setCurrentTab(0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTabSelect(int position) {
        binding.vpWorkWall.setCurrentItem(position);
    }

    @Override
    public void onTabReselect(int position) {

    }
}