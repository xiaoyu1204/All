package com.example.shipin.model;

import com.example.shipin.base.BaseModel;
import com.example.shipin.base.IVideo;
import com.example.shipin.model.api.Callback;
import com.example.shipin.model.bean.VideoBean;
import com.example.shipin.net.CommonSubscriber;
import com.example.shipin.net.HttpManager;
import com.example.shipin.utils.RxUtils;

import io.reactivex.disposables.Disposable;

public class VideoModel extends BaseModel implements IVideo.Model {

    @Override
    public void loadVideo(Callback callback) {
        addDisposable(
                HttpManager.getInstance().getApiService()
                .getVideo()
                .compose(RxUtils.rxScheduler())
                .subscribeWith(new CommonSubscriber<VideoBean>(callback){
                    @Override
                    public void onNext(VideoBean videoBean) {
                        callback.success(videoBean);
                    }
                })
        );
    }

}
