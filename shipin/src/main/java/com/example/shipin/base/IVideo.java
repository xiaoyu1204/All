package com.example.shipin.base;

import com.example.shipin.model.api.Callback;
import com.example.shipin.model.bean.VideoBean;

public interface IVideo {

    interface View extends IBaseView{
        //定义一个被视频页实现的View层接口方法
        void getVideoReturn(VideoBean result);
    }

    interface Persenter extends IBasePresenter<IVideo.View>{
        //定义一个视频推荐页面V层调用的接口
        void getVideo();
    }

    interface Model extends IBaseModel{
        //定义一个加载视频数据的接口方法 被P层
        void loadVideo(Callback callback);
    }

}
