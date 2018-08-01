package com.example.androidtrain.buildingconnect.connectwirelessly;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.R;
import com.example.androidtrain.media.ControlCameraActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

//两个设备需要在同一个局域网内才能够发现对方
public class ConnectNSDActivity extends BaseActivity {

    private static final String TAG = "ConnectWireless";
    private static final String SERVICE_TYPE = "_http._tcp.";

    ServerSocket mServerSocket;

    NsdServiceInfo mService;

    int mLocalPort;

    NsdManager.RegistrationListener mRegistrationListener;

    NsdHelper mNsdHelper;

    String mServiceName;
    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolveListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_wireless);
    }

    //注册自己的NSD服务，让其他设备可以发现
    public void doMyselfService(){
        initializeServerSocket();
        initializeRegistrationListener();
        registerService(mLocalPort);
    }

    //如果使用的是 socket，那么我们可以将端口设置为 0，来初始化 socket 到任意可用的端口。
    public void initializeServerSocket(){
        try {
            //设置port为0，表示端口自动分配
            mServerSocket = new ServerSocket(0);

            //Store the chosen port
            mLocalPort = mServerSocket.getLocalPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //注册自己的服务，以便局域网内的其他设备能够发现我们的服务，
    //并决定是否连接到我们所提供的服务

    /**
     *
     * @param port 不要使用硬编码来定义端口，因为可能与其他应用产生冲突。
     *             可以使用方法来获取下一个可用的端口。
     */
    public void registerService(int port){
        //创建NsdServiceInfo对象，并发出
        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        //Name若与同一网络中的其他服务Name冲突，将会变化
        serviceInfo.setServiceName("NsdChatTrain");

        //指定应用使用的协议和传输层
        serviceInfo.setServiceType("_http._tcp.");
        serviceInfo.setPort(port);

        //注册服务
        mNsdManager = (NsdManager)getSystemService(Context.NSD_SERVICE);
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    //监听：通知应用程序服务注册和注销的成功或者失败。
    public void initializeRegistrationListener(){
        mRegistrationListener = new NsdManager.RegistrationListener(){
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                //由于若有服务名冲突，android会自动修改服务名，所以在注册成功后，
                //获取最终的服务名。
                mServiceName = serviceInfo.getServiceName();
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

            }
        };
    }

    //创建发现网络中的服务
    public void startDiscovery(){
        initializeDiscoveryListener();
        initializeResolveListener();
        startDiscoverServices();
    }

    //服务发现需要两步骤：一：用相应的回调函数设置发现监听器（Discover Listener），
    // 二：调用 discoverServices() 这个异步API。
    //NSD API 通过使用此接口中的方法通知用户程序发现何时开始、何时失败以及何时
    // 找到可用服务和何时服务丢失（丢失意味着“不再可用”）

    //一：用相应的回调函数设置发现监听器（Discover Listener），
    public void initializeDiscoveryListener(){
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE) ;
        //初始化一个新的DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener(){
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code: " + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code: " + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            //Called as soon as service discovery begins
            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                //当发现服务时，调用这个接口。此时还无法获取服务的host与port，需要等确定连接该服务时才能获取
                Log.d(TAG, "Service discovery success " + serviceInfo);
                //1：比较找到服务的名称与本地服务的名称，判断设备是否获得自己的（合法的）广播。
                if (!serviceInfo.getServiceType().equals(SERVICE_TYPE)){
                    //Service_type是一个包含协议与传输层的字符串
                    Log.d(TAG, "Unknown Service Type: " + serviceInfo.getServiceType());
                }
                //2：检查服务的类型，确认这个类型我们的应用是否可以接入。
                else if (serviceInfo.getServiceName().equals(mServiceName)){
                    //判断所连接的服务是什么样的服务
                    Log.d(TAG, "Same machine: " + mServiceName);
                }
                //检查服务的名称，确认是否接入了正确的应用。
                //（我们并不需要每次都检查服务名称，仅当我们想要接入特定的应用时需要检查。）
                else if (serviceInfo.getServiceName().contains("NsdChat")){
                    //调用 resolveService() 方法，以确定服务的连接信息
                    mNsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener(){
                        @Override
                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                            //确定服务失败
                            Log.e(TAG, "Resolve failed " + errorCode);
                        }

                        @Override
                        public void onServiceResolved(NsdServiceInfo serviceInfo) {
                            //服务确定成功，服务确定后才能够获取服务端host与port信息
                            Log.d(TAG, "Resolve Succeeded. " + serviceInfo);

                            if (serviceInfo.getServiceName().equals(mServiceName)){
                                Log.d(TAG, "Same IP.");
                                return;
                            }

                            mService = serviceInfo;
                            //获得服务的详细资料，包括其 IP 地址和端口号。此时，我们就可以创建自己网络连接与服务进行通讯。
                            //如何通讯？？？？
                            int port = mService.getPort();
                            InetAddress host = mService.getHost();
                        }
                    });
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                //当网络不再可用时，调用此接口
                Log.e(TAG, "service lost " + serviceInfo);
            }
        };
    }

    // 二：调用 discoverServices() 这个异步API。
    public void startDiscoverServices(){
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener
        );
    }

    public void initializeResolveListener(){
        mResolveListener = new NsdManager.ResolveListener(){
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                //确定服务失败
                Log.e(TAG, "Resolve failed " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                //服务确定成功，服务确定后才能够获取服务端host与port信息
                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);

                if (serviceInfo.getServiceName().equals(mServiceName)){
                    Log.d(TAG, "Same IP.");
                    return;
                }

                mService = serviceInfo;
                //获得服务的详细资料，包括其 IP 地址和端口号。此时，我们就可以创建自己网络连接与服务进行通讯。
                //如何通讯？？？？
                int port = mService.getPort();
                InetAddress host = mService.getHost();
            }
        };
    }

    //服务开销大，在程序退出时，需要注销该服务。一般在项目的ApplicationActivity中注销

    @Override
    protected void onPause() {
        if (mNsdHelper != null){
            mNsdHelper.tearDown();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNsdHelper != null){
            //在此处执行doMyselfService，startDiscovery，以重新注册服务与发现的代码
        }
    }

    @Override
    protected void onDestroy() {
        if (mNsdHelper != null){
            mNsdHelper.tearDown();
            //也需要让网络连接中断
        }
        super.onDestroy();
    }

    public void wirelessStartNSD(View view) {
        doMyselfService();
    }

    public void wirelessFindNSD(View view) {
        startDiscovery();
    }


    public class NsdHelper {
        public void tearDown(){
            mNsdManager.unregisterService(mRegistrationListener);
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
    }
}
