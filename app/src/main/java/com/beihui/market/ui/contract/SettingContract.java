package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.AppUpdate;

public interface SettingContract {

    interface Presenter extends BasePresenter {
        void checkVersion();

        void logout();
    }

    interface View extends BaseView<Presenter> {
        void showLatestVersion(String version);

        void showLogoutSuccess();

        void showUpdate(AppUpdate appUpdate);

        void showHasBeenLatest(String msg);
    }
}
