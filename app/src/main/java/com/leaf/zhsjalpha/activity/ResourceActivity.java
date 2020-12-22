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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.ResourceAdapter;
import com.leaf.zhsjalpha.databinding.ActivityResourceBinding;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.Resource;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.model.network.RetrofitHelper;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class ResourceActivity extends AppCompatActivity {

    private ActivityResourceBinding binding;
    private ResourceAdapter resourceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityResourceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));

        initToolbar();
        initRecyclerView();
        requestList();
    }

    private void initRecyclerView() {
        resourceAdapter = new ResourceAdapter();
        resourceAdapter.setAnimationEnable(true);
        resourceAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom);
        resourceAdapter.setAnimationFirstOnly(false);
        binding.recyclerViewResource.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewResource.setAdapter(resourceAdapter);
        resourceAdapter.setEmptyView(R.layout.view_loading);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void requestList() {
        RetrofitHelper.getInstance().getTeachResourceCall().enqueue(new Callback<Result<DataList<Resource>>>() {
            @Override
            public void onResponse(@NotNull Call<Result<DataList<Resource>>> call, @NotNull Response<Result<DataList<Resource>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result<DataList<Resource>> result = response.body();
                    if (result.getCode() == 200) {
                        resourceAdapter.setList(result.getData().getData());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Result<DataList<Resource>>> call, @NotNull Throwable t) {
                ToastUtils.showToast("加载课程资源失败", Toast.LENGTH_SHORT);
                Log.d("aaa", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}