package com.beihui.market.helper.updatehelper;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.beihui.market.App;
import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.entity.AppUpdate;
import com.beihui.market.helper.FileProviderHelper;
import com.beihui.market.injection.component.DaggerAppUpdateHelperComponent;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.util.LogUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AppUpdateHelper {

    @Inject
    Api api;

    private Disposable disposable;
    private AppUpdate app;
    private WeakReference<AppCompatActivity> weakReference;
    private DownloadAppTask task;

    private Dialog progressDialog;
    private ProgressBar progressBar;

    public static AppUpdateHelper newInstance() {
        return new AppUpdateHelper();
    }

    private AppUpdateHelper() {
        DaggerAppUpdateHelperComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .build()
                .inject(this);
    }

    public void checkUpdate(AppCompatActivity activity) {
        weakReference = new WeakReference<>(activity);
        if (this.disposable != null && !this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
        disposable = api.queryUpdate()
                .compose(RxUtil.<ResultEntity<AppUpdate>>io2main())
                .subscribe(new Consumer<ResultEntity<AppUpdate>>() {
                               @Override
                               public void accept(@NonNull ResultEntity<AppUpdate> result) throws Exception {
                                   if (result.isSuccess() && result.getData() != null) {
                                       app = result.getData();
                                       handleUpdate(app, weakReference);
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LogUtils.e("AppUpdateHelper", throwable);
                            }
                        });
    }

    public void destroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
        task = null;
    }

    public void processAppUpdate(AppUpdate appUpdate, AppCompatActivity activity) {
        weakReference = new WeakReference<>(activity);
        startDownload(activity, appUpdate.getVersionUrl(), appUpdate.getVersion(), appUpdate.getHasForcedUpgrade() == 1);
    }

    private void handleUpdate(AppUpdate app, WeakReference<AppCompatActivity> wr) {
        if (wr.get() != null) {
            final AppCompatActivity context = wr.get();
            final String appVersion = app.getVersion();
            final String appUrl = app.getVersionUrl();
            final boolean isForce = app.getHasForcedUpgrade() == 1;
            try {
                String version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                if (version.compareTo(appVersion) < 0) {
                    CommNoneAndroidDialog dialog = new CommNoneAndroidDialog()
                            .withMessage(app.getContent())
                            .withPositiveBtn("立即更新", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startDownload(context, appUrl, appVersion, isForce);
                                }
                            });
                    //非强制更新
                    if (!isForce) {
                        dialog.withNegativeBtn("稍后再说", null);
                    }
                    dialog.setCancelable(false);
                    dialog.show(context.getSupportFragmentManager(), "update");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void startDownload(Context context, String url, String version, boolean isForce) {
        if (weakReference.get() == null)
            return;
        String name = version.replace(".", "_");
        String dirPath = FileProviderHelper.getTempDirPath(context);
        String filePath = dirPath + "/" + name + ".apk";
        try {
            File dir = new File(dirPath);
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            File file = new File(filePath);
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
            weakReference.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(weakReference.get(), "文件读取失败，请检查应用权限", null);
                    //重新弹窗
                    handleUpdate(app, weakReference);
                }
            });
            return;
        }

        //如果是强制更新，则在当前界面下载，禁止用户其他操作
        if (isForce) {
            task = new DownloadAppTask(this, filePath);
            task.execute(url);
        } else {//如果非强制更新，则进入service下载
            Intent intent = new Intent(context, DownloadService.class);
            intent.putExtra("url", url);
            intent.putExtra("filePath", filePath);
            context.startService(intent);
        }
    }

    private void install(String filePath) {
        Context context = weakReference.get();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(filePath);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                uri = FileProvider.getUriForFile(context, FileProviderHelper.getFileProvider(context), apkFile);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void showDownloadProgress() {
        if (weakReference.get() != null) {
            View holder = LayoutInflater.from(weakReference.get())
                    .inflate(R.layout.dialog_download_progress, null);
            progressBar = (ProgressBar) holder.findViewById(R.id.number_progress_bar);
            progressDialog = new Dialog(weakReference.get(), R.style.DownloadProgressDialogStyle);
            progressDialog.setContentView(holder);
            progressDialog.setCancelable(false);

            Window window = progressDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);
            }
            progressDialog.show();
        }
    }

    private void dismissDownloadProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    private void updateProgress(long po) {
        if (progressBar != null) {
            progressBar.setProgress((int) po);
        }
    }

    private static class DownloadAppTask extends AsyncTask<String, Long, Boolean> {

        private AppUpdateHelper helper;

        private String filePath;

        public DownloadAppTask(AppUpdateHelper helper, String filePath) {
            this.helper = helper;
            this.filePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (helper != null) {
                helper.showDownloadProgress();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String url = params[0];
            return DownloadHelper.download(url, filePath, new ProgressResponseListener() {
                @Override
                public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
                    publishProgress((100 * bytesRead) / contentLength);
                }
            });
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            if (helper != null) {
                helper.updateProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                if (helper != null) {
                    helper.dismissDownloadProgress();
                    helper.install(filePath);
                }
            } else {
                if (helper != null && helper.weakReference.get() != null) {
                    ToastUtils.showShort(helper.weakReference.get(), "下载失败，请检查网络或者应用权限", null);
                }
            }
        }
    }
}
