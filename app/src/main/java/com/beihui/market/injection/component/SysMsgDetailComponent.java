package com.beihui.market.injection.component;


import com.beihui.market.injection.module.SysMsgDetailModule;
import com.beihui.market.ui.activity.SysMsgDetailActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SysMsgDetailModule.class)
public interface SysMsgDetailComponent {

    void inject(SysMsgDetailActivity activity);
}
