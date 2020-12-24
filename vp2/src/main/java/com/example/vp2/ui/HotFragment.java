package com.example.vp2.ui;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.example.vp2.R;
import com.example.vp2.adapter.ShowVideoAdapter;
import com.example.vp2.adapter.VideoAdapter;
import com.example.vp2.base.BaseAdapter;
import com.example.vp2.base.BaseFragment;
import com.example.vp2.model.VideoBean;
import com.example.vp2.presenter.VideoPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HotFragment extends BaseFragment<VideoPresenter> {
    @BindView(R.id.mRlv_hot)
    RecyclerView mRlv;

    private List<VideoBean.DataBean.ListBean> list = new ArrayList<>();
    private VideoAdapter adapter;
    private View view = null;

    public void setList(List<VideoBean.DataBean.ListBean> list) {
        this.list = list;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_hot;
    }

    @Override
    protected VideoPresenter createPersenter() {
        return null;
    }

    @Override
    public void initView() {
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

        ArrayList<VideoBean.DataBean.ListBean> beans = new ArrayList<>();
        for(int i=0;i<list.size()-pos-1;i++){
            beans.add(list.get(i+pos));
        }

        ViewPager2 viewpager2 = view.findViewById(R.id.mVp2);
        ShowVideoAdapter vieewPager2Adapter = new ShowVideoAdapter(getActivity(), beans);
        viewpager2.setAdapter(vieewPager2Adapter);
        viewpager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        window.showAtLocation(mRlv, Gravity.CENTER,0,0);

//        RecyclerView mRec_video = view.findViewById(R.id.mRlv_popup_video);
//        mRec_video.setLayoutManager(new LinearLayoutManager(getActivity()));
//        ShowVideoAdapter videoAdapter = new ShowVideoAdapter(getActivity(), list);
//        mRec_video.setAdapter(videoAdapter);

//        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
////                videoAdapter.onDestroyVideo();//调用释放视频资源的方法
//            }
//        });
    }

    @Override
    public void initData() {
        adapter.notifyDataSetChanged();
    }
}
