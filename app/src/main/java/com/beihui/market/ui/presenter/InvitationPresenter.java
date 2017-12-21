package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Invitation;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.InvitationContract;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class InvitationPresenter extends BaseRxPresenter implements InvitationContract.Presenter {

    private Api mApi;
    private InvitationContract.View mView;
    private UserHelper mUserHelper;

    @Inject
    InvitationPresenter(Api api, InvitationContract.View view, Context context) {
        mApi = api;
        mView = view;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        mView.showInvitationCode(mUserHelper.getProfile().getAccount());

        Disposable dis = mApi.queryInvitation(mUserHelper.getProfile().getId())
                .compose(RxUtil.<ResultEntity<Invitation>>io2main())
                .subscribe(new Consumer<ResultEntity<Invitation>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<Invitation> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().getTotal() > 0) {
                                           mView.showInvitations(result.getData().getRows());
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(InvitationPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }

}
