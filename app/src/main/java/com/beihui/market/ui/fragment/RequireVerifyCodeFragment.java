package com.beihui.market.ui.fragment;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerVerifyCodeComponent;
import com.beihui.market.injection.module.VerifyCodeModule;
import com.beihui.market.ui.busevents.ResetPsdNavigationEvent;
import com.beihui.market.ui.contract.ResetPwdVerifyContract;
import com.beihui.market.ui.presenter.ResetPwdVerifyPresenter;
import com.beihui.market.util.CountDownTimerUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class RequireVerifyCodeFragment extends BaseComponentFragment implements ResetPwdVerifyContract.View {
    @BindView(R.id.phone_number)
    EditText phoneNumberEt;
    @BindView(R.id.verify_code)
    EditText verifyCodeEt;
    @BindView(R.id.fetch_text)
    TextView fetchText;
    @BindView(R.id.next_step)
    TextView nextStepBtn;

    @Inject
    ResetPwdVerifyPresenter presenter;

    private CountDownTimerUtils countDownTimer;

    @Override
    public void onDestroyView() {
        InputMethodUtil.closeSoftKeyboard(getActivity());
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = null;
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_require_verify_code;
    }

    @Override
    public void configViews() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = phoneNumberEt.getText().toString();
                boolean validatePhone = LegalInputUtils.validatePhone(phone);
                if (validatePhone && !(countDownTimer != null && countDownTimer.isRunning())) {
                    //手机号符合规则，且没有正在请求验证码倒数,则设置enable
                    fetchText.setEnabled(true);
                } else {
                    fetchText.setEnabled(false);
                }
                boolean validateCode = verifyCodeEt.getText().length() == 4;

                nextStepBtn.setEnabled(validatePhone && validateCode);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        phoneNumberEt.addTextChangedListener(textWatcher);
        verifyCodeEt.addTextChangedListener(textWatcher);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerVerifyCodeComponent.builder()
                .appComponent(appComponent)
                .verifyCodeModule(new VerifyCodeModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.fetch_text, R.id.next_step})
    void onNextStepClicked(View view) {
        if (view.getId() == R.id.fetch_text) {
            presenter.requestVerification(phoneNumberEt.getText().toString());
        } else {
            presenter.nextMove(phoneNumberEt.getText().toString(), verifyCodeEt.getText().toString());
        }
    }

    @Override
    public void setPresenter(ResetPwdVerifyContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showVerificationSend(String msg) {
        ToastUtils.showShort(getContext(), msg, null);
        fetchText.setEnabled(false);
        countDownTimer = new CountDownTimerUtils(fetchText, phoneNumberEt);
        countDownTimer.start();
    }

    @Override
    public void moveToNextStep(String requestPhone) {
        EventBus.getDefault().post(new ResetPsdNavigationEvent(requestPhone));
    }
}
