package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.LoginContract;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    private LoginContract.View mView;

    public LoginModule(LoginContract.View view) {
        mView = view;
    }

    @Provides
    public LoginContract.View provideLoginContractView() {
        return mView;
    }
}
