package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface ChangePsdContract {

    interface Presenter extends BasePresenter {
        void updatePsd(String origin, String newPsd, String confirm);
    }

    interface View extends BaseView<Presenter> {
        void showUpdateSuccess(String msg, String account);
    }
}
