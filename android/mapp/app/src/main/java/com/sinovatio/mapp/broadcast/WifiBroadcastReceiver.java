package com.sinovatio.mapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import org.xutils.common.util.LogUtil;


public class WifiBroadcastReceiver extends BroadcastReceiver {

    public WifiBroadcastReceiver(Handler handler){
        super();
        this.handler = handler;
    }
    private Handler handler;

    public interface Handler{
        void onWifiConnect();

        void onWifiDisconnect();

        void onScanResultChanged();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d("广播状态：" + intent.getAction());

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            switch (state) {
                /**
                 * WIFI_STATE_DISABLED WLAN已经关闭
                 * WIFI_STATE_DISABLING WLAN正在关闭
                 * WIFI_STATE_ENABLED WLAN已经打开
                 * WIFI_STATE_ENABLING WLAN正在打开
                 * WIFI_STATE_UNKNOWN 未知
                 */
                case WifiManager.WIFI_STATE_DISABLED: {
                    LogUtil.d( "已经关闭");
                    Toast.makeText(context, "WIFI处于关闭状态", Toast.LENGTH_SHORT).show();
                    break;
                }
                case WifiManager.WIFI_STATE_DISABLING: {
                    LogUtil.d( "正在关闭");
                    break;
                }
                case WifiManager.WIFI_STATE_ENABLED: {
                    LogUtil.d( "已经打开");
                    handler.onScanResultChanged();
                    break;
                }
                case WifiManager.WIFI_STATE_ENABLING: {
                    LogUtil.d( "正在打开");
                    break;
                }
                case WifiManager.WIFI_STATE_UNKNOWN: {
                    LogUtil.d( "未知状态");
                    break;
                }
            }
        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            LogUtil.d( "--NetworkInfo--" + info.toString());
            if (NetworkInfo.State.DISCONNECTED == info.getState()) {// wifi没连接上
                LogUtil.d( "wifi没连接上");
                handler.onWifiDisconnect();

            } else if (NetworkInfo.State.CONNECTED == info.getState()) {// wifi连接上了
                LogUtil.d( "wifi连接上了");
                //fragment内容修改
                handler.onWifiConnect();

            } else if (NetworkInfo.State.CONNECTING == info.getState()) {// 正在连接
                LogUtil.d( "wifi正在连接");
            }
        } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
            LogUtil.d( "网络列表变化了");
            handler.onScanResultChanged();

        } else if(WifiManager.RSSI_CHANGED_ACTION.equals(intent.getAction())){
            LogUtil.d( "信号强度变化了");

        }
    }
}
