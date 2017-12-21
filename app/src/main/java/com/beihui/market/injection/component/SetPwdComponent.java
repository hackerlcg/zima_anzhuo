package com.beihui.market.injection.component;

import com.beihui.market.injection.module.SetPwdModule;
import com.beihui.market.ui.fragment.SetPsdFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SetPwdModule.class)
public interface SetPwdComponent {

    void inject(SetPsdFragment fragment);
}
