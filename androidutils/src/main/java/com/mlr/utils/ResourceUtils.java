package com.mlr.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by mulinrui on 6/21 0021.
 */

public class ResourceUtils {


    /**
     * 根据资源上下文，将dip值转换为pixel值
     *
     * @param dipValue dip值
     * @return pixel值
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据资源上下文，将pixel值转换为dip值
     *
     * @param pxValue pixel值
     * @return dip值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取资源图片
     *
     * @param resId 资源id
     * @return 图片
     */
    public static Drawable getResDrawable(Context context, int resId) {//5.0方法冲突
        return context.getResources().getDrawable(resId);
    }

    /**
     * 获取资源色值
     *
     * @param resId 资源id
     * @return 色值
     */
    public static int getResColor(Context context, int resId) {
        return context.getResources().getColor(resId);
    }

    /**
     * 获取资源像素值
     *
     * @param resId 资源id
     * @return 像素值
     */
    public static int getDimensionPixel(Context context, int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }

}
