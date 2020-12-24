package com.example.shipin.ui.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shipin.R;
import com.example.shipin.adapter.ShowVideoAdapter;
import com.example.shipin.adapter.VideoAdapter;
import com.example.shipin.base.BaseAdapter;
import com.example.shipin.base.BaseFragment;
import com.example.shipin.model.bean.VideoBean;
import com.example.shipin.presenter.VideoPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class NewsFragment extends BaseFragment<VideoPresenter> {

    @BindView(R.id.mRlv_newest)
    RecyclerView mRlv;

    List<VideoBean.DataBean.ListBean> list = new ArrayList<>();
    private VideoAdapter adapter;

    public void setList(List<VideoBean.DataBean.ListBean> list) {
        this.list = list;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        mRlv.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        adapter = new VideoAdapter(getActivity(),list);
        mRlv.setAdapter(adapter);

        adapter.addListClick(new BaseAdapter.IListClick() {
            @Override
            public void itemClick(int pos) {
                videoPopup(pos);//弹出来弹窗
            }
        });

    }

    @Override
    protected VideoPresenter createpersenter() {
        return null;
    }

    private void videoPopup(int pos) {
        View view = null;

        if(view == null){
            view = View.inflate(getActivity(),R.layout.video_popu,null);
        }

        PopupWindow window = null;
        if(window == null){
            window = new PopupWindow(view,-1,-2);
        }
        window.showAtLocation(mRlv, Gravity.CENTER,0,0);

        ShowVideoAdapter videoAdapter = null;
        if(videoAdapter == null){

            List<VideoBean.DataBean.ListBean> beanList = new ArrayList<>();
            for (int i = 0; i <list.size()-pos-1 ; i++) {
                beanList.add(list.get(i+pos));
            }

            RecyclerView mRec_video = view.findViewById(R.id.mRlv_popup_video);
            mRec_video.setLayoutManager(new LinearLayoutManager(getActivity()));
            ShowVideoAdapter showVideoAdapter = new ShowVideoAdapter(getActivity(), beanList);
            mRec_video.setAdapter(showVideoAdapter);

        }

        ShowVideoAdapter finalVideoAdapter = videoAdapter;
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                finalVideoAdapter.onDestroyVideo();
            }
        });


    }

    @Override
    protected void initData() {
        adapter.notifyDataSetChanged();
    }

}