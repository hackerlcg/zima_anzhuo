package com.beihui.market.getui;


import android.content.Context;
import android.content.Intent;

import com.beihui.market.BuildConfig;
import com.beihui.market.api.NetConstants;
import com.beihui.market.ui.activity.ComWebViewActivity;
import com.beihui.market.ui.activity.LoanDetailActivity;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.activity.NewsDetailActivity;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.NotificationUtil;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import org.json.JSONObject;

public class PushReceiveIntentService extends GTIntentService {
    private static final String TAG = PushReceiveIntentService.class.getSimpleName();

    @Override
    public void onReceiveServicePid(Context context, int i) {
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveServicePid " + i);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String s) {
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveClintId " + s);
        }
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveMessageData " + gtTransmitMessage);
        }
        try {
            String json = new String(gtTransmitMessage.getPayload());
            JSONObject obj = new JSONObject(json);

            String title = obj.getString("title") != null ? obj.getString("title") : "";
            String content = obj.getString("content") != null ? obj.getString("content") : "";

            Intent intent = null;
            int type = obj.getInt("type");
            if (type == 1) {//跳转原生界面
                int localType = obj.getInt("localType");
                String localId = obj.getString("localId");
                if (localType == 1) {
                    //借款产品
                    intent = new Intent(context, LoanDetailActivity.class);
                    intent.putExtra("loanId", localId);
                } else if (localType == 2) {
                    //资讯
                    intent = new Intent(context, NewsDetailActivity.class);
                    intent.putExtra("newsId", localId);
                    intent.putExtra("newsTitle", title);
                }
            } else if (type == 2) {//跳转网页
                intent = new Intent(context, ComWebViewActivity.class);
                intent.putExtra("url", obj.getString("url"));
                intent.putExtra("title", title);
            } else if (type == 3) { //站内推送内容
                intent = new Intent(context, ComWebViewActivity.class);
                intent.putExtra("url", NetConstants.generateInternalMessageUrl(obj.getString("localId")));
                intent.putExtra("title", title);
            } else if (type == 4) {//跳转到首页
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("home", true);
            }
            NotificationUtil.showNotification(context, title, content, intent, context.getPackageName() + ".push_message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveOnlineState " + b);
        }
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        if (BuildConfig.DEBUG) {
            LogUtils.i(TAG, "onReceiveCommandResult " + gtCmdMessage);
        }
    }
}
