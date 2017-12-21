package com.beihui.market.injection.component;


import com.beihui.market.ui.activity.HelperAndFeedbackActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface FeedbackComponent {

    void inject(HelperAndFeedbackActivity activity);
}
