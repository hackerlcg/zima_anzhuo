package com.beihui.market.ui.rvdecoration;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HotNewsItemDeco extends RecyclerView.ItemDecoration {

    private int margin = -1;
    private int padding = -1;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (margin == -1) {
            float density = parent.getContext().getResources().getDisplayMetrics().density;
            margin = (int) (density * 7);
            padding = (int) (density * 5);
        }
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = margin;
        } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.right = margin;
            outRect.left = padding;
        } else {
            outRect.left = padding;
        }
    }
}
