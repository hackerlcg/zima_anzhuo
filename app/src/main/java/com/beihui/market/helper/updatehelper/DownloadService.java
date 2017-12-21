package com.beihui.market.helper.updatehelper;


import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;

import com.beihui.market.helper.FileProviderHelper;
import com.beihui.market.util.NotificationUtil;

import java.io.File;

public class DownloadService extends IntentService {

    public DownloadService() {
        super(DownloadService.class.getSimpleName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String url = intent.getStringExtra("url");
            String filePath = intent.getStringExtra("filePath");
            if (url != null && filePath != null) {
                final NotificationCompat.Builder builder = NotificationUtil.showNotificationProgress(getApplicationContext());
                boolean res = DownloadHelper.download(url, filePath, new ProgressResponseListener() {
                    @Override
                    public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                        NotificationUtil.updateProgress(getApplicationContext(), builder, (int) ((100 * bytesRead) / contentLength));
                    }
                });

                NotificationUtil.dismissProgress(getApplicationContext());
                if (res) {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    File apkFile = new File(filePath);
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        try {
                            uri = FileProvider.getUriForFile(getApplicationContext(), FileProviderHelper.getFileProvider(this), apkFile);
                            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    } else {
                        uri = Uri.fromFile(apkFile);
                    }
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.setDataAndType(uri, "application/vnd.android.package-archive");
                    startActivity(install);
                } else {
                    NotificationUtil.showDownloadApkFailed(getApplicationContext());
                }
            }
        }
    }
}
