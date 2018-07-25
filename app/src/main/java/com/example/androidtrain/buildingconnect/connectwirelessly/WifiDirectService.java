package com.example.androidtrain.buildingconnect.connectwirelessly;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizz on 2018/7/25.
 */

//创建并提供一个本地服务，当本地服务被成功注册，系统将自动回复所有来自附近的服务发现请求。
public class WifiDirectService {

    private static final String TAG = "WifiP2pService";
    private static int SERVER_PORT = 8081;

    WifiP2pManager.Channel mChannel;

    WifiP2pManager mManager;

    public WifiDirectService(WifiP2pManager manager, WifiP2pManager.Channel channel) {
        mManager = manager;
        mChannel = channel;
    }

    //创建本地服务三步骤
    //1：新建 WifiP2pServiceInfo 对象
    //2：加入相应服务的详细信息
    //3：调用 addLocalService() 为服务发现注册本地服务

    public  void startRegistration(){
        //创建字符串map，并存入服务信息
        Map record = new HashMap();
        record.put("listenport", String.valueOf(SERVER_PORT));
        record.put("buddyname", "John Doe" + (int)(Math.random() * 1000));
        record.put("available", "visible");

        //创建ServiceInfo对象，并存入信息
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_testLSX", "_presence._tcp", record);

        //注册本地WifiP2P服务
        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "DirectService is onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "DirectService is onFailure");
            }
        });
    }

}
