package com.beihui.market.view.stateprovider;


import android.view.LayoutInflater;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.view.StateLayout;

public class HomeStateViewProvider implements StateLayout.StateViewProvider {

    private View.OnClickListener clickListener;

    public HomeStateViewProvider(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public View getViewByState(int newState, StateLayout container) {
        if (newState == StateLayout.STATE_NET_ERROR) {
            View view = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.layout_state_net_error, container, false);
            view.findViewById(R.id.reload).setOnClickListener(clickListener);
            return view;
        }
        return null;
    }
}
