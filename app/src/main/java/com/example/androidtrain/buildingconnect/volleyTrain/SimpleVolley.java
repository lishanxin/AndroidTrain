package com.example.androidtrain.buildingconnect.volleyTrain;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by lizz on 2018/8/6.
 */

/**
 * 使用 Volley 的方式是，创建一个 RequestQueue 并传递 Request 对象给它。RequestQueue
 * 管理用来执行网络操作的工作线程，从缓存中读取数据，写数据到缓存，并解析 Http 的响
 * 应内容。请求解析原始的响应数据，Volley 会把解析完的响应数据分发给主线程。
 */
public class SimpleVolley {

    private Context mContext;
    TextView mTextView;
    public static final String TAG = "MyTag";

    StringRequest mStringRequest;//Assume this exists;
    RequestQueue mRequestQueue;//Assume this exists;

    public SimpleVolley(View view, Context context){
        mTextView = (TextView) view;
        mContext = context;
    }

    public void sendRequest(){

        //初始化RequestQueues
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://www.baidu.com";

        //从提供的url地址中获取字符串返回值；Volley总是将解析后的数据返回至主线程中
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTextView.setText("Response is: " + response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });

        //将网络请求添加进请求队列中RequestQueue
        //当你添加一个请求到队列中，它将被缓存线程所捕获并触发：如果这个请求可以被缓存处理，
        // 那么会在缓存线程中执行响应数据的解析并返回到主线程。如果请求不能被缓存所处理，它
        // 会被放到网络队列中。
        queue.add(stringRequest);
    }


    //跟踪所有已经发送的请求，以便在需要的时候可以取消他们
    //有一个简便的方法：你可以为每一个请求对象都绑定一个tag对象。然后你可以使用这个
    // tag来提供取消的范围。例如，你可以为你的所有请求都绑定到执行的Activity上，然后
    // 你可以在onStop()方法执行requestQueue.cancelAll(this) 。
    public void addForCancelRequest(){
        //在每一个跟踪的请求中设置一个tag，作为标记
        mStringRequest.setTag(TAG);
        mRequestQueue.add(mStringRequest);
    }

    //可以在活动的onStop监听方法中写入此方法，以取消标记的网络请求
    public void cancelRequest(){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(TAG);
        }
    }
}
