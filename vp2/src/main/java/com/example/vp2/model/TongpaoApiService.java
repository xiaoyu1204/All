package com.example.vp2.model;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TongpaoApiService {
    String BASE_URL = "http://cdwan.cn:7000/tongpao/";

    @GET("home/video.json")
    Flowable<VideoBean> getVideo();//首页视频数据

}
