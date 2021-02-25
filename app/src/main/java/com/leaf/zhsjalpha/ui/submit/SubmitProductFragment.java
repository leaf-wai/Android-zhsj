package com.leaf.zhsjalpha.ui.submit;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.bean.PostTypeBean;
import com.leaf.zhsjalpha.bean.User;
import com.leaf.zhsjalpha.databinding.FragmentSubmitProductBinding;
import com.leaf.zhsjalpha.fragment.LoadingFragment;
import com.leaf.zhsjalpha.utils.JsonUtils;
import com.leaf.zhsjalpha.utils.ToastUtils;
import com.leaf.zhsjalpha.utils.UriTofilePath;
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

public class SubmitProductFragment extends Fragment {

    private Uri mCameraUri;
    private Uri mGalleryUri;
    private Uri submitUri;
    private String mCameraImagePath;
    private final boolean isAndroid10 = Build.VERSION.SDK_INT >= 29;

    private SubmitViewModel mViewModel;
    private FragmentSubmitProductBinding binding;
    private LoadingFragment loadingFragment;

    private String[] classItem = null;
    private int selectedClass = -1;
    private List<PostTypeBean> postTypeBeanList;
    private List<String> postTypeNameList = new ArrayList<>();

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    private final Callback<User> submitCallback = new Callback<User>() {
        @Override
        public void onResponse(@NotNull Call<User> call, Response<User> response) {
            loadingFragment.dismiss();
            if (response.isSuccessful() && response.body() != null)
                ToastUtils.showToast(getContext(), response.body().getDetail());
        }

        @Override
        public void onFailure(@NotNull Call<User> call, @NotNull Throwable t) {
            loadingFragment.dismiss();
            ToastUtils.showToast(getContext(), "网络请求失败！请重试");
        }
    };

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_class:
                    if (classItem != null)
                        showDialogSelectClass();
                    break;
                case R.id.iv_delete:
                    cleanImage();
                    break;
                case R.id.iv_uploadImage:
                    showAddPhotoDialog();
                    break;
                case R.id.btn_submit:
                    if (submitUri != null) {
                        loadingFragment.show(getChildFragmentManager(), "submit");
                        mViewModel.postProduct(submitUri, String.valueOf(binding.etTitle.getText()), String.valueOf(binding.etContent.getText()), submitCallback);
                    }
                    break;
            }
        }
    };

    private final View.OnClickListener previewListener = v -> {
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

    public static SubmitProductFragment newInstance() {
        return new SubmitProductFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubmitProductBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(SubmitViewModel.class);
        loadingFragment = new LoadingFragment().newInstance("正在提交…", getResources().getColor(R.color.colorPrimary));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initACTV();
        addListener();
        addObserver();
    }

    private void addListener() {
        binding.actvType.setOnItemClickListener((parent, view, position, id) -> {
            mViewModel.postType = postTypeBeanList.get(position).getPostTypeValue();
        });
        binding.llClass.setOnClickListener(listener);
        binding.ivDelete.setOnClickListener(listener);
        binding.ivUploadImage.setOnClickListener(listener);
        binding.btnSubmit.setOnClickListener(listener);
        binding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    binding.btnSubmit.setEnabled(true);
                } else {
                    binding.btnSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addObserver() {
        mViewModel.getClassItem().observe(getViewLifecycleOwner(), strings -> {
            if (strings.size() == 0) {
                binding.tvClass.setText("暂无班级，快去选课吧~");
                binding.llClass.setClickable(false);
                binding.btnSubmit.setEnabled(false);
            } else {
                binding.tvClass.setText("选择班级");
                binding.llClass.setClickable(true);
                classItem = strings.toArray(new String[strings.size()]);
            }
        });
    }

    private void initACTV() {
        String JsonData = JsonUtils.getJson(getContext(), "PostType.json");
        postTypeBeanList = JsonUtils.parsePostTypeData(JsonData);
        for (PostTypeBean postTypeBean : postTypeBeanList) {
            postTypeNameList.add(postTypeBean.getPostTypeName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_actv_item, postTypeNameList);
        binding.actvType.setAdapter(adapter);
    }

    private void showDialogSelectClass() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle("请选择班级");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(classItem, selectedClass, (dialog, which) -> {
            selectedClass = which;
            mViewModel.getClassId().postValue(mViewModel.courseDataList.get(which).getClassId());
            binding.tvClass.setText(classItem[which]);
            dialog.dismiss();
        });
        builder.show();
    }

    private void showAddPhotoDialog() {
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

    private void cleanImage() {
        mCameraUri = null;
        mGalleryUri = null;
        mCameraImagePath = null;
        Glide.with(SubmitProductFragment.this)
                .load(getActivity().getResources().getDrawable(R.drawable.ic_add_image))
                .into(binding.ivUploadImage);
        binding.ivDelete.setVisibility(View.INVISIBLE);
        binding.ivUploadImage.setOnClickListener(listener);
    }

    private void openCamera() {
        PermissionX.init(SubmitProductFragment.this)
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
        PermissionX.init(SubmitProductFragment.this)
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

    @Override
    public void onResume() {
        super.onResume();
    }
}