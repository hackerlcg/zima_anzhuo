package com.beihui.market.injection.component;


import com.beihui.market.helper.DataStatisticsHelper;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface DataStatisticHelperComponent {

    void inject(DataStatisticsHelper helper);
}
