package com.example.shipin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shipin.MainActivity;
import com.example.shipin.R;
import com.example.shipin.base.BaseAdapter;
import com.example.shipin.model.bean.VideoBean;
import com.example.shipin.utils.TxtUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

public class ShowVideoAdapter extends BaseAdapter {

    private StandardGSYVideoPlayer mGs;//三方视频
    private List<VideoBean.DataBean.ListBean> list;
    private Context context;

    public ShowVideoAdapter(Context context, List Data) {
        super(context, Data);
        this.context = context;
    }

    @Override
    protected int getLayout() {
        return R.layout.video_show_item;
    }

    @Override
    protected void bindData(Object data, VH vh) {

        VideoBean.DataBean.ListBean bean = (VideoBean.DataBean.ListBean) data;

        mGs = (StandardGSYVideoPlayer) vh.getViewById(R.id.mGs);
        //bean.getVideoPath() 视频路径
        mGs.setUp(bean.getVideoPath(),false,bean.getContent());

        //增加封面
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//铺满
        imageView.setImageResource(R.mipmap.ic_launcher);
        TxtUtils.setImageView(context,imageView,bean.getCover());//第一帧

        mGs.setThumbImageView(imageView);
        //增加title
        mGs.getTitleTextView().setVisibility(View.VISIBLE);

        //设置返回键
        mGs.getBackButton().setVisibility(View.VISIBLE);

        //设置全屏按键功能
        mGs.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGs.startWindowFullscreen(context,false,true);
            }
        });
        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        mGs.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        mGs.setReleaseWhenLossAudio(true);
        //小屏时不触摸滑动
        mGs.setIsTouchWiget(false);
        //是否可以滑动调整
        mGs.setIsTouchWiget(true);
        //设置返回按键功能
        mGs.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过意图，跳转回视频Fragment
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("index",2);
                context.startActivity(intent);
                //释放所有
                mGs.setVideoAllCallBack(null);
            }
        });
        mGs.startPlayLogic();//开启视频

    }


    public void onDestroyVideo() {
        GSYVideoManager.releaseAllVideos();//释放所有视频
        mGs.setVideoAllCallBack(null);
    }

}
