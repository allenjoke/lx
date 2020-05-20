package com.domyos.econnected.net;

import android.content.Context;

import com.domyos.econnected.net.cookie.AddCookiesInterceptor;
import com.domyos.econnected.net.cookie.ReceivedCookiesInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by HouWei on 16/8/17.
 */

public class OkHttpManager {

    /**
     * 网络请求缓存目录
     */
    public static String RESPONSE_CACHE = "netcache";

    /**
     * 网络请求缓存大小10M
     */
    public static long RESPONSE_CACHE_SIZE = 1024 * 1024 * 10;

    /**
     * 网络请求链接超时5秒
     */
    public static long HTTP_CONNECT_TIMEOUT = 10000;

    /**
     * 网络请求读取超时10秒
     */
    public static long HTTP_READ_TIMEOUT = 20000;

    private static OkHttpClient singleton;

    /**
     * @param context
     */
    public static void initOkHttp(Context context) {
        if (singleton == null) {
            File cacheDir = new File(context.getCacheDir(), RESPONSE_CACHE);
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cache(new Cache(cacheDir, RESPONSE_CACHE_SIZE));
            builder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
            builder.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
            builder.interceptors().add(new ReceivedCookiesInterceptor(context));
            builder.interceptors().add(new AddCookiesInterceptor(context));
            builder.addInterceptor(httpLoggingInterceptor);
            singleton = builder.build();
        }
    }

    /**
     * 添加请求的相关操作
     *
     * @param interceptor
     */
    public static void addInterceptor(Interceptor interceptor) {
        if (singleton != null)
            singleton.interceptors().add(interceptor);
    }

    /**
     * 获取OkHttpClient实体
     *
     * @return
     */
    public static OkHttpClient getInstance() {
        if (singleton == null) {
            singleton = new OkHttpClient();
        }
        return singleton;
    }
}
