package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.UserProfileContract;

import dagger.Module;
import dagger.Provides;

@Module
public class UserProfileModule {

    private UserProfileContract.View mView;

    public UserProfileModule(UserProfileContract.View view) {
        mView = view;
    }

    @Provides
    public UserProfileContract.View provideUsreProfileContractView() {
        return mView;
    }
}
