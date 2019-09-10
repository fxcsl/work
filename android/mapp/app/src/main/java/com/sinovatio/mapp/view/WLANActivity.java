package com.sinovatio.mapp.view;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.ArrayTableData;
import com.sinovatio.mapp.R;
import com.sinovatio.mapp.base.BaseActivity;
import com.sinovatio.mapp.base.MyApplication;
import com.sinovatio.mapp.bean.WifiInfoBean;
import com.sinovatio.mapp.broadcast.WifiBroadcastReceiver;
import com.sinovatio.mapp.greendao.DaoSession;
import com.sinovatio.mapp.model.db.DeviceFactoryUtils;
import com.sinovatio.mapp.overwrite.MyArrayTableData;
import com.sinovatio.mapp.utils.DeviceUtil;
import com.sinovatio.mapp.utils.WifiSupport;

import org.xutils.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class WLANActivity extends BaseActivity {

    @BindView(R.id.tv_device)
    public TextView tv_device;
    @BindView(R.id.tv_spot)
    public TextView tv_spot;
    @BindView(R.id.tv_ap)
    public TextView tv_ap;
    @BindView(R.id.tv_more)
    public TextView tv_more;
    @BindView(R.id.connected_wifi_device)
    public SmartTable connected_wifi_device;
    @BindView(R.id.layout_nodata)
    public LinearLayout layout_nodata;
    @BindView(R.id.layout_connected)
    public LinearLayout layout_connected;

    @BindView(R.id.bt_back)
    public Button bt_back;
    @BindView(R.id.bt_refresh)
    public Button bt_refresh;

    @BindView(R.id.swipe_refresh)
    public SwipeRefreshLayout swipe_Refresh;

    Handler mhandler = new mHandler();


    //全局广播
    private WifiBroadcastReceiver wifiReceiver;
    //wifi列表
    List<WifiInfoBean> realWifiList = null;

    DaoSession daoSession;

    public String ipString;

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    public void initView() {
        connected_wifi_device.getConfig()
                //.setShowTableTitle(false)
                .setShowXSequence(false)
                .setShowYSequence(false)
                .setColumnTitleGridStyle(new LineStyle(this,1, Color.parseColor("#2A56C6")))
                .setColumnTitleStyle(new FontStyle(this,12, Color.parseColor("#2A56C6")))
                .setColumnTitleBackground(new BaseBackgroundFormat(Color.parseColor("#1F1D5CA0")))
                .setContentGridStyle(new LineStyle(this,1,Color.parseColor("#2A56C6")))
                .setContentStyle(new FontStyle(this,12, Color.parseColor("#2A56C6")))
                .setContentBackground(new BaseBackgroundFormat(Color.parseColor("#1F1D5CA0")))
        ;

        WifiBroadcastReceiver.Handler handler = new WifiFragmentHandler();
        wifiReceiver = new WifiBroadcastReceiver(handler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);// 监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);// 监听wifi连接状态广播,是否连接了一个有效路由

        //注册广播
        this.registerReceiver(wifiReceiver, filter);

        daoSession = ((MyApplication) this.getApplication()).getDaoSession();

        initData();

        bt_back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        bt_refresh.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(mContext,"局域网扫描开始",Toast.LENGTH_SHORT).show();
                initData();
                Toast.makeText(mContext,"局域网扫描完成",Toast.LENGTH_SHORT).show();
            }
        });

        swipe_Refresh.setColorSchemeResources(R.color.colorPrimary);
        swipe_Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(mContext,"局域网扫描开始",Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initData();
                swipe_Refresh.setRefreshing(false);
                Toast.makeText(mContext,"局域网扫描完成",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initData() {

        boolean isWifiConnected = WifiSupport.isWifiConnected(this);
        boolean isWifiEnable = WifiSupport.isWifiEnable(this);
        LogUtil.d("isWifiConnected："+isWifiConnected);
        LogUtil.d("isWifiEnable："+isWifiEnable);

        initConnectedInfo(isWifiConnected);
        ipString = getHostIP();
        for(int i = 0;i<12;i++) {
            new UDPThread(ipString).start();
        }
        execCatForArp();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wlan;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(wifiReceiver);
    }

    /**
     * 广播handler
     */
    public class WifiFragmentHandler implements WifiBroadcastReceiver.Handler{

        @Override
        public void onWifiConnect() {
            initConnectedInfo(true);
        }

        @Override
        public void onWifiDisconnect() {
            initConnectedInfo(false);
        }

        @Override
        public void onScanResultChanged(){
        }

    }

    /**
     * 初始化连接区域代码
     * @param isWifiConnected
     */
    public void initConnectedInfo(boolean isWifiConnected){
        LogUtil.d("------initConnectedInfo------");
        if(!isWifiConnected){
            setConnectedLayoutVisibility(false);
        }else{
            setConnectedLayoutVisibility(true);
            String mac = DeviceUtil.getMacAddress(this);

            String factory = DeviceFactoryUtils.getFactoryByMac(daoSession,mac);
            String ip = WifiSupport.getLocalIpV4Address();
            tv_device.setText(factory+"\n" +
                    ip+"\n" +
                    mac);

            WifiInfo wifiInfo = WifiSupport.getConnectedWifiInfo(this);
            DhcpInfo dhcpInfo = WifiSupport.getDhcpInfo(this);
            //带引号处理
            String routName = wifiInfo.getSSID().replaceAll("(^\")|(\"$)", "");
            int rssi = wifiInfo.getRssi();//-49dbm
            int speed = wifiInfo.getLinkSpeed();//433Mbps
            int frequency = wifiInfo.getFrequency();//5745MHz
            int channel = WifiSupport.getChannelByFrequency(frequency);//信道

            tv_spot.setText(routName+"\n" +
                    rssi+" dbm\n" +
                    speed+" Mbps\n" +
                    frequency+" MHz\n" +
                    "信道"+channel);


            String routeMac = wifiInfo.getBSSID();
            String  serverAddress = WifiSupport.int2Ip(dhcpInfo.serverAddress);
            String brand = DeviceFactoryUtils.getFactoryByMac(daoSession,routeMac);

            tv_ap.setText(brand+"\n" +
                    serverAddress+"\n" +
                    routeMac);
            String dns1 = WifiSupport.int2Ip(dhcpInfo.dns1);
            String dns2 = WifiSupport.int2Ip(dhcpInfo.dns2);
            String DNS = dns1+"/"+dns2;
            DNS = DNS.replaceAll("(^/)|(/$)","");
            String mask = WifiSupport.int2Ip(dhcpInfo.netmask);
            tv_more.setText("DNS:"+DNS+"  MASK:"+mask);
        }
    }


    public void setConnectedLayoutVisibility(boolean flag){
        if(flag){
            layout_nodata.setVisibility(View.GONE);
            layout_connected.setVisibility(View.VISIBLE);
            LogUtil.d("wifi显示连接数据!!");
        }else{
            layout_nodata.setVisibility(View.VISIBLE);
            layout_connected.setVisibility(View.GONE);
            LogUtil.d("wifi连接无数据!!");
        }
    }

    /**
     * 获取本机 ip地址
     *
     * @return
     */
    private String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
//            Log.i("kalshen", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }

    /**
     * 执行 cat命令 查找android 设备arp表
     * arp表 包含ip地址和对应的mac地址
     */
    private void execCatForArp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Map<String, String> map = new HashMap<>();
                    Process exec = Runtime.getRuntime().exec("cat proc/net/arp");
                    InputStream is = exec.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.d("kalshen", "run: " + line);
                        if (!line.contains("00:00:00:00:00:00")&&!line.contains("IP")) {
                            String[] split = line.split("\\s+");
                            map.put(split[3], split[0]);
                        }
                    }
                    Message msg = Message.obtain(); // 实例化消息对象
                    msg.obj = map; // 消息内容存放
                    mhandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public class UDPThread extends Thread {
        String ip;
        String targetIp;
        InetAddress address;
        DatagramSocket socket;
        DatagramPacket packet;
        byte[] data = new byte[0];

        short NBUDPP = 9876;

        public UDPThread(String ip) {
            this.ip = ip;
        }

        @Override
        public synchronized void run() {
            try {
                int lastIndexOf = ip.lastIndexOf(".");
                String substring = ip.substring(0, lastIndexOf + 1);
                int count = 2;
                socket = new DatagramSocket();
                while (count < 255) {
                    targetIp = substring + count;
                    address = InetAddress.getByName(targetIp);
                    packet = new DatagramPacket(data, data.length, address, NBUDPP);
                    socket.send(packet);
                    count += 1;
                }
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class mHandler extends Handler {

        // 通过复写handlerMessage() 从而确定更新UI的操作
        @Override
        public void handleMessage(Message msg) {
            Map<String, String> map = new HashMap<>();
            map = (Map) msg.obj;
         // 需执行的UI操作
            Object[][] data = new Object[map.size()][];
            int i = 0;
            for(Map.Entry<String, String> entry : map.entrySet()){
                String mapMac = entry.getKey();
                String mapIp = entry.getValue();
                String factory = DeviceFactoryUtils.getFactoryByMac(daoSession,mapMac);
                LogUtil.d(mapMac+mapIp);
                Object[] m = {mapIp,mapMac,factory};
                data[i] = m;
                i++;
            }
            if(data.length>0){
                Object[][] data_transformed= ArrayTableData.transformColumnArray(data);

                ArrayTableData tabledata = MyArrayTableData.create("",
                        new String[]{"IP","MAC","设备"},
                        data_transformed,
                        new int[]{80,100,65},mContext);

                connected_wifi_device.setTableData(tabledata);
                connected_wifi_device.setVisibility(View.VISIBLE);
                layout_nodata.setVisibility(View.GONE);

            }else{

                LogUtil.d("无wifi列表数据！！");
                connected_wifi_device.setVisibility(View.GONE);
                layout_nodata.setVisibility(View.VISIBLE);
            }
        }
    }

}
