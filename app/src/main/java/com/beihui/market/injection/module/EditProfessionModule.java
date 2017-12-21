package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.EditProfessionContract;

import dagger.Module;
import dagger.Provides;

@Module
public class EditProfessionModule {

    private EditProfessionContract.View mView;

    public EditProfessionModule(EditProfessionContract.View view) {
        mView = view;
    }

    @Provides
    public EditProfessionContract.View provideEditProfessionContractView() {
        return mView;
    }
}
