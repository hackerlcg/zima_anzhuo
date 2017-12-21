package com.beihui.market.umeng;


import android.content.Context;

import com.beihui.market.BuildConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

public class Umeng {

    public static void install(Context context) {
        //分享
        PlatformConfig.setWeixin("wx85ba05e3d5eca8a5", "ed4bfef7288e94df20e2b3a4ef92d792");
        PlatformConfig.setQQZone("1106217443", "UiOL1Ct0h3tGOirD");
        PlatformConfig.setSinaWeibo("2037274409", "ad8ac41cb179ffcb92f28b312a055074", "http://sns.whalecloud.com");

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(false);
        config.isOpenShareEditActivity(false);
        config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        UMShareAPI.get(context).setShareConfig(config);

        //统计使用
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //统计错误
        MobclickAgent.setCatchUncaughtExceptions(true);
        //统计自定义事件
        Statistic.registerContext(context);

        MobclickAgent.setDebugMode(BuildConfig.DEBUG);

    }
}
