package com.beihui.market.ui.presenter;


import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Message;
import com.beihui.market.entity.NoticeAbstract;
import com.beihui.market.entity.SysMsgAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.MessageCenterContract;
import com.beihui.market.util.RxUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MessageCenterPresenter extends BaseRxPresenter implements MessageCenterContract.Presenter {
    private Api mApi;
    private MessageCenterContract.View mView;
    private UserHelper mUserHelper;
    private List<Message> messages = new ArrayList<>();

    private int curPage = 0;

    @Inject
    MessageCenterPresenter(Api api, MessageCenterContract.View view, Context context) {
        mApi = api;
        mView = view;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        Disposable dis = Observable.concat(mApi.queryNoticeHome(), mApi.querySysMsgHome(mUserHelper.getProfile().getId()))
                .compose(RxUtil.io2main())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        ResultEntity result = (ResultEntity) o;
                        if (result.isSuccess()) {
                            Object data = result.getData();
                            if (data instanceof NoticeAbstract) {
                                mView.showAnnounce((NoticeAbstract) data);
                            } else if (data instanceof SysMsgAbstract) {
                                mView.showSysMsg((SysMsgAbstract) data);
                            }
                        } else {
                            mView.showErrorMsg(result.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        logError(MessageCenterPresenter.this, throwable);
                        mView.showErrorMsg(generateErrorMsg(throwable));
                    }
                });
        addDisposable(dis);

        refreshMessage();
    }

    @Override
    public void refreshMessage() {
        curPage++;
        Disposable dis = mApi.queryMessage(curPage, 3)
                .compose(RxUtil.<ResultEntity<List<Message>>>io2main())
                .subscribe(new Consumer<ResultEntity<List<Message>>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<List<Message>> result) throws Exception {
                                   if (result.isSuccess()) {
                                       if (result.getData() != null && result.getData().size() > 0) {
                                           messages.clear();
                                           messages.addAll(result.getData());
                                           mView.showMessages(Collections.unmodifiableList(messages));
                                       } else {
                                           mView.showNoMessage();
                                       }
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(MessageCenterPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
