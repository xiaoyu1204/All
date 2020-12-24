package com.example.kuangjia.presenter.base;

import com.example.kuangjia.model.base.IBaseModel;
import com.example.kuangjia.view.base.IBaseView;

import java.lang.ref.WeakReference;

//P层的基类
public class BasePresenter<V extends IBaseView> implements IBasePresenter<V>{

    protected V mView;

    //通过弱引用把V层关联
    WeakReference<V> weakReference;
    //关联M层
    IBaseModel model;

    //绑定
    @Override
    public void attachView(V view) {    //括号里面改成V类型
        weakReference = new WeakReference<V>(view);
        mView = weakReference.get();
    }
    //解除绑定
    @Override
    public void unAttachView() {
        mView = null;
        if(model != null){
            //清除缓存
            model.clear();
        }
    }


}
