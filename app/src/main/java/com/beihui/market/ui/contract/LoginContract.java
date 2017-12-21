package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface LoginContract {

    interface Presenter extends BasePresenter {
        void login(String account, String pwd);
    }

    interface View extends BaseView<Presenter> {
        void showLoading();

        void showLoginSuccess();
    }
}
