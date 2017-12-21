package com.beihui.market.injection.component;


import com.beihui.market.injection.module.MessageCenterModule;
import com.beihui.market.ui.activity.MessageCenterActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = MessageCenterModule.class)
public interface MessageCenterComponent {

    void inject(MessageCenterActivity activity);
}
