package com.beihui.market.ui.rvdecoration;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.R;

public class MessageCenterItemDeco extends RecyclerView.ItemDecoration {

    private int dividerHeight;
    private int padding;

    private Paint paint = new Paint();
    private Rect drawRect = new Rect();

    private int dividerColor;

    public MessageCenterItemDeco(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        dividerHeight = (int) (density * 0.5);
        padding = (int) (density * 7);
        dividerColor = context.getResources().getColor(R.color.common_bg);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        for (int i = 0; i < parent.getChildCount(); ++i) {
            View child = parent.getChildAt(i);
            if (parent.getChildAdapterPosition(child) >= 2) {
                int childTop = child.getTop();
                int childWidth = child.getMeasuredWidth();

                paint.setColor(Color.WHITE);
                drawRect.set(0, childTop - dividerHeight, childWidth, childTop);
                c.drawRect(drawRect, paint);

                paint.setColor(dividerColor);
                drawRect.set(padding, childTop - dividerHeight, childWidth - padding, childTop);
                c.drawRect(drawRect, paint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) >= 2) {
            outRect.set(0, dividerHeight, 0, 0);
        }
    }
}
