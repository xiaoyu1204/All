package com.example.vp2.presenter;

import com.example.vp2.base.BasePresenter;
import com.example.vp2.model.Callback;
import com.example.vp2.model.VideoBean;
import com.example.vp2.model.VideoModel;
import com.example.vp2.view.IVideo;

public class VideoPresenter extends BasePresenter<IVideo.View> implements IVideo.Persenter{

    IVideo.View view;
    IVideo.BaseModel model;

    public VideoPresenter(IVideo.View view) {
        this.view = view;
        this.model= new VideoModel();
    }

    @Override
    public void getVideo() {//首页视频页面
        this.model.loadVideo(new Callback() {
            @Override
            public void fail(String msg) {
                view.tips(msg);
            }

            @Override
            public void success(Object o) {
                view.getVideoReturn((VideoBean) o);
            }
        });
    }
}
