package com.apex.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sx on 2018/11/20.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {

    View mRoot;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //初始化参数
        initArgs(getArguments());
    }

    /**
     * 得到当前界面的资源文件ID
     * @return 资源文件ID
     */
    protected abstract int getContentLayoutId();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mRoot == null){
            //获取资源ID
            int layoutId = getContentLayoutId();
            View root = inflater.inflate(layoutId, container, false);
            initWidget(root);
            mRoot = root;
        }else {
            if (mRoot.getParent() != null){
                //把当前root从其父空间中移除
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //组件创建完成后，初始化数据
        initData();
    }

    /**
     * 初始化相关参数
     * @param bundle
     */
    protected void initArgs(Bundle bundle){

    }

    /**
     * 初始化控件
     * @param root
     */
    protected void initWidget(View root){
        //ButterKnife绑定操作
    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    /**
     * 返回按键触发时调用
     * @return 返回true时表示此处已经处理完毕返回逻辑，Activity不用自己finish。
     * 返回false表示此处未处理返回逻辑，Activity自己进行处理。
     */
    public boolean onBackPressed(){
        return false;
    }
}
