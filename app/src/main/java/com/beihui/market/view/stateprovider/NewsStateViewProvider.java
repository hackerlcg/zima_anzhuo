package com.beihui.market.view.stateprovider;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.view.StateLayout;

public class NewsStateViewProvider implements StateLayout.StateViewProvider {

    private Context context;
    private View.OnClickListener onClickListener;

    public NewsStateViewProvider(Context context, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public View getViewByState(int newState, StateLayout container) {
        if (newState == StateLayout.STATE_EMPTY) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_state_empty, container, false);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(R.drawable.news_state_empty);
            ((TextView) view.findViewById(R.id.text)).setText("暂无消息");
            return view;
        } else if (newState == StateLayout.STATE_NET_ERROR) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.layout_state_net_error, container, false);
            view.findViewById(R.id.reload).setOnClickListener(onClickListener);
            return view;
        }
        return null;
    }
}
