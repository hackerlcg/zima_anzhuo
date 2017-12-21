package com.beihui.market.helper;


import android.content.Context;
import android.os.Environment;

public class FileProviderHelper {

    public static String getFileProvider(Context context) {
        return context.getPackageName() + ".fileprovider";
    }

    public static String getTempDirPath(Context context) {
        if (context.getExternalCacheDir() != null) {
            return context.getExternalCacheDir().getAbsolutePath() + "/temp";
        } else {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp";
        }
    }
}
