package com.sinovatio.mapp.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.data.table.ArrayTableData;
import com.sinovatio.mapp.R;
import com.sinovatio.mapp.base.BaseLazyLoadFragment;
import com.sinovatio.mapp.base.MyApplication;
import com.sinovatio.mapp.bean.WifiInfoBean;
import com.sinovatio.mapp.broadcast.WifiBroadcastReceiver;
import com.sinovatio.mapp.greendao.DaoSession;
import com.sinovatio.mapp.model.db.DeviceFactoryUtils;
import com.sinovatio.mapp.overwrite.CustTextDrawFormat;
import com.sinovatio.mapp.utils.DeviceUtil;
import com.sinovatio.mapp.utils.OpenSomeDialog;
import com.sinovatio.mapp.utils.WifiSupport;
import com.sinovatio.mapp.view.WLANActivity;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;


public class WifiFragment extends BaseLazyLoadFragment {


    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    @BindView(R.id.tv_device)
    public TextView tv_device;
    @BindView(R.id.tv_spot)
    public TextView tv_spot;
    @BindView(R.id.tv_ap)
    public TextView tv_ap;
    @BindView(R.id.tv_more)
    public TextView tv_more;
    @BindView(R.id.layout_nodata)
    public LinearLayout layout_nodata;
    @BindView(R.id.layout_connected)
    public LinearLayout layout_connected;
    @BindView(R.id.table_wifi)
    public SmartTable table_wifi;
    @BindView(R.id.layout_nodata_wifilist)
    public LinearLayout layout_nodata_wifilist;
    @BindView(R.id.tv_arp)
    public TextView tv_arp;


    //全局广播
    private WifiBroadcastReceiver wifiReceiver;
    //wifi列表
    List<WifiInfoBean> realWifiList = null;

    DaoSession daoSession;

    public WifiFragment() {
    }

    public static WifiFragment getInstance(String param1) {
        WifiFragment fragment = new WifiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        realWifiList = new ArrayList<>();
        daoSession = ((MyApplication) getActivity().getApplication()).getDaoSession();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(wifiReceiver);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    protected int getLayoutId(){
        return R.layout.fragment_wifi;
    }

    @Override
    protected void initView(View view) {
        tv_arp.setClickable(true);
        tv_arp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent =new Intent(mContext,WLANActivity.class);
                startActivity(intent);
            }
        });

        table_wifi.getConfig()
                .setShowXSequence(false)
                .setShowYSequence(false)
                .setColumnTitleGridStyle(new LineStyle(mContext,1,Color.parseColor("#2A56C6")))
                .setColumnTitleStyle(new FontStyle(mContext,12, Color.parseColor("#2A56C6")))
                .setColumnTitleBackground(new BaseBackgroundFormat(Color.parseColor("#1F1D5CA0")))
                .setContentGridStyle(new LineStyle(mContext,1,Color.parseColor("#2A56C6")))
                .setContentStyle(new FontStyle(mContext,12, Color.parseColor("#2A56C6")))
                .setContentBackground(new BaseBackgroundFormat(Color.parseColor("#1F1D5CA0")));

        WifiBroadcastReceiver.Handler handler = new WifiFragmentHandler();
        wifiReceiver = new WifiBroadcastReceiver(handler);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);// 监听wifi是开关变化的状态
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);// 监听wifi连接状态广播,是否连接了一个有效路由
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);// 监听wifi列表变化（开启一个热点或者关闭一个热点）

        filter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);//信号强度


        //注册广播
        mContext.registerReceiver(wifiReceiver, filter);

    }

    @Override
    protected void initData() {

 //      boolean isWifiConnected = WifiSupport.isWifiConnected(mContext);
//        boolean isWifiEnable = WifiSupport.isWifiEnable(mContext);
//        LogUtil.d("isWifiConnected："+isWifiConnected);
//        LogUtil.d("isWifiEnable："+isWifiEnable);

//        initConnectedInfo(isWifiConnected);//广播会先加载这块区域，不需要重复加载

        initWifiList();





    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

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
     * 初始化连接区域代码
     * @param isWifiConnected
     */
    public void initConnectedInfo(boolean isWifiConnected){
        LogUtil.d("------initConnectedInfo------");
        if(!isWifiConnected){
            setConnectedLayoutVisibility(false);
        }else{
            WifiInfo wifiInfo = WifiSupport.getConnectedWifiInfo(mContext);
            String mac = DeviceUtil.getMacAddress(getContext());

            String factory = DeviceFactoryUtils.getFactoryByMac(daoSession,mac);
            String ip = WifiSupport.int2Ip(wifiInfo.getIpAddress());
            tv_device.setText(factory+"\n" +
                    ip+"\n" +
                    mac);


            DhcpInfo dhcpInfo = WifiSupport.getDhcpInfo(mContext);
            //带引号处理
            String routName = wifiInfo.getSSID().replaceAll("(^\")|(\"$)", ""); ;
            int rssi = wifiInfo.getRssi();//-49dbm
            int speed = wifiInfo.getLinkSpeed();//433Mbps
            int frequency = wifiInfo.getFrequency();//5745MHz
            int channel = WifiSupport.getChannelByFrequency(frequency);//信道

            tv_spot.setText(routName+"\n" +
                    rssi+" dbm\n" +
                    speed+" Mbps\n" +
                    frequency+" MHz\n" +
                    "信道"+channel);

            LogUtil.d("初始化值："+routName+"\n" +
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

            setConnectedLayoutVisibility(true);
        }

    }


    public void initWifiList(){
        LogUtil.d("------initWifiList------");
        Object[][] data = new Object[0][];
        if (WifiSupport.isOpenWifi(mContext)) { // wifi打开的情况下
                List<ScanResult> scanResults = WifiSupport.getWifiScanResult(mContext);
                realWifiList.clear();
                if (scanResults!=null&&scanResults.size()>0) {
                    WifiInfo cWifiInfo = WifiSupport.getConnectedWifiInfo(mContext);
                    String cMac = "";
                    if(cWifiInfo != null){
                        cMac = cWifiInfo.getBSSID();
                    }

                    for (int i = 0; i < scanResults.size(); i++) {
                        WifiInfoBean wifiinfoBean = new WifiInfoBean(scanResults.get(i),daoSession);
                        realWifiList.add(wifiinfoBean);
                        String listMac = wifiinfoBean.getMac();
                        if(listMac.equals(cMac)){
                            int listRssi = wifiinfoBean.getRssi();
                            //更新信号强度
                            String spotText = tv_spot.getText().toString();
                            String pattern = "\n([-|\\d]+) dbm\n";
                            Pattern p = Pattern.compile(pattern);
                            Matcher m = p.matcher(spotText);
                            if (m.find()) {
                                int spotRssi = Integer.valueOf(m.group(1));
                                if(spotRssi!=listRssi){
                                    tv_spot.setText(spotText.replaceFirst(pattern,
                                            "\n"+listRssi+" dbm\n"));
                                    LogUtil.d("更新当前连接强度rssi为："+spotText.replaceFirst(pattern,
                                            "\n"+listRssi+" dbm\n"));
                                }
                            }
                        }

                    }
                }
                if(realWifiList!=null&&realWifiList.size()>0){
                    //按照强度排序
                    Collections.sort(realWifiList);
                    int length = realWifiList.size();
                    data = new Object[length][];
                    for(int i=0;i<length;i++){
                        data[i] = realWifiList.get(i).toArray();
                    }

                }

        } else {
            //isOpenWifiDialog();
        }


        if(data.length>0){
            Object[][] data_transformed= ArrayTableData.transformColumnArray(data);
            ArrayTableData tabledata = ArrayTableData.create("",
                    new String[]{"名称","设备","强度","频率","信道","MAC"},
                    data_transformed,
                    new  CustTextDrawFormat(mContext,100));

            table_wifi.setTableData(tabledata);
            table_wifi.setVisibility(View.VISIBLE);
            layout_nodata_wifilist.setVisibility(View.GONE);

        }else{

            LogUtil.d("无wifi列表数据！！");
            table_wifi.setVisibility(View.GONE);
            layout_nodata_wifilist.setVisibility(View.VISIBLE);


        }

    }


    private OpenSomeDialog wifiDialog;

    private void isOpenWifiDialog() {
        wifiDialog = new OpenSomeDialog(mContext, "使用该应用需要使用Wifi，确认开启wifi？");
        wifiDialog.setOpenSomeDialogListener(new OpenSomeDialog.OnDialogClickListener() {

            @Override
            public void onPositiveClickListener(DialogInterface dialog) {
                WifiSupport.openWifi1(mContext);
            }

            @Override
            public void onNegativeClickListener(DialogInterface dialog) {
                Toast.makeText(mContext, "没有打开Wifi，请前往设置界面打开", Toast.LENGTH_SHORT).show();
            }
        });
        wifiDialog.show();
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
            initWifiList();
        }

    }



    public static void main(String[] args){
        String s = "TP-Link_android"+"\n" +
                "-43"+" dbm\n" +
                "433"+" Mbps\n" +
                "5745"+" MHz\n" +
                "信道"+"149";

        Pattern p = Pattern.compile("\n([-|\\d]+) dbm\n");
        Matcher m = p.matcher(s);
        if (m.find( )) {
            System.out.println(m.group(1));
            int spotRssi = Integer.valueOf(m.group(1));
        }

        //System.out.println(s.replaceFirst("\n([-|\\d]+) dbm\n","\n-88 dbm\n"));

    }






}
