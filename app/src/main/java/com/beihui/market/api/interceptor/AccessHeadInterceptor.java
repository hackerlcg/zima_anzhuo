package com.beihui.market.api.interceptor;


import com.beihui.market.api.NetConstants;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class AccessHeadInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private Map<String, Object> keyValue = new HashMap<>();
    private Comparator<String> keyComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    };

    @Override
    public Response intercept(Chain chain) throws IOException {
        long reqTime = System.currentTimeMillis();

        Request request = chain.request();
        Request.Builder builder = request.newBuilder()
                .addHeader("reqTime", "" + reqTime)
                .addHeader("accessKey", NetConstants.ACCESS_KEY);
        StringBuilder sb = new StringBuilder(NetConstants.SECRET_KEY).append(reqTime);
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            MediaType contentType = requestBody.contentType();
            if (contentType != null && contentType.toString().equals("application/x-www-form-urlencoded")) {
                appendHeadWithBody(request, sb);
            }
        }
        builder.addHeader("sign", new String(Hex.encodeHex(DigestUtils.md5(new String(Hex.encodeHex(DigestUtils.md5(sb.toString())))))));
        return chain.proceed(builder.build());
    }

    private void appendHeadWithBody(Request request, StringBuilder sb) {
        try {
            RequestBody requestBody = request.body();
            Buffer buffer = new Buffer();
            if (requestBody != null) {
                requestBody.writeTo(buffer);

                Charset charset = null;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                if (charset == null) {
                    charset = UTF8;
                }
                String param = buffer.readString(charset);
                String[] pairs = param.split("&");
                String[] keys = new String[pairs.length];
                keyValue.clear();
                for (int i = 0, len = pairs.length; i < len; ++i) {
                    String[] keyvalue = pairs[i].split("=");
                    keys[i] = keyvalue[0];
                    keyValue.put(keys[i], URLDecoder.decode(keyvalue[1], "utf-8"));
                }
                Arrays.sort(keys, keyComparator);
                for (String key : keys) {
                    sb.append(key).append(keyValue.get(key));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
