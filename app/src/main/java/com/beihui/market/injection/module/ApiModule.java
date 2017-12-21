package com.beihui.market.injection.module;


import com.beihui.market.api.Api;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Provides
    protected Api provideApi() {
        return Api.getInstance();
    }

}
