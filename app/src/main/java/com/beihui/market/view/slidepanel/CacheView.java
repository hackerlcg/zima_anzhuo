package com.beihui.market.view.slidepanel;


import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CacheView extends View {

    private View cacheView;

    public CacheView(Context context) {
        super(context);
    }

    public CacheView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CacheView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void drawCache(View cacheView) {
        this.cacheView = cacheView;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cacheView != null) {
            cacheView.draw(canvas);
        }
    }
}
