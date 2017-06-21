package com.mlr.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mlr.widget.LoadingAndRetryManager;
import com.mlr.widget.OnLoadingAndRetryListener;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 如果需要LoadingAndRetryManager 请调用initLoadingAndRetryManager方法
 */
public class BaseActivity extends AppCompatActivity implements OnLoadingAndRetryListener {

    // ==========================================================================
    // Constants
    // ==========================================================================

    // ==========================================================================
    // Fields
    // ==========================================================================

    protected LoadingAndRetryManager mLoadingAndRetryManager;

    private static List<BaseActivity> activityList = new LinkedList<BaseActivity>();

    // ==========================================================================
    // Constructors
    // ==========================================================================

    // ==========================================================================
    // Getters
    // ==========================================================================


    // ==========================================================================
    // Setters
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================


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
        super.onCreate(savedInstanceState);
        activityList.add(this);

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


    //**********************LoadingAndRetryManager相关start*****************************

    /**
     * 如果需要LoadingAndRetryManager 请调用initLoadingAndRetryManager方法
     */
    protected void initLoadingAndRetryManager() {
        mLoadingAndRetryManager = LoadingAndRetryManager.generate(this, this);
    }

    protected void showProgress() {
        if (mLoadingAndRetryManager == null) {
            throw new RuntimeException("没有初始化initLoadingAndRetryManager 不能使用该方法");
        }
        mLoadingAndRetryManager.showLoading();
    }

    protected void hideProgress() {
        if (mLoadingAndRetryManager == null) {
            throw new RuntimeException("没有初始化initLoadingAndRetryManager 不能使用该方法");
        }
        mLoadingAndRetryManager.showContent();
    }

    protected void showToast(String msg) {
        ToastUtils.showToastSafe(this, msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void setRetryEvent(View retryView) {

    }

    @Override
    public void setEmptyEvent(View emptyView) {
    }

    @Override
    public void setLoadingEvent(View loadingView) {

    }

    @Override
    public int generateLoadingLayoutId() {
        return R.layout.base_loading;
    }

    @Override
    public int generateRetryLayoutId() {
        return R.layout.offline_layout;
    }

    @Override
    public int generateEmptyLayoutId() {
        return R.layout.no_content_layout;
    }

    @Override
    public View generateLoadingLayout() {
        return null;
    }

    @Override
    public View generateRetryLayout() {
        return null;
    }

    @Override
    public View generateEmptyLayout() {
        return null;
    }

    //**********************LoadingAndRetryManager相关end*****************************
    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================

}
