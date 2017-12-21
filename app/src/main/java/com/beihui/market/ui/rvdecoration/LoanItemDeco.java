package com.beihui.market.ui.rvdecoration;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class LoanItemDeco extends RecyclerView.ItemDecoration {

    private int padding = -1;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (padding == -1) {
            padding = (int) (parent.getContext().getResources().getDisplayMetrics().density * 1);
        }
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = padding;
        } else {
            outRect.top = padding * 3;
        }
    }
}
