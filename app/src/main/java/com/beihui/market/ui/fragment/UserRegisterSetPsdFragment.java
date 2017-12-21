package com.beihui.market.ui.fragment;


import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerRegisterSetPwdComponent;
import com.beihui.market.injection.module.RegisterSetPwdModule;
import com.beihui.market.ui.activity.ComWebViewActivity;
import com.beihui.market.ui.busevents.UserLoginEvent;
import com.beihui.market.ui.contract.RegisterSetPwdContract;
import com.beihui.market.ui.presenter.RegisterSetPwdPresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class UserRegisterSetPsdFragment extends BaseComponentFragment implements RegisterSetPwdContract.View {
    @BindView(R.id.password)
    EditText passwordEt;
    @BindView(R.id.invitation_code)
    EditText invitationCodeEt;
    @BindView(R.id.register)
    TextView registerBtn;
    @BindView(R.id.psd_visibility)
    CheckBox psdVisibilityCb;
    @BindView(R.id.contract)
    TextView contractTv;

    @Inject
    RegisterSetPwdPresenter presenter;

    private String requestPhone;

    @Override
    public void onDestroyView() {
        InputMethodUtil.closeSoftKeyboard(getActivity());
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_register_set_psd;
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
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registerBtn.setEnabled(LegalInputUtils.validatePassword(passwordEt.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contractTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ComWebViewActivity.class);
                intent.putExtra("title", "用户协议");
                intent.putExtra("url", NetConstants.H5_USER_AGREEMENT);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initDatas() {
        requestPhone = getArguments().getString("requestPhone");
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerRegisterSetPwdComponent.builder()
                .appComponent(appComponent)
                .registerSetPwdModule(new RegisterSetPwdModule(this))
                .build()
                .inject(this);
    }

    @OnClick(R.id.register)
    void onViewClicked() {
        presenter.register(requestPhone, passwordEt.getText().toString(), invitationCodeEt.getText().toString());
    }

    @Override
    public void setPresenter(RegisterSetPwdContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissProgress();
        ToastUtils.showShort(getContext(), msg, null);
    }

    @Override
    public void showRegisterSuccess() {
        dismissProgress();
        ToastUtils.showShort(getContext(), "注册成功", R.mipmap.white_success);
        //登录后发送全局事件，更新UI
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
