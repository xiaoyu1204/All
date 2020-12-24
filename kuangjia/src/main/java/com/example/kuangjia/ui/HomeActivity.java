package com.example.kuangjia.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuangjia.R;
import com.example.kuangjia.adapter.HomeAdapter;
import com.example.kuangjia.model.bean.HomeBean;
import com.example.kuangjia.presenter.HomePresenter;
import com.example.kuangjia.ui.base.BaseActivity;
import com.example.kuangjia.view.IHome;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//V层的实现类
public class HomeActivity extends BaseActivity<HomePresenter> implements IHome.View {

    //指明接口P层类型
    IHome.Persenter presenter;
    @BindView(R.id.rlv)
    RecyclerView rlv;
    private Unbinder bind;
    private HomeAdapter adapter;
    private List<HomeBean.DataBean.BrandListBean> mBean;

    @Override
    protected int getLayout() {
        return R.layout.activity_base;
    }

    @Override
    protected void initView() {

        bind = ButterKnife.bind(this);

        rlv.setLayoutManager(new LinearLayoutManager(this));
        rlv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mBean = new ArrayList<>();

        adapter = new HomeAdapter(this,mBean);
        rlv.setAdapter(adapter);

    }

    @Override
    protected HomePresenter createpersenter() {
        return new HomePresenter(this);
    }

    @Override
    protected void initData() {
        presenter = new HomePresenter(this);
        persenter.getHome();
    }

    @Override
    public void getHomeReturn(HomeBean result) {
        mBean.clear();
        List<HomeBean.DataBean.BrandListBean> brandList = result.getData().getBrandList();
        mBean.addAll(brandList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

}
