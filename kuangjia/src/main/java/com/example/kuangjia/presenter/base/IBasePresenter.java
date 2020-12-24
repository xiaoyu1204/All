package com.example.kuangjia.presenter.base;

import com.example.kuangjia.view.base.IBaseView;

public interface IBasePresenter<V extends IBaseView> {

    //绑定View
    void attachView(V view);
    //解除绑定View
    void unAttachView();

}
