package com.example.shipin;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.shipin.base.BaseActivity;
import com.example.shipin.base.BasePresenter;
import com.example.shipin.base.IVideo;
import com.example.shipin.base.IVideo.View;
import com.example.shipin.model.bean.VideoBean;
import com.example.shipin.presenter.VideoPresenter;
import com.example.shipin.ui.fragment.HotFragment;
import com.example.shipin.ui.fragment.NewsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity<BasePresenter> implements IVideo.View {

    @BindView(R.id.mRb_newest)
    RadioButton mRbNewest;
    @BindView(R.id.mRb_hot)
    RadioButton mRbHot;
    @BindView(R.id.mRg)
    RadioGroup mRg;
    @BindView(R.id.mFl)
    FrameLayout mFl;
    private NewsFragment newsFragment;
    private HotFragment hotFragment;
    private FragmentManager manager;
    IVideo.Persenter presenter;
    private List<VideoBean.DataBean.ListBean> list_video;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        newsFragment = new NewsFragment();
        hotFragment = new HotFragment();
        //点击方法
        initFragment();
    }

    @Override
    protected BasePresenter createPresenter() {
        return new VideoPresenter(this);
    }

    @Override
    protected void initData() {
        presenter = new VideoPresenter(this);
        presenter.getVideo();
    }

    private void initFragment() {
        //按钮的监听与fragment结合
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //碎片管理器
                FragmentTransaction transaction = manager.beginTransaction();
                switch (checkedId) {
                    case R.id.mRb_newest:       //点击最新
                        transaction.show(newsFragment).hide(hotFragment).commit();
                        break;
                    case R.id.mRb_hot:       //点击热门
                        transaction.show(hotFragment).hide(newsFragment).commit();
                        break;
                }
            }
        });
    }

    @Override
    public void getVideoReturn(VideoBean result) {

        list_video = new ArrayList<>();

        if(result.getData().getList().size()>0){

            list_video.addAll(result.getData().getList());

            newsFragment.setList(list_video);//调用构造方法传入集合
            hotFragment.setList(list_video
            );

            //碎片管理器
            manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.mFl,hotFragment)
                    .add(R.id.mFl,newsFragment)     //显示最新
                    .hide(hotFragment).commit();    //隐藏热门并提交事物

        }
    }

}
