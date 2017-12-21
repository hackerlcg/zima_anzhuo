package com.beihui.market.ui.fragment;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerSetPwdComponent;
import com.beihui.market.injection.module.SetPwdModule;
import com.beihui.market.ui.contract.ResetPwdSetPwdContract;
import com.beihui.market.ui.presenter.ResetPwdSetPwdPresenter;
import com.beihui.market.util.InputMethodUtil;
import com.beihui.market.util.LegalInputUtils;
import com.beihui.market.util.viewutils.ToastUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SetPsdFragment extends BaseComponentFragment implements ResetPwdSetPwdContract.View {
    @BindView(R.id.password)
    EditText passwordEt;
    @BindView(R.id.confirm)
    TextView confirmBtn;

    @Inject
    ResetPwdSetPwdPresenter presenter;

    private String requestPhone;

    @Override
    public void onDestroyView() {
        InputMethodUtil.closeSoftKeyboard(getActivity());
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_set_psd;
    }

    @Override
    public void configViews() {
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmBtn.setEnabled(LegalInputUtils.validatePassword(passwordEt.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initDatas() {
        requestPhone = getArguments().getString("requestPhone");
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerSetPwdComponent.builder()
                .appComponent(appComponent)
                .setPwdModule(new SetPwdModule(this))
                .build()
                .inject(this);

    }

    @OnClick(R.id.confirm)
    void onViewClicked() {
        presenter.resetPwd(requestPhone, passwordEt.getText().toString());
    }

    @Override
    public void setPresenter(ResetPwdSetPwdContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissProgress();
        ToastUtils.showShort(getContext(), msg, null);
    }

    @Override
    public void showRestPwdSuccess(String msg) {
        dismissProgress();
        ToastUtils.showShort(getContext(), msg, R.mipmap.white_success);
        getActivity().finish();
    }
}
