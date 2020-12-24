package com.example.vp2.base;

public interface IBasePersenter<V extends IBaseView>  {
    //绑定View
    void attachView(V view);
    //解除View绑定
    void unAttachView();
}
