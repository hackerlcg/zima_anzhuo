package com.beihui.market.ui.rvdecoration;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class NewsItemDeco extends RecyclerView.ItemDecoration {

    private int mDividerHeight;
    private int mPaddingLeft;
    private int mPaddingRight;

    private Paint mPaint = new Paint();
    private Rect mDrawRect = new Rect();

    public NewsItemDeco(int dividerHeight, int paddingLeft, int paddingRight) {
        mDividerHeight = dividerHeight;
        mPaddingLeft = paddingLeft;
        mPaddingRight = paddingRight;

        mPaint.setColor(Color.parseColor("#f0f0f0"));

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); ++i) {
            View child = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(child) != 0) {
                int childTop = child.getTop();
                int childWidth = child.getMeasuredWidth();
                mDrawRect.set(mPaddingLeft, childTop - mDividerHeight, childWidth - mPaddingRight, childTop);
                c.drawRect(mDrawRect, mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.set(0, mDividerHeight, 0, 0);
        }
    }

}
