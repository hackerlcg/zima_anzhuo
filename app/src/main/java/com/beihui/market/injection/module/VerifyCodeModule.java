package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.ResetPwdVerifyContract;

import dagger.Module;
import dagger.Provides;

@Module
public class VerifyCodeModule {

    private ResetPwdVerifyContract.View mView;

    public VerifyCodeModule(ResetPwdVerifyContract.View view) {
        mView = view;
    }

    @Provides
    public ResetPwdVerifyContract.View provideResetPwdVerifyContractView() {
        return mView;
    }
}
