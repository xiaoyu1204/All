package com.example.ditu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ditu.R;

import java.util.List;

public class MapTypeAdapter extends RecyclerView.Adapter<MapTypeAdapter.ViewHodler> {
    private List<String> list;
    private Context context;

    public MapTypeAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public MyClick myClick;

    public void setMyClick(MyClick myClick) {
        this.myClick = myClick;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.map_type_item, null);
        return new ViewHodler(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, final int position) {
        holder.txt_type.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        TextView txt_type;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            txt_type=itemView.findViewById(R.id.txt_type);
        }
    }

    //定义回调接口
    public interface MyClick{
        void itemClick(int pos);
    }

}
