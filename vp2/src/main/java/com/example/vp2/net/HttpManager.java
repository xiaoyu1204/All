package com.example.vp2.net;
import com.example.vp2.model.TongpaoApiService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private TongpaoApiService tongpaoApiService;//同袍请求

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
        public Response intercept(Chain chain) throws IOException {
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
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    //使用同袍网络请求
    public TongpaoApiService getTongpaoApiService(){
        if(tongpaoApiService==null){
            tongpaoApiService=getRetrofit(TongpaoApiService.BASE_URL).create(TongpaoApiService.class);
        }
        return tongpaoApiService;
    }

}
