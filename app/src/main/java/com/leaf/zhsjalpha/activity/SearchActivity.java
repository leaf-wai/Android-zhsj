package com.leaf.zhsjalpha.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.ChipsAdapter;
import com.leaf.zhsjalpha.databinding.ActivitySearchBinding;
import com.leaf.zhsjalpha.entity.SearchHistory;
import com.leaf.zhsjalpha.utils.StatusBar;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.leaf.zhsjalpha.utils.StatusBar.getStatusBarHeight;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchViewModel searchViewModel;
    private SharedPreferences userRead;
    private ChipsAdapter adapter;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBar.fitSystemBar(this);
        StatusBar.lightStatusBar(this, false);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        adapter = new ChipsAdapter();
        setContentView(binding.getRoot());
        addListener();
        ToastUtils.getInstance().initToast(this);
        initSpinner();
        initFlowLayout();
        addObserver();
        userRead = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);

        binding.statusBarFix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(this)));
        binding.statusBarFix.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        showSoftInput();
    }

    private void addListener() {
        binding.searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (TextUtils.isEmpty(binding.searchEditText.getText())) {
                    ToastUtils.showToast("请输入搜索关键字！", Toast.LENGTH_SHORT);
                    return true;
                } else {
                    if (searchViewModel.getSearchType().getValue() == 0) {
                        Intent intent = new Intent(this, CourseListActivity.class);
                        intent.putExtra("keyword", String.valueOf(binding.searchEditText.getText()));
                        startActivityForResult(intent, 1);
                        searchViewModel.insertHistory(new SearchHistory(userRead.getString("studentName", "guest"), String.valueOf(binding.searchEditText.getText())));
                    } else {
                        Intent intent = new Intent(this, ActivityListActivity.class);
                        intent.putExtra("keyword", String.valueOf(binding.searchEditText.getText()));
                        startActivityForResult(intent, 1);
                        searchViewModel.insertHistory(new SearchHistory(userRead.getString("studentName", "guest"), String.valueOf(binding.searchEditText.getText())));
                    }
                    return false;
                }
            }
            return false;
        });
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    binding.buttonClear.setVisibility(VISIBLE);
                } else {
                    binding.buttonClear.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.buttonBack.setOnClickListener(v -> {
            finish();
        });
        binding.buttonClear.setOnClickListener(v -> {
            binding.searchEditText.setText(null);
        });
        binding.cvClearAll.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("确认清除搜索历史？");
            builder.setNegativeButton("取消", (dialog, which) -> {

            });
            builder.setPositiveButton("确定", (dialog, which) -> {
                searchViewModel.deleteAllHistory();
                ToastUtils.showToast("搜索历史已清空", Toast.LENGTH_SHORT);
            });
            builder.show();
        });
    }

    private void addObserver() {
        searchViewModel.getAllHistoriesLive().observe(this, searchHistories -> {
            if (searchHistories.isEmpty()) {
                binding.cvSearchHistory.setVisibility(GONE);
            } else {
                binding.cvSearchHistory.setVisibility(VISIBLE);
                adapter.setSearchHistories(searchHistories);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initSpinner() {
        List<String> data_list = new ArrayList<>();
        data_list.add("课程");
        data_list.add("活动");
        arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, data_list);
        arrayAdapter.setDropDownViewResource(R.layout.list_spinner);
        binding.spinner.setAdapter(arrayAdapter);
        binding.spinner.setPopupBackgroundResource(R.drawable.bg_white_round);
        binding.spinner.setDropDownVerticalOffset(110);
        binding.spinner.setLayoutMode(Spinner.MODE_DROPDOWN);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        searchViewModel.getSearchType().setValue(0);
                        break;
                    case 1:
                        searchViewModel.getSearchType().setValue(1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initFlowLayout() {
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(this)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();
        binding.rvSearchHistory.setLayoutManager(chipsLayoutManager);
        binding.rvSearchHistory.setAdapter(adapter);
    }

    private void showSoftInput() {
        binding.searchEditText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) binding.searchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(binding.searchEditText, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    binding.searchEditText.setText(data.getStringExtra("keyword"));
                    binding.searchEditText.setSelection(data.getStringExtra("keyword").length());
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(this::showSoftInput, 200);
    }
}