package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface RegisterSetPwdContract {

    interface Presenter extends BasePresenter {
        /**
         * 注册
         *
         * @param phone      手机号
         * @param pwd        密码
         * @param inviteCode 邀请码，可空
         */
        void register(String phone, String pwd, String inviteCode);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 注册成功
         */
        void showRegisterSuccess();
    }
}
