package com.example.shipin.base;

public interface IBasePresenter<V extends IBaseView> {

    //绑定View
    void attachView(V view);
    //解除view绑定
    void unAttachView();

}
