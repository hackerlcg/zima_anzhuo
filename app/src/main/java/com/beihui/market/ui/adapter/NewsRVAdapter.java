package com.beihui.market.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.News;
import com.beihui.market.util.DateFormatUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


public class NewsRVAdapter extends BaseQuickAdapter<News.Row, BaseViewHolder> {
    private List<News.Row> dataSet;

    public NewsRVAdapter() {
        super(R.layout.rv_item_news);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(BaseViewHolder helper, News.Row item) {
        if (item.getImage() != null) {
            Context context = helper.itemView.getContext();
            Glide.with(context)
                    .load(item.getImage())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into((ImageView) helper.getView(R.id.news_image));
        } else {
            helper.setImageResource(R.id.news_image, R.drawable.image_place_holder);
        }
        if (item.getTitle() != null) {
            helper.setText(R.id.news_title, item.getTitle());
        }
        if (item.getSource() != null) {
            helper.setText(R.id.news_source, item.getSource());
        }
        helper.setText(R.id.news_publish_time, DateFormatUtils.generateNewsDate(item.getGmtCreate()));
        helper.setText(R.id.news_read_times, item.getPv() + "阅读");

    }


    public void notifyNewsSetChanged(List<News.Row> list) {
        if (dataSet == null) {
            dataSet = new ArrayList<>();
        }
        dataSet.clear();
        if (list != null) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }

}
