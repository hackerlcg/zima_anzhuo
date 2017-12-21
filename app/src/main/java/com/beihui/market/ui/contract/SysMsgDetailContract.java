package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.SysMsgDetail;

public interface SysMsgDetailContract {

    interface Presenter extends BasePresenter {
        void queryMsgDetail(String id);
    }

    interface View extends BaseView<Presenter> {
        void showSysMsgDetail(SysMsgDetail detail);
    }
}
