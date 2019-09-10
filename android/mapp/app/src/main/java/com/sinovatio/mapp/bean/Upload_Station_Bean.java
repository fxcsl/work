package com.sinovatio.mapp.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class Upload_Station_Bean {
    @JSONField(ordinal = 1)
    private String imei="";
    @JSONField(ordinal = 2)
    private String mcc="";
    @JSONField(ordinal = 3)
    private String mnc="";
    @JSONField(ordinal = 4)
    private  String base_station_model="";
    @JSONField(ordinal = 5)
    private String base_station_code="";
    @JSONField(ordinal = 6)
    private String signal_value="";
    @JSONField(ordinal = 7)
    private String frequency_band="";
    @JSONField(ordinal = 8)
    private String frequency_point="";
    @JSONField(ordinal = 9)
    private String data_network="";
    @JSONField(ordinal = 10)
    private String community_type="";
    @JSONField(ordinal = 11)
    private String longitude="";
    @JSONField(ordinal = 12)
    private String latitude="";
    @JSONField(ordinal = 13)
    private String capturetime="";

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getBase_station_model() {
        return base_station_model;
    }

    public void setBase_station_model(String base_station_model) {
        this.base_station_model = base_station_model;
    }

    public String getBase_station_code() {
        return base_station_code;
    }

    public void setBase_station_code(String base_station_code) {
        this.base_station_code = base_station_code;
    }

    public String getSignal_value() {
        return signal_value;
    }

    public void setSignal_value(String signal_value) {
        this.signal_value = signal_value;
    }

    public String getFrequency_band() {
        return frequency_band;
    }

    public void setFrequency_band(String frequency_band) {
        this.frequency_band = frequency_band;
    }

    public String getFrequency_point() {
        return frequency_point;
    }

    public void setFrequency_point(String frequency_point) {
        this.frequency_point = frequency_point;
    }

    public String getData_network() {
        return data_network;
    }

    public void setData_network(String data_network) {
        this.data_network = data_network;
    }

    public String getCommunity_type() {
        return community_type;
    }

    public void setCommunity_type(String community_type) {
        this.community_type = community_type;
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
