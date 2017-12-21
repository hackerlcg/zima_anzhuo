package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.Notice;

import java.util.List;

public interface NoticeContract {

    interface Presenter extends BasePresenter {
        void loadMore();
    }

    interface View extends BaseView<Presenter> {
        void showAnnounce(List<Notice.Row> announceList);

        void showNoAnnounce();

        void showNoMoreAnnounce();
    }
}
