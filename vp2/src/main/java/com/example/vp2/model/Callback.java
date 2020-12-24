package com.example.vp2.model;

public interface Callback<T> {
    void fail(String msg);

    void success(T t);
}
