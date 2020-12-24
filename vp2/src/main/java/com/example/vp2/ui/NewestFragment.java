package com.example.vp2.ui;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.example.vp2.R;
import com.example.vp2.adapter.VideoAdapter;
import com.example.vp2.adapter.ViewPagerAdapter;
import com.example.vp2.base.BaseAdapter;
import com.example.vp2.base.BaseFragment;
import com.example.vp2.model.VideoBean;
import com.example.vp2.presenter.VideoPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class NewestFragment extends BaseFragment<VideoPresenter> {

    @BindView(R.id.mRlv_newest)
    RecyclerView mRlv;
    private View view = null;
    private PopupWindow window = null;

    private VideoAdapter adapter;
    private List<VideoBean.DataBean.ListBean> list = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;

    public void setList(List<VideoBean.DataBean.ListBean> list) {
        this.list = list;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_newest;
    }

    @Override
    protected VideoPresenter createPersenter() {
        return null;
    }

    @Override
    public void initView() {
        mRlv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new VideoAdapter(getActivity(), list);
        mRlv.setAdapter(adapter);

        adapter.addListClick(new BaseAdapter.IListClick() {
            @Override
            public void itemClick(int pos) {
                videoPopup(pos);//弹出来弹窗
            }
        });

    }

    private void videoPopup(int pos) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.video_popu, null);
        }
        if (window == null) {
            window = new PopupWindow(view, -1, -2);
        }


        List<VideoBean.DataBean.ListBean> beanList = new ArrayList<>();
        for (int i = 0; i < list.size() - pos - 1; i++) {
            beanList.add(list.get(i + pos));
        }

        ViewPager2 mVp2 = view.findViewById(R.id.mVp2);
        viewPagerAdapter = new ViewPagerAdapter(getActivity(), beanList);
        mVp2.setAdapter(viewPagerAdapter);
        //默认是水平方向ORIENTATION_HORIZONTAL,垂直是ORIENTATION_VERTICAL
        //设置垂直方向属性
        mVp2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        window.showAtLocation(mRlv, Gravity.CENTER, 0, 0);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                viewPagerAdapter.onDestroyVideo();//销毁视频
            }
        });
    }

    @Override
    public void initData() {
        adapter.notifyDataSetChanged();
    }
}
