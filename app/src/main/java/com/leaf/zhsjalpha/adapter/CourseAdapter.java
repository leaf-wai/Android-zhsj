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
import com.leaf.zhsjalpha.viewholder.CourseViewHolder;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {
    private List<Course> courses;

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_course_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        Glide.with(MyApplication.getContext())
                .load(course.getCourseImgUrl())
                .placeholder(R.drawable.vector_drawable_logo_zhsj)
                .into(holder.rivCourseImage);
        holder.tvCourseName.setText(course.getCourseName());
//        holder.tvOriginalPrice.setText(course.getOriginalPrice());
        if (course.getPrice() == 0) {
            holder.tvPrice.setText("免费");
        } else {
            holder.tvPrice.setText(String.valueOf(course.getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
