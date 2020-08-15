package com.leaf.zhsjalpha.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.leaf.zhsjalpha.R;

public class ToastUtils {
    private static Toast currentToast;
    private static ToastUtils toastUtils;
    private static Context mContext;

    public static ToastUtils getInstance() {
        if (toastUtils == null) {
            toastUtils = new ToastUtils();
        }
        return toastUtils;
    }

    public static void showToast(String message, int duration) {
        if (currentToast == null) {
            currentToast = new Toast(mContext);
        }

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.toast_layout, null);
        TextView toast_message = view.findViewById(R.id.toast_message);
        toast_message.setText(message);
        currentToast.setView(view);
        currentToast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 300);
        currentToast.setDuration(duration);
        currentToast.show();
    }

    public static void showToast(String message, int duration, int textColor, int bgColor) {
        if (currentToast == null) {
            currentToast = new Toast(mContext);
        }

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.toast_layout, null);
        TextView toast_message = view.findViewById(R.id.toast_message);
        CardView cardView = view.findViewById(R.id.cv_toast);
        toast_message.setText(message);
        toast_message.setTextColor(textColor);
        cardView.setCardBackgroundColor(bgColor);
        currentToast.setView(view);
        currentToast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 300);
        currentToast.setDuration(duration);
        currentToast.show();
    }

    public void initToast(Context context) {
        if (null == mContext) {
            this.mContext = context;
        }
        if (null == currentToast) {
            currentToast = new Toast(mContext);
        }
    }

}