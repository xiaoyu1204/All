package com.example.shipin.base;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    protected V mView;
    //通过弱引用把V层关联
    private WeakReference<V> vWeakReference;

    IBaseModel model;

    //绑定
    @Override
    public void attachView(V view) {
        vWeakReference = new WeakReference<>(view);
        mView = vWeakReference.get();
    }

    //解除绑定
    @Override
    public void unAttachView() {
        mView=null;
        if(model!=null){
            //清除缓存
            model.clear();
        }
    }

}
