package com.example.kuangjia.adapter.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<D> extends RecyclerView.Adapter{

    Context context;
    //adapter的数据
    List<D> mData;
    //接口回调
    IListClick click;

    public void addListClick(IListClick click) {
        this.click = click;
    }

    public BaseAdapter(Context context, List<D> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //布局
        View view = View.inflate(context, getLayout(), null);
        //绑定视图给View
        VH vh = new VH(view);

        //接口回调
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接口回调的使用
                if(click != null){  //多条目点击也能获取
                    click.itemClick(vh.getLayoutPosition());    //获取下标
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindData(mData.get(position),(VH)holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected abstract int getLayout();
    protected abstract void bindData(D data,VH vh);

    //定义接口回调
    public interface IListClick{
        void itemClick(int pos);
    }

    public class VH extends RecyclerView.ViewHolder{
        //最轻量级的数组
        SparseArray views = new SparseArray();
        public VH(@NonNull View itemView) {
            super(itemView);
        }
        //查找item的view
        public View getViewById(int id){
            View view = (View) views.get(id);
            if(view == null){
                //添加id
                view = itemView.findViewById(id);
                views.append(id,view);
            }
            return view;
        }
    }

}
