package com.example.kuangjia.presenter;

import com.example.kuangjia.model.HomeModel;
import com.example.kuangjia.model.api.Callback;
import com.example.kuangjia.model.bean.HomeBean;
import com.example.kuangjia.presenter.base.BasePresenter;
import com.example.kuangjia.ui.HomeActivity;
import com.example.kuangjia.view.IHome;

//P层的实现类
public class HomePresenter extends BasePresenter<IHome.View> implements IHome.Persenter {

    IHome.View view;
    IHome.Model model;

    //构造参数
    public HomePresenter(IHome.View view) {
        this.view = view;       //关联对应的V层接口
        model = new HomeModel();        //创建M层的实现类对象,并且赋值
    }

    @Override
    public void getHome() {     //P层接口实现的方法
        //通过Model层调用对应的网络请求接口
        model.loadHome(new Callback() {
            //请求成功
            @Override
            public void success(Object o) {
                if(view != null){
                    view.getHomeReturn((HomeBean) o);
                }
            }
            //请求错误
            @Override
            public void fail(String msg) {
                if(view != null){
                    view.tips(msg);
                }
            }
        });
    }


    @Override
    public void unAttachView() {
        super.unAttachView();
        //释放当前页面还未完成的网络请求
        if(model != null){
            model.clear();
        }
    }
}
