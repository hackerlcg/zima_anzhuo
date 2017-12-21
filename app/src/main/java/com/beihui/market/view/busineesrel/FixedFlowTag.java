package com.beihui.market.view.busineesrel;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.beihui.market.R;


public class FixedFlowTag extends FrameLayout {

    private View curSelectedTagView;

    private OnTagChangedListener tagChangedListener;

    public FixedFlowTag(@NonNull Context context) {
        super(context);
        init(context);
    }

    public FixedFlowTag(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FixedFlowTag(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_fixed_flow_tag, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setOnTagChangedListener(OnTagChangedListener listener) {
        tagChangedListener = listener;
    }


    public interface OnTagChangedListener {
        void OnTagChanged(String tagSelected);
    }
}
