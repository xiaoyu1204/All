package com.example.kuangjia.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kuangjia.R;
import com.example.kuangjia.adapter.base.BaseAdapter;
import com.example.kuangjia.model.bean.HomeBean;
import com.example.kuangjia.utils.TxUtils;

import java.util.List;

public class HomeAdapter extends BaseAdapter {

    Context context;

    public HomeAdapter(Context context, List mData) {
        super(context, mData);
        this.context = context;
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_item;
    }

    @Override
    protected void bindData(Object data, VH vh) {
        HomeBean.DataBean.BrandListBean bean = (HomeBean.DataBean.BrandListBean) data;

        ImageView img= (ImageView) vh.getViewById(R.id.item_img);
        TextView txtTag = (TextView) vh.getViewById(R.id.item_tv);

        //赋值    img
        Glide.with(context).load(bean.getPic_url()).into(img);
        //赋值    txtTag
        TxUtils.setTextView(txtTag,bean.getName());

    }

}
