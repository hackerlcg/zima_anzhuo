package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.NoticeDetail;

public interface NoticeDetailContract {

    interface Presenter extends BasePresenter {
        void queryDetail(String id);
    }

    interface View extends BaseView<Presenter> {
        void showDetail(NoticeDetail detail);
    }
}
