package com.example.kuangjia.model.api;

import com.example.kuangjia.model.bean.HomeBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface ApiService {

    String BASE_URL = "https://cdplay.cn/api/";

    @GET("index")
    Flowable<HomeBean> gethome();

}
