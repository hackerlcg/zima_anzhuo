package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.EditUserNameContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class EditUserNamePresenter extends BaseRxPresenter implements EditUserNameContract.Presenter {

    private Api mApi;
    private EditUserNameContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    @Inject
    EditUserNamePresenter(Api api, EditUserNameContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }


    @Override
    public void onStart() {
        super.onStart();
        mView.showUserName(mUserHelper.getProfile().getUserName());
    }

    @Override
    public void updateUserName(String username) {
        final String name = username;
        mView.showLoading();
        Disposable dis = mApi.updateUsername(mUserHelper.getProfile().getId(), username)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(@NonNull ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       mUserHelper.updateUsername(name, mContext);
                                       mView.showUpdateNameSuccess(result.getMsg());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(EditUserNamePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
