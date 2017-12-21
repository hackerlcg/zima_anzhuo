package com.beihui.market.view;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beihui.market.R;
import com.beihui.market.util.CommonUtils;

public class IndicatorView extends LinearLayout {

    private ImageView[] indicators;

    private int selected;

    public IndicatorView(Context context) {
        super(context);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
    }

    public void setupWithViewPager(ViewPager viewPager) {
        if (viewPager == null || viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager is null or ViewPager has not attached a adapter");
        }
        PagerAdapter adapter = viewPager.getAdapter();
        inflateIndicators(adapter.getCount(), 0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void inflateIndicators(int size, int selected) {
        if (size > 0) {
            indicators = new ImageView[size];
            for (int i = 0; i < size; ++i) {
                ImageView indicator = new ImageView(getContext());
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                int margin = CommonUtils.dip2px(getContext(), 3);
                lp.leftMargin = margin;
                lp.rightMargin = margin;
                indicator.setLayoutParams(lp);
                indicator.setImageResource(R.drawable.pager_select);
                indicators[i] = indicator;

                addView(indicator);
            }

            selectIndicator(selected);
        }
    }

    private void selectIndicator(int selected) {
        if (this.selected < indicators.length) {
            indicators[this.selected].setSelected(false);
        }
        this.selected = selected;
        if (this.selected < indicators.length) {
            indicators[this.selected].setSelected(true);
        }
    }
}
