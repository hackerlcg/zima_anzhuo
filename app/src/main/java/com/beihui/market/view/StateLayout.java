package com.beihui.market.view;


import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

public class StateLayout extends FrameLayout {

    public static final int STATE_CONTENT = 0;
    public static final int STATE_EMPTY = 1;
    public static final int STATE_NET_ERROR = 2;

    private SparseArray<View> stateViewArray = new SparseArray<>();

    private int curState = STATE_CONTENT;

    private StateViewProvider stateViewProvider;

    public StateLayout(@NonNull Context context) {
        super(context);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            if (getChildCount() > 1) {
                throw new IllegalStateException("StateLayout can only have one direct child");
            }
            View child = getChildAt(0);
            stateViewArray.put(STATE_CONTENT, child);
        }

    }

    public void switchState(int newState) {
        if (newState != curState && stateViewProvider != null) {
            curState = newState;
            View newView = stateViewArray.get(newState);
            if (newView == null) {
                newView = stateViewProvider.getViewByState(newState, this);
            }
            if (newView == null) {
                throw new NullPointerException("StateViewProvider return null");
            }
            stateViewArray.put(newState, newView);

            switchView(newView);
        }
    }

    public void setStateViewProvider(StateViewProvider provider) {
        if (provider != stateViewProvider) {
            stateViewProvider = provider;
            View content = stateViewArray.get(STATE_CONTENT);
            stateViewArray.clear();
            stateViewArray.put(STATE_CONTENT, content);
        }
    }

    private void switchView(View newView) {
        removeAllViews();
        addView(newView);
    }

    public interface StateViewProvider {

        View getViewByState(int newState, StateLayout container);
    }
}
