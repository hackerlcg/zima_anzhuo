package com.beihui.market.util;


import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.TextView;

public class CountDownTimerUtils extends CountDownTimer {
    private TextView targetView;
    private EditText phoneInput;
    private boolean isRunning;

    public CountDownTimerUtils(TextView targetView, EditText phoneInput) {
        super(60 * 1000, 1000);
        this.targetView = targetView;
        this.phoneInput = phoneInput;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        isRunning = true;
        targetView.setEnabled(false); //设置不可点击
        targetView.setText(millisUntilFinished / 1000 + "s");  //设置倒计时时间
    }

    @Override
    public void onFinish() {
        isRunning = false;
        targetView.setText("重新获取");
        if (LegalInputUtils.validatePhone(phoneInput.getText().toString())) {
            targetView.setEnabled(true);//重新获得点击
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}