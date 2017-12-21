package com.beihui.market.base;


public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

    void showErrorMsg(String msg);

    void showLoading();
}
