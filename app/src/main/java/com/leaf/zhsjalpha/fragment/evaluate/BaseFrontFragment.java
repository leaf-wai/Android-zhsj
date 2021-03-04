package com.leaf.zhsjalpha.fragment.evaluate;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.FragmentBaseFrontBinding;
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


public class BaseFrontFragment extends Fragment {

    private String type;
    private FragmentBaseFrontBinding binding;
    private EvaluateViewModel mViewModel;
    private LoadingFragment loadingFragment;
    private View.OnClickListener listener;
    private List<String> templateIdList = new ArrayList<>();

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

    public static BaseFrontFragment newInstance(String type, View.OnClickListener listener) {
        BaseFrontFragment fragment = new BaseFrontFragment();
        fragment.setType(type);
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentBaseFrontBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(EvaluateViewModel.class);
        if (type.equals("family"))
            loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.evaluateFamily));
        else
            loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.evaluateMy));

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView();
        addListener();
        if (type.equals("family"))
            mViewModel.getTemplate(4, callback);
        else
            mViewModel.getTemplate(2, callback);
        return binding.getRoot();
    }

    private void initView() {
        switch (type) {
            case "family":
                binding.llSubmit.setBackground(getResources().getDrawable(R.drawable.evaluate_home_gradient));
                binding.lvEvaluate.setLabelBackgroundDrawable(getResources().getDrawable(R.drawable.label_evaluate_family_bg));
                binding.lvEvaluate.setLabelTextColor(getResources().getColorStateList(R.color.label_evaluate_family_text_color));
                binding.llSwitch.setBackground(getResources().getDrawable(R.drawable.border_evaluate_family));
                binding.ivSwitch.setColorFilter(getResources().getColor(R.color.evaluateFamily));
                binding.tvSwitch.setTextColor(getResources().getColor(R.color.evaluateFamily));
                break;
            case "my":
                binding.llSubmit.setBackground(getResources().getDrawable(R.drawable.evaluate_my_gradient));
                binding.lvEvaluate.setLabelBackgroundDrawable(getResources().getDrawable(R.drawable.label_evaluate_my_bg));
                binding.lvEvaluate.setLabelTextColor(getResources().getColorStateList(R.color.label_evaluate_my_text_color));
                binding.llSwitch.setBackground(getResources().getDrawable(R.drawable.border_evaluate_family));
                binding.ivSwitch.setColorFilter(getResources().getColor(R.color.evaluateMy));
                binding.tvSwitch.setTextColor(getResources().getColor(R.color.evaluateMy));
                break;
        }
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
            StringBuilder templateId = new StringBuilder();
            for (String template : templateIdList) {
                if (!templateId.toString().equals(""))
                    templateId.append(",").append(template);
                else
                    templateId = new StringBuilder(template);
            }
            if (type.equals("family"))
                mViewModel.quickEvaluate(templateId.toString(), 4, submitCallback);
            else
                mViewModel.quickEvaluate(templateId.toString(), 2, submitCallback);
        });
    }
}