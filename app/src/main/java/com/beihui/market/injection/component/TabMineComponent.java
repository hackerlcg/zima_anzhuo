package com.beihui.market.injection.component;


import com.beihui.market.injection.module.TabMineModule;
import com.beihui.market.ui.fragment.TabMineFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = TabMineModule.class)
public interface TabMineComponent {

    void inject(TabMineFragment fragment);
}
