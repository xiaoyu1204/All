package com.example.vp2.view;


import com.example.vp2.base.IBaseModel;
import com.example.vp2.base.IBasePersenter;
import com.example.vp2.base.IBaseView;
import com.example.vp2.model.Callback;
import com.example.vp2.model.VideoBean;

/**
 * 同袍首页视频功能接口锲约类
 */
public interface IVideo {
    interface View extends IBaseView {
        //定义一个被视频页实现的View层接口方法
        void getVideoReturn(VideoBean result);
    }

    interface Persenter extends IBasePersenter<View> {
        //定义一个视频推荐页面V层调用的接口
        void getVideo();
    }

    interface BaseModel extends IBaseModel {
        //定义一个加载视频数据的接口方法 被P层
        void loadVideo(Callback callback);

    }
}
