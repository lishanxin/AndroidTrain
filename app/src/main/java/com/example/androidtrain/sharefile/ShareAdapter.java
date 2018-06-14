package com.example.androidtrain.sharefile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidtrain.R;

import java.io.File;
import java.util.List;

/**
 * Created by sx on 2018/6/12.
 */

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder>{

    private List<File> mList;

    public ShareAdapter(List<File> list){
        mList = list;
    }

    protected OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onClick(ShareAdapter.ViewHolder viewHolder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    //创建子项item的布局
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_main_adapter_item, parent, false);
        return new ViewHolder(view);
    }

    //给控件设置数据
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_main.setText(mList.get(position).getName());
        if (mOnItemClickListener != null){
            holder.tv_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder, holder.getLayoutPosition());
                }
            });
        }
    }


   //子项item的数量
    @Override
    public int getItemCount() {
        return mList.size();
    }

    //初始化子项控件
    static class ViewHolder extends RecyclerView.ViewHolder{

        Button tv_main;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_main = itemView.findViewById(R.id.recycler_view_button_item);
        }
    }
}
