package com.beihui.market.ui.contract;


import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;
import com.beihui.market.entity.Profession;

import java.util.List;

public interface EditProfessionContract {

    interface Presenter extends BasePresenter {

        void updateProfession(Profession profession);
    }

    interface View extends BaseView<Presenter> {
        void showProfession(List<Profession> professions);

        void showUpdateSuccess(String msg);
    }
}
