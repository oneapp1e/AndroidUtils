package com.mlr.test.mvp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mlr.adapter.MRecyclerViewAdapter;
import com.mlr.holder.BaseHolder;
import com.mlr.utils.BaseActivity;

import java.util.List;

/**
 * Created by mulinrui on 12/16 0016.
 */
public class NewsListAdapter extends MRecyclerViewAdapter {

    public NewsListAdapter(BaseActivity activity, List items) {
        super(activity, items);
    }

    @Override
    protected BaseHolder createItemHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void bindItemHolder(BaseHolder holder, int position, int viewType) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
