package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.ResetPwdSetPwdContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SetPwdModule {

    private ResetPwdSetPwdContract.View mView;

    public SetPwdModule(ResetPwdSetPwdContract.View view) {
        mView = view;
    }

    @Provides
    public ResetPwdSetPwdContract.View provideResetPwdSetPwdContractView() {
        return mView;
    }
}
