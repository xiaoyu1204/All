package com.example.huanxin.liaotian;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huanxin.R;
import com.example.huanxin.touxiang.Constants;
import com.example.huanxin.touxiang.EMUserInfo;
import com.example.huanxin.touxiang.SpUtils;
import com.example.huanxin.touxiang.UserInfoManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTxtTitle;
    private RecyclerView mRecyChat;
    private EditText mInputWord;
    private Button mBtnSend;

    private String toUserId;    //聊天用户的Id
    private String selfId;  //自己的Id

    List<EMMessage> msgsList;
    private ChatAdapter chatAdapter;
    private ImageView mImgInput;

    //EMClient.getInstance().chatManager();
    private EMChatManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initData();
        initAll();
        initMsgListner();
        initThrough();
        initReset();
    }

    private void initReset() {
//        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toUserId);
//        //指定会话消息未读数清零
//        if(conversation != null){
//        conversation.markAllMessagesAsRead();
//        //把一条消息置为已读
//        //conversation.markMessageAsRead(messageId);
//        }
        //所有未读消息数清零
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }

    private void initThrough() {
        //发送透传消息
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);

        String action="action1";//action可以自定义
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        String toUsername = toUserId;//发送给某个人
        cmdMsg.setTo(toUsername);
        cmdMsg.addBody(cmdBody);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mRecyChat = (RecyclerView) findViewById(R.id.recy_chat);
        mInputWord = (EditText) findViewById(R.id.input_word);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mImgInput = (ImageView) findViewById(R.id.img_input);

        manager = EMClient.getInstance().chatManager();

        mBtnSend.setOnClickListener(this);
        mImgInput.setOnClickListener(this);

    }

    private void initData() {
        //获取传来的好友
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("touserid")) {
                //给标题赋值名称
                toUserId = intent.getStringExtra("touserid");
                mTxtTitle.setText(toUserId);    //和谁聊天
            }
        }
        //获取自己的ID
        selfId = EMClient.getInstance().getCurrentUser();

        msgsList = new ArrayList<>();
        mRecyChat.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(this, msgsList);
        mRecyChat.setAdapter(chatAdapter);

        EMClient.getInstance().chatManager().importMessages(msgsList);//此句为设置

    }

    //获取聊天记录
    private void initAll() {

        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toUserId);
        if (conversation!=null){
            //获取此会话的所有消息
            String msgId = conversation.getLastMessage().getMsgId();
            //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
            //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
            List<EMMessage> messages = conversation.loadMoreMsgFromDB(msgId, 20);

            msgsList.addAll(messages);
            chatAdapter.notifyDataSetChanged();

        }
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.img_input:
                openPhoto();
                break;
        }
    }

    //发送消息
    private void sendMsg() {
        //获取聊天框的信息
        String content = mInputWord.getText().toString().trim();
        if (TextUtils.isEmpty(content)) { //如果输入框为空
            Toast.makeText(this, "请输入消息内容", Toast.LENGTH_SHORT).show();
            return;
        }

        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toUserId);
        //如果是群聊，设置chattype，默认是单聊
//        message.setChatType(EMMessage.ChatType.GroupChat);
//        message.setChatType(EMMessage.ChatType.ChatRoom);

        msgsList.add(message);
        chatAdapter.notifyDataSetChanged();

        //发送消息
        manager.sendMessage(message);

        //实现键盘弹出界面上移
        mRecyChat.smoothScrollToPosition(msgsList.size());
        mInputWord.setText("");

    }

    /**
     * 三方打开相册
     */
    private void openPhoto() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                .maxSelectNum(9)    //几张图片
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    //注册接收
    private void initMsgListner() {
        manager.addMessageListener(msgListener);
    }


    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List <EMMessage> messages) {
            //收到消息
            msgsList.addAll(messages);
            mRecyChat.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                    //实现键盘弹出界面上移
                    mRecyChat.smoothScrollToPosition(msgsList.size());
//                    Toast.makeText(ChatActivity.this, "收到的消息"+messages.get(position).getBody().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        /**
         * 接收透传消息  --- 头像更新
         * @param messages
         */
        @Override
        public void onCmdMessageReceived(List <EMMessage> messages) {
            //收到透传消息
            for (EMMessage item : messages) {
                if(item.getType() == EMMessage.Type.CMD){
                    EMCmdMessageBody msg = (EMCmdMessageBody) item.getBody();
                    if(Constants.ACTION_UPDATEHEADER.equals(msg.action())){
                        //刷新界面更新用户头像
                        String action = msg.action();
                        if(!TextUtils.isEmpty(action)){
                            //拿到了对方传过来的uid
                            String uid = item.getFrom();
                            //更新内存里所有用户的值
                            /**
                             * 为了保证进入聊天界面以后
                             * 所有的用户信息都能得到我的控制
                             * 我的好友信息一次性获取下来
                             * 存在内存里面
                             */
                            SpUtils.getInstance().setValue(uid,action);

                            //把好友找出来
                            EMUserInfo user = UserInfoManager.getInstance().getUserInfoByUid(uid);
                            if(user != null){   //如果好友不等于空,说明好友在我的列表里面
                                //更新头像          action头像
                                user.setHeader(action);
                            }
                        }
                    }else if(Constants.ACTION_UPDATENICKNAME.equals(msg.action())){
                        //刷新界面更新用户昵称
                    }
                }
            }
        }

        @Override
        public void onMessageRead(List <EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List <EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageRecalled(List <EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    /**
     * 图片二次采样
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // onResult Callback
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList.size() == 0) return;
                //获取本地图片的选择地址，上传到服务器
                //头像的压缩和二次采样
                //把选中的图片插入到列表
                for(LocalMedia item:selectList){
                    sendMsgByImage(item.getPath());
                }
                break;
            default:
                break;
        }
    }

    //发图片
    private void sendMsgByImage(String path) {
        Uri uri = Uri.parse(path);
        EMMessage msg = EMMessage.createImageSendMessage(uri, false, toUserId);
        /*EMImageMessageBody body = new EMImageMessageBody(uri);
        msg.addBody(body);*/
        //如果是群聊，设置chattype，默认是单聊
        manager.sendMessage(msg);
        msgsList.add(msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatAdapter.notifyDataSetChanged();
                //实现键盘弹出界面上移
                mRecyChat.smoothScrollToPosition(msgsList.size());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭监听器
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

}