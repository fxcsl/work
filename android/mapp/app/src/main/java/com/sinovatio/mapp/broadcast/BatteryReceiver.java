package com.sinovatio.mapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryReceiver extends BroadcastReceiver {
    public BatteryReceiver(Handler handler){
        super();
        this.handler = handler;
    }
    private Handler handler;

    public interface Handler{
        void onBatteryChanged(Intent batteyIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent){
//        int currLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);  //当前电量
//        int total = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);      //总电量
//        int technology = intent.getIntExtra(BatteryManager.EXTRA_TECHNOLOGY, 2); //电池型号
//        int health = intent.getIntExtra(EXTRA_HEALTH, BATTERY_HEALTH_UNKNOWN);//健康状态
//        int status = intent.getIntExtra(EXTRA_STATUS, BATTERY_STATUS_UNKNOWN);//电池状态
//        int batteryVolt = intent.getIntExtra(EXTRA_VOLTAGE, -1);//电压
//        int temperature = intent.getIntExtra(EXTRA_TEMPERATURE, -1);//温度
//        int percent = currLevel * 100 / total;
//        String batteryTechnologyDescript = intent.getStringExtra(EXTRA_TECHNOLOGY);//电池类型

        handler.onBatteryChanged(intent);
    }
}
