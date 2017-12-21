package com.beihui.market.view.slidepanel;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SlidePanel extends FrameLayout {

    private ViewDragHelper dragHelper;
    private View cacheContentView;
    private View contentView;

    private CacheView cacheView;
    private ShadowView shadowView;

    private int slideOutVel;
    private int slideOutRange;
    private int screenWidth;

    private SlideCallback callback;

    public SlidePanel(@NonNull Context context) {
        super(context);
    }

    public SlidePanel(Context context, View contentView, View cacheContentView, SlideCallback callback) {
        super(context);
        this.contentView = contentView;
        this.cacheContentView = cacheContentView;
        this.callback = callback;

        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        slideOutVel = 2000;
        slideOutRange = (int) (screenWidth * .5);

        dragHelper = ViewDragHelper.create(this, 1.0f, new DragCallback());
        dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

        cacheView = new CacheView(context);
        cacheView.setVisibility(View.GONE);
        addView(cacheView);
        shadowView = new ShadowView(context);
        shadowView.setVisibility(View.GONE);
        addView(shadowView, screenWidth / 28, LayoutParams.MATCH_PARENT);

        addView(contentView);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isEnabled() && dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean enable = isEnabled();
        if (enable) {
            dragHelper.processTouchEvent(event);
        }
        return enable;
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    private class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            boolean capture = dragHelper.isEdgeTouched(ViewDragHelper.EDGE_LEFT) && child == contentView;
            if (capture) {
                cacheView.setVisibility(VISIBLE);
                cacheView.drawCache(cacheContentView);

                shadowView.setVisibility((VISIBLE));
            }
            return capture;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return screenWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return Math.max(Math.min(screenWidth, left), 0);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_IDLE:
                    if (contentView.getLeft() == 0) {
                        cacheView.setVisibility(GONE);
                        shadowView.setVisibility(GONE);

                        if (callback != null) {
                            callback.onSlideReset();
                        }
                    } else if (contentView.getLeft() == screenWidth) {
                        if (callback != null) {
                            callback.onSlideEnd();
                        }
                    }
                    break;
                case ViewDragHelper.STATE_DRAGGING:
                    if (callback != null) {
                        callback.onSlideStart();
                    }
                    break;
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            float percent = left * 1.0f / screenWidth;
            cacheView.setX(-screenWidth / 2 + percent * (screenWidth / 2));
            shadowView.setX(contentView.getX() - shadowView.getWidth());
            shadowView.setAlpha(1 - percent);

            if (callback != null) {
                callback.onSlide(left, dx);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel >= slideOutVel) {
                dragHelper.settleCapturedViewAt(screenWidth, 0);
                invalidate();
                return;
            }

            if (contentView.getLeft() < slideOutRange) {
                dragHelper.settleCapturedViewAt(0, 0);
            } else {
                dragHelper.settleCapturedViewAt(screenWidth, 0);
            }
            invalidate();
        }

    }

    public interface SlideCallback {
        void onSlideStart();

        void onSlide(int left, int dx);

        void onSlideEnd();

        void onSlideReset();
    }
}
