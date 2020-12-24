package com.example.vp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.vp2.base.BaseActivity;
import com.example.vp2.model.VideoBean;
import com.example.vp2.presenter.VideoPresenter;
import com.example.vp2.ui.HotFragment;
import com.example.vp2.ui.NewestFragment;
import com.example.vp2.view.IVideo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<VideoPresenter> implements IVideo.View {

    IVideo.Persenter persenter;
    @BindView(R.id.mRg)
    RadioGroup mRg;

    List<VideoBean.DataBean.ListBean> list_Video = new ArrayList<>();
    private NewestFragment newestFragment;
    private HotFragment hotFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager manager;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        newestFragment = new NewestFragment();//创建fragment 最新
        hotFragment = new HotFragment();//热门
        initFragment();//点击方法
    }

    private void initFragment() {
        //按钮的监听与fragment结合
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //碎片管理器
                FragmentTransaction transaction = manager.beginTransaction();
                switch (i) {
                    case R.id.mRb_newest://点击最新
                        transaction.show(newestFragment).hide(hotFragment);
                        break;

                    case R.id.mRb_hot://点击热门
                        transaction.show(hotFragment).hide(newestFragment);
                        break;

                }
                transaction.commit();//提交事物
            }
        });
    }

    @Override
    protected VideoPresenter createPersenter() {
        return new VideoPresenter(this);
    }

    @Override
    protected void initData() {
        persenter = new VideoPresenter(this);
        persenter.getVideo();//调用方法
    }

    @Override
    public void getVideoReturn(VideoBean result) {
        if (result.getData().getList().size() > 0) {//如果集合的长度大于0
            list_Video.addAll(result.getData().getList());

            newestFragment.setList(list_Video);//调用构造方法传入集合
            hotFragment.setList(list_Video);

            manager = getSupportFragmentManager();//碎片管理器
            fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(R.id.mFl, hotFragment)
                    .add(R.id.mFl, newestFragment)//显示最新
                    .hide(hotFragment).commit();//隐藏热门并提交事物
        }
    }
}
