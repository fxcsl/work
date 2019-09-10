package com.sinovatio.mapp.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Upload_Wifi_Bean {
    @JSONField(ordinal = 1)
    private String imei = "";
    @JSONField(ordinal = 2)
    private String ssid = "";
    @JSONField(ordinal = 3)
    private String wifi_mac = "";
    @JSONField(ordinal = 4)
    private String device_type = "";
    @JSONField(ordinal = 5)
    private String signal_value = "";
    @JSONField(ordinal = 6)
    private String channel = "";
    @JSONField(ordinal = 7)
    private String longitude = "";
    @JSONField(ordinal = 8)
    private String latitude = "";
    @JSONField(ordinal = 9)
    private String capturetime="";

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getWifi_mac() {
        return wifi_mac;
    }

    public void setWifi_mac(String wifi_mac) {
        this.wifi_mac = wifi_mac;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getSignal_value() {
        return signal_value;
    }

    public void setSignal_value(String signal_value) {
        this.signal_value = signal_value;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCapturetime() {
        return capturetime;
    }

    public void setCapturetime(String capturetime) {
        this.capturetime = capturetime;
    }

}
