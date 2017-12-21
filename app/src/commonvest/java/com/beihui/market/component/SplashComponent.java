package com.beihui.market.component;


import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.SplashActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface SplashComponent {

    void inject(SplashActivity activity);
}
