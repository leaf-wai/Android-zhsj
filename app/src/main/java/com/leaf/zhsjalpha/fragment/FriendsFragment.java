package com.leaf.zhsjalpha.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.adapter.FriendsAdapter;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.FragmentFriendsBinding;
import com.leaf.zhsjalpha.entity.Friend;
import com.leaf.zhsjalpha.entity.FriendData;
import com.leaf.zhsjalpha.entity.StudentData;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.viewmodel.FriendsViewModel;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment {

    private int mPosition;
    private int deletePosition;
    private static final String POSITION = "position";
    private FragmentFriendsBinding binding;
    private FriendsViewModel friendsViewModel;
    private FriendsAdapter friendsAdapter;
    private FriendDetailFragment dialogFragment;
    private List<Friend> friendList;
    private List<Friend> studentList;

    private Callback<User> deleteCallback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, Response<User> response) {
            if (response.isSuccessful() && response.body() != null) {
                ToastUtils.showToast(response.body().getDetail(), Toast.LENGTH_SHORT);
                if (response.body().getCode() == 200) {
                    friendList.remove(deletePosition);
                    friendsViewModel.friendDataList.remove(deletePosition);
                    friendsAdapter.notifyItemRemoved(deletePosition);
                    friendsAdapter.notifyItemRangeChanged(deletePosition, friendsViewModel.getFriend().getValue().size() - deletePosition);
                }
            }
        }

        @Override
        public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
            ToastUtils.showToast("删除好友失败，请稍后重试！", Toast.LENGTH_SHORT);
            Log.d("aaa", "onFailure: " + t.getMessage());
        }
    };

    public static FriendsFragment newInstance(int position) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(getLayoutInflater());
        friendsViewModel = new ViewModelProvider(getActivity()).get(FriendsViewModel.class);
        binding.srlFriends.setColorSchemeResources(R.color.colorPrimary);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        addObserver();
        addListener();
    }

    private void initRecyclerView() {
        friendsAdapter = new FriendsAdapter();
        binding.swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.swipeRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        binding.swipeRecyclerView.setOnItemMenuClickListener(menuItemClickListener);
        binding.swipeRecyclerView.setOnItemClickListener((view, adapterPosition) -> binding.swipeRecyclerView.smoothOpenRightMenu(adapterPosition));
        binding.swipeRecyclerView.setAdapter(friendsAdapter);
    }

    private void addObserver() {
        friendsViewModel.getClassId().observe(getViewLifecycleOwner(), s -> {
            if (!s.equals("")) {
                if (mPosition == 0) {
                    friendsViewModel.getFriendList();
                } else if (mPosition == 1) {
                    friendsViewModel.getAllStudent();
                }
            }
        });

        friendsViewModel.getFriend().observe(getViewLifecycleOwner(), friends -> {
            if (mPosition == 0) {
                if (binding.srlFriends.isRefreshing())
                    binding.srlFriends.setRefreshing(false);
                if (friends.size() == 0) {
                    binding.clEmpty.setVisibility(View.VISIBLE);
                    binding.srlFriends.setVisibility(View.GONE);
                } else {
                    binding.clEmpty.setVisibility(View.GONE);
                    binding.srlFriends.setVisibility(View.VISIBLE);
                    friendList = friends;
                    friendsAdapter.notifyDataSetChanged(friendList);
                }
            }
        });

        friendsViewModel.getAllStudents().observe(getViewLifecycleOwner(), friends -> {
            if (mPosition == 1) {
                if (binding.srlFriends.isRefreshing())
                    binding.srlFriends.setRefreshing(false);
                if (friends.size() == 0) {
                    binding.clEmpty.setVisibility(View.VISIBLE);
                    binding.srlFriends.setVisibility(View.GONE);
                } else {
                    binding.clEmpty.setVisibility(View.GONE);
                    binding.srlFriends.setVisibility(View.VISIBLE);
                    studentList = friends;
                    friendsAdapter.notifyDataSetChanged(studentList);
                }
            }
        });

        friendsViewModel.getLoadingStatus().observe(getViewLifecycleOwner(), integer -> {
            if (integer == 404) {
                if (binding.srlFriends.isRefreshing())
                    binding.srlFriends.setRefreshing(false);
            }
        });
    }

    private void addListener() {
        binding.srlFriends.setOnRefreshListener(() -> {
            if (mPosition == 0)
                friendsViewModel.getFriendList();
            else if (mPosition == 1)
                friendsViewModel.getAllStudent();
        });
    }

    private SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, position) -> {
        int width = getResources().getDimensionPixelSize(R.dimen.dp_80);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        SwipeMenuItem infoItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.selector_info)
                .setText("详细信息")
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height);

        SwipeMenuItem deleteItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.selector_delete)
                .setText("删除")
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height);

        SwipeMenuItem addFriendItem = new SwipeMenuItem(getContext()).setBackground(R.drawable.selector_add_friend)
                .setText("添加好友")
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height);

        swipeRightMenu.addMenuItem(infoItem);
        if (mPosition == 0)
            swipeRightMenu.addMenuItem(deleteItem);
        else if (mPosition == 1)
            swipeRightMenu.addMenuItem(addFriendItem);
    };

    private OnItemMenuClickListener menuItemClickListener = (menuBridge, position) -> {
        menuBridge.closeMenu();

        int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
        int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
            if (mPosition == 0) {
                FriendData friendData = friendsViewModel.friendDataList.get(position);
                if (menuPosition == 0) {
                    dialogFragment = new FriendDetailFragment().newInstance(friendData.getStudentName(), friendData.getStudentId(), friendData.getBirthday(), friendData.getFamilyPhone(), friendData.getNation(), friendData.getSex());
                    dialogFragment.show(getChildFragmentManager(), "friendDetail");
                    new Handler().postDelayed(() -> dialogFragment.getDialog().setCanceledOnTouchOutside(false), 200);
                } else if (menuPosition == 1) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    builder.setTitle("确定删除？");
                    builder.setNegativeButton("取消", (dialog, which) -> {

                    });
                    builder.setPositiveButton("确定", (dialog, which) -> {
                        deletePosition = position;
                        friendsViewModel.deleteFriend(friendData.getStudentId(), deleteCallback);
                    });
                    builder.show();
                }
            } else if (mPosition == 1) {
                StudentData studentData = friendsViewModel.studentDataList.get(position);
                if (menuPosition == 0) {
                    dialogFragment = new FriendDetailFragment().newInstance(studentData.getName(), studentData.getStudentId(), studentData.getBirthday(), studentData.getFamilyPhone(), studentData.getNation(), studentData.getSex());
                    dialogFragment.show(getChildFragmentManager(), "studentDetail");
                    new Handler().postDelayed(() -> dialogFragment.getDialog().setCanceledOnTouchOutside(false), 200);
                } else if (menuPosition == 1) {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.add_friend_apply_dialog, null, false);
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    builder.setTitle("请填写申请内容");
                    builder.setView(view);
                    builder.setNegativeButton("取消", (dialog, which) -> {

                    });
                    builder.setPositiveButton("确定", (dialog, which) -> {
                        friendsViewModel.addFriend(studentData.getStudentId(), String.valueOf(((TextInputEditText) view.findViewById(R.id.et_apply_content)).getText()));
                    });
                    builder.show();
                }
            }
        }
    };

}