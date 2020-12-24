package com.example.shipin.net;

import com.example.shipin.model.api.ApiService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    //单例模式
    private static HttpManager instance;

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
    static class LoggingInterceptor implements Interceptor{
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            return chain.proceed(request);
        }
    }

    //头部拦截器
    static class HeaderInterceptor implements Interceptor{
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

    //使用视频网络请求
    private ApiService apiservice;//同袍天气请求
    public ApiService getApiService(){
        if(apiservice==null){
            apiservice=getRetrofit(ApiService.BASE_URL).create(ApiService.class);
        }
        return apiservice;
    }

}
