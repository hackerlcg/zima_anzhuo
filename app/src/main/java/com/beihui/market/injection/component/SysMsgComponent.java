package com.beihui.market.injection.component;


import com.beihui.market.injection.module.SysMsgModule;
import com.beihui.market.ui.activity.SysMsgActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SysMsgModule.class)
public interface SysMsgComponent {

    void inject(SysMsgActivity activity);
}
