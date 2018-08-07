package com.example.androidtrain.buildingconnect.volleyTrain;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Created by lizz on 2018/8/7.
 */

public class MyOwnRequest extends Request<String> {

    Response.Listener<String> mListener;

    public MyOwnRequest(int method, String url, Response.Listener listener , Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String json;
        try {
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            json = new String(response.data);
        }
        return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        if (mListener != null){
            mListener.onResponse(response);
        }
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        mListener = null;
    }
}
