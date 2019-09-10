package com.sinovatio.iesi.service;

import android.app.Service;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.http.RetrofitClient;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.tools.LocationHelper;
import com.sinovatio.iesi.tools.LocationUtils;
import com.sinovatio.iesi.tools.SharedPreferencesHelper;

import org.litepal.LitePal;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.functions.Consumer;

public class LocationService extends Service {

    private Timer mTimer;
    private TimerTask mTask;
    private Location cLocation;



    public LocationService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, PlayerMusicService.class);
        startService(intent);

        initLocation();
        initTimer();

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }

        if(mTask!=null){
            mTask.cancel();
            mTask = null;
        }
    }

    public void initLocation() {
        LocationUtils.getInstance(this).initLocation(new LocationHelper() {
            @Override
            public void UpdateLocation(Location location) {
                Log.e("MoLin", "location.getLatitude():" + location.getLatitude());
                cLocation = location;
            }

            @Override
            public void UpdateStatus(String provider, int status, Bundle extras) {
                Log.e("MoLin", "UpdateStatus"+provider);
            }

            @Override
            public void UpdateGPSStatus(GpsStatus pGpsStatus) {
                Log.e("MoLin", "UpdateGPSStatus");

            }

            @Override
            public void UpdateLastLocation(Location location) {
                Log.e("MoLin", "UpdateLastLocation_location.getLatitude():" + location.getLatitude());
                cLocation = location;
            }
        });
    }


    /**
     * 定时器任务
     */
    public void initTimer(){
        mTimer = new Timer();
        mTask = new TimerTask(){
            @Override
            public void run() {
                uploadInfo();
            }};
        mTimer.scheduleAtFixedRate(mTask, 0, 5*1000);
    }

    /**
     * 上报位置信息
     */
    public void uploadInfo() {

        if (cLocation != null) {
            String account = LitePal.findFirst(UserInfo.class).getAccount();
            String longitude = cLocation.getLongitude()+"";
            String latitude = cLocation.getLatitude()+"";
            SharedPreferencesHelper sh = new SharedPreferencesHelper(getApplicationContext(),"taskId");
            String missionId = sh.getString("MissionId");
            Log.d("MoLin",account+"~~"+missionId);

            if(missionId!=null&&account!=null){
                RetrofitClient.getInstance().getInterface().receiveGpsInfo(account,longitude,latitude,missionId)
                        .compose(RxScheduler.<BaseArrayBean>Flo_io_main())
                        .subscribe(new Consumer<BaseArrayBean>() {
                            @Override
                            public void accept(BaseArrayBean bean) throws Exception {
                                Log.d("MoLin","uploadInfo success!!");

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("MoLin","uploadInfo error!"+throwable);
                            }
                        });
            }
        }
    }

    private final IBinder binder = new MyBinder();        //绑定器
    public class MyBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;    //返回本服务
        }
    }
}
