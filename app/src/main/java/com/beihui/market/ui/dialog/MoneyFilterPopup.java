package com.beihui.market.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.Constant;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.viewutils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoneyFilterPopup extends PopupWindow {

    @BindView(R.id.money)
    EditText etMoney;

    private Activity context;

    private View shadowView;
    private TextView tv;
    private ImageView iv;

    private onBrMoneyListener listener;

    public MoneyFilterPopup(final Activity context, String money, View shadowView, TextView tv, ImageView iv) {
        super(context);
        this.context = context;
        this.shadowView = shadowView;
        this.tv = tv;
        this.iv = iv;

        shadowView.setVisibility(View.VISIBLE);
        shadowView.invalidate();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_brmoney, null);
        ButterKnife.bind(this, view);

        setContentView(view);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tv.setTextColor(Color.parseColor("#5591FF"));
        iv.setImageResource(R.mipmap.daosanjiao_blue);
        rotateArrow(0, 180, iv);

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dstart == 0 && !TextUtils.isEmpty(source)) {
                    if (source.equals("0")) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter[] filters = etMoney.getFilters();
        if (filters == null) {
            filters = new InputFilter[]{filter};
        } else {
            InputFilter[] temp = filters;
            filters = new InputFilter[temp.length + 1];
            for (int i = 0; i < temp.length; ++i) {
                filters[i] = temp[i];
            }
            filters[temp.length] = filter;
        }
        etMoney.setFilters(filters);
        etMoney.setText(money);
        etMoney.setSelection(etMoney.getText().length());
        etMoney.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodUtil.openSoftKeyboard(context, etMoney);
            }
        }, 200);

    }


    @Override
    public void dismiss() {
        InputMethodUtil.closeSoftKeyboard(context, etMoney);
        super.dismiss();
        shadowView.setVisibility(View.GONE);
        tv.setTextColor(Color.parseColor("#424251"));
        iv.setImageResource(R.mipmap.daosanjiao);
        rotateArrow(180, 0, iv);
    }


    @OnClick({R.id.cancel, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                String money = etMoney.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    ToastUtils.showShort(context, "请输入金额", null);
                    return;
                }
                try {
                    int amount = Integer.parseInt(money);
                    if (amount < Constant.MIN_FILTER_MONEY) {
                        ToastUtils.showShort(context, "最低借款" + Constant.MIN_FILTER_MONEY + "元", null);
                        return;
                    }
                    dismiss();
                    if (listener != null) {
                        listener.onMoneyItemClick(amount);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtils.showShort(context, "请输入正确金额", null);
                }
                break;
        }
    }

    public void setShareItemListener(onBrMoneyListener listener) {
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

    public interface onBrMoneyListener {
        void onMoneyItemClick(int money);
    }

}
