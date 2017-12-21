package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.LoginContract;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginPresenter extends BaseRxPresenter implements LoginContract.Presenter {
    private Api mApi;
    private LoginContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    @Inject
    LoginPresenter(Api api, LoginContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void login(String account, String pwd) {
        final String phone = account;
        mView.showLoading();
        Disposable dis = mApi.login(account, pwd)
                .compose(RxUtil.<ResultEntity<UserProfileAbstract>>io2main())
                .subscribe(new Consumer<ResultEntity<UserProfileAbstract>>() {
                    @Override
                    public void accept(@NonNull ResultEntity<UserProfileAbstract> result) throws Exception {
                        if (result.isSuccess()) {
                            //umeng统计
                            Statistic.onEvent(Events.LOGIN_SUCCESS);

                            //登录之后，将用户信息注册到本地
                            mUserHelper.update(result.getData(), phone, mContext);
                            mView.showLoginSuccess();
                            //umeng统计
                            Statistic.login(result.getData().getId());
                        } else {
                            //umeng统计
                            Statistic.onEvent(Events.LOGIN_FAILED);

                            mView.showErrorMsg(result.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        logError(LoginPresenter.this, throwable);
                        mView.showErrorMsg(generateErrorMsg(throwable));
                    }
                });
        addDisposable(dis);
    }
}
