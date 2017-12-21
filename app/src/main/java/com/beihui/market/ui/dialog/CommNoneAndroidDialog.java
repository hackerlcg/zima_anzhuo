package com.beihui.market.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beihui.market.R;


public class CommNoneAndroidDialog extends DialogFragment {

    FrameLayout titleContainer;
    TextView titleTv;
    FrameLayout messageContainer;
    TextView messageTv;
    FrameLayout btnContainer;
    TextView positiveTv;
    TextView negativeTv;

    private String title;
    private String message;
    private String positiveStr;
    private View.OnClickListener positiveClick;
    private String negativeStr;
    private View.OnClickListener negativeClick;

    private boolean dimBackground;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_comm_none_android, container, false);
        titleContainer = (FrameLayout) view.findViewById(R.id.title_container);
        titleTv = (TextView) view.findViewById(R.id.title);
        messageContainer = (FrameLayout) view.findViewById(R.id.message_container);
        messageTv = (TextView) view.findViewById(R.id.message);
        btnContainer = (FrameLayout) view.findViewById(R.id.btn_container);
        positiveTv = (TextView) view.findViewById(R.id.positive_btn);
        negativeTv = (TextView) view.findViewById(R.id.negative_btn);

        configureViews();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int) (getResources().getDisplayMetrics().density * 270);
            window.setAttributes(lp);
            if (dimBackground) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setDimAmount(0.6f);
            }
        }
    }

    private void configureViews() {
        if (title != null) {
            titleContainer.setVisibility(View.VISIBLE);
            titleTv.setText(title);
        }
        if (message != null) {
            messageContainer.setVisibility(View.VISIBLE);
            messageTv.setText(message);
        }
        if (positiveStr != null) {
            btnContainer.setVisibility(View.VISIBLE);
            positiveTv.setVisibility(View.VISIBLE);
            positiveTv.setText(positiveStr);
            positiveTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (positiveClick != null) {
                        positiveClick.onClick(v);
                    }
                    dismiss();
                }
            });
        }

        if (this.negativeStr != null) {
            if (btnContainer.getVisibility() == View.GONE) {
                btnContainer.setVisibility(View.VISIBLE);
            }
            negativeTv.setVisibility(View.VISIBLE);
            negativeTv.setText(negativeStr);
            negativeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (negativeClick != null) {
                        negativeClick.onClick(v);
                    }
                    dismiss();
                }
            });
        }
    }

    public CommNoneAndroidDialog withTitle(String title) {
        this.title = title;
        return this;
    }

    public CommNoneAndroidDialog withMessage(String message) {
        this.message = message;
        return this;
    }

    public CommNoneAndroidDialog withPositiveBtn(String positiveStr, View.OnClickListener clickListener) {
        this.positiveStr = positiveStr;
        this.positiveClick = clickListener;

        return this;
    }

    public CommNoneAndroidDialog withNegativeBtn(String negativeStr, View.OnClickListener clickListener) {
        this.negativeStr = negativeStr;
        this.negativeClick = clickListener;
        return this;
    }

    public CommNoneAndroidDialog dimBackground(boolean dim) {
        dimBackground = dim;
        return this;
    }
}
