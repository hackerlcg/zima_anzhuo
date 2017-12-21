package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.entity.Message;
import com.beihui.market.entity.SysMsgAbstract;

import java.util.List;

public interface MessageCenterContract {

    interface Presenter extends BasePresenter {
        void refreshMessage();
    }

    interface View extends BaseView<Presenter> {
        void showAnnounce(NoticeAbstract announce);

        void showSysMsg(SysMsgAbstract sysMsg);

        void showMessages(List<Message> messages);

        void showNoMessage();

    }
}
