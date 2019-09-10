package com.sinovatio.mapp.bean;

import android.net.wifi.ScanResult;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;
import com.sinovatio.mapp.greendao.DaoSession;
import com.sinovatio.mapp.model.db.DeviceFactoryUtils;
import com.sinovatio.mapp.utils.WifiSupport;

@SmartTable
public class WifiInfoBean implements Comparable<WifiInfoBean> {
    public WifiInfoBean( String name, String brand, int rssi, int frequency, int channel, String
            mac) {
        this.name = name;
        this.brand = brand;
        this.rssi = rssi;
        this.frequency = frequency;
        this.channel = channel;
        this.mac = mac;
    }

    public WifiInfoBean(ScanResult sr,DaoSession daoSession){
        this.name = sr.SSID;
        this.brand = DeviceFactoryUtils.getFactoryByMac(daoSession,sr.BSSID);;
        this.rssi = sr.level;
        this.frequency = sr.frequency;
        this.channel  = WifiSupport.getChannelByFrequency(sr.frequency);
        this.mac = sr.BSSID;
    }


    @SmartColumn(id = 0, name = "名称")
    private String name;
    @SmartColumn(id = 1, name = "设备")
    private String brand;
    @SmartColumn(id = 2, name = "强度")
    private int rssi;
    @SmartColumn(id = 3, name = "频率")
    private int frequency;
    @SmartColumn(id = 4, name = "信道")
    private int channel;
    @SmartColumn(id = 5, name = "MAC")
    private String mac;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Object[] toArray(){
        return new Object[]{name,brand,rssi,frequency,channel,mac};
    }


    @Override
    public int compareTo(WifiInfoBean o) {
        int level1 = this.getRssi();
        int level2 = o.getRssi();
        return level1 - level2;
    }
}
