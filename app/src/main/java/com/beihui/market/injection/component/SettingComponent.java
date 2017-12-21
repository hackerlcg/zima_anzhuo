package com.beihui.market.injection.component;


import com.beihui.market.injection.module.SettingModule;
import com.beihui.market.ui.activity.SettingsActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = SettingModule.class)
public interface SettingComponent {

    void inject(SettingsActivity activity);
}
