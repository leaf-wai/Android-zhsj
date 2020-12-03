package com.leaf.zhsjalpha.adapter;

import android.content.Intent;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.activity.TeamDetailActivity;
import com.leaf.zhsjalpha.entity.Team;

import org.jetbrains.annotations.NotNull;

public class TeamAdapter extends BaseQuickAdapter<Team, BaseViewHolder> {
    private int id;

    public TeamAdapter(int layoutResId) {
        super(layoutResId);
        id = layoutResId;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Team team) {
        baseViewHolder.setText(R.id.tv_team_name, team.getTeamName());
        baseViewHolder.setText(R.id.tv_team_type, team.getTeamType());
        baseViewHolder.setText(R.id.tv_team_leader, team.getTeamLeader());
        if (id == R.layout.list_select_team_item)
            baseViewHolder.findView(R.id.btn_manage).setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), TeamDetailActivity.class);
                intent.putExtra("teamId", team.getTeamId());
                intent.putExtra("teamName", team.getTeamName());
                intent.putExtra("processId", team.getProcessId());
                getContext().startActivity(intent);
            });
    }
}
