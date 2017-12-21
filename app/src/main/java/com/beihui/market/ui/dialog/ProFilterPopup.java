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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beihui.market.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProFilterPopup extends PopupWindow {

    @BindView(R.id.ly_1)
    LinearLayout ly1;
    @BindView(R.id.ly_2)
    LinearLayout ly2;
    @BindView(R.id.ly_3)
    LinearLayout ly3;
    @BindView(R.id.ly_4)
    LinearLayout ly4;

    private View shadowView;
    private TextView tv;
    private ImageView iv;

    private onBrZhiyeListener listener;

    public ProFilterPopup(final Activity context, View shadowView, TextView tv, ImageView iv) {
        super(context);
        this.shadowView = shadowView;


        shadowView.setVisibility(View.VISIBLE);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_brzhiye, null);
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
    }


    @Override
    public void dismiss() {
        super.dismiss();
        shadowView.setVisibility(View.GONE);
        tv.setTextColor(Color.parseColor("#424251"));
        iv.setImageResource(R.mipmap.daosanjiao);
        rotateArrow(180, 0, iv);
    }


    @OnClick({R.id.ly_1, R.id.ly_2, R.id.ly_3, R.id.ly_4})
    public void onViewClicked(View view) {
        int selectedIndex;
        switch (view.getId()) {
            case R.id.ly_1:
                selectedIndex = 0;
                break;
            case R.id.ly_2:
                selectedIndex = 1;
                break;
            case R.id.ly_3:
                selectedIndex = 2;
                break;
            case R.id.ly_4:
                selectedIndex = 3;
                break;
            default:
                selectedIndex = -1;
        }

        if (listener != null)
            listener.onZhiyeItemClick(selectedIndex);
        dismiss();
    }

    public void setShareItemListener(onBrZhiyeListener listener) {
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


    public interface onBrZhiyeListener {
        void onZhiyeItemClick(int selectIndex);
    }
}
