package com.chuyendoidauso.chuyendoidanhba.api;

import android.content.Context;
import android.util.Log;

import com.chuyendoidauso.chuyendoidanhba.AppNumberChanger;
import com.chuyendoidauso.chuyendoidanhba.R;
import com.chuyendoidauso.chuyendoidanhba.api.services.AppService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jacky on 1/9/18.
 */

public class AppClient {

    private static final int DEFAULT_TIMEOUT = 30;
    private AppService apiService;
    private static Retrofit retrofit;
    private Cache cache = null;
    private File httpCacheDirectory;

    public Class<AppService> getClassService() {
        return AppService.class;
    }

    public static class SingletonHolder {
        private static AppClient INSTANCE = new AppClient();
    }

    public static AppClient getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public AppClient() {
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(AppNumberChanger.newInstance().getCacheDir(), "tamic_cache");
        }

        try {
            if (cache == null && httpCacheDirectory != null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        // todo deal with the issues the way you need to
                        return response;
                    }
                })
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                //.cache(cache)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                .build();


        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(AppNumberChanger.newInstance().getString(R.string.url_app))
                .build();

        createAppApi();
    }


    private AppClient createAppApi() {
        apiService = getApiService();
        return this;
    }

    public AppService getApiService() {
        if (apiService == null) {
            apiService = create(getClassService());
        }
        return apiService;
    }


    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }
}