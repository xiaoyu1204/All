package com.example.kuangjia.model;

import com.example.kuangjia.model.api.Callback;
import com.example.kuangjia.model.base.BaseModel;
import com.example.kuangjia.model.bean.HomeBean;
import com.example.kuangjia.net.CommonSubscriber;
import com.example.kuangjia.net.HttpManager;
import com.example.kuangjia.utils.RxUtils;
import com.example.kuangjia.view.IHome;

//M层的实现类
public class HomeModel extends BaseModel implements IHome.Model {

    @Override
    public void loadHome(Callback callback) {
        addDisposable(      //把请求对象添加到对象池
                HttpManager.getInstance().getApiService()
                .gethome()
                .compose(RxUtils.rxScheduler())
                .subscribeWith(new CommonSubscriber<HomeBean>(callback){
                    @Override
                    public void onNext(HomeBean homeBean) {
                        //通过HttpManager调用对应的网络请求
                        callback.success(homeBean);
                    }
                })
        );      //产生一个网络请求disposable对象
    }

}
