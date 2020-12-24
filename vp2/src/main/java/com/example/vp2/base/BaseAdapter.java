package com.example.vp2.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<D> extends RecyclerView.Adapter {

    List<D> mData; //adapter的数据
    protected Context context;
    IListClick click;

    public BaseAdapter( Context context,List<D> Data) {
        this.context = context;
        this.mData = Data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(getLayout(),parent,false);
        final VH vh=new VH(view);//绑定视图给VH
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //接口回调的调用
                if(click!=null){//多条目点击也能获取
                    click.itemClick(vh.getLayoutPosition());
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindData(mData.get(position), (VH) holder);
    }

    @Override
    public int getItemCount() {
        if (mData == null || mData.size() == 0) {
            return 0;
        }
        return mData.size();
    }


    protected abstract int getLayout();
    protected abstract void bindData(D data,VH vh);

    public void addListClick(IListClick click){
        this.click = click;
    }
    //定义回调接口
    public interface IListClick{
        void itemClick(int pos);
    }

    public class VH extends RecyclerView.ViewHolder{
        SparseArray views = new SparseArray();
        public VH(@NonNull View itemView) {
            super(itemView);
        }
        public View getViewById(int id){//查找item的view
            View view = (View) views.get(id);
            if(view == null){
                view = itemView.findViewById(id);
                views.append(id,view);
            }
            return view;
        }
    }
}
