package com.example.huanxin.liaotian;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.huanxin.R;
import com.example.huanxin.touxiang.EMUserInfo;
import com.example.huanxin.touxiang.SpUtils;
import com.example.huanxin.touxiang.UserInfoManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<EMMessage> msgsList;
    private String selfid;

    public MyItemClick myItemClick;

    public void setMyItemClick(MyItemClick myItemClick) {
        this.myItemClick = myItemClick;
    }

    public ChatAdapter(Context context, List<EMMessage> msgsList) {
        this.context = context;
        this.msgsList = msgsList;
        selfid = EMClient.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view1 = View.inflate(context, R.layout.easemob_chat_item1, null);
            return new Holder1(view1);
        } else {
            View view2 = View.inflate(context, R.layout.easemob_chat_item2, null);
            return new Holder2(view2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);

        switch (type){
            case 0:

                EMMessage emMessage = msgsList.get(position);
                Holder1 holder1 = (Holder1) holder;

                //头像
                if(selfid.equals(emMessage.getFrom())){     //自己
                    String header = SpUtils.getInstance().getString(selfid);
                    if(!TextUtils.isEmpty(header)){
                        Glide.with(holder1.iv_img1).load(header).into(holder1.iv_img1);
                    }
                }else{      //好友
                    //通过id查询所有用户
                    EMUserInfo user = UserInfoManager.getInstance().getUserInfoByUid(emMessage.getFrom());
                    if(user != null){
                        if(!TextUtils.isEmpty(user.getHeader())){   //头像不等于空
                            Glide.with(holder1.iv_img1).load(user.getHeader()).into(holder1.iv_img1);
                        }
                    }
                }

                //消息内容
                if(emMessage.getType()== EMMessage.Type.TXT) {
                    holder1.chat1.setVisibility(View.VISIBLE);
                    holder1.img_icon1.setVisibility(View.GONE);
                    EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
                    holder1.chat1.setText(body.getMessage());
                }
                if(emMessage.getType()== EMMessage.Type.IMAGE) {
                    holder1.chat1.setVisibility(View.GONE);
                    holder1.img_icon1.setVisibility(View.VISIBLE);
                    EMImageMessageBody imageMessageBody = (EMImageMessageBody) emMessage.getBody();
                    Uri localUri = imageMessageBody.thumbnailLocalUri();
                    Glide.with(context).load(localUri).into(holder1.img_icon1);
                }

                break;
            case 1:

                EMMessage emMessage2 = msgsList.get(position);
                Holder2 holder2 = (Holder2) holder;

                //头像
                if(selfid.equals(emMessage2.getFrom())){
                    String header = SpUtils.getInstance().getString(selfid);
                    if(!TextUtils.isEmpty(header)){
                        Glide.with(holder2.iv_img2).load(header).into(holder2.iv_img2);
                    }
                }else{
                    EMUserInfo user = UserInfoManager.getInstance().getUserInfoByUid(emMessage2.getFrom());
                    if(user != null){
                        if(!TextUtils.isEmpty(user.getHeader())){
                            Glide.with(holder2.iv_img2).load(user.getHeader()).into(holder2.iv_img2);
                        }
                    }
                }

                //消息内容
                if(emMessage2.getType()== EMMessage.Type.TXT) {
                    holder2.chat2.setVisibility(View.VISIBLE);
                    holder2.img_icon2.setVisibility(View.GONE);
                    EMTextMessageBody body1 = (EMTextMessageBody) emMessage2.getBody();
                    holder2.chat2.setText(body1.getMessage());
                }
                if(emMessage2.getType()== EMMessage.Type.IMAGE){
                    holder2.chat2.setVisibility(View.GONE);
                    holder2.img_icon2.setVisibility(View.VISIBLE);
                    EMImageMessageBody imageMessageBody2 = (EMImageMessageBody) emMessage2.getBody();
                    Uri path = imageMessageBody2.thumbnailLocalUri();
                    Glide.with(context).load(path).into(holder2.img_icon2);

                }

                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接口回调的调用
                if (myItemClick != null) {
                    myItemClick.Item(position);
                }
            }
        });

    }

    /**
     * 如果消息是自己发送的 0   消息是其他人的 1
     */
    @Override
    public int getItemCount() {
        return msgsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(selfid.equals(msgsList.get(position).getFrom())){
            return 0;
        }else{
            return 1;
        }
    }

    class Holder1 extends RecyclerView.ViewHolder {
        TextView chat1;
        ImageView iv_img1;
        ImageView img_icon1;
        public Holder1(@NonNull View itemView) {
            super(itemView);
            chat1 = itemView.findViewById(R.id.tv_chat1);
            iv_img1 = itemView.findViewById(R.id.iv_img1);
            img_icon1 = itemView.findViewById(R.id.img_icon1);
        }
    }

    class Holder2 extends RecyclerView.ViewHolder {
        TextView chat2;
        ImageView iv_img2;
        ImageView img_icon2;
        public Holder2(@NonNull View itemView) {
            super(itemView);
            chat2 = itemView.findViewById(R.id.tv_chat2);
            iv_img2 = itemView.findViewById(R.id.iv_img2);
            img_icon2 = itemView.findViewById(R.id.img_icon2);
        }
    }

    public interface MyItemClick {
        void Item(int pos);
    }

}
