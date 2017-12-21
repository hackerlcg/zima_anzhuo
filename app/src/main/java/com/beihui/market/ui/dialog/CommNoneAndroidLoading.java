package com.beihui.market.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;

public class CommNoneAndroidLoading extends Dialog {

    private String message;

    public CommNoneAndroidLoading(Context context, String message) {
        super(context, R.style.CommNoneAndroidLoadingStyle);
        this.message = message;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comm_none_android_loading);
        TextView messageTv = (TextView) findViewById(R.id.message);
        if (this.message != null) {
            messageTv.setText(message);
        } else {
            messageTv.setVisibility(View.GONE);
        }
        setCanceledOnTouchOutside(false);
    }
}
