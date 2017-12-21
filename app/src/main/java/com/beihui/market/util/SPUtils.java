package com.beihui.market.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private static final String TAG = "Info";

    public static String getLastDialogAdId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("lastDialogAdId", null);
    }

    public static void setLastDialogAdId(Context context, String id) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastDialogAdId", id);
        editor.apply();
    }

    public static String getLastInstalledVersion(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("lastInstalledVersion", null);
    }

    public static void setLastInstalledVersion(Context context, String version) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastInstalledVersion", version);
        editor.apply();
    }

    public static String getLastNoticeId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getString("lastNoticeId", null);
    }

    public static void setLastNoticeId(Context context, String lastNoticeId) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("lastNoticeId", lastNoticeId);
        editor.apply();
    }

    public static boolean getNoticeClosed(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("noticeClose", false);
    }

    public static void setNoticeClosed(Context context, boolean closed) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("noticeClose", closed);
        editor.apply();
    }

    public static boolean getCheckPermission(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return sp.getBoolean("checkPermission", false);
    }

    public static void setCheckPermission(Context context, boolean check) {
        SharedPreferences sp = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("checkPermission", check);
        editor.apply();
    }
}
