package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.EditUserNameContract;

import dagger.Module;
import dagger.Provides;

@Module
public class EditUserNameModule {

    private EditUserNameContract.View mView;

    public EditUserNameModule(EditUserNameContract.View view) {
        mView = view;
    }

    @Provides
    public EditUserNameContract.View provideEditUserNameContractView() {
        return mView;
    }
}
