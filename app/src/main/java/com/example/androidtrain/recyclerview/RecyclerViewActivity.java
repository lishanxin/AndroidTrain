package com.example.androidtrain.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.androidtrain.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mMainRecyclerView;
    private List<String> mList;

    private RecyclerView.LayoutManager mLayoutManager;

    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        initView();
        initData();
        initAdapter();
    }

    //初始化控件
    private void initView() {
        mMainRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
    }

    //初始化数据
    private void initData(){
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            mList.add("纵向和横向滑动");
            mList.add("纵向和横向瀑布流");
            mList.add("添加头布局和角布局");
            mList.add("下拉刷新和上拉加载");
            mList.add("多布局页面");
            mList.add("滑动删除");
            mList.add("点击事件");
            mList.add("添加空布局");
            mList.add("添加分割线");
        }
    }

    //初始化Adapter
    private void initAdapter(){
        //设置布局管理器
        mMainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter mainAdapter = new MainAdapter(mList);
        mMainRecyclerView.setAdapter(mainAdapter);
    }

    public void changeLayoutManager(View view) {
    }
}
