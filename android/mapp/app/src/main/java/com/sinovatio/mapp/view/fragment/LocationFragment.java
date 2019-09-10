package com.sinovatio.mapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.sinovatio.mapp.R;
import com.sinovatio.mapp.adapter.RecyclerViewAdapter;
import com.sinovatio.mapp.base.BaseLazyLoadFragment;
import com.sinovatio.mapp.view.LocationActivity;

import org.xutils.common.util.LogUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 为了初始化地图 map_location.onCreate(savedInstanceState);重写了BaseLazyLoadFragment的一部分方法
 */
public class LocationFragment extends BaseLazyLoadFragment{

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    Map<String,String> data;

    @BindView(R.id.rv_location)
    public RecyclerView rv_location;
    @BindView(R.id.tv_location)
    public TextView tv_location;



    public LocationFragment() {
    }

    public static LocationFragment getInstance(String param1) {
        LocationFragment fragment = new LocationFragment();
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    @Override
    protected void initView(View view) {
        //设置为垂直的样式
        rv_location.setLayoutManager(new LinearLayoutManager(mContext));

        tv_location.setClickable(true);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent =new Intent(mContext,LocationActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initData() {
        initRecyclerViewData();
        initLocation();
        startLocation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_location;
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    /**
     * 初始化RecyclerViewData
     */
    private void initRecyclerViewData(){
        if(data==null){
            data = new LinkedHashMap<>();
        }
        data.put("坐标类型","");
        data.put("坐标类型","");
        data.put("经度","");
        data.put("纬度","");
        data.put("定位方式","");
        data.put("精度(米)","");
        data.put("高度(米)","");
        data.put("方位","");
        data.put("速度","");
        data.put("卫星数","");
        data.put("兴趣点","");
        data.put("地址","");
    }

    /**
     * 初始化定位
     *
     */
    private void initLocation(){
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
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);//场景模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(10000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            initRecyclerViewData();
            if (null != location) {
                LogUtil.d(location.toString());
                if(location.getErrorCode() == 0){
                    String provider = location.getProvider();
                    if(provider.equals("lbs")){
                        data.put("坐标类型",AMapLocation.COORD_TYPE_GCJ02);
                    }else{//gps
                        data.put("坐标类型",AMapLocation.COORD_TYPE_WGS84);
                    }
                    data.put("经度",location.getLongitude()+"");
                    data.put("纬度",""+location.getLatitude()+"");
                    data.put("定位方式",provider);
                    data.put("精度(米)",location.getAccuracy() + "米");
                    data.put("高度(米)",location.getAltitude()+"米");
                    data.put("方位",location.getBearing()+"");
                    data.put("速度",location.getSpeed() + "米/秒");
                    data.put("卫星数",location.getSatellites()+"");
                    data.put("兴趣点",location.getPoiName());
                    data.put("地址",location.getAddress());

                }
            }
            //取不到数据就清空
            rv_location.setAdapter(new RecyclerViewAdapter(mContext,data));
        }

    };

    /**
     * 获取GPS状态的字符串
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode){
        String str = "";
        switch (statusCode){
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    /**
     * 开始定位
     *
     */
    private void startLocation(){
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 销毁定位
     *
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.stopLocation();
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

}
