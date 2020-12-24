package com.example.vp2.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IBaseView {
    Unbinder unbinder;
    protected P persenter;
    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(getLayout(),null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        unbinder = ButterKnife.bind(this,view);
        persenter = createPersenter();
        if(persenter != null){
            persenter.attachView(this);
        }

        initView();
        initData();
    }

    public abstract int getLayout();

    protected abstract P createPersenter();
    //初始化布局
    public abstract void initView();
    //绑定数据
    public abstract void initData();

    @Override
    public void tips(String tip) {
        Log.e("TAG",tip);
    }

    @Override
    public void loading(int visible) {

    }

    @Override
    public void showToast(String msg, int time) {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
        if(persenter != null){
            persenter.unAttachView();
        }
    }
}
