package com.leaf.zhsjalpha.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.leaf.zhsjalpha.R;

public class ChipsViewHolder extends RecyclerView.ViewHolder {
    public Chip chip;

    public ChipsViewHolder(@NonNull View itemView) {
        super(itemView);
        chip = itemView.findViewById(R.id.chip_search_history);
    }
}
