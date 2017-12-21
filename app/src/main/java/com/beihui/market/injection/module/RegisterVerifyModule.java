package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.RegisterVerifyContract;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterVerifyModule {

    private RegisterVerifyContract.View mView;

    public RegisterVerifyModule(RegisterVerifyContract.View view) {
        mView = view;
    }

    @Provides
    public RegisterVerifyContract.View provideRegisterVerifyContractView() {
        return mView;
    }
}
