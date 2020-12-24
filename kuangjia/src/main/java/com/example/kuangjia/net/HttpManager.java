package com.example.kuangjia.net;

import com.example.kuangjia.model.api.ApiService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求
 * 整个项目全局使用
 */
public class HttpManager {
    //单例模式
    private static HttpManager instance;
    private ApiService apiService;  //请求
    public static HttpManager getInstance(){
        if(instance == null){
            synchronized(HttpManager.class){//加锁
                if(instance == null){
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    //日志拦截器
    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            return chain.proceed(request);
        }
    }

    //头部拦截器
    static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization","APPCODE 964e16aa1ae944e9828e87b8b9fbd30a")
                    .build();
            return chain.proceed(request);
        }
    }

    private OkHttpClient getOkHttpClient(){
        OkHttpClient ok = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .addInterceptor(new HeaderInterceptor())
                .build();
        return ok;
    }

    //网络请求
    private Retrofit getRetrofit(String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClient())///添加拦截器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    //使用网络请求
    public ApiService getApiService(){
        if(apiService==null){
            apiService=getRetrofit(ApiService.BASE_URL).create(ApiService.class);
        }
        return apiService;
    }

}
