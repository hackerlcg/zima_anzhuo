package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.TabMineContract;

import javax.inject.Inject;

public class TabMinePresenter extends BaseRxPresenter implements TabMineContract.Presenter {

    private TabMineContract.View mView;

    private UserHelper mUserHelper;

    @Inject
    TabMinePresenter(TabMineContract.View view, Context context) {
        mView = view;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        UserHelper.Profile profile = mUserHelper.getProfile();
        if (profile != null) {
            mView.showProfile(profile);
        }
    }

    @Override
    public void checkMessage() {
        if (checkValidUser()) {
            mView.navigateMessage(mUserHelper.getProfile().getId());
        }
    }

    @Override
    public void checkUserProfile() {
        if (checkValidUser()) {
            mView.navigateUserProfile(mUserHelper.getProfile().getId());
        }
    }

    @Override
    public void checkInvitation() {
        if (checkValidUser()) {
            mView.navigateInvitation(mUserHelper.getProfile().getId());
        }
    }

    @Override
    public void checkHelpAndFeedback() {
        if (checkValidUser()) {
            mView.navigateHelpAndFeedback(mUserHelper.getProfile().getId());
        }
    }

    @Override
    public void checkSetting() {
        if (checkValidUser()) {
            mView.navigateSetting(null);
        }
    }

    private boolean checkValidUser() {
        if (mUserHelper.getProfile() == null) {
            mView.navigateLogin();
            return false;
        }
        return true;
    }
}
