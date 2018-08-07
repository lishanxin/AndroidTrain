package com.example.androidtrain.buildingconnect.volleyTrain;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by sx on 2018/8/7.
 */
//自定义RequestQueue.如果我们仅仅是想做一个单次的请求并且不想要线程池一直保留，我们可以通
// 过使用在前面一课：发送一个简单的请求（Sending a Simple Request）文章中提到的 Volley.
// newRequestQueue() 方法，在任何需要的时刻创建 RequestQueue，然后在我们的响应回调里面执
// 行 stop() 方法来停止操作。但是更通常的做法是创建一个 RequestQueue 并设置为一个单例。
public class MyRequeestQueue {
    //一个 RequestQueue 需要两部分来支持它的工作：一部分是网络操作，
    // 用来传输请求，另外一个是用来处理缓存操作的 Cache
    Context mContext;
    RequestQueue mRequestQueue;

    HttpStack stack;

    public MyRequeestQueue(Context context){
        mContext = context;
    }

    public void initialRequestQueue(){
        //Instantiate the cache
        Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024);

        //Set up the network to use HttpURLConnection as the Http Client.
        Network network = new BasicNetwork(new HurlStack());

        //Instantiate the RequestQueue with the cache and network
        mRequestQueue = new RequestQueue(cache, network);

        //Start the queue
        mRequestQueue.start();

        String url = "https://www.baidu.com";

        //设置请求参数，并处理得到的数据
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Do something with the response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });

        // Add the request to the RequestQueue
        mRequestQueue.add(stringRequest);
    }

    private void chooseHttpCilent(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            //...use HttpURLConnection for stack.
        }else {
            //...use AndroidHttpClient for stack
        }
        Network network = new BasicNetwork(stack);
    }

   // 如果我们仅仅是想做一个单次的请求并且不想要线程池一直保留，我们可以通
// 过使用在前面一课：发送一个简单的请求（Sending a Simple Request）文章中提到的 Volley.
// newRequestQueue() 方法，在任何需要的时刻创建 RequestQueue，然后在我们的响应回调里面执
// 行 stop() 方法来停止操作。但是更通常的做法是创建一个 RequestQueue 并设置为一个单例。
    //一个关键的概念是 RequestQueue 必须使用 Application context 来实例化，而不是 Activity
    // context。这确保了 RequestQueue 在我们 app 的生命周期中一直存活，而不会因为 activity
    // 的重新创建而被重新创建(例如，当用户旋转设备时)。

    /**
     *Demo
     *  //Get a RequestQueue
     *  RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
     *   ...
     *  // Add a request (in this example, called stringRequest) to your RequestQueue
     *  MySingleton.getInstance(this).addToRequestQueue(stringRequest);
     */
    public static class MySingleton{
        private static MySingleton mInstance;
        private RequestQueue mRequestQueue;
        private ImageLoader mImageLoader;
        private static Context mContext;

        private MySingleton(Context context){
            mContext = context;
            mRequestQueue = getRequestQueue();

            mImageLoader = new ImageLoader(mRequestQueue,
                    new ImageLoader.ImageCache() {
                        private final LruCache<String,Bitmap>
                            cache = new LruCache<>(20);
                        @Override
                        public Bitmap getBitmap(String url) {
                            return cache.get(url);
                        }

                        @Override
                        public void putBitmap(String url, Bitmap bitmap) {
                            cache.put(url, bitmap);
                        }
                    });
        }

       public static synchronized MySingleton getInstance(Context context){
            if (mInstance == null){
                mInstance = new MySingleton(context);
            }
            return mInstance;
        }

       private RequestQueue getRequestQueue() {
           if(mRequestQueue == null){
               //getApplicationContext() is key , it keeps you from leaking the
               // Activity or BroadcastReceiver if someone passes one in.
               mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
           }
           return mRequestQueue;
       }

       public <T> void addToRequestQueue(Request<T> request){
           getRequestQueue().add(request);
       }

       public ImageLoader getImageLoader(){
           return mImageLoader;
       }
   }
}
