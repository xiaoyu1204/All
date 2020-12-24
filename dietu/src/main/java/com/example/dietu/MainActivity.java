package com.example.dietu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.discussionavatarview.DiscussionAvatarListener;
import com.discussionavatarview.DiscussionAvatarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DiscussionAvatarView mDaView;
    /**
     * 初始化数据
     */
    private Button mBtReset;
    /**
     * 设置最大头像数为4
     */
    private Button mBtMaxCount;
    /**
     * 添加头像1
     */
    private Button mBtAdd;
    /**
     * 添加头像2
     */
    private Button mBtAdd2;
    private ArrayList<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mDaView = (DiscussionAvatarView) findViewById(R.id.da_view);
        mBtReset = (Button) findViewById(R.id.bt_reset);
        mBtReset.setOnClickListener(this);
        mBtMaxCount = (Button) findViewById(R.id.bt_max_count);
        mBtMaxCount.setOnClickListener(this);
        mBtAdd = (Button) findViewById(R.id.bt_add);
        mBtAdd.setOnClickListener(this);
        mBtAdd2 = (Button) findViewById(R.id.bt_add2);
        mBtAdd2.setOnClickListener(this);

        mDatas = new ArrayList<>();
        initTestDatas();

    }

    private void initTestDatas() {
        mDatas.add("https://b-ssl.duitang.com/uploads/item/201811/04/20181104223950_vygmz.thumb.700_0.jpeg");
        mDatas.add("https://b-ssl.duitang.com/uploads/item/201807/11/20180711091152_FakCJ.thumb.700_0.jpeg");
        mDatas.add("https://b-ssl.duitang.com/uploads/item/201811/04/20181104223952_zfhli.thumb.700_0.jpeg");
        mDatas.add("https://b-ssl.duitang.com/uploads/item/201810/30/20181030153225_mixve.thumb.700_0.jpg");
        mDatas.add("https://b-ssl.duitang.com/uploads/item/201807/08/20180708095827_SYPL3.thumb.700_0.jpeg");
        mDatas.add("https://b-ssl.duitang.com/uploads/item/201811/01/20181101093301_u2NKu.thumb.700_0.jpeg");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.bt_reset:
                mDaView.initDatas(mDatas);
                break;
            case R.id.bt_max_count:
                mDaView.setMaxCount(4);
                break;
            case R.id.bt_add:
                String url1 = "https://b-ssl.duitang.com/uploads/item/201807/11/20180711091152_FakCJ.thumb.700_0.jpeg";
                mDaView.addData(url1, new DiscussionAvatarListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {

                    }
                });
                break;
            case R.id.bt_add2:
                String url = "https://b-ssl.duitang.com/uploads/item/201902/10/20190210103053_fQA8f.thumb.700_0.jpeg";
                mDaView.addData(url);
                break;
        }
    }



}
