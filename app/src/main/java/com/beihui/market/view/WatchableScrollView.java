package com.beihui.market.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


public class WatchableScrollView extends ScrollView {

    private OnScrollListener onScrollListener;

    public WatchableScrollView(Context context) {
        super(context);
    }

    public WatchableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WatchableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScrolled(t);
        }
    }

    public void setOnScrollListener(OnScrollListener listener) {
        onScrollListener = listener;
    }

    public interface OnScrollListener {
        void onScrolled(int dy);
    }

}
