package com.leaf.zhsjalpha.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.ActivityPostDetailBinding;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.PostDetailViewModel;

import org.jetbrains.annotations.NotNull;

import cc.shinichi.library.ImagePreview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leaf.zhsjalpha.api.ApiService.BASE_URL;
import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class PostDetailActivity extends AppCompatActivity implements Callback<User> {
    private ActivityPostDetailBinding binding;
    private PostDetailViewModel postDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        postDetailViewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);
        setContentView(binding.getRoot());
        initToolbar();
        setPostDetail();
        addListener();
        if (getIntent().getBooleanExtra("myProduct", false))
            binding.cvBanner.setVisibility(View.GONE);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
    }

    private void setPostDetail() {
        binding.tvPostAuthor.setText(getIntent().getStringExtra("postAuthor"));
        binding.tvDescription.setText(getIntent().getStringExtra("description"));
        binding.tvPostTitle.setText(getIntent().getStringExtra("postTitle"));
        binding.tvPostContent.setText(getIntent().getStringExtra("postContent"));
        binding.tvPostTime.setText(getIntent().getStringExtra("postTime"));
        binding.tvCommentLevel.setText(getIntent().getStringExtra("commentLevel") == null ? "未评价" : getIntent().getStringExtra("commentLevel"));
        binding.tvCommentContent.setText(getIntent().getStringExtra("commentContent") == null ? "未评价" : getIntent().getStringExtra("commentContent"));
        if (!getIntent().getStringExtra("thumbUpNumber").equals("0"))
            binding.tvLike.setText(getIntent().getStringExtra("thumbUpNumber"));
        if (!getIntent().getStringExtra("commentNumber").equals("0"))
            binding.tvComment.setText(getIntent().getStringExtra("commentNumber"));
        if (getIntent().getStringExtra("postImageUrl") != null) {
            binding.rivPostImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(BASE_URL + getIntent().getStringExtra("postImageUrl"))
                    .placeholder(R.drawable.no_image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.rivPostImage);
        } else {
            binding.rivPostImage.setVisibility(View.GONE);
        }
        if (getIntent().getBooleanExtra("thumb", false)) {
            setThumbUpView(true);
        }
    }

    private void addListener() {
        binding.llLike.setOnClickListener(v -> postDetailViewModel.thumbUp(getIntent().getStringExtra("classId"), getIntent().getStringExtra("postId"), this));
        binding.rivPostImage.setOnClickListener(v -> ImagePreview.getInstance()
                .setContext(this)
                .setIndex(0)
                .setImage(BASE_URL + getIntent().getStringExtra("postImageUrl"))
                .setShowCloseButton(true)
                .setEnableDragClose(true)
                .setEnableUpDragClose(true)
                .setErrorPlaceHolder(R.drawable.image_broken)
                .start());
    }

    private void setThumbUpView(boolean isThumbUp) {
        if (isThumbUp) {
            binding.tvLike.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.ivLike.setColorFilter(getResources().getColor(R.color.colorPrimary));
        } else {
            binding.tvLike.setTextColor(getResources().getColor(R.color.gray3));
            binding.ivLike.setColorFilter(getResources().getColor(R.color.gray3));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(@NotNull Call<User> call, Response<User> response) {
        if (response.isSuccessful() && response.body() != null) {
            if (getIntent().getBooleanExtra("thumb", false)) {
                if (response.body().getDetail().equals("取消点赞成功")) {
                    if (!getIntent().getStringExtra("thumbUpNumber").equals("1"))
                        binding.tvLike.setText(String.valueOf(Integer.parseInt(getIntent().getStringExtra("thumbUpNumber")) - 1));
                    else
                        binding.tvLike.setText("点赞");
                    setThumbUpView(false);
                } else if (response.body().getDetail().equals("点赞成功")) {
                    binding.tvLike.setText(getIntent().getStringExtra("thumbUpNumber"));
                    setThumbUpView(true);
                }
            } else {
                if (response.body().getDetail().equals("点赞成功")) {
                    binding.tvLike.setText(String.valueOf(Integer.parseInt(getIntent().getStringExtra("thumbUpNumber")) + 1));
                    setThumbUpView(true);
                } else if (response.body().getDetail().equals("取消点赞成功")) {
                    if (!getIntent().getStringExtra("thumbUpNumber").equals("0"))
                        binding.tvLike.setText(getIntent().getStringExtra("thumbUpNumber"));
                    else
                        binding.tvLike.setText("点赞");
                    setThumbUpView(false);
                }
            }
        }
    }

    @Override
    public void onFailure(@NotNull Call<User> call, Throwable t) {
        ToastUtils.showToast(getApplicationContext(), "网络请求失败！请稍后重试");
        Log.d("aaa", "onFailure: " + t.getMessage());
    }
}