package com.example.kuangjia.model.api;

public interface Callback<T> {

    void success(T t);
    void fail(String msg);

}
