package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface ResetPwdSetPwdContract {

    interface Presenter extends BasePresenter {
        void resetPwd(String phone, String pwd);
    }

    interface View extends BaseView<Presenter> {
        void showRestPwdSuccess(String msg);
    }
}
