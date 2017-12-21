package com.beihui.market.ui.presenter;


import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.UserProfileAbstract;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.RegisterSetPwdContract;
import com.beihui.market.umeng.Events;
import com.beihui.market.umeng.Statistic;
import com.beihui.market.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RegisterSetPwdPresenter extends BaseRxPresenter implements RegisterSetPwdContract.Presenter {
    private Api mApi;
    private RegisterSetPwdContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    @Inject
    RegisterSetPwdPresenter(Api api, RegisterSetPwdContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void register(String phone, String pwd, String inviteCode) {
        String channelId = "unknown";
        try {
            channelId = mContext.getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("CHANNEL_ID");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final String account = phone;
        final String password = pwd;
        //do not pass empty str to retrofit query annotation, if empty then set null
        if (TextUtils.isEmpty(inviteCode)) {
            inviteCode = null;
        }
        mView.showLoading();
        Disposable dis = mApi.register(phone, pwd, channelId, inviteCode)
                .compose(RxUtil.<ResultEntity>io2main())
                .flatMap(new Function<ResultEntity, ObservableSource<ResultEntity<UserProfileAbstract>>>() {
                    @Override
                    public ObservableSource<ResultEntity<UserProfileAbstract>> apply(@NonNull ResultEntity resultEntity) throws Exception {
                        //注册成功后，执行登录
                        if (resultEntity.isSuccess()) {
                            //umeng统计
                            Statistic.onEvent(Events.REGISTER_SUCCESS);

                            return mApi.login(account, password).subscribeOn(Schedulers.io());
                        } else {
                            //umeng统计
                            Statistic.onEvent(Events.REGISTER_FAILED);

                            //注册失败则停止
                            mView.showErrorMsg(resultEntity.getMsg());
                            return Observable.empty();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultEntity<UserProfileAbstract>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<UserProfileAbstract> result) throws Exception {
                                   if (result.isSuccess()) {
                                       //登录成功后，将用户信息注册到本地
                                       mUserHelper.update(result.getData(), account, mContext);
                                       mView.showRegisterSuccess();
                                       //umeng统计
                                       Statistic.login(result.getData().getId());
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(RegisterSetPwdPresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
