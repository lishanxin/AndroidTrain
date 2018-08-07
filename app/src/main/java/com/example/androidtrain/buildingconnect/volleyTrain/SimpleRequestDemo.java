package com.example.androidtrain.buildingconnect.volleyTrain;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.androidtrain.R;

import org.json.JSONObject;

/**
 * Created by sx on 2018/8/7.
 */
//编写Volley支持的常用请求类型的Demo
    //常用类型有三：1：StringRequest/ImageRequest/JsonObjectRequest(JsonArrayRequest)
    //其中，StringRequest可查看SimpleVolley.java
public class SimpleRequestDemo {


    //请求一张图片：
    /**
     * ImageRequest 处理Url请求，并且返回Bitmap
     * ImageLoader 加载和缓存从网络上获取的图片。是大量ImageRequest的协调器。
     * NetworkImageView 在ImageLoader的基础上建立，在取回网络图片后，有效替换ImageView
     */
    Context mContext;
    TextView mTextView;
    ImageView mImageView;
    ImageLoader mImageLoader;
    NetworkImageView mNetworkImageView;

    String url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533650934978&di=e60118fe7c80d8ae21c36025e7b849e0&imgtype=0&src=http%3A%2F%2Ffd.topitme.com%2Fd%2F58%2Fb6%2F1112124071636b658do.jpg";
    String url2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533655543151&di=aa5dfb73962ddfe1df52f81be0e86cfa&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201504%2F19%2F20150419H1905_eViFR.png";
    String jsonUrl = "http://json_test";
    public SimpleRequestDemo(Context context, ImageView imageView, NetworkImageView networkImageView){
        mContext = context;
        mImageView = imageView;
        mNetworkImageView = networkImageView;
    }


    public void testImageRequest(){
        ImageRequest request = new ImageRequest(url1,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        mImageView.setImageBitmap(response);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mImageView.setImageResource(R.drawable.jiguang_socialize_wxcircle);
                    }
                });

        MyRequeestQueue.MySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    public void testImageLoader(){
        //Get the ImageLoader through your singleton class.
        mImageLoader = MyRequeestQueue.MySingleton.getInstance(mContext).getImageLoader();
        mImageLoader.get(url1, ImageLoader.getImageListener(mImageView, R.drawable.jiguang_socialize_qq, R.drawable.jiguang_socialize_wechat));
    }

    public void testNetworkImageView(){
        //Get the ImageLoader through your sinleton class.
        mImageLoader = MyRequeestQueue.MySingleton.getInstance(mContext).getImageLoader();

        //Set the URL of the image that should be loaded into this view, and specify the ImageLoader
        //that will be used to make the request
        mNetworkImageView.setImageUrl(url2, mImageLoader);
    }

    public void testJsonObjectRequest(){
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, jsonUrl,new Response.Listener<JSONObject>(){

                    /**
                     * Called when a response is received.
                     *
                     * @param response
                     */
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener(){

                    /**
                     * Callback method that an error has been occurred with the
                     * provided error code and optional user-readable message.
                     *
                     * @param error
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        // Access the RequestQueue through your singleton class.
        MyRequeestQueue.MySingleton.getInstance(mContext).addToRequestQueue(jsObjRequest);

    }
}
