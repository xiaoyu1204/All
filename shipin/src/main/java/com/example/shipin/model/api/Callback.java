package com.example.shipin.model.api;

public interface Callback<T> {

    void success(T t);
    void fail(String msg);

}
