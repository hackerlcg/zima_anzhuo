package com.beihui.market.ui.presenter;


import android.content.Context;
import android.graphics.Bitmap;

import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseRxPresenter;
import com.beihui.market.entity.Avatar;
import com.beihui.market.entity.UserProfile;
import com.beihui.market.entity.request.RequestConstants;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.contract.UserProfileContract;
import com.beihui.market.util.RxUtil;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UserProfilePresenter extends BaseRxPresenter implements UserProfileContract.Presenter {

    private Api mApi;
    private UserProfileContract.View mView;
    private Context mContext;
    private UserHelper mUserHelper;

    @Inject
    UserProfilePresenter(Api api, UserProfileContract.View view, Context context) {
        mApi = api;
        mView = view;
        mContext = context;
        mUserHelper = UserHelper.getInstance(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        UserHelper.Profile profile = mUserHelper.getProfile();
        if (profile != null) {
            mView.showProfile(profile);

            Disposable dis = mApi.queryUserProfile(profile.getId())
                    .compose(RxUtil.<ResultEntity<UserProfile>>io2main())
                    .subscribe(new Consumer<ResultEntity<UserProfile>>() {
                                   @Override
                                   public void accept(@NonNull ResultEntity<UserProfile> result) throws Exception {
                                       if (result.isSuccess()) {
                                           mUserHelper.update(result.getData(), mContext);
                                           mView.showProfile(mUserHelper.getProfile());
                                       } else {
                                           mView.showErrorMsg(result.getMsg());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    logError(UserProfilePresenter.this, throwable);
                                    mView.showErrorMsg(generateErrorMsg(throwable));
                                }
                            });
            addDisposable(dis);
        }

    }

    @Override
    public void updateAvatar(final Bitmap avatar) {
        mView.showLoading();
        Disposable dis = Observable.just(avatar)
                .observeOn(Schedulers.io())
                .map(new Function<Bitmap, byte[]>() {
                    @Override
                    public byte[] apply(@io.reactivex.annotations.NonNull Bitmap source) throws Exception {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int quality = 100;
                        source.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                        while (baos.size() > RequestConstants.AVATAR_BYTE_SIZE) {
                            quality -= 5;
                            if (quality <= 0) {
                                quality = 0;
                            }
                            baos.reset();
                            source.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                            if (quality == 0) {
                                break;
                            }
                        }
                        return baos.toByteArray();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<byte[], ObservableSource<ResultEntity<Avatar>>>() {
                    @Override
                    public ObservableSource<ResultEntity<Avatar>> apply(@NonNull byte[] bytes) throws Exception {
                        String fileName = System.currentTimeMillis() + ".jpg";
                        return mApi.updateUserAvatar(mUserHelper.getProfile().getId(), fileName, bytes);
                    }
                })
                .compose(RxUtil.<ResultEntity<Avatar>>io2main())
                .subscribe(new Consumer<ResultEntity<Avatar>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<Avatar> result) throws Exception {
                                   if (result.isSuccess()) {
                                       mView.showAvatarUpdateSuccess(result.getData().getFilePath());
                                       mUserHelper.updateAvatar(result.getData().getFilePath(), mContext);
                                   } else {
                                       mView.showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                logError(UserProfilePresenter.this, throwable);
                                mView.showErrorMsg(generateErrorMsg(throwable));
                            }
                        });
        addDisposable(dis);
    }
}
