package com.leaf.zhsjalpha.fragment.evaluate;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.FragmentFriendFrontBinding;
import com.leaf.zhsjalpha.entity.CurrencyTypeData;
import com.leaf.zhsjalpha.entity.DataList;
import com.leaf.zhsjalpha.entity.EvaluateTemplate;
import com.leaf.zhsjalpha.entity.Result;
import com.leaf.zhsjalpha.entity.Template;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.EvaluateViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFrontFragment extends Fragment {

    private FragmentFriendFrontBinding binding;
    private EvaluateViewModel mViewModel;
    private LoadingFragment loadingFragment;
    private View.OnClickListener listener;
    private List<String> templateIdList = new ArrayList<>();
    private String[] friendItem = null;
    private boolean[] checkItem = null;
    private List<String> friendList = new ArrayList<>();
    private List<String> selectedFriends = new ArrayList<>();

    private final Callback<Result<DataList<CurrencyTypeData>>> callback = new Callback<Result<DataList<CurrencyTypeData>>>() {
        @Override
        public void onResponse(@NotNull Call<Result<DataList<CurrencyTypeData>>> call, Response<Result<DataList<CurrencyTypeData>>> response) {
            if (response.isSuccessful() && response.body() != null) {
                Result<DataList<CurrencyTypeData>> result = response.body();
                List<EvaluateTemplate> evaluateTemplateList = new ArrayList<>();
                List<CurrencyTypeData> currencyTypeDataList = result.getData().getData();
                for (CurrencyTypeData currencyTypeData : currencyTypeDataList) {
                    if (currencyTypeData.getTemplateList().size() != 0) {
                        List<Template> templateList = currencyTypeData.getTemplateList();
                        for (Template template : templateList) {
                            EvaluateTemplate evaluateTemplate = new EvaluateTemplate();
                            evaluateTemplate.setContent(template.getContent());
                            evaluateTemplate.setTemplateId(template.getTemplateId());
                            evaluateTemplateList.add(evaluateTemplate);
                        }
                    }
                }
                binding.lvEvaluate.setLabels(evaluateTemplateList, (label, position, data) -> data.getContent());
                binding.lvEvaluate.clearAllSelect();
            }
        }

        @Override
        public void onFailure(@NotNull Call<Result<DataList<CurrencyTypeData>>> call, Throwable t) {
            ToastUtils.showToast(getContext(), "加载评价模板失败", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    private final Callback<User> submitCallback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, Response<User> response) {
            if (response.isSuccessful() && response.body() != null) {
                loadingFragment.dismiss();
                if (response.body().getCode() == 200) {
                    ToastUtils.showToast(getContext(), response.body().getDetail(), getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    new Handler().postDelayed(() -> requireActivity().finish(), 2000);
                } else {
                    ToastUtils.showToast(getContext(), response.body().getDetail(), getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                }
            }

        }

        @Override
        public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
            loadingFragment.dismiss();
            ToastUtils.showToast(getContext(), "网络请求失败！请重试", getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
        }
    };

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public static FriendFrontFragment newInstance(View.OnClickListener listener) {
        FriendFrontFragment fragment = new FriendFrontFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentFriendFrontBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(EvaluateViewModel.class);
        loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.evaluateMy));
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addListener();
        addObserver();
        mViewModel.getTemplate(5, callback);
        setButtonEnable(false);
        return binding.getRoot();
    }

    private void addObserver() {
        mViewModel.getFriendItems().observe(getViewLifecycleOwner(), strings -> {
            if (strings.size() == 0) {
                binding.tvFriend.setText("暂无小伙伴，快点去添加吧！");
                binding.llFriend.setClickable(false);
            } else {
                binding.tvFriend.setText("选择同伴");
                binding.llFriend.setClickable(true);
                friendItem = strings.toArray(new String[0]);
                checkItem = new boolean[friendItem.length];
                for (int i = 0; i < friendItem.length; i++) {
                    checkItem[i] = false;
                }
            }
        });
    }

    private void addListener() {
        binding.lvEvaluate.setOnLabelSelectChangeListener((label, data, isSelect, position) -> {
            if (isSelect) {
                templateIdList.add(((EvaluateTemplate) data).getTemplateId());
            } else {
                templateIdList.remove(((EvaluateTemplate) data).getTemplateId());
            }
        });
        binding.llSwitch.setOnClickListener(listener);
        binding.llSubmit.setOnClickListener(v -> {
            loadingFragment.show(getChildFragmentManager(), "submit");
            String friends = "";
            String templateId = "";
            for (String friend : friendList) {
                if (!friends.equals(""))
                    friends = friends + "," + friend;
                else
                    friends = friend;
            }
            for (String template : templateIdList) {
                if (!templateId.equals(""))
                    templateId = templateId + "," + template;
                else
                    templateId = template;
            }
            mViewModel.quickFriendEvaluate(templateId, friends, submitCallback);
        });
        binding.llFriend.setOnClickListener(v -> {
            if (friendItem != null)
                showDialogSelectFriend();
        });
    }

    private void showDialogSelectFriend() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("请选择评价的小伙伴");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(friendItem, checkItem, (dialog, which, isChecked) -> checkItem[which] = isChecked);
        builder.setPositiveButton("确定", (dialog, which) -> {
            selectedFriends.clear();
            friendList.clear();
            for (int i = 0; i < checkItem.length; i++) {
                if (checkItem[i]) {
                    selectedFriends.add(friendItem[i]);
                    friendList.add(mViewModel.friendDataList.get(i).getStudentId());
                }
            }
            String selected = "";
            for (String friend : selectedFriends) {
                if (!selected.equals(""))
                    selected = selected + "," + friend;
                else
                    selected = friend;

            }
            if (selectedFriends.size() == 0) {
                binding.tvFriend.setText("选择同伴");
                setButtonEnable(false);
            } else {
                binding.tvFriend.setText(selected);
                setButtonEnable(true);
            }
            dialog.dismiss();
        });
        builder.show();
    }

    private void setButtonEnable(boolean enable) {
        if (enable) {
            binding.llSubmit.setClickable(true);
            binding.llSubmit.setBackground(getResources().getDrawable(R.drawable.evaluate_friend_gradient));
        } else {
            binding.llSubmit.setClickable(false);
            binding.llSubmit.setBackgroundColor(getResources().getColor(R.color.gray2));
        }
    }
}