package com.example.shipin.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView{

    //P层关联
    protected P persenter;
    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //需要界面View
        setContentView(getLayout());
        bind = ButterKnife.bind(this);
        //初始化界面
        initView();
        //P层
        persenter = createPresenter();
        if(persenter != null){
            persenter.attachView(this);
        }
        //初始化界面数据
        initData();

    }

    //定义一个获取当前界面的方法  由子类提供的
    protected abstract int getLayout();
    //初始化界面
    protected abstract void initView();
    //初始化p层的方法
    protected abstract P createPresenter();
    //初始化界面数据
    protected abstract void initData();

    @Override
    public void tips(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }

    //界面销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null){  //解除
            bind.unbind();
        }
        //释放p关联的v的引用
        if(persenter != null){
            persenter.unAttachView();
        }

    }

}
