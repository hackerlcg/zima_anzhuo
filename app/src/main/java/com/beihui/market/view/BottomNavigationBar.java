package com.beihui.market.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class BottomNavigationBar extends LinearLayout {

    private int mSelectedId = -1;

    private OnSelectedChangedListener mListener;

    private OnClickListener mInternalClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int clickedId = v.getId();
            if (clickedId != mSelectedId) {
                select(clickedId);
            }
        }
    };

    public BottomNavigationBar(Context context) {
        super(context);
        init();
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        child.setOnClickListener(mInternalClickListener);
    }

    /**
     * change selected view
     *
     * @param id id for the view that be selected
     */
    public void select(int id) {
        if (mSelectedId != id) {
            setSelectedStateForView(mSelectedId, false);
            mSelectedId = id;
            setSelectedStateForView(mSelectedId, true);
        }
    }

    private void setSelectedStateForView(int id, boolean selected) {
        View child = findViewById(id);
        if (child != null) {
            child.setSelected(selected);

            if (selected && mListener != null) {
                mListener.onSelected(id);
            }
        }
    }

    public void setOnSelectedChangedListener(OnSelectedChangedListener listener) {
        mListener = listener;
    }

    public interface OnSelectedChangedListener {
        /**
         * called when child selected state changed
         *
         * @param selectedId selected view's or -1 if all state is cleared
         */
        void onSelected(int selectedId);
    }
}
