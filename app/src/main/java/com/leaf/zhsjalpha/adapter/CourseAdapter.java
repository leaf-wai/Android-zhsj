package com.leaf.zhsjalpha.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leaf.zhsjalpha.R;
import com.leaf.zhsjalpha.entity.Course;
import com.leaf.zhsjalpha.viewholder.CourseViewHolder;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {

    private List<Course> courses;

    public CourseAdapter(List<Course> courses) {
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
        holder.tvCourseName.setText(course.getCourseName());
        holder.tvPrice.setText(course.getPrice());
        holder.tvOriginalPrice.setText(course.getOriginalPrice());
        holder.labelDate.setText(course.getDate());
        holder.labelAge.setText(course.getAge());
        holder.tvCourseType.setText(course.getType());
        holder.rivCourseImage.setImageResource(course.getCourseImageID());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
