package com.leaf.zhsjalpha.fragment.evaluate;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.FragmentFriendBackBinding;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.utils.UriTofilePath;
import com.leaf.zhsjalpha.viewmodel.EvaluateViewModel;
import com.permissionx.guolindev.PermissionX;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cc.shinichi.library.ImagePreview;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendBackFragment extends Fragment {

    private Uri mCameraUri;
    private Uri mGalleryUri;
    private Uri submitUri;
    private String mCameraImagePath;
    private boolean isAndroid10 = Build.VERSION.SDK_INT >= 29;

    private String[] friendItem = null;
    private boolean[] checkItem = null;
    private List<String> friendList = new ArrayList<>();
    private List<String> selectedFriends = new ArrayList<>();

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    private LoadingFragment loadingFragment;
    private FragmentFriendBackBinding binding;
    private View.OnClickListener listener;
    private EvaluateViewModel mViewModel;

    private Callback<User> callback = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            loadingFragment.dismiss();
            if (response.isSuccessful() && response.body() != null) {
                if (response.body().getCode() == 200) {
                    ToastUtils.showToast(response.body().getDetail(), Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                    new Handler().postDelayed(() -> getActivity().finish(), 2000);
                } else {
                    ToastUtils.showToast(response.body().getDetail(), Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
                }
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            loadingFragment.dismiss();
            ToastUtils.showToast("网络请求失败！请重试", Toast.LENGTH_SHORT, getResources().getColor(R.color.textBlack), getResources().getColor(R.color.white));
        }
    };

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.iv_uploadImage:
                showDialogAddImage();
                break;
            case R.id.ll_submit:
                if (submitUri != null) {
                    loadingFragment.show(getChildFragmentManager(), "submit");
                    String friends = "";
                    for (String friend : friendList) {
                        if (!friends.equals(""))
                            friends = friends + "," + friend;
                        else
                            friends = friend;
                    }

                    mViewModel.customFriendEvaluate(friends, String.valueOf(binding.etContent.getText()), callback);
                }
                break;
        }
    };

    private View.OnClickListener previewListener = v -> {
        String url = UriTofilePath.getFilePathByUri(getContext(), submitUri);
        ImagePreview
                .getInstance()
                .setContext(getContext())
                .setIndex(0)
                .setImage(url)
                .setShowCloseButton(true)
                .setShowDownButton(false)
                .setEnableDragClose(true)
                .setEnableUpDragClose(true)
                .setErrorPlaceHolder(R.drawable.image_broken)
                .start();
    };

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public static FriendBackFragment newInstance(View.OnClickListener listener) {
        FriendBackFragment fragment = new FriendBackFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentFriendBackBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(requireActivity()).get(EvaluateViewModel.class);
        loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.evaluateMy));
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initACTV();
        addListener();
        addObserver();
        binding.ivDelete.setVisibility(View.INVISIBLE);
        return binding.getRoot();
    }

    private void initACTV() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_actv_item, mViewModel.getItems().getValue());
        binding.actvType.setAdapter(adapter);
    }

    private void addListener() {
        binding.llSwitch.setOnClickListener(listener);
        binding.ivDelete.setOnClickListener(v -> {
            mCameraUri = null;
            mGalleryUri = null;
            mCameraImagePath = null;
            Glide.with(this)
                    .load(getActivity().getResources().getDrawable(R.drawable.ic_add_image))
                    .into(binding.ivUploadImage);
            binding.ivDelete.setVisibility(View.INVISIBLE);
            binding.ivUploadImage.setOnClickListener(onClickListener);
        });
        binding.ivUploadImage.setOnClickListener(onClickListener);
        binding.actvType.setOnItemClickListener((parent, view1, position, id) -> {
            mViewModel.getSubcurrencyId().setValue(mViewModel.currencyTypeList.get(position).getSubcurrencyId());
            mViewModel.getCurrencyId().setValue(mViewModel.currencyTypeList.get(position).getCurrencyId());
        });
        binding.srbScore.setOnRatingChangeListener((ratingBar, rating, fromUser) -> {
            Log.d("aaa", "rating: " + rating);
            mViewModel.getScore().setValue((int) rating);
            Log.d("aaa", "score: " + mViewModel.getScore().getValue());
        });
        binding.llSubmit.setOnClickListener(onClickListener);
        binding.llFriend.setOnClickListener(v -> {
            if (friendItem != null)
                showDialogSelectFriend();
        });
    }

    private void addObserver() {
        mViewModel.getFriendItems().observe(getViewLifecycleOwner(), strings -> {
            if (strings.size() == 0) {
                binding.tvFriend.setText("暂无小伙伴，快点去添加吧！");
                binding.llFriend.setClickable(false);
            } else {
                binding.tvFriend.setText("选择同伴");
                binding.llFriend.setClickable(true);
                friendItem = strings.toArray(new String[strings.size()]);
                checkItem = new boolean[friendItem.length];
                for (int i = 0; i < friendItem.length; i++) {
                    checkItem[i] = false;
                }
            }
        });
    }

    private void openCamera() {
        PermissionX.init(FriendBackFragment.this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "综合实践平台 需要您同意以下权限才能访问相册", "确定", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "确定"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 判断是否有相机
                        if (captureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            File photoFile = null;
                            Uri photoUri = null;

                            if (isAndroid10) {
                                // 适配android 10
                                photoUri = createImageUri();
                            } else {
                                photoFile = createImageFile();
                                if (photoFile != null) {
                                    mCameraImagePath = photoFile.getAbsolutePath();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                                        photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", photoFile);
                                    } else {
                                        photoUri = Uri.fromFile(photoFile);
                                    }
                                }
                            }

                            mCameraUri = photoUri;
                            if (photoUri != null) {
                                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                startActivityForResult(captureIntent, TAKE_PHOTO);
                            }
                        }
                        Toast.makeText(getContext(), "所有申请的权限都已通过", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getActivity().getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    private void openGallery() {
        PermissionX.init(FriendBackFragment.this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> scope.showRequestReasonDialog(deniedList, "综合实践平台 需要您同意以下权限才能访问相册", "确定", "取消"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "确定"))
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        intent.setType("image/*");
                        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
                        Toast.makeText(getContext(), "所有申请的权限都已通过", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDialogAddImage() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("请选择");
        String[] item = new String[]{"相机", "从相册选择图片"};
        builder.setItems(item, (dialog, which) -> {
            switch (which) {
                case 0:
                    openCamera();
                    break;
                case 1:
                    openGallery();
                    break;
            }
        });
        builder.show();
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
            } else {
                binding.tvFriend.setText(selected);
            }
            dialog.dismiss();
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    if (isAndroid10) {
                        Glide.with(this)
                                .load(mCameraUri)
                                .into(binding.ivUploadImage);
                    } else {
                        Glide.with(this)
                                .load(BitmapFactory.decodeFile(mCameraImagePath))
                                .into(binding.ivUploadImage);
                    }
                    submitUri = mCameraUri;
                    binding.ivDelete.setVisibility(View.VISIBLE);
                    binding.ivUploadImage.setOnClickListener(previewListener);
                    break;
                case CHOOSE_PHOTO:
                    mGalleryUri = data.getData();
                    Glide.with(this)
                            .load(mGalleryUri)
                            .into(binding.ivUploadImage);
                    submitUri = mGalleryUri;
                    binding.ivDelete.setVisibility(View.VISIBLE);
                    binding.ivUploadImage.setOnClickListener(previewListener);
                    break;
            }
        }
    }
}