package com.beihui.market.ui.adapter;


import android.graphics.Color;

import com.beihui.market.R;
import com.beihui.market.entity.Invitation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class InvitationAdapter extends BaseQuickAdapter<Invitation.Row, BaseViewHolder> {

    private List<Invitation.Row> dataSet;

    public InvitationAdapter() {
        super(R.layout.rv_item_invitation);
    }

    @Override
    protected void convert(BaseViewHolder helper, Invitation.Row item) {
        if (item.getPhone() != null) {
            helper.setText(R.id.phone, item.getPhone());
        }
        if (item.getStatus() != null) {
            helper.setText(R.id.status, item.getStatus());
        }
        int colorBg;
        if (helper.getAdapterPosition() % 2 == 0) {
            colorBg = Color.parseColor("#f5f8ff");
        } else {
            colorBg = Color.parseColor("#eef3ff");
        }
        helper.itemView.setBackgroundColor(colorBg);
    }

    public void notifyInvitationChanged(List<Invitation.Row> list) {
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
