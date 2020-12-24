package com.example.shipin.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements  IBaseView{

    protected P persenter;
    private Context mContext;
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(container.getContext()).inflate(getLayout(),null);
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        bind = ButterKnife.bind(this, view);
        persenter = createpersenter();
        if(persenter != null){
            persenter.attachView(this);
        }

        initView();
        initData();

    }

    protected abstract int getLayout();
    //初始化布局
    protected abstract void initView();
    protected abstract P createpersenter();
    //绑定数据
    protected abstract void initData();

    @Override
    public void tips(String tip) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(bind != null){
            bind.unbind();
        }
        if(persenter != null){
            persenter.unAttachView();
        }

    }
}
