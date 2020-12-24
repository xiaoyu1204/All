package com.example.kuangjia.view;

import com.example.kuangjia.model.api.Callback;
import com.example.kuangjia.model.base.IBaseModel;
import com.example.kuangjia.model.bean.HomeBean;
import com.example.kuangjia.presenter.base.IBasePresenter;
import com.example.kuangjia.view.base.IBaseView;

//接口契约类
public interface IHome {

    interface View extends IBaseView{
        //定义一个被实现的View层接口方法
        void getHomeReturn(HomeBean result);
    }

    interface Persenter extends IBasePresenter<View> {
        //定义一个V层调用的接口
        void getHome();
    }

    interface Model extends IBaseModel {
        //定义一个加载的接口方法   被P层
        void loadHome(Callback callback);
    }


}
