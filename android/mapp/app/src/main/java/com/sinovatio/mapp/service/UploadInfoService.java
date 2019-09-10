package com.sinovatio.mapp.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import com.google.gson.Gson;
import com.sinovatio.mapp.bean.Upload_Base_Bean;
import com.sinovatio.mapp.bean.Upload_Station_Bean;
import com.sinovatio.mapp.bean.Upload_Wifi_Bean;
import com.sinovatio.mapp.bean.Upload_APDeviceInfoBean;
import com.sinovatio.mapp.bean.Upload_SimBean;
import com.sinovatio.mapp.model.db.DeviceFactoryUtils;
import com.sinovatio.mapp.utils.SimUtils;
import com.sinovatio.mapp.view.MonitorActivity;
import com.sinovatio.mapp.R;
import com.sinovatio.mapp.base.MyApplication;
import com.sinovatio.mapp.bean.BaseObjectBean;
import com.sinovatio.mapp.bean.WifiInfoBean;
import com.sinovatio.mapp.greendao.DaoSession;
import com.sinovatio.mapp.net.RetrofitClient;
import com.sinovatio.mapp.net.RxScheduler;
import com.sinovatio.mapp.utils.DeviceUtil;
import com.sinovatio.mapp.utils.WifiSupport;

import org.xutils.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.functions.Consumer;

/**
 * 实时上传信息
 */
public class UploadInfoService extends Service {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private AMapLocation cLocation = null;

    private Context mContext;

    private DaoSession daoSession;

    private Timer mTimer;

    private TimerTask mTask;

    private UploadInfoBinder uploadInfoBinder = new UploadInfoBinder();

    private boolean refreshFlag = true;

    private MonitorActivity monitorActivity;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    TelephonyManager telephone;

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(mContext);
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }


    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);//场景模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(20000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        //mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    private void startLocation() {
        locationClient.startLocation();
    }

    private void destroyLocation() {
        if (null != locationClient) {
            locationClient.stopLocation();
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {//定位成功

                cLocation = null;
                cLocation = location;
                LogUtil.d(cLocation.toStr(2));

            } else {//定位失败

            }
        }
    };

    /**
     * 定时器任务
     */
    public void initTimer() {
        mTimer = new Timer();
        mTask = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                uploadInfo();
            }
        };
        mTimer.scheduleAtFixedRate(mTask, 0, 20 * 1000);

    }

    /**
     * 位置信息、热点信息、局域网设备、基站信息
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void uploadInfo() {
        if (!refreshFlag) {
            return;
        }
        //位置信息+手机基础信息
        String locationInfo = "UNKNOW";
        if (cLocation != null) {
            locationInfo = cLocation.toStr(2);
        }

        String deviceInfo = DeviceUtil.getDeviceId(mContext);

//        //热点信息
        List<WifiInfoBean> wifiList = getWifiInfoList();

        //局域网设备
        Map<String, String> wlanMap = getWlanDeviceInfo();

//        //基站信息（小区）
        List<CellInfo> cellList = getCellInfo();

//        String result = deviceInfo + locationInfo+new Gson().toJson(wifiList)+new Gson().toJson
//                (wlanMap)+new
//                Gson().toJson(cellList);

        if (!"UNKNOW".equals(locationInfo)) {
            upLoadWifi(telephone, cLocation);
        }

    }


    /**
     * 获取wifi信息
     *
     * @return
     */
    public List<WifiInfoBean> getWifiInfoList() {
        List list = new ArrayList<WifiInfoBean>();
        if (WifiSupport.isOpenWifi(getApplicationContext())) { // wifi打开的情况下
            List<ScanResult> scanResults = WifiSupport.getWifiScanResult(mContext);
            if (scanResults != null && scanResults.size() > 0) {
                //WifiInfo cWifiInfo = WifiSupport.getConnectedWifiInfo(mContext);
                for (int i = 0; i < scanResults.size(); i++) {
                    WifiInfoBean wifiinfoBean = new WifiInfoBean(scanResults.get(i), daoSession);
                    list.add(wifiinfoBean);
                }
            }
        }
        return list;
    }

    /**
     * 获取基站（小区）信息
     *
     * @return
     */
    public List<CellInfo> getCellInfo() {
        List list = new ArrayList<CellInfo>();
        telephone = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            }
        }
        list = telephone.getAllCellInfo();
        return list;
    }

    /**
     * 获取局域网设备信息
     *
     * @return
     */
    public Map<String, String> getWlanDeviceInfo() {
        Map<String, String> map = new HashMap<>();
        try {
            Process exec = Runtime.getRuntime().exec("cat proc/net/arp");
            InputStream is = exec.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("00:00:00:00:00:00") && !line.contains("IP")) {
                    String[] split = line.split("\\s+");
                    map.put(split[3], split[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return uploadInfoBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        daoSession = ((MyApplication) getApplication()).getDaoSession();
        Intent intent = new Intent(this, PlayerMusicService.class);
        startService(intent);
//        Notification  noti = new Notification();
//        noti.flags = Notification.FLAG_NO_CLEAR|Notification.FLAG_ONGOING_EVENT;
//        startForeground(1, noti);
        startForeground(1, buildNotification());

        initLocation();
        startLocation();
        initTimer();

        LogUtil.d("UploadInfoService onCreate");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("UploadInfoService onStartCommand");
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                uploadInfo();
//                LogUtil.d("Alarm run!");
//            }
//        }).start();
//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        int time1 = 20 * 1000;
//        long triggerAtTime = SystemClock.elapsedRealtime() + time1;
//        Intent i = new Intent(this,UploadInfoService.class);
//        PendingIntent pi = PendingIntent.getService(this,0,i,0);
//        manager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        LogUtil.d("UploadInfoService onDestroy");
        super.onDestroy();
        stopForeground(true);
        destroyLocation();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }

    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private NotificationManager notificationManager = null;
    boolean isCreateChannel = false;

    @SuppressLint("NewApi")
    private Notification buildNotification() {

        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            }
            String channelId = getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        builder.setSmallIcon(R.mipmap.ic_mapp)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }

    public class UploadInfoBinder extends Binder {
        public void setRefreshFlag(boolean flag) {
            refreshFlag = flag;
            LogUtil.d("实时上传标志位：" + flag);
        }

        public boolean getRefreshFlag() {
            return refreshFlag;
        }

        public void initActivity(MonitorActivity m) {
            monitorActivity = m;
        }
    }

//

    /**
     * 上传wifi信息
     */
    @SuppressLint("MissingPermission")
    public void upLoadWifi(TelephonyManager tel, AMapLocation location) {
        Upload_Base_Bean<String> bean = new Upload_Base_Bean<>();
        List<String> list = new ArrayList<>();

        //热点信息
        List<WifiInfoBean> wifiList = getWifiInfoList();
        for (int i = 0; i < wifiList.size(); i++) {
            Upload_Wifi_Bean wbean = new Upload_Wifi_Bean();
            wbean.setImei(tel.getDeviceId() + "");
            wbean.setChannel(wifiList.get(i).getChannel() + "");//信道i
            wbean.setWifi_mac(wifiList.get(i).getMac() + "");//mac
            wbean.setSsid(wifiList.get(i).getName() + "");//热点名称
            wbean.setSignal_value(wifiList.get(i).getRssi() + "");//衰减值
            wbean.setDevice_type(DeviceFactoryUtils.getFactoryByMac(daoSession, wifiList.get(i).getMac()) + "");
            wbean.setLatitude("" + location.getLatitude());
            wbean.setLongitude("" + location.getLongitude());

            list.add(JSON.toJSONString(wbean));
        }
        bean.setJsonDate(list);
        RetrofitClient.getInstance().getApi().wifi_ap(bean)
                .compose(RxScheduler.<BaseObjectBean>Flo_io_main())
                .subscribe(new Consumer<BaseObjectBean>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void accept(BaseObjectBean bean) throws Exception {

                        LogUtil.d("uploadInfo success!!" + new Gson().toJson(bean));
                        upLoadStation(telephone, cLocation);
                    }
                }, new Consumer<Throwable>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.d("uploadInfo error!" + throwable);
                        upLoadStation(telephone, cLocation);
                    }
                });
    }


    /**
     * 上传基站数据
     */
    @SuppressLint("MissingPermission")
    public void upLoadStation(TelephonyManager tel, AMapLocation location) {
        Upload_Base_Bean<String> bean = new Upload_Base_Bean<>();
        List<String> list = new ArrayList<>();
        //基站信息（小区）
        List<CellInfo> cellList = getCellInfo();
        for (int i = 0; i < cellList.size(); i++) {
            CellInfo cellInfo = cellList.get(i);
//            if (cellInfo.isRegistered()) {
            Upload_Station_Bean sbean = new Upload_Station_Bean();

            if (cellInfo instanceof CellInfoLte) {
                sbean.setBase_station_model("LTE");
                sbean.setBase_station_code(((CellInfoLte) cellInfo).getCellIdentity().getTac() + "_" + ((CellInfoLte) cellInfo).getCellIdentity().getPci() + "_" + ((CellInfoLte) cellInfo).getCellIdentity().getCi());// ((CellInfoLte) cellInfo).getCellIdentity().getTac()+"_"+(CellInfoLte) cellInfo).getCellIdentity().getPci()+"_"+(CellInfoLte) cellInfo).getCellIdentity().getCi()
                sbean.setSignal_value(((CellInfoLte) cellInfo).getCellSignalStrength().getDbm() + "");
                sbean.setFrequency_band((SimUtils.getNetworkFrequency(cellInfo)).get(1)
                        + (SimUtils.getNetworkFrequency(cellInfo)).get(2) + "");
                sbean.setFrequency_point((SimUtils.getNetworkFrequency(cellInfo)).get(0) + "");
                sbean.setCommunity_type("LTE");
            } else if (cellInfo instanceof CellInfoGsm) {
                sbean.setBase_station_model("GSM");
                sbean.setBase_station_code(((CellInfoGsm) cellInfo).getCellIdentity().getLac() + "_" + ((CellInfoGsm) cellInfo).getCellIdentity().getCid());// ((CellInfoLte) cellInfo).getCellIdentity().getTac()+"_"+(CellInfoLte) cellInfo).getCellIdentity().getPci()+"_"+(CellInfoLte) cellInfo).getCellIdentity().getCi()
                sbean.setSignal_value(((CellInfoGsm) cellInfo).getCellSignalStrength().getDbm() + "");
                sbean.setFrequency_band("");//频段
                sbean.setFrequency_point("");//频点
                sbean.setCommunity_type("GSM");
            } else if (cellInfo instanceof CellInfoCdma) {
                sbean.setBase_station_model("CDMA");
                sbean.setBase_station_code(((CellInfoCdma) cellInfo).getCellIdentity().getNetworkId() + "_" + ((CellInfoCdma) cellInfo).getCellIdentity().getBasestationId());// ((CellInfoLte) cellInfo).getCellIdentity().getTac()+"_"+(CellInfoLte) cellInfo).getCellIdentity().getPci()+"_"+(CellInfoLte) cellInfo).getCellIdentity().getCi()
                sbean.setSignal_value(((CellInfoCdma) cellInfo).getCellSignalStrength().getDbm() + "");
                sbean.setFrequency_band("");//频段
                sbean.setFrequency_point("");//频点
                sbean.setCommunity_type("CDMA");
            } else if (cellInfo instanceof CellInfoWcdma) {
                sbean.setBase_station_model("WCDMA");
                sbean.setBase_station_code(((CellInfoWcdma) cellInfo).getCellIdentity().getLac() + "_" + ((CellInfoWcdma) cellInfo).getCellIdentity().getCid());// ((CellInfoLte) cellInfo).getCellIdentity().getTac()+"_"+(CellInfoLte) cellInfo).getCellIdentity().getPci()+"_"+(CellInfoLte) cellInfo).getCellIdentity().getCi()
                sbean.setSignal_value(((CellInfoWcdma) cellInfo).getCellSignalStrength().getDbm() + "");
                sbean.setFrequency_band("");//频段
                sbean.setFrequency_point("");//频点
                sbean.setCommunity_type("WCDMA");
            }
            sbean.setMcc(tel.getNetworkOperator().substring(0, 3) + "");
            sbean.setMnc(tel.getNetworkOperator().substring(3, 5) + "");
            sbean.setImei(tel.getDeviceId() + "");
            sbean.setData_network(SimUtils.getNetworkType(tel) + "");
            sbean.setLatitude("" + location.getLatitude());
            sbean.setLongitude("" + location.getLongitude());
            list.add(JSON.toJSONString(sbean));
//                break;
//            }
        }

        bean.setJsonDate(list);
        RetrofitClient.getInstance().getApi().base_station(bean)
                .compose(RxScheduler.<BaseObjectBean>Flo_io_main())
                .subscribe(new Consumer<BaseObjectBean>() {
                    @Override
                    public void accept(BaseObjectBean bean) throws Exception {

                        LogUtil.d("uploadInfo success!!" + new Gson().toJson(bean));
                        uploadSimInfo(telephone, cLocation);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.d("uploadInfo error!" + throwable);
                        uploadSimInfo(telephone, cLocation);
                    }
                });
    }

    /**
     * 上传SIM卡信息
     */
    public void uploadSimInfo(TelephonyManager telephone, AMapLocation aMaplocation) {
        TelephonyManager tel = telephone;
        AMapLocation location = aMaplocation;
        Upload_Base_Bean<String> bean = new Upload_Base_Bean<>();
        List<String> list = new ArrayList<>();
        Upload_SimBean simBean = new Upload_SimBean();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

            }
        }

        simBean.setImei("" + tel.getDeviceId());
        simBean.setImsi("" + tel.getSubscriberId());
        simBean.setMac("" + DeviceUtil.getMacAddress(mContext));
        simBean.setPhone_number("" + tel.getLine1Number());
        simBean.setPhone_model("" + DeviceUtil.getPhoneModel());
        simBean.setIsp("" + tel.getNetworkOperatorName());
        simBean.setLongitude("" + location.getLongitude());
        simBean.setLatitude("" + location.getLatitude());
        list.add(JSON.toJSONString(simBean));
        bean.setJsonDate(list);

        RetrofitClient.getInstance().getApi().imsi(bean)
                .compose(RxScheduler.<BaseObjectBean>Flo_io_main())
                .subscribe(new Consumer<BaseObjectBean>() {
                    @Override
                    public void accept(BaseObjectBean bean) throws Exception {

                        LogUtil.d("uploadSIMInfo success!!" + new Gson().toJson(bean));
                        uploadAPDeviceInfo(telephone, location);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.d("uploadSIMInfo error!" + throwable);
                        uploadAPDeviceInfo(telephone, location);
                    }
                });
    }

    /**
     * 上传同一AP下设备信息
     */
    public void uploadAPDeviceInfo(TelephonyManager telephone, AMapLocation aMaplocation) {
        TelephonyManager tel = telephone;
        Upload_Base_Bean<String> bean = new Upload_Base_Bean<>();
        List<String> list = new ArrayList<>();
        Map<String, String> map = getWlanDeviceInfo();
        WifiInfo wifiInfo = WifiSupport.getConnectedWifiInfo(mContext);
        for (String key : map.keySet()) {
            Upload_APDeviceInfoBean apDeviceInfoBean = new Upload_APDeviceInfoBean();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mContext.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                }
            }
            apDeviceInfoBean.setImei("" + tel.getDeviceId());
            apDeviceInfoBean.setSsid("" + wifiInfo.getSSID());
            apDeviceInfoBean.setWifi_mac("" + wifiInfo.getBSSID());
            apDeviceInfoBean.setDevice_type("" + DeviceFactoryUtils.getFactoryByMac(daoSession, key));//map.get(key)
            apDeviceInfoBean.setSignal_value("" + wifiInfo.getRssi());
            if (Build.VERSION.SDK_INT >= 21) {
                apDeviceInfoBean.setChannel("" + WifiSupport.getChannelByFrequency(wifiInfo.getFrequency()));
            } else {
                apDeviceInfoBean.setChannel("");
            }
            apDeviceInfoBean.setAp_mac("" + key);
            apDeviceInfoBean.setLongitude("" + aMaplocation.getLongitude());
            apDeviceInfoBean.setLatitude("" + aMaplocation.getLatitude());
            list.add(JSON.toJSONString(apDeviceInfoBean));
        }
        bean.setJsonDate(list);

        RetrofitClient.getInstance().getApi().ap_same_terminal(bean)
                .compose(RxScheduler.<BaseObjectBean>Flo_io_main())
                .subscribe(new Consumer<BaseObjectBean>() {
                    @Override
                    public void accept(BaseObjectBean bean) throws Exception {

                        LogUtil.d("uploadAPDeviceInfo success!!" + new Gson().toJson(bean));

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.d("uploadAPdeviceInfo error!" + throwable);
                    }
                });
    }
}
