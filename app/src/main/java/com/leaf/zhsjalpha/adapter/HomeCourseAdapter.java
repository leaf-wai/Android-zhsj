package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Course;
import com.leaf.zhsjalpha.utils.MyApplication;
import com.leaf.zhsjalpha.viewholder.HomeCourseViewHolder;

import java.util.List;

public class HomeCourseAdapter extends RecyclerView.Adapter<HomeCourseViewHolder> {

    private List<Course> courses;

    public HomeCourseAdapter(List<Course> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public HomeCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_home_course_item, parent, false);
        return new HomeCourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.tvCourseName.setText(course.getCourseName());
        holder.tvOriginalPrice.setText(course.getOriginalPrice());
        holder.labelDate.setText(course.getDate());
        holder.labelAge.setText(course.getAge());
        holder.tvCourseType.setText(course.getType());

        if (course.getPrice() == 0) {
            holder.tvPrice.setText("免费");
        } else {
            holder.tvPrice.setText(String.valueOf(course.getPrice()));
        }
        Glide.with(MyApplication.getContext())
                .load(course.getCourseImgUrl())
                .placeholder(R.drawable.vector_drawable_logo_zhsj)
                .into(holder.rivCourseImage);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
