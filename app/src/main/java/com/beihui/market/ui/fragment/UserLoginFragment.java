package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerLoginComponent;
import com.beihui.market.injection.module.LoginModule;
import com.beihui.market.ui.activity.ResetPsdActivity;
import com.beihui.market.ui.busevents.AuthNavigationEvent;
import com.beihui.market.ui.busevents.UserLoginEvent;
import com.beihui.market.ui.contract.LoginContract;
import com.beihui.market.ui.presenter.LoginPresenter;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class UserLoginFragment extends BaseComponentFragment implements LoginContract.View {
    @BindView(R.id.phone_number)
    EditText phoneNumberEt;
    @BindView(R.id.password)
    EditText passwordEt;
    @BindView(R.id.login)
    TextView loginBtn;
    @BindView(R.id.psd_visibility)
    CheckBox psdVisibilityCb;

    @Inject
    LoginPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //umeng统计
        Statistic.onEvent(Events.ENTER_LOGIN);
    }

    @Override
    public void onDestroyView() {
        InputMethodUtil.closeSoftKeyboard(getActivity());
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_user_login;
    }

    @Override
    public void configViews() {
        psdVisibilityCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                CommonUtils.setEditTextCursorLocation(passwordEt);
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = phoneNumberEt.getText().toString();
                String pwd = passwordEt.getText().toString();
                if (LegalInputUtils.isPhoneAndPwdLegal(phone, pwd)) {
                    if (!loginBtn.isEnabled()) {
                        loginBtn.setEnabled(true);
                    }
                } else {
                    if (loginBtn.isEnabled()) {
                        loginBtn.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        phoneNumberEt.addTextChangedListener(textWatcher);
        passwordEt.addTextChangedListener(textWatcher);


        String phone = null;
        if (getArguments() != null) {
            phone = getArguments().getString("phone");
        }
        if (phone != null) {
            phoneNumberEt.setText(phone);
        }
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerLoginComponent.builder().appComponent(appComponent)
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }


    @OnClick({R.id.register, R.id.forget_psd, R.id.login})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register:
                //umeng统计
                Statistic.onEvent(Events.LOGIN_REGISTER);

                EventBus.getDefault().post(new AuthNavigationEvent(AuthNavigationEvent.TAG_REGISTER));
                break;
            case R.id.forget_psd:
                Intent toResetPsd = new Intent(getActivity(), ResetPsdActivity.class);
                startActivity(toResetPsd);
                break;
            case R.id.login:
                //umeng统计
                Statistic.onEvent(Events.LOGIN_LOGIN);

                presenter.login(phoneNumberEt.getText().toString(), passwordEt.getText().toString());
                break;
        }
    }


    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        //injected, do nothing.
    }

    @Override
    public void showLoading() {
        showProgress("登录中");
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissProgress();
        ToastUtils.showShort(getContext(), msg, null);
    }

    @Override
    public void showLoginSuccess() {
        dismissProgress();
        ToastUtils.showShort(getContext(), "登录成功", R.mipmap.white_success);
        //登录后发送全局事件,更新UI
        EventBus.getDefault().post(new UserLoginEvent());
        if (getView() != null) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().finish();
                }
            }, 200);
        }
    }
}
