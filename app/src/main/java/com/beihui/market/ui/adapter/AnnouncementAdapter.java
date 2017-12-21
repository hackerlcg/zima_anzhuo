package com.beihui.market.ui.adapter;


import com.beihui.market.R;
import com.beihui.market.entity.Notice;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnnouncementAdapter extends BaseQuickAdapter<Notice.Row, BaseViewHolder> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
    private List<Notice.Row> dataSet;

    public AnnouncementAdapter() {
        super(R.layout.rv_item_announcement);
    }

    @Override
    protected void convert(BaseViewHolder helper, Notice.Row item) {
        if (item.getTitle() != null) {
            helper.setText(R.id.title, item.getTitle());
        }
        if (item.getExplain() != null) {
            helper.setText(R.id.content, item.getExplain());
        }
        helper.setText(R.id.date, dateFormat.format(new Date(item.getGmtCreate())));
    }

    public void notifyAnnounceDataChanged(List<Notice.Row> list) {
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
