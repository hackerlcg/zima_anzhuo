package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.helper.UserHelper;

public interface TabMineContract {

    interface Presenter extends BasePresenter {

        void checkMessage();

        void checkUserProfile();

        void checkInvitation();

        void checkHelpAndFeedback();

        void checkSetting();
    }

    interface View extends BaseView<Presenter> {

        void showProfile(UserHelper.Profile profile);

        void showHasMessage(boolean hasMessage);

        void navigateLogin();

        void navigateUserProfile(String userId);

        void navigateMessage(String userId);

        void navigateInvitation(String userId);

        void navigateHelpAndFeedback(String userId);

        void navigateSetting(String userId);

    }
}
