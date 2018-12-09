package com.trantan.listenmusicex.utils;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class Utils {
    public static String passTimeToString(int millisecond) {
        int minute = (int) ((millisecond / 1000) / 60);
        int second = (int) (millisecond / 1000) % 60;
        return String.format(("%02d:%02d"), minute, second);
    }

    public static void rotateImage(ImageView imageView, boolean isRotate) {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        if (isRotate) {
            if (imageView.getAnimation() == null) {
                imageView.startAnimation(rotateAnimation);
            }
        } else {
            imageView.setAnimation(null);
        }

    }
}
