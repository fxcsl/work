package com.sinovatio.iesi.model.entity;

public class Equipment_recycler_entity {


    /**
     * lastEquipmentId : 63,51
     * dicPorperty : 单警设备,通讯设备
     * equmentName : equipment3,equipment1
     * equmentType : ZB06,ZB05
     */

    private String lastEquipmentId;
    private String dicPorperty;
    private String equmentName;
    private String equmentType;
    private int type=0;//是否已选择
    private int isNew=0;//0，后台获取，1新加未上传

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLastEquipmentId() {
        return lastEquipmentId;
    }

    public void setLastEquipmentId(String lastEquipmentId) {
        this.lastEquipmentId = lastEquipmentId;
    }

    public String getDicPorperty() {
        return dicPorperty;
    }

    public void setDicPorperty(String dicPorperty) {
        this.dicPorperty = dicPorperty;
    }

    public String getEqumentName() {
        return equmentName;
    }

    public void setEqumentName(String equmentName) {
        this.equmentName = equmentName;
    }

    public String getEqumentType() {
        return equmentType;
    }

    public void setEqumentType(String equmentType) {
        this.equmentType = equmentType;
    }
}
