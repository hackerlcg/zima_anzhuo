package com.beihui.market.umeng;


import android.annotation.SuppressLint;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class Statistic {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void registerContext(Context context) {
        if (context != null) {
            sContext = context.getApplicationContext();
        }
    }


    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void onPageStart(String screen) {
        MobclickAgent.onPageStart(screen);
    }

    public static void onPageEnd(String screen) {
        MobclickAgent.onPageEnd(screen);
    }

    public static void login(String userId) {
        MobclickAgent.onProfileSignIn(userId);
    }

    public static void logout() {
        MobclickAgent.onProfileSignOff();
    }


    public static void onEvent(String eventId) {
        MobclickAgent.onEvent(sContext, eventId);
    }

    public static void onEvent(String eventId, String associatedId) {
        MobclickAgent.onEvent(sContext, eventId, associatedId);
    }
}
