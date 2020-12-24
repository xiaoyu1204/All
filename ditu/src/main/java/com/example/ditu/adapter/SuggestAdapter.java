package com.example.ditu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.example.ditu.R;

import java.util.List;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.ViewHolder>{
    private List<SuggestionResult.SuggestionInfo> list;
    private Context context;

    public SuggestAdapter(List<SuggestionResult.SuggestionInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public MyClick myClick;

    public void setMyClick(MyClick myClick) {
        this.myClick = myClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.layout_suggest_item, null);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtName.setText(list.get(position).getKey());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_type);
            txtName=itemView.findViewById(R.id.txt_name);
        }
    }

    //定义回调接口
    public interface MyClick{
        void itemClick(int pos);
    }
}
