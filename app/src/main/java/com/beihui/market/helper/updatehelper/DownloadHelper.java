package com.beihui.market.helper.updatehelper;


import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class DownloadHelper {

    static boolean download(String url, String filePath, ProgressResponseListener listener) {
        boolean res = false;
        if (url != null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Request request = new Request.Builder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .url(url)
                    .get()
                    .build();

            OkHttpClient client = ProgressHelper.addProgressResponseListener(builder, listener);
            FileOutputStream fos = null;
            try {
                Response response = client.newCall(request).execute();
                if (response.code() == 200) {
                    res = true;
                    fos = new FileOutputStream(filePath);
                    ResponseBody body = response.body();
                    if (body != null) {
                        fos.write(body.bytes());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                res = false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return res;
    }

}
