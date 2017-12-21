package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.RegisterSetPwdContract;

import dagger.Module;
import dagger.Provides;

@Module
public class RegisterSetPwdModule {
    private RegisterSetPwdContract.View mView;

    public RegisterSetPwdModule(RegisterSetPwdContract.View view) {
        mView = view;
    }

    @Provides
    public RegisterSetPwdContract.View provideRegisterSetPwdContractView() {
        return mView;
    }
}
