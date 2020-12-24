package com.example.shipin.presenter;

import com.example.shipin.base.BasePresenter;
import com.example.shipin.base.IVideo;
import com.example.shipin.model.VideoModel;
import com.example.shipin.model.api.Callback;
import com.example.shipin.model.bean.VideoBean;

public class VideoPresenter extends BasePresenter<IVideo.View> implements IVideo.Persenter {

    IVideo.View view;
    IVideo.Model model;

    public VideoPresenter(IVideo.View view) {
        this.view = view;
        model = new VideoModel();
    }

    @Override
    public void getVideo() {    //首页视频页面
        model.loadVideo(new Callback() {
            @Override
            public void success(Object o) {
                view.getVideoReturn((VideoBean) o);
            }

            @Override
            public void fail(String msg) {
                view.tips(msg);
            }
        });
    }

}
