package com.example.kuangjia.ui.base;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kuangjia.R;
import com.example.kuangjia.presenter.base.BasePresenter;
import com.example.kuangjia.view.IHome;
import com.example.kuangjia.view.base.IBaseView;

//V层基类
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView {

    //P层关联
    protected P persenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //需要界面View
        setContentView(getLayout());
        //初始化界面
        initView();

        //初始化P层
        persenter = createpersenter();
        if(persenter != null){
            persenter.attachView(this);
        }

        //初始化数据
        initData();
    }

    //定义一个获取当前界面的方法 由子类提供
    protected abstract int getLayout();
    //初始化界面
    protected abstract void initView();
    //初始化P层的方法
    protected abstract P createpersenter();
    //初始化数据
    protected abstract void initData();

    //错误的方法
    @Override
    public void tips(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
    }

    //界面销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放关联的V的引用
        if(persenter != null){
            persenter.unAttachView();
        }
    }

}