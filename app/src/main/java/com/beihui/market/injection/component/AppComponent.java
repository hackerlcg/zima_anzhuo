package com.beihui.market.injection.component;

import android.content.Context;

import com.beihui.market.api.Api;
import com.beihui.market.injection.module.AppModule;
import com.beihui.market.injection.module.ApiModule;

import dagger.Component;

@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    Context getContext();

    Api getApi();

}
