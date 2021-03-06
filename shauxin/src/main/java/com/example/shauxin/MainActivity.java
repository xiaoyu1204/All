package com.example.shauxin;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.smart)
    SmartRefreshLayout smart;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        smartRefreshView();
    }

    /**
     * 1，刷新控件的监听
     */
    private void smartRefreshView() {
        smart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                //延迟3秒关闭
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadMore();
                    }
                }, 3000);

            }

            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                //2，刷新完成关闭，正常情况是请求接口完成关闭
                //3,如果需要在网络请求结束后关闭，则调用
//                smart.finishRefresh();
//                smart.finishLoadMore();
                refreshLayout.finishRefresh();
                refreshLayout.setNoMoreData(true);
            }
        });
    }


    @OnClick(R.id.text)
    public void onViewClicked() {
        smart.finishRefresh();
    }
}
