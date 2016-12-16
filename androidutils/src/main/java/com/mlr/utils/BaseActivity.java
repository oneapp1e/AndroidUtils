package com.mlr.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaseActivity extends AppCompatActivity {

    // ==========================================================================
    // Constants
    // ==========================================================================

    // ==========================================================================
    // Fields
    // ==========================================================================
    private LayoutInflater mInflater;

    protected BaseActivity mActivity;

    private static List<BaseActivity> activityList = new LinkedList<BaseActivity>();

    // ==========================================================================
    // Constructors
    // ==========================================================================

    // ==========================================================================
    // Getters
    // ==========================================================================

    /**
     * 内置handler
     */
    private Handler mDefaultHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            onHandleMessage(msg);
        }

    };

    // ==========================================================================
    // Setters
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================

    /**
     * 内置handler处理到某个消息时，该方法被回调。子类实现该方法以定义对消息的处理。
     *
     * @param msg 消息
     */
    protected void onHandleMessage(Message msg) {

    }


    /**
     * 向内置handler的消息队列中增加一个任务。该任务会在将来的某一时刻在UI线程执行。
     *
     * @param r 任务
     */
    public boolean post(Runnable r) {
        return mDefaultHandler.post(r);
    }

    /**
     * 向内置handler的消息队列中增加一个任务。该任务会在指定延时后的某一时刻在UI线程执行。
     *
     * @param r           任务
     * @param delayMillis 延时的毫秒数
     */
    public boolean postDelayed(Runnable r, long delayMillis) {
        return mDefaultHandler.postDelayed(r, delayMillis);
    }

    private boolean initInflater() {
        try {
            mInflater = LayoutInflater.from(this);
        } catch (Throwable tr) {
            mInflater = null;
        }
        return null != mInflater;
    }

    /**
     * 根据指定的layout索引，创建一个View
     *
     * @param resId 指定的layout索引
     * @return 新的View
     */
    public View inflate(int resId) {
        return inflate(resId, null, false);
    }


    //**********************资源相关start*****************************

    /**
     * 根据指定的layout索引，创建一个View
     *
     * @param resId        指定的layout索引
     * @param parent       父view  没有可以传null
     * @param attachToRoot 视图生成后是否直接加入到parent中
     * @return 新的View
     */
    public View inflate(int resId, ViewGroup parent, boolean attachToRoot) {
        if (null == mInflater && !initInflater()) {
            return null;
        }
        return mInflater.inflate(resId, parent, attachToRoot);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 根据资源上下文，将dip值转换为pixel值
     *
     * @param dipValue dip值
     * @return pixel值
     */
    public int dip2px(float dipValue) {
        return dip2px(this, dipValue);
    }

    /**
     * 根据资源上下文，将pixel值转换为dip值
     *
     * @param pxValue pixel值
     * @return dip值
     */
    public int px2dip(float pxValue) {
        return px2dip(this, pxValue);
    }

    /**
     * 获取资源图片
     *
     * @param resId 资源id
     * @return 图片
     */
    public Drawable getResDrawable(int resId) {//5.0方法冲突
        return getResources().getDrawable(resId);
    }

    /**
     * 获取资源色值
     *
     * @param resId 资源id
     * @return 色值
     */
    public int getResColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取资源像素值
     *
     * @param resId 资源id
     * @return 像素值
     */
    public int getDimensionPixel(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    //**********************资源相关end*****************************

    //**********************事件相关start*****************************

    /**
     * 检测一个Motion事件是否在指定View的区域内
     *
     * @param v  指定的View
     * @param ev Motion事件
     * @return 在区域内返回true，否则返回false
     */
    public static boolean isMotionEventInView(View v, MotionEvent ev) {
        if (v == null || ev == null) {
            return false;
        }
        int[] coord = new int[2];

        v.getLocationOnScreen(coord);
        final int absLeft = coord[0];
        final int absRight = coord[0] + v.getWidth();
        final int absTop = coord[1];
        final int absBottom = coord[1] + v.getHeight();

        return (ev.getX() >= absLeft && ev.getX() < absRight
                && ev.getY() >= absTop && ev.getY() < absBottom);
    }

    /**
     * 将一个Motion事件的坐标转换为相对于某个View的坐标
     *
     * @param v        指定的View
     * @param srcEvent Motion事件
     * @param inView   如果Motion事件发生在指定的View内，该引用将被赋为true，反之false
     * @return 返回转换后的结果
     */
    public static MotionEvent getRelativeMotionEventInView(View v,
                                                           MotionEvent srcEvent, AtomicBoolean inView) {
        if (v == null || srcEvent == null) {
            return null;
        }
        int[] coord = new int[2];

        v.getLocationOnScreen(coord);
        final int absLeft = coord[0];
        final int absRight = coord[0] + v.getWidth();
        final int absTop = coord[1];
        final int absBottom = coord[1] + v.getHeight();

        inView.set(srcEvent.getX() >= absLeft && srcEvent.getX() < absRight
                && srcEvent.getY() >= absTop && srcEvent.getY() < absBottom);

        MotionEvent relativeEvent = MotionEvent.obtain(srcEvent);
        relativeEvent.offsetLocation(-absLeft, -absTop);

        return relativeEvent;
    }
    //**********************事件相关end*****************************

    public boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    //**********************生命周期相关start*****************************

    protected BaseActivity getActivity() {
        return mActivity;
    }

    public List<BaseActivity> getActivityList() {
        return activityList;
    }

    /**
     * 退出应用的时候调用
     */
    public void finishAll() {
        for (Activity a : activityList) {
            a.finish();
        }
        activityList.clear();
    }

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        activityList.add(this);
        initInflater();
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
    }

    @CallSuper
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
    }

    @CallSuper
    @Override
    protected void onStop() {
        super.onStop();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        activityList.remove(this);
        super.onDestroy();
    }

    //**********************生命周期相关end*****************************
    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================

}
