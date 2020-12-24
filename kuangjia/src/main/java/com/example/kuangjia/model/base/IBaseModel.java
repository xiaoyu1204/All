package com.example.kuangjia.model.base;

import io.reactivex.disposables.Disposable;

public interface IBaseModel {

    //把当前的网络请求添加到缓存
    void addDisposable(Disposable disposable);
    //取消还未进行的网络请求
    void clear();

}
