package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.SettingContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingModule {

    private SettingContract.View mView;

    public SettingModule(SettingContract.View view) {
        mView = view;
    }

    @Provides
    public SettingContract.View provideSettingContractView() {
        return mView;
    }
}
