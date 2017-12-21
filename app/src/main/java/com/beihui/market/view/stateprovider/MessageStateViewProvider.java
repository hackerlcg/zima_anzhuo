package com.beihui.market.view.stateprovider;

import android.view.LayoutInflater;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.view.StateLayout;


public class MessageStateViewProvider implements StateLayout.StateViewProvider {
    @Override
    public View getViewByState(int newState, StateLayout container) {
        if (newState == StateLayout.STATE_EMPTY) {
            return LayoutInflater.from(container.getContext())
                    .inflate(R.layout.layout_notice_message_empty, container, false);
        }
        return null;
    }
}
