package com.example.vp2.model;

import com.example.vp2.base.BaseModel;
import com.example.vp2.net.CommonSubscriber;
import com.example.vp2.net.HttpManager;
import com.example.vp2.utils.RxUtils;
import com.example.vp2.view.IVideo;

public class VideoModel extends BaseModel implements IVideo.BaseModel {
    @Override
    public void loadVideo(final Callback callback) {
        addDisposable(
                HttpManager.getInstance().getTongpaoApiService().getVideo()
                .compose(RxUtils.<VideoBean>rxScheduler())
                .subscribeWith(new CommonSubscriber<VideoBean>(callback) {
                    @Override
                    public void onNext(VideoBean videoBean) {
                        callback.success(videoBean);
                    }
                })
        );
    }
}