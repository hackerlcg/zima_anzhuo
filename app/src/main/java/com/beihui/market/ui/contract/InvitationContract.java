package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.Invitation;

import java.util.List;

public interface InvitationContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {
        void showInvitationCode(String code);

        void showInvitations(List<Invitation.Row> list);
    }
}
