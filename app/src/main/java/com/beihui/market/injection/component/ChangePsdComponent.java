package com.beihui.market.injection.component;


import com.beihui.market.injection.module.ChangePsdModule;
import com.beihui.market.ui.activity.ChangePsdActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = ChangePsdModule.class)
public interface ChangePsdComponent {

    void inject(ChangePsdActivity activity);
}
