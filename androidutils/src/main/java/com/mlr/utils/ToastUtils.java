package com.mlr.utils;

import android.widget.Toast;

/**
 * 吐司相关工具类
 */
public class ToastUtils {

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //**********************Toast相关start*****************************
    private static Toast sToast;
    //当连续弹出吐司时，是要弹出新吐司还是只修改文本内容
    private static boolean isJumpWhenMore = false;

    public static void init(boolean isJumpWhenMore) {
        ToastUtils.isJumpWhenMore = isJumpWhenMore;
    }

    /**
     * 安全地显示短时吐司
     *
     * @param activity BaseActivity
     * @param text     文本
     */
    public static void showToastSafe(final BaseActivity activity, final CharSequence text, final int duration) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(activity, text, duration);
            }
        });
    }

    /**
     * 显示吐司
     *
     * @param activity BaseActivity
     * @param text     文本
     * @param duration 显示时长
     */
    private static void showToast(BaseActivity activity, CharSequence text, int duration) {
        if (isJumpWhenMore) {
            cancelToast();
        }
        if (sToast == null) {
            sToast = Toast.makeText(activity, text, duration);
        } else {
            sToast.setText(text);
            sToast.setDuration(duration);
        }
        sToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
    //**********************Toast相关end*****************************
}