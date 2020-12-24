package com.example.vp2.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vp2.R;
import com.example.vp2.base.BaseAdapter;
import com.example.vp2.model.VideoBean;
import com.example.vp2.utils.TxtUtils;

import java.util.List;

public class VideoAdapter extends BaseAdapter {

    private Context context;
    public VideoAdapter(Context context, List Data) {
        super(context, Data);
        this.context=context;
    }

    @Override
    protected int getLayout() {
        return R.layout.video_item;
    }

    @Override
    protected void bindData(Object data, VH vh) {

        final VideoBean.DataBean.ListBean bean = (VideoBean.DataBean.ListBean) data;

        ImageView fragment = (ImageView) vh.getViewById(R.id.video_fragment);
        TextView title = (TextView) vh.getViewById(R.id.tv_video_title);
        ImageView img = (ImageView) vh.getViewById(R.id.iv_video_img);
        TextView name = (TextView) vh.getViewById(R.id.tv_video_name);
        final CheckBox love= (CheckBox) vh.getViewById(R.id.rb_video_love);
        final TextView count = (TextView) vh.getViewById(R.id.tv_video_count);

        love.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(love.isChecked()){
                    //点赞数量
                    TxtUtils.setTextView(count,bean.getLikeNumber()+1+"");
                }else {
                    //点赞数量
                    TxtUtils.setTextView(count,bean.getLikeNumber()+"");
                }
            }
        });

        //封面
        TxtUtils.setImageView(context,fragment,bean.getCover());
        TxtUtils.setTextView(title,bean.getContent());//标题
        //头像
        Glide.with(context).load(bean.getHeadUrl()).apply(new RequestOptions().circleCrop()).into(img);
        //名字
        TxtUtils.setTextView(name,bean.getNickName());
        //点赞数量
        TxtUtils.setTextView(count,bean.getLikeNumber()+"");
    }
}
