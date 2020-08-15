package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.MyCourse;
import com.leaf.zhsjalpha.viewholder.MyCourseViewHolder;

import java.util.List;

public class MyCourseAdapter extends RecyclerView.Adapter<MyCourseViewHolder> {

    private List<MyCourse> myCourses;

    public MyCourseAdapter(List<MyCourse> myCourses) {
        this.myCourses = myCourses;
    }

    @NonNull
    @Override
    public MyCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_my_course_item, parent, false);
        return new MyCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCourseViewHolder holder, int position) {
        MyCourse myCourse = myCourses.get(position);
        holder.tvCourseName.setText(myCourse.getCourseName());
        holder.rivCourseImage.setImageResource(myCourse.getCourseImageID());
    }

    @Override
    public int getItemCount() {
        return myCourses.size();
    }
}
