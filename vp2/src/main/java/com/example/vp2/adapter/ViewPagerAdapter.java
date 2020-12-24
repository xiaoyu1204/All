package com.example.vp2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vp2.MainActivity;
import com.example.vp2.R;
import com.example.vp2.base.BaseAdapter;
import com.example.vp2.model.VideoBean;
import com.example.vp2.utils.TxtUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

public class ViewPagerAdapter extends BaseAdapter<VideoBean.DataBean.ListBean> {
    private StandardGSYVideoPlayer mGs;

    public ViewPagerAdapter(Context context, List<VideoBean.DataBean.ListBean> data) {
        super(context, data);
    }

    @Override
    protected int getLayout() {
        return R.layout.adapter_show_item;
    }

    @Override
    protected void bindData(VideoBean.DataBean.ListBean data, VH vh) {
        mGs = (StandardGSYVideoPlayer) vh.getViewById(R.id.mGs);
        ImageView video_stand_head = (ImageView) vh.getViewById(R.id.video_stand_head);
        TextView video_stand_name = (TextView) vh.getViewById(R.id.video_stand_name);
        TextView tv_context = (TextView) vh.getViewById(R.id.tv_context);


        mGs.setUp(data.getVideoPath(),false,data.getContent());
        TxtUtils.setTextView(video_stand_name,data.getNickName());
        TxtUtils.setImageView(video_stand_head,data.getHeadUrl());
        TxtUtils.setTextView(tv_context,data.getContent());
        //增加封面
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);
        TxtUtils.setImageView(imageView,data.getCover());
        mGs.setThumbImageView(imageView);
        //设置返回键
        mGs.getBackButton().setVisibility(View.VISIBLE);
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
