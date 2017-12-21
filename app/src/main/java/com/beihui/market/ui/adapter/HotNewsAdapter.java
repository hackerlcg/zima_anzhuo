package com.beihui.market.ui.adapter;


import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.HotNews;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class HotNewsAdapter extends BaseQuickAdapter<HotNews, BaseViewHolder> {
    private List<HotNews> dataSet;

    public HotNewsAdapter() {
        super(R.layout.rv_item_hot_news);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotNews item) {
        if (item.getTitle() != null) {
            helper.setText(R.id.news_title, item.getTitle());
        }
        if (item.getFilePath() != null) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getFilePath())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into((ImageView) helper.getView(R.id.news_image));
        } else {
            helper.setImageResource(R.id.news_image, R.drawable.image_place_holder);
        }
    }

    public void notifyHotNewsChanged(List<HotNews> news) {
        if (dataSet == null) {
            dataSet = new ArrayList<>();
        }
        dataSet.clear();
        if (news != null) {
            dataSet.addAll(news);
        }
        setNewData(dataSet);
    }
}
