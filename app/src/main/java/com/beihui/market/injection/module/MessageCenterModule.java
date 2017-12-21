package com.beihui.market.injection.module;


import com.beihui.market.ui.contract.MessageCenterContract;

import dagger.Module;
import dagger.Provides;

@Module
public class MessageCenterModule {

    private MessageCenterContract.View mView;

    public MessageCenterModule(MessageCenterContract.View view) {
        mView = view;
    }

    @Provides
    public MessageCenterContract.View provideMessageCenterContractView() {
        return mView;
    }
}
