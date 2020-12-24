package com.example.shipin.ui.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

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

public class HotFragment extends BaseFragment<VideoPresenter> {

    @BindView(R.id.mRlv_hot)
    RecyclerView mRlv;

    private List<VideoBean.DataBean.ListBean> list = new ArrayList<>();
    private VideoAdapter adapter;

    public void setList(List<VideoBean.DataBean.ListBean> list) {
        this.list = list;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initView() {
        mRlv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VideoAdapter(getActivity(), list);
        mRlv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.addListClick(new BaseAdapter.IListClick() {
            @Override
            public void itemClick(int pos) {
                videoPopup(pos);//弹出来弹窗
            }
        });
    }

    private void videoPopup(int pos) {
        View view = View.inflate(getActivity(),R.layout.video_popu,null);
        PopupWindow window = new PopupWindow(view, -1, -2);
        window.showAtLocation(mRlv, Gravity.CENTER,0,0);

        RecyclerView mRec_video = view.findViewById(R.id.mRlv_popup_video);
        mRec_video.setLayoutManager(new LinearLayoutManager(getActivity()));
        ShowVideoAdapter videoAdapter = new ShowVideoAdapter(getActivity(), list);
        mRec_video.setAdapter(videoAdapter);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                videoAdapter.onDestroyVideo();//调用释放视频资源的方法
            }
        });
    }

    @Override
    protected VideoPresenter createpersenter() {
        return null;
    }

    @Override
    protected void initData() {
        adapter.notifyDataSetChanged();
    }

}