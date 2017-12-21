package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface EditUserNameContract {

    interface Presenter extends BasePresenter {

        void updateUserName(String username);
    }


    interface View extends BaseView<Presenter> {

        void showUserName(String name);

        void showUpdateNameSuccess(String msg);
    }

}
