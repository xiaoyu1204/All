package com.example.shipin.model.api;

import com.example.shipin.model.bean.VideoBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface ApiService {

    String BASE_URL = "http://cdwan.cn:7000/tongpao/";

    @GET("home/video.json")
    Flowable<VideoBean> getVideo();//首页视频数据

}
