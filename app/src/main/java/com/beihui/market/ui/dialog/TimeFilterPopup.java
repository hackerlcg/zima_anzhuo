package com.beihui.market.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.view.busineesrel.FlowTagLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeFilterPopup extends PopupWindow {

    @BindView(R.id.flow_layout)
    FlowTagLayout flowTagLayout;

    private View shadowView;
    private TextView tv;
    private ImageView iv;

    private onBrTimeListener listener;

    public TimeFilterPopup(final Activity context, final int selectTimeIndex, View shadowView, TextView tv, ImageView iv,
                           String[] tags) {
        super(context);
        this.shadowView = shadowView;

        shadowView.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_brtime, null);
        ButterKnife.bind(this, view);

        this.setContentView(view);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.tv = tv;
        this.iv = iv;
        tv.setTextColor(Color.parseColor("#5591FF"));
        iv.setImageResource(R.mipmap.daosanjiao_blue);
        rotateArrow(0, 180, iv);

        flowTagLayout.setTags(tags, selectTimeIndex);
        flowTagLayout.setTagSelectedListener(new FlowTagLayout.TagSelectedListener() {
            @Override
            public void onTagSelected(int selected) {
                if (listener != null) {
                    listener.onTimeItemClick(selected);
                }
                dismiss();
            }
        });

    }


    @Override
    public void dismiss() {
        super.dismiss();
        shadowView.setVisibility(View.GONE);
        tv.setTextColor(Color.parseColor("#424251"));
        iv.setImageResource(R.mipmap.daosanjiao);
        rotateArrow(180, 0, iv);
    }

    public void setShareItemListener(onBrTimeListener listener) {
        this.listener = listener;
    }

    private void rotateArrow(float fromDegrees, float toDegrees, ImageView imageView) {
        RotateAnimation mRotateAnimation =
                new RotateAnimation(fromDegrees, toDegrees,
                        (int) (imageView.getMeasuredWidth() / 2.0),
                        (int) (imageView.getMeasuredHeight() / 2.0));
        mRotateAnimation.setDuration(150);
        mRotateAnimation.setFillAfter(true);
        imageView.startAnimation(mRotateAnimation);
    }


    public interface onBrTimeListener {
        void onTimeItemClick(int selectTimeIndex);
    }
}
