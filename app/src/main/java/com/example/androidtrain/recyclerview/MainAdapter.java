package com.example.androidtrain.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidtrain.R;

import java.util.List;

/**
 * Created by sx on 2018/6/12.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private List<String> mList;

    public MainAdapter(List<String> list){
        mList = list;
    }

    //创建子项item的布局
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_main_adapter_item, parent, false);
        return new ViewHolder(view);
    }

    //给控件设置数据
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_main.setText(mList.get(position));
    }


   //子项item的数量
    @Override
    public int getItemCount() {
        return mList.size();
    }

    //初始化子项控件
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_main;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_main = itemView.findViewById(R.id.recycler_view_text_item);
        }
    }
}
