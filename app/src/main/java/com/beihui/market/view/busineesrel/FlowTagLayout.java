package com.beihui.market.view.busineesrel;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beihui.market.R;

public class FlowTagLayout extends ViewGroup implements View.OnClickListener {

    private int childrenMargin;
    private int lineSpace;

    private View selectedView;

    private TagSelectedListener listener;

    public FlowTagLayout(Context context) {
        this(context, null);
    }

    public FlowTagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowTagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        childrenMargin = (int) (context.getResources().getDisplayMetrics().density * 10);
        lineSpace = childrenMargin;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int avaWidth = width - getPaddingLeft() - getPaddingRight();
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            int height = getChildAt(0).getMeasuredHeight();
            int usedWidth = 0;
            int spaceNumber = 0;
            for (int i = 0, count = getChildCount(); i < count; ++i) {
                View child = getChildAt(i);
                usedWidth += child.getMeasuredWidth() + childrenMargin;
                if (usedWidth > avaWidth) {
                    i--;
                    usedWidth = 0;
                    height += child.getMeasuredHeight();
                    spaceNumber++;
                }
            }
            setMeasuredDimension(width, height + getPaddingTop() + getPaddingBottom() + spaceNumber * lineSpace);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() > 0) {
            int anchorX = getPaddingLeft();
            int anchorY = getPaddingTop();
            int width = getMeasuredWidth() - anchorX - anchorY;
            for (int i = 0, count = getChildCount(); i < count; ++i) {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                anchorX += childWidth + childrenMargin;
                if (anchorX > width) {
                    i--;
                    anchorX = getPaddingLeft();
                    anchorY = childHeight + lineSpace;
                } else {
                    child.layout(anchorX - childWidth - childrenMargin, anchorY, anchorX - childrenMargin, anchorY + childHeight);
                }

            }
        }
    }

    public void setTags(String[] tags, int selected) {
        removeAllViews();
        inflateTag(tags);
        if (selected >= 0 && selected < getChildCount()) {
            selectedTag(selected, false);
        }
    }

    private void inflateTag(String[] tags) {
        if (tags != null) {
            for (int i = 0; i < tags.length; ++i) {
                inflate(getContext(), R.layout.layout_flow_tag_layout_item, this);
                TextView tagView = (TextView) getChildAt(getChildCount() - 1);
                tagView.setText(tags[i]);
                tagView.setOnClickListener(this);
                tagView.setTag(i);
            }
        }
    }

    private void selectedTag(int selected, boolean notify) {
        if (getChildAt(selected) == selectedView) {
            return;
        }
        if (selectedView != null) {
            selectedView.setSelected(false);
        }
        selectedView = getChildAt(selected);
        selectedView.setSelected(true);

        if (listener != null && notify) {
            listener.onTagSelected(selected);
        }
    }

    @Override
    public void onClick(View v) {
        selectedTag((Integer) v.getTag(), true);
    }

    public void setTagSelectedListener(TagSelectedListener listener) {
        this.listener = listener;
    }

    public interface TagSelectedListener {
        void onTagSelected(int selected);
    }
}
