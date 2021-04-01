package com.leaf.zhsjalpha.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.leaf.zhsjalpha.R;

public class ToastUtils {
    private static Toast mToast;

    public static void showToast(Context context, String message) {
        showToast(context, message, 0, 0);
    }

    public static void showToast(Context context, String message, int textColor, int bgColor) {
        if (context != null) {
            mToast = new Toast(context);
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
            TextView toast_message = view.findViewById(R.id.toast_message);
            CardView cardView = view.findViewById(R.id.cv_toast);
            toast_message.setText(message);
            if (textColor != 0 && bgColor != 0) {
                toast_message.setTextColor(textColor);
                cardView.setCardBackgroundColor(bgColor);
            }
            mToast.setView(view);
            mToast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 300);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    public static void showToastInThread(Context context, String message) {
        Looper.prepare();
        showToast(context, message);
        Looper.loop();
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}