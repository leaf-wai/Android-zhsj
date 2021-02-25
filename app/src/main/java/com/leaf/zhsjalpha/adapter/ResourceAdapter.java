package com.leaf.zhsjalpha.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Resource;
import com.leaf.zhsjalpha.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import static com.leaf.zhsjalpha.api.ApiService.BASE_URL2;

public class ResourceAdapter extends BaseQuickAdapter<Resource, BaseViewHolder> {
    public ResourceAdapter() {
        super(R.layout.list_resource_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Resource resource) {
        baseViewHolder.setText(R.id.tv_course_name, resource.getClassName());
        baseViewHolder.setText(R.id.tv_teacher_name, resource.getTeacherName());
        baseViewHolder.setText(R.id.tv_file_name, resource.getNowFileName());
        baseViewHolder.getView(R.id.btn_download).setOnClickListener(v -> FileUtils.downloadByBrowser(getContext(), BASE_URL2 + resource.getResourceURL()));
    }
}
