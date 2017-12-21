package com.beihui.market.ui.adapter;


import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.Message;
import com.beihui.market.util.DateFormatUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {
    private List<Message> dataSet;

    public MessageCenterAdapter() {
        super(R.layout.rv_item_internal_message);
    }


    public void notifyMessageChanged(List<Message> list) {
        if (dataSet == null) {
            dataSet = new ArrayList<>();
        }
        dataSet.clear();
        if (list != null) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        if (item.getImgUrl() != null) {
            Context context = helper.itemView.getContext();
            Glide.with(context)
                    .load(item.getImgUrl())
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
        helper.setText(R.id.news_publish_time, DateFormatUtils.generateNewsDate(item.getGmtCreate()));
        helper.itemView.setBackgroundColor(Color.WHITE);
    }

}
