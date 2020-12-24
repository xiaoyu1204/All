package com.example.huanxin.liaotian;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huanxin.R;
import com.example.huanxin.touxiang.BaseAdapter;
import com.example.huanxin.touxiang.EMUserInfo;
import com.example.huanxin.touxiang.SpUtils;
import com.example.huanxin.touxiang.UserDetailActivity;
import com.example.huanxin.touxiang.UserInfoManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<EMUserInfo> userList;
    private FriendsAdapter friendsAdapter;
    private RecyclerView mRecyUserList;
    private EMUserInfo emUserInfo;

    /**
     * 请输入用户名：
     */
    private EditText mEtName;
    /**
     * 请输入密码：
     */
    private EditText mEtPwd;
    /**
     * 登录
     */
    private Button mBtnLogin;
    /**
     * 登出
     */
    private Button mBtnOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        mRecyUserList = (RecyclerView) findViewById(R.id.recy_userList);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mBtnOut = (Button) findViewById(R.id.btn_out);
        mBtnOut.setOnClickListener(this);

        initUserList();

    }

    private void initUserList() {

        //用的是getAllUsers里面的对象
        userList = UserInfoManager.getInstance().getAllUsers();

        friendsAdapter = new FriendsAdapter(this, userList);
        mRecyUserList.setLayoutManager(new LinearLayoutManager(this));
        mRecyUserList.setAdapter(friendsAdapter);

        friendsAdapter.addListClick(new BaseAdapter.IListClick() {

            @Override
            public void itemClick(int pos) {


                //封装类确定的东西
                String touserid = userList.get(pos).getUid();
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("touserid", touserid);
                startActivity(intent);

            }
        });

        //处理点击条目中的按钮
        friendsAdapter.addItemViewClick(new BaseAdapter.IItemViewClick<EMUserInfo>() {
            @Override
            public void itemViewClick(int viewid, EMUserInfo data) {
                emUserInfo = data;
                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                intent.putExtra("username", data.getUid());
                startActivity(intent);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_login:
                //登录
                login();
                break;
            case R.id.btn_out:
                //登出
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(true);
                    }
                });
                break;
        }
    }

    //登录
    private void login() {
        String username = mEtName.getText().toString();
        String password = mEtPwd.getText().toString();
        Log.i(TAG, "环信loginlogin");
        if (TextUtils.isEmpty(username)&&TextUtils.isEmpty(password)) {
            Toast.makeText(this, "您输入的账号，用户名不能为空", Toast.LENGTH_SHORT).show();
        }else{
            EMClient.getInstance().login(username, password, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.e(TAG, "登录成功");
                    getFriends();
                }

                @Override
                public void onError(int code, String error) {
                    Log.e(TAG, "onError:" + error);
                }

                @Override
                public void onProgress(int progress, String status) {
                    Log.e(TAG, "status:" + status);

                }
            });
        }

    }

    /**
     * 获取好友
     */
    private void getFriends() {
        try {
            List<String> friends = EMClient.getInstance().contactManager().getAllContactsFromServer();

            List<EMUserInfo> list = new ArrayList<>();
            for (String item : friends) {
                EMUserInfo user = new EMUserInfo();
                user.setUid(item);

                //服务器没办法保存,存到了sp上
                String header = SpUtils.getInstance().getString(item);
                //如果不为空
                if (!TextUtils.isEmpty(header)) {
                    user.setHeader(header);
                }

                list.add(user);

            }
            UserInfoManager.getInstance().addUsers(list);

            for (int i = 0; i <userList.size() ; i++) {
                String uid = userList.get(i).getUid();
                Log.i(TAG, "getFriends: "+uid);
                //获取未读信息
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(uid);
                if(conversation != null){
                    userList.get(i).setCount(conversation.getUnreadMsgCount()+"");
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    friendsAdapter.notifyDataSetChanged();
                }
            });

            if (friends != null) {
                mRecyUserList.post(new Runnable() {
                    @Override
                    public void run() {
                        friendsAdapter.notifyDataSetChanged();
                    }
                });
            }

        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

}
