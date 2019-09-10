package com.sinovatio.mapp.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Upload_SimBean {
    @JSONField(ordinal=1)
    private String imei;
    @JSONField(ordinal=2)
    private String imsi;
    @JSONField(ordinal=3)
    private String mac;
    @JSONField(ordinal=4)
    private String phone_number;
    @JSONField(ordinal=5)
    private String phone_model;
    @JSONField(ordinal=6)
    private String isp;
    @JSONField(ordinal=7)
    private String longitude;
    @JSONField(ordinal=8)
    private String latitude;
    @JSONField(ordinal = 9)
    private String capturetime="";

    public String getCapturetime() {
        return capturetime;
    }

    public void setCapturetime(String capturetime) {
        this.capturetime = capturetime;
    }



    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPhone_model() {
        return phone_model;
    }

    public void setPhone_model(String phone_model) {
        this.phone_model = phone_model;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
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
}
