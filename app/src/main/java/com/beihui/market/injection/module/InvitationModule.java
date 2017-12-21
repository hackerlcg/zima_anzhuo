package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.InvitationContract;

import dagger.Module;
import dagger.Provides;

@Module
public class InvitationModule {

    private InvitationContract.View mView;

    public InvitationModule(InvitationContract.View view) {
        mView = view;
    }

    @Provides
    public InvitationContract.View provideInvitationContractView() {
        return mView;
    }
}
