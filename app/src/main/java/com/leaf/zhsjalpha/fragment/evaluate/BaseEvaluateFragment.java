package com.leaf.zhsjalpha.fragment.evaluate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.leaf.zhsjalpha.databinding.FragmentBaseEvaluateBinding;
import com.leaf.zhsjalpha.utils.MyPagerHelper;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

import org.jetbrains.annotations.NotNull;

public class BaseEvaluateFragment extends Fragment {

    private static final String TYPE = "type";
    private String mType;
    private FragmentBaseEvaluateBinding binding;

    private final View.OnClickListener listener_front = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyPagerHelper.setCurrentItem(binding.vpBaseEvaluate, 1, 1200);
        }
    };
    private final View.OnClickListener listener_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyPagerHelper.setCurrentItem(binding.vpBaseEvaluate, 0, 1200);
        }
    };

    public static BaseEvaluateFragment newInstance(String type) {
        BaseEvaluateFragment fragment = new BaseEvaluateFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
        binding = FragmentBaseEvaluateBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initViewPager();
        return binding.getRoot();
    }

    private void initViewPager() {
        binding.vpBaseEvaluate.setOffscreenPageLimit(2);
        binding.vpBaseEvaluate.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (mType) {
                    case "family":
                        if (position == 0)
                            return BaseFrontFragment.newInstance("family", listener_front);
                        else
                            return BaseBackFragment.newInstance("family", listener_back);
                    case "my":
                        if (position == 0)
                            return BaseFrontFragment.newInstance("my", listener_front);
                        else
                            return BaseBackFragment.newInstance("my", listener_back);
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 2;
            }

        });
        CardFlipPageTransformer2 cardFlipPageTransformer = new CardFlipPageTransformer2();
        cardFlipPageTransformer.setScalable(true);
        binding.vpBaseEvaluate.setPageTransformer(cardFlipPageTransformer);
        binding.vpBaseEvaluate.setUserInputEnabled(false);
    }
}