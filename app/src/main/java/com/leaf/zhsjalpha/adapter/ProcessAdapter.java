package com.leaf.zhsjalpha.adapter;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.SelectTeamActivity;
import com.leaf.zhsjalpha.entity.Processes;

import org.jetbrains.annotations.NotNull;

public class ProcessAdapter extends BaseQuickAdapter<Processes, BaseViewHolder> {
    public ProcessAdapter() {
        super(R.layout.list_process_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Processes processes) {
        baseViewHolder.setText(R.id.tv_processDetail, processes.getProcessDetail());
        baseViewHolder.setText(R.id.tv_processType, processes.getProcessType());
        baseViewHolder.setText(R.id.tv_ruleDetail, processes.getRuleDetail());
        baseViewHolder.getView(R.id.btn_post).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SelectTeamActivity.class);
            intent.putExtra("processId", processes.getProcessId());
            getContext().startActivity(intent);
        });
        if (baseViewHolder.getAdapterPosition() == (this.getItemCount() - 1)) {
            baseViewHolder.getView(R.id.line).setVisibility(View.GONE);
        }
    }
}
