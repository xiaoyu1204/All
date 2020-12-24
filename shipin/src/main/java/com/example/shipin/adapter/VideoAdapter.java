package com.example.shipin.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shipin.R;
import com.example.shipin.base.BaseAdapter;
import com.example.shipin.model.bean.VideoBean;
import com.example.shipin.utils.TxtUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.List;

public class VideoAdapter extends BaseAdapter {

    //第三方视频
    private StandardGSYVideoPlayer mGs;
    Context context;

    public VideoAdapter(Context context, List Data) {
        super(context, Data);
        this.context = context;
    }

    @Override
    protected int getLayout() {
        return R.layout.video_item;
    }

    @Override
    protected void bindData(Object data, VH vh) {
        VideoBean.DataBean.ListBean bean = (VideoBean.DataBean.ListBean) data;

        ImageView fragment = (ImageView) vh.getViewById(R.id.video_fragment);
        TextView title = (TextView) vh.getViewById(R.id.tv_video_title);
        ImageView img = (ImageView) vh.getViewById(R.id.iv_video_img);
        TextView name = (TextView) vh.getViewById(R.id.tv_video_name);
        TextView count = (TextView) vh.getViewById(R.id.tv_video_count);

        //封面
        TxtUtils.setImageView(context,fragment,bean.getCover());
        //标题
        TxtUtils.setTextView(title,bean.getContent());
        //头像
        Glide.with(context).load(bean.getHeadUrl()).apply(new RequestOptions().circleCrop()).into(img);
        //名字
        TxtUtils.setTextView(name,bean.getNickName());
        //点赞数量
        TxtUtils.setTextView(count,bean.getLikeNumber()+"");

    }

}
