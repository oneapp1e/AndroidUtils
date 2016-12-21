package com.mlr.test.mvp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mlr.holder.BaseHolder;
import com.mlr.test.R;
import com.mlr.test.mvp.entity.NewsSummary;
import com.mlr.utils.BaseActivity;

/**
 * Created by mulinrui on 12/21 0021.
 */
public class ItemViewHolder extends BaseHolder<NewsSummary> {

    ImageView mNewsSummaryPhotoIv;
    TextView mNewsSummaryTitleTv;
    TextView mNewsSummaryDigestTv;
    TextView mNewsSummaryPtimeTv;

    public ItemViewHolder(View itemView, BaseActivity activity) {
        super(itemView, activity);
        mNewsSummaryPhotoIv = (ImageView) itemView.findViewById(R.id.news_summary_photo_iv);
        mNewsSummaryTitleTv = (TextView) itemView.findViewById(R.id.news_summary_title_tv);
        mNewsSummaryDigestTv = (TextView) itemView.findViewById(R.id.news_summary_digest_tv);
        mNewsSummaryPtimeTv = (TextView) itemView.findViewById(R.id.news_summary_ptime_tv);
    }

    @Override
    public void setData(NewsSummary newsSummary) {
        super.setData(newsSummary);
        String title = newsSummary.getLtitle();
        if (title == null) {
            title = newsSummary.getTitle();
        }
        String ptime = newsSummary.getPtime();
        String digest = newsSummary.getDigest();
        String imgSrc = newsSummary.getImgsrc();

        mNewsSummaryTitleTv.setText(title);
        mNewsSummaryPtimeTv.setText(ptime);
        mNewsSummaryDigestTv.setText(digest);

        Glide.with(getActivity()).load(imgSrc).asBitmap() // gif格式有时会导致整体图片不显示，貌似有冲突
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.image_place_holder)
                .error(R.drawable.ic_load_fail)
                .into(mNewsSummaryPhotoIv);
    }
}
