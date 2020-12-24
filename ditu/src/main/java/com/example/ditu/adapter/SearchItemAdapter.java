package com.example.ditu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.baidu.mapapi.search.core.PoiInfo;
import com.example.ditu.R;

import java.util.List;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {
    private List<PoiInfo> poiList;
    private Context context;

    public SearchItemAdapter(List<PoiInfo> poiList, Context context) {
        this.poiList = poiList;
        this.context = context;
    }

    public MyClick myClick;

    public void setMyClick(MyClick myClick) {
        this.myClick = myClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.layout_search_item, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtName.setText(poiList.get(position).getName()+" "+poiList.get(position).getAddress());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=itemView.findViewById(R.id.txt_name);
        }
    }

    //定义回调接口
    public interface MyClick{
        void itemClick(int pos);
    }

}
