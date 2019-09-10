package com.sinovatio.mapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import org.xutils.common.util.LogUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WifiSupport {

    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    public WifiSupport() {
    }

    public static List<ScanResult> getWifiScanResult(Context context) {
        boolean b = context == null;
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getScanResults();
    }

    public static boolean isWifiEnable(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    public static WifiInfo getConnectedWifiInfo(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
    }

    public static DhcpInfo getDhcpInfo(Context context){
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getDhcpInfo();
    }



    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }

    public static List getConfigurations(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConfiguredNetworks();
    }


    public static WifiConfiguration createWifiConfig(String SSID, String password, WifiCipherType type) {

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        if (type == WifiCipherType.WIFICIPHER_NOPASS) {
//            config.wepKeys[0] = "";  //注意这里
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
        }

        if (type == WifiCipherType.WIFICIPHER_WEP) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }

        if (type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;

    }

    /**
     * 接入某个wifi热点
     */
    public static boolean addNetWork(WifiConfiguration config, Context context) {

        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiinfo = wifimanager.getConnectionInfo();

        if (null != wifiinfo) {
            wifimanager.disableNetwork(wifiinfo.getNetworkId());
        }

        boolean result = false;

        if (config.networkId > 0) {
            result = wifimanager.enableNetwork(config.networkId, true);
            wifimanager.updateNetwork(config);
        } else {

            int i = wifimanager.addNetwork(config);
            result = false;

            if (i > 0) {

                wifimanager.saveConfiguration();
                return wifimanager.enableNetwork(i, true);
            }
        }

        return result;

    }

    /**
     * 判断wifi热点支持的加密方式
     */
    public static WifiCipherType getWifiCipher(String s) {

        if (TextUtils.isEmpty(s)) {
            return WifiCipherType.WIFICIPHER_INVALID;
        } else if (s.contains("WEP")) {
            return WifiCipherType.WIFICIPHER_WEP;
        } else if (s.contains("WPA") || s.contains("WPA2") || s.contains("WPS")) {
            return WifiCipherType.WIFICIPHER_WPA;
        } else {
            return WifiCipherType.WIFICIPHER_NOPASS;
        }
    }

    //查看以前是否也配置过这个网络
    public static WifiConfiguration isExsits(String SSID, Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> existingConfigs = wifimanager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    // 打开WIFI
    public static void openWifi(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifimanager.isWifiEnabled()) {
            wifimanager.setWifiEnabled(true);
        }
    }

    // 关闭WIFI
    public static void closeWifi(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifimanager.isWifiEnabled()) {
            wifimanager.setWifiEnabled(false);
        }
    }

    public static boolean isOpenWifi(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean b = wifimanager.isWifiEnabled();
        return b;
    }

    /**
     * 将idAddress转化成string类型的Id字符串
     *
     * @param idString
     * @return
     */
    public static String getStringId(int idString) {
        StringBuffer sb = new StringBuffer();
        int b = (idString >> 0) & 0xff;
        sb.append(b + ".");
        b = (idString >> 8) & 0xff;
        sb.append(b + ".");
        b = (idString >> 16) & 0xff;
        sb.append(b + ".");
        b = (idString >> 24) & 0xff;
        sb.append(b);
        return sb.toString();
    }

    /**
     * 设置安全性
     *
     * @param capabilities
     * @return
     */
    public static String getCapabilitiesString(String capabilities) {
        if (capabilities.contains("WEP")) {
            return "WEP";
        } else if (capabilities.contains("WPA") || capabilities.contains("WPA2") || capabilities.contains("WPS")) {
            return "WPA/WPA2";
        } else {
            return "OPEN";
        }
    }

    public static boolean getIsWifiEnabled(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifimanager.isWifiEnabled();
    }

    /**
     * 去除同名WIFI
     *
     * @param oldSr 需要去除同名的列表
     * @return 返回不包含同命的列表
     */
    public static List<ScanResult> noSameName(List<ScanResult> oldSr) {
        List<ScanResult> newSr = new ArrayList<ScanResult>();
        for (ScanResult result : oldSr) {
            if (!TextUtils.isEmpty(result.SSID) && !containName(newSr, result.SSID))
                newSr.add(result);
        }
        return newSr;
    }

    /**
     * 判断一个扫描结果中，是否包含了某个名称的WIFI
     *
     * @param sr   扫描结果
     * @param name 要查询的名称
     * @return 返回true表示包含了该名称的WIFI，返回false表示不包含
     */
    public static boolean containName(List<ScanResult> sr, String name) {
        for (ScanResult result : sr) {
            if (!TextUtils.isEmpty(result.SSID) && result.SSID.equals(name))
                return true;
        }
        return false;
    }

    /**
     * 返回level 等级
     */
    public static int getLevel(int level) {
        if (Math.abs(level) < 50) {
            return 1;
        } else if (Math.abs(level) < 75) {
            return 2;
        } else if (Math.abs(level) < 90) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * 判断sdk版本是否大于23（Android系统是否是6.0及以上）
     *
     * @return
     */
    public static boolean isBigSDK() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是否开启了位置信息
     *
     * @return
     */
    public static boolean getIsLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * 让用户去打开wifi
     */
    public static void openWifi1(Context context) {
        //第一种直接打开Wifi设置界面的方式
//		Intent intent = new Intent();
//		intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
//		startActivity(intent);

        //第二种：直接跳转至wifi设置界面
//		Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");  
//		startActivity(wifiSettingsIntent); 

        //第三种
//		Intent intent = new Intent();
//		if(android.os.Build.VERSION.SDK_INT >= 11){
//		    //Honeycomb
//			intent.setClassName("com.android.settings", "com.android.settings.Settings$WifiSettingsActivity");
//		 }else{
//		    //other versions
//			 intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
//		 }
//		 startActivity(intent);
        //第四种
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        context.startActivity(wifiSettingsIntent);
    }

    /**
     * 直接跳转至位置信息设置界面
     */
    public static void openLocation(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 根据频率获取信道
     * @param frequency
     * @return
     */
    public static int getChannelByFrequency(int frequency) {

        int channel = -1;

        switch  (frequency) {

            case 2412:
                channel = 1;
                break;
            case 2417:
                channel = 2;
                break;
            case 2422:
                channel = 3;
                break;
            case 2427:
                channel = 4;
                break;
            case 2432:
                channel = 5;
                break;
            case 2437:
                channel = 6;
                break;
            case 2442:
                channel = 7;
                break;
            case 2447:
                channel = 8;
                break;
            case 2452:
                channel = 9;
                break;
            case 2457:
                channel = 10;
                break;
            case 2462:
                channel = 11;
                break;
            case 2467:
                channel = 12;
                break;
            case 2472:
                channel = 13;
                break;
            case 2484:
                channel = 14;
                break;
            case 5745:
                channel = 149;
                break;
            case 5765:
                channel = 153;
                break;
            case 5785:
                channel = 157;
                break;
            case 5805:
                channel = 161;
                break;
            case 5825:
                channel = 165;
                break;
        }
        return channel;
    }

    public static String getTypeByMac(String mac){
        return "";
    }

    /**
     * 获取内网ip(互联网状态下)
     * @return
     */
    public static String getLocalIpV4Address() {
        try {
            String ipv4;
            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni: nilist)
            {
                ArrayList<InetAddress>  ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address: ialist){
                    if (!address.isLoopbackAddress() && !address.isLinkLocalAddress())
                    {
                        ipv4=address.getHostAddress();
                        return ipv4;
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtil.e( ex.toString());
        }
        return "";
    }




    public static long ip2long(String strIp) {
        String[] ip = strIp.split("\\.");
        return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);
    }

    public static String long2Ip(long ip) {
        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }

    /**
     * dchpinfo中的ip使用
     * @param ip
     * @return
     */
    public static String int2Ip(int ip) {
        return (ip & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }




    public static void main(String[] args){

        String ip = "255.255.255.0";
        long a = ip2long(ip);
        //int m = 4294967040;
        System.out.println(a);
        int c = 16885952;
        String b = long2Ip(c);
        System.out.println(b);
    }





}
