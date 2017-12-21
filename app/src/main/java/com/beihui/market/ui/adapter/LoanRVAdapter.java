package com.beihui.market.ui.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.LoanProduct;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


public class LoanRVAdapter extends BaseQuickAdapter<LoanProduct.Row, BaseViewHolder> {

    private List<LoanProduct.Row> dataSet = new ArrayList<>();

    private int[] tagIds = {R.id.tag_1, R.id.tag_2};

    public LoanRVAdapter() {
        super(R.layout.rv_item_loan);
        setNewData(dataSet);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoanProduct.Row item) {
        //参考日月息
        if (item.getInterestTimeText() != null) {
            helper.setText(R.id.interest_text, item.getInterestTimeText());
        }
        //logo
        if (!TextUtils.isEmpty(item.getLogoUrl())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogoUrl())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into((ImageView) helper.getView(R.id.loan_image));
        } else {
            helper.setImageResource(R.id.loan_image, R.drawable.image_place_holder);
        }
        //name
        if (item.getProductName() != null) {
            helper.setText(R.id.loan_name, item.getProductName());
        }
        //tags
        String[] feature = item.getFeature() != null ? item.getFeature().split(",") : null;
        int visibleCount = 0;
        if (feature != null && feature.length > 0 && !feature[0].equals("")) {
            for (int i = 0; i < feature.length && i < tagIds.length; ++i) {
                helper.setVisible(tagIds[i], true);
                helper.setText(tagIds[i], feature[i]);
                visibleCount++;
            }
        }
        for (int i = tagIds.length - 1; i >= visibleCount; --i) {
            helper.setVisible(tagIds[i], false);
        }
        //loaned number
        SpannableString ss = new SpannableString("成功借款" + item.getSuccessCount() + "人");
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff395e")), 4, ss.length() - 1,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        helper.setText(R.id.loaned_number, ss);
        //amount
        if (item.getBorrowingHighText() != null) {
            helper.setText(R.id.loan_max_amount, item.getBorrowingHighText());
        }
        //interest
        if (item.getInterestLowText() != null) {
            helper.setText(R.id.loan_interests, item.getInterestLowText());
        }
        //time
        if (item.getDueTimeText() != null) {
            helper.setText(R.id.loan_time_range, item.getDueTimeText());
        }
        //badge
        int sign = getLabelIcon(item.getProductSign());
        if (sign != -1) {
            helper.setVisible(R.id.loan_badge, true);
            helper.setImageResource(R.id.loan_badge, sign);
        } else {
            helper.setVisible(R.id.loan_badge, false);
        }
    }

    public void notifyLoanProductChanged(List<LoanProduct.Row> list) {
        dataSet.clear();
        if (list != null) {
            dataSet.addAll(list);
        }
        notifyDataSetChanged();
    }

    private int getLabelIcon(int type) {
        switch (type) {
            case 1:
                return R.drawable.label_new;
            case 2:
                return R.drawable.label_hot;
            case 3:
                return R.drawable.label_jian;
            case 4:
                return R.drawable.label_featured;
            default:
                return -1;
        }
    }
}
