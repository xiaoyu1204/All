package com.example.huanxin.liaotian;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.huanxin.R;
import com.example.huanxin.touxiang.BaseAdapter;
import com.example.huanxin.touxiang.EMUserInfo;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends BaseAdapter {

    private Context context;
    List<EMUserInfo> userList;

    public FriendsAdapter(Context context, List<EMUserInfo> userList) {
        super(context,userList);
        this.context = context;
        this.userList = userList;
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_friend_item;
    }

    @Override
    protected void bindData(Object data, VH vh) {

        EMUserInfo _data = (EMUserInfo) data;

        TextView txtUserName = (TextView) vh.getViewById(R.id.txt_username);
        Button btnOpenDetil = (Button) vh.getViewById(R.id.btn_openUserDetail);
        ImageView imgheader = (ImageView) vh.getViewById(R.id.img_header);
        TextView text = (TextView) vh.getViewById(R.id.text);

        //未读消息
        if(Integer.parseInt(_data.getCount())!=0){
            text.setVisibility(View.VISIBLE);
            //未读消息
            text.setText(_data.getCount()+"");
        }else{
            text.setVisibility(View.GONE);
        }

        //文字
        if(!TextUtils.isEmpty(_data.getNickname())){
            txtUserName.setText(_data.getNickname());
        }else{
            txtUserName.setText(_data.getUid());
        }

        //头像
        String header = _data.getHeader();
        if(!TextUtils.isEmpty(header)){
            Glide.with(imgheader).load(header).into(imgheader);
        }

        //按钮
        btnOpenDetil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iItemViewClick!=null){//如果不为空，就把数据传送给外面
                    iItemViewClick.itemViewClick(v.getId(),_data);
                }
            }
        });

    }

}