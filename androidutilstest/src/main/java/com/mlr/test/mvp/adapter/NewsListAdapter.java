package com.mlr.test.mvp.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.mlr.adapter.MRecyclerViewAdapter;
import com.mlr.holder.BaseHolder;
import com.mlr.test.R;
import com.mlr.test.mvp.viewholder.ItemViewHolder;
import com.mlr.test.mvp.viewholder.PhotoViewHolder;
import com.mlr.utils.BaseActivity;

import java.util.List;

/**
 * Created by mulinrui on 12/16 0016.
 */
public class NewsListAdapter extends MRecyclerViewAdapter {

    public final static int VIEW_TYPE_COMMON_ITEM = VIEW_TYPE_ITEM;
    public final static int VIEW_TYPE_PHONE_ITEM = VIEW_TYPE_ITEM + 1;

    public NewsListAdapter(BaseActivity activity, List items) {
        super(activity, items);
    }

    @Override
    protected BaseHolder createItemHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_COMMON_ITEM) {
            View view = getActivity().inflate(R.layout.item_news, parent, false);
            return new ItemViewHolder(view, getActivity());
        } else {
            View view = getActivity().inflate(R.layout.item_news_photo, parent, false);
            return new PhotoViewHolder(view, getActivity());
        }
    }

    @Override
    protected void bindItemHolder(BaseHolder holder, int position, int viewType) {
        holder.setData(getData().get(position));
    }

}
