package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

public interface ResetPwdVerifyContract {

    interface Presenter extends BasePresenter {
        void requestVerification(String phone);

        void nextMove(String phone, String verificationCode);
    }

    interface View extends BaseView<Presenter> {
        void showVerificationSend(String msg);

        void moveToNextStep(String requestPhone);

    }
}
