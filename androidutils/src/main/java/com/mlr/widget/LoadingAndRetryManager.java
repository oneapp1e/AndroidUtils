package com.mlr.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.mlr.utils.R;

/**
 * Created by zhy on 15/8/27.
 */
public class LoadingAndRetryManager {
    public static final int NO_LAYOUT_ID = 0;
    public static int BASE_LOADING_LAYOUT_ID = R.layout.base_loading;
    public static int BASE_RETRY_LAYOUT_ID = R.layout.offline_layout;
    public static int BASE_EMPTY_LAYOUT_ID = R.layout.no_content_layout;

    public LoadingAndRetryLayout mLoadingAndRetryLayout;


    public OnLoadingAndRetryListener DEFAULT_LISTENER = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView) {

        }

        @Override
        public void setLoadingEvent(View loadingView) {

        }

        @Override
        public void setEmptyEvent(View emptyView) {

        }

        public int generateLoadingLayoutId() {
            return LoadingAndRetryManager.NO_LAYOUT_ID;
        }

        public int generateRetryLayoutId() {
            return LoadingAndRetryManager.NO_LAYOUT_ID;
        }

        public int generateEmptyLayoutId() {
            return LoadingAndRetryManager.NO_LAYOUT_ID;
        }

        public View generateLoadingLayout() {
            return null;
        }

        public View generateRetryLayout() {
            return null;
        }

        public View generateEmptyLayout() {
            return null;
        }

    };


    public LoadingAndRetryManager(Object activityOrFragmentOrView, OnLoadingAndRetryListener listener) {
        if (listener == null) listener = DEFAULT_LISTENER;

        ViewGroup contentParent = null;
        Context context;
        if (activityOrFragmentOrView instanceof Activity) {
            Activity activity = (Activity) activityOrFragmentOrView;
            context = activity;
            contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        } else if (activityOrFragmentOrView instanceof Fragment) {
            Fragment fragment = (Fragment) activityOrFragmentOrView;
            context = fragment.getActivity();
            contentParent = (ViewGroup) (fragment.getView().getParent());
        } else if (activityOrFragmentOrView instanceof View) {
            View view = (View) activityOrFragmentOrView;
            contentParent = (ViewGroup) (view.getParent());
            context = view.getContext();
        } else {
            throw new IllegalArgumentException("the argument's type must be Fragment or Activity: init(context)");
        }
        int childCount = contentParent.getChildCount();
        //get contentParent
        int index = 0;
        View oldContent;
        if (activityOrFragmentOrView instanceof View) {
            oldContent = (View) activityOrFragmentOrView;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    index = i;
                    break;
                }
            }
        } else {
            oldContent = contentParent.getChildAt(0);
        }
        contentParent.removeView(oldContent);
        //setup content layout
        LoadingAndRetryLayout loadingAndRetryLayout = new LoadingAndRetryLayout(context);

        ViewGroup.LayoutParams lp = oldContent.getLayoutParams();
        contentParent.addView(loadingAndRetryLayout, index, lp);
        loadingAndRetryLayout.setContentView(oldContent);
        // setup loading,retry,empty layout
        setupLoadingLayout(listener, loadingAndRetryLayout);
        setupRetryLayout(listener, loadingAndRetryLayout);
        setupEmptyLayout(listener, loadingAndRetryLayout);
        //callback
        listener.setRetryEvent(loadingAndRetryLayout.getRetryView());
        listener.setLoadingEvent(loadingAndRetryLayout.getLoadingView());
        listener.setEmptyEvent(loadingAndRetryLayout.getEmptyView());
        mLoadingAndRetryLayout = loadingAndRetryLayout;
    }

    private void setupEmptyLayout(OnLoadingAndRetryListener listener, LoadingAndRetryLayout loadingAndRetryLayout) {
        if (isSetEmptyLayout(listener)) {
            int layoutId = listener.generateEmptyLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setEmptyView(layoutId);
            } else {
                loadingAndRetryLayout.setEmptyView(listener.generateEmptyLayout());
            }
        } else {
            if (BASE_EMPTY_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setEmptyView(BASE_EMPTY_LAYOUT_ID);
        }
    }

    private void setupLoadingLayout(OnLoadingAndRetryListener listener, LoadingAndRetryLayout loadingAndRetryLayout) {
        if (isSetLoadingLayout(listener)) {
            int layoutId = listener.generateLoadingLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setLoadingView(layoutId);
            } else {
                loadingAndRetryLayout.setLoadingView(listener.generateLoadingLayout());
            }
        } else {
            if (BASE_LOADING_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setLoadingView(BASE_LOADING_LAYOUT_ID);
        }
    }

    private void setupRetryLayout(OnLoadingAndRetryListener listener, LoadingAndRetryLayout loadingAndRetryLayout) {
        if (isSetRetryLayout(listener)) {
            int layoutId = listener.generateRetryLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setRetryView(layoutId);
            } else {
                loadingAndRetryLayout.setRetryView(listener.generateRetryLayout());
            }
        } else {
            if (BASE_RETRY_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setRetryView(BASE_RETRY_LAYOUT_ID);
        }
    }


    public final boolean isSetLoadingLayout(OnLoadingAndRetryListener listener) {
        if (listener.generateLoadingLayoutId() != LoadingAndRetryManager.NO_LAYOUT_ID || listener.generateLoadingLayout() != null)
            return true;
        return false;
    }

    public final boolean isSetRetryLayout(OnLoadingAndRetryListener listener) {
        if (listener.generateRetryLayoutId() != LoadingAndRetryManager.NO_LAYOUT_ID || listener.generateRetryLayout() != null)
            return true;
        return false;
    }

    public final boolean isSetEmptyLayout(OnLoadingAndRetryListener listener) {
        if (listener.generateEmptyLayoutId() != LoadingAndRetryManager.NO_LAYOUT_ID || listener.generateEmptyLayout() != null)
            return true;
        return false;
    }


    public static LoadingAndRetryManager generate(Object activityOrFragment, OnLoadingAndRetryListener listener) {
        return new LoadingAndRetryManager(activityOrFragment, listener);
    }

    public void showLoading() {
        mLoadingAndRetryLayout.showLoading();
    }

    public void showRetry() {
        mLoadingAndRetryLayout.showRetry();
    }

    public void showContent() {
        mLoadingAndRetryLayout.showContent();
    }

    public void showEmpty() {
        mLoadingAndRetryLayout.showEmpty();
    }


}
