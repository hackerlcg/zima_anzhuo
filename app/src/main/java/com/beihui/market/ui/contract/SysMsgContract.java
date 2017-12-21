package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.SysMsg;

import java.util.List;

public interface SysMsgContract {

    interface Presenter extends BasePresenter {
        void loadMore();
    }

    interface View extends BaseView<Presenter> {
        void showSysMsg(List<SysMsg.Row> sysMsg);

        void showNoSysMsg();

        void showNoMoreSysMsg();
    }
}
