package com.beihui.market.ui.adapter;


import com.beihui.market.R;
import com.beihui.market.entity.SysMsg;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SysMsgAdapter extends BaseQuickAdapter<SysMsg.Row, BaseViewHolder> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
    private List<SysMsg.Row> dataSet;

    public SysMsgAdapter() {
        super(R.layout.rv_sys_msg);
    }

    @Override
    protected void convert(BaseViewHolder helper, SysMsg.Row item) {
        if (item.getTitle() != null) {
            helper.setText(R.id.title, item.getTitle());
        }
        if (item.getExplain() != null) {
            helper.setText(R.id.content, item.getExplain());
        }
        helper.setText(R.id.date, dateFormat.format(new Date(item.getGmtCreate())));
    }

    public void notifySysMsgChanged(List<SysMsg.Row> list) {
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
