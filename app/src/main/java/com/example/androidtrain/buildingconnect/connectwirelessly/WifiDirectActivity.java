package com.example.androidtrain.buildingconnect.connectwirelessly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidtrain.BaseActivity;
import com.example.androidtrain.R;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WifiDirectActivity extends BaseActivity {

    private static final String TAG = "WifiDirect";

    private final IntentFilter intentFilter = new IntentFilter();

    WifiP2pManager.Channel mChannel;

    WifiP2pManager mManager;

    boolean p2pEnable = true;

    BroadcastReceiver receiver;

    WifiDirectService wifiP2pService;

    ListView mListView;

    ArrayAdapter<String> mAdapter;

    WifiP2pDnsSdServiceRequest p2pDnsSdServiceRequest;

    WifiP2pServiceRequest serviceRequest;

    //获取对等节点列表（Peer list），第一步
    private ArrayList<WifiP2pDevice> peers = new ArrayList();
    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            //清除旧信息，存储新信息
            peers.clear();
            peers.addAll(peerList.getDeviceList());

            //刷新使用此数据的Adapter视图
//            ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
            if (peers.size() == 0){
                Log.d(TAG, "No devices found");
                return;
            }
            //连接设备
            connect();
        }

    };

    //监听连接状态的变化，当有多个设备同时试图连接到一台设备时（例如多人游戏或者聊天群），
    // 这一台设备将被指定为“群主”（group owner）
    WifiP2pManager.ConnectionInfoListener connectionListener =  new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            //获取group owner地址
            InetAddress groupOwnerAddress = info.groupOwnerAddress;

            //确认是否是group owner
            if (info.groupFormed && info.isGroupOwner){
                Log.d(TAG, "This device is Group Owner!");
                //进行group owner特定的操作
                // One common case is creating a server thread and accepting
                // incoming connections.
            }else if (info.groupFormed){
                Log.d(TAG, "this device is Client");
                //进行client相关操作
                // you'll want to create a client thread that connects to the group
                // owner.
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_direct);

        mListView = (ListView) findViewById(R.id.wifi_direct_found_list);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"waiting for device!"});
        mListView.setAdapter(mAdapter);
        //使用Wifi P2P时，需要监听事件发出的广播

        // 监听Wifi-p2p 状态，指示　Wi-Fi P2P　是否开启
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        //监听节点列表的变化，代表对等节点（peer）列表发生了变化
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        //监听Wifi-p2p连接状态的变化
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        //监听设备的详细配置发生的变化
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //步骤一：初始化Manager，Channel，并确认是否初始化成功
        //后面将使用Channel对象连接Wifi-p2p框架
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        //搜索附件带有Wifip2p的设备,Peer Discovery。仅仅表示是否能够正常发现设备。检测是否初始化成功
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Peer Discovery initial succeed");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Peer Discovery initial failed");
            }
        });
    }

    private void setIsWifiP2pEnabled(boolean b) {
        p2pEnable = b;
    }

    final HashMap<String, String> buddies = new HashMap<>();

    private void discoverService(){
        //监听实时收到的记录（record）
        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
            @Override
            public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {
                Log.d(TAG, "DnsSdTxtRecord available - " + txtRecordMap.toString());
                Log.d(TAG, "deviceAddress:" + srcDevice.deviceAddress + "; buddyname:" + txtRecordMap.get("buddyname"));
                buddies.put(srcDevice.deviceAddress, txtRecordMap.get("buddyname"));
            }
        };

        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {
                // Update the device name with the human-friendly version from
                // the DnsTxtRecord, assuming one arrived.
                srcDevice.deviceName = buddies.containsKey(srcDevice.deviceAddress)?
                        buddies.get(srcDevice.deviceAddress): srcDevice.deviceName;

                // Add to the custom adapter defined specifically for showing
                // wifi devices.
                mAdapter.add(srcDevice.deviceAddress + ":" + srcDevice.deviceName);
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, "onBonjourServiceAvailable " + instanceName);
            }
        };

        mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);

        p2pDnsSdServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        mManager.addServiceRequest(mChannel, p2pDnsSdServiceRequest, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "WifiP2pDnsSdServiceRequest onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "WifiP2pDnsSdServiceRequest onFailure");
            }
        });
        serviceRequest = WifiP2pServiceRequest.newInstance(WifiP2pServiceInfo.SERVICE_TYPE_ALL);
        mManager.addServiceRequest(mChannel, serviceRequest, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "WifiP2pServiceRequest onSuccess");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "WifiP2pServiceRequest onFailure");
            }
        });
    }

    //动态注册广播监听，使用：intentFilter, Broadcast Receiver
    @Override
    protected void onResume() {
        super.onResume();

        //步骤二：设置广播监听器，监听各个事件，并作处理
        receiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void connect(){
        // Picking the first device found on the network
        WifiP2pDevice device = peers.get(0);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        Log.d(TAG, "connect() start!");
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "connect() success!");
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(WifiDirectActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void startWifiP2pService(View view) {
        wifiP2pService = new WifiDirectService(mManager, mChannel);
        wifiP2pService.startRegistration();
    }

    public void startFoundWifiP2pService(View view) {
        discoverService();
    }

    /**
     * 创建一个新的 BroadcastReceiver 类侦听系统中 Wi-Fi P2P 状态的变化。在 onReceive() 方法中，
     * 加入对上述四种不同 P2P 状态变化的处理
     */
    public class WiFiDirectBroadcastReceiver extends BroadcastReceiver{
        WifiDirectActivity activity;
        WifiP2pManager manager;
        WifiP2pManager.Channel channel;
        public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, WifiDirectActivity activity) {
            this.activity = activity;
            this.manager = manager;
            this.channel = channel;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
                //确认Wifi P2P模式是否可用，并通知
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                    activity.setIsWifiP2pEnabled(true);
                }else {
                    activity.setIsWifiP2pEnabled(false);
                }
            }else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
                //节点列表已经变化，我们需要在此处做些事情。
                Log.d(TAG, "The peer list has changed!");

                //步骤3：获取节点信息时，进行处理，可对其列表显示
                //调用 requestPeer() 方法获取对等节点列表
                if (manager != null){
                    manager.requestPeers(mChannel, peerListListener);
                }
            }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
                //连接状态变化
                Log.d(TAG, "Connection state changed!");
                if (manager ==null){
                    return;
                }
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()){
                    //步骤四：检查连接状态
                    //此设备连接了其他设备，请求获取连接信息以确认group owner
                    manager.requestConnectionInfo(mChannel, connectionListener);
                }
            }else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
                //设备配置详情发生变化，获取的时本机设备信息
                WifiP2pDevice wifiP2pDevice = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
                Log.d(TAG, "Device detail changed!:" + wifiP2pDevice);
            }
        }
    }




}
