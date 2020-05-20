package com.domyos.econnected.net;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HouWei on 16/8/17.
 */

public class RetrofitManager {
    private static Retrofit sRetrofit;

    /**
     * 初始化
     *
     * @param baseUrl
     */
    public static void init(String baseUrl) {
        if (sRetrofit == null) {
            OkHttpClient okHttpClient = OkHttpManager.getInstance();
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
    }

    /**
     * 获取Retrofit实体
     *
     * @return
     */
    public static Retrofit getInstance() {
        if (sRetrofit == null) {
            throw new RuntimeException("please init retrofit");
        }
        return sRetrofit;
    }

    /**
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> service) {
        return getInstance().create(service);
    }
}
