package com.leaf.zhsjalpha.fragment.evaluate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.leaf.zhsjalpha.databinding.FragmentBaseEvaluateBinding;
import com.leaf.zhsjalpha.utils.MyPagerHelper;
import com.wajahatkarim3.easyflipviewpager.CardFlipPageTransformer2;

public class FriendEvaluateFragment extends Fragment {
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

    public static FriendEvaluateFragment newInstance() {
        return new FriendEvaluateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentBaseEvaluateBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewPager();
        return binding.getRoot();
    }

    private void initViewPager() {
        binding.vpBaseEvaluate.setOffscreenPageLimit(2);
        binding.vpBaseEvaluate.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0)
                    return FriendFrontFragment.newInstance(listener_front);
                else
                    return FriendBackFragment.newInstance(listener_back);
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
