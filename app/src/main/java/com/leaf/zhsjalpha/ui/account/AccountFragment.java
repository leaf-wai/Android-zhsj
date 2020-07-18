package com.leaf.zhsjalpha.ui.account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.AboutActivity;
import com.leaf.zhsjalpha.activity.LoginActivity;
import com.leaf.zhsjalpha.bean.UserInfo;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        Button btnlogin = root.findViewById(R.id.btn_login);
        Button btnlogout = root.findViewById(R.id.btn_logout);
        TextView tvUsername = root.findViewById(R.id.tv_username);
        TextView tvGrade = root.findViewById(R.id.tv_grade);
        TextView tvIntegral = root.findViewById(R.id.tv_integral);
        TextView tvPost = root.findViewById(R.id.tv_post);
        TextView tvThumbup = root.findViewById(R.id.tv_thumbup);
        ConstraintLayout userPanel = root.findViewById(R.id.userPannel);
        LinearLayout listPanel = root.findViewById(R.id.LL_list_panel);
        FrameLayout buttonPanel = root.findViewById(R.id.buttonPanel);
        LinearLayout llabout = root.findViewById(R.id.LL_about);

        SharedPreferences.Editor useredit = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        SharedPreferences userread = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        BannerUtils.setBannerRound(userPanel, 20);
        BannerUtils.setBannerRound(listPanel, 20);
        BannerUtils.setBannerRound(buttonPanel, 20);

        if (userread.getBoolean("hasLogined", false)) {
            tvUsername.setText(userread.getString("studentName", ""));
            btnlogout.setVisibility(View.VISIBLE);
            btnlogin.setVisibility(View.GONE);
            tvGrade.setVisibility(View.VISIBLE);

            OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = new ArrayList<>();
                    String token = userread.getString("cookie", null);

                    if (token != null) {
                        Cookie cookie = Cookie.parse(url, token);
                        cookies.add(cookie);
                    }

                    return cookies;
                }
            }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://zhsj.bnuz.edu.cn/ComprehensiveSys/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            userInfoApi api = retrofit.create(userInfoApi.class);
            Call<UserInfo> userInfoCall = api.userInfo();
            userInfoCall.enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                    UserInfo userInfo = response.body();
                    if (userInfo.code == 200) {
                        tvGrade.setText(userInfo.getData().getGradeId() + "年级");
                        tvIntegral.setText(String.valueOf(userInfo.getData().getIntegral()));
                        tvPost.setText(String.valueOf(userInfo.getData().getPostNum()));
                        tvThumbup.setText(String.valueOf(userInfo.getData().getThumbUpNum()));
                    }
                }

                @Override
                public void onFailure(Call<UserInfo> call, Throwable t) {
                    Toast.makeText(getContext(), "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("aaa", t.getMessage());
                }
            });
        } else {
            btnlogout.setVisibility(View.GONE);
            btnlogin.setVisibility(View.VISIBLE);
            tvGrade.setVisibility(View.GONE);
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setMessage("确定退出登录？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        useredit.remove("studentName");
                        useredit.remove("cookie");
                        useredit.putBoolean("hasLogined", false);
                        useredit.apply();
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_account);
                    }
                });
                builder.show();
            }
        });

        llabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    public interface userInfoApi {
        @GET("student/getStudentInfo")
        Call<UserInfo> userInfo();
    }
}