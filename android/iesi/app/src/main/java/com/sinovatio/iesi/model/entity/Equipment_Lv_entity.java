package com.sinovatio.iesi.model.entity;

import java.util.List;

public class Equipment_Lv_entity {

    public String getDicPorperty() {
        return dicPorperty;
    }

    public void setDicPorperty(String dicPorperty) {
        this.dicPorperty = dicPorperty;
    }

    private String dicPorperty;
    /**
     * dicValue : ZB01
     * equipments : [{"uuid":"11","equipmentName":"equipment1","equipmentType":"ZB01"},{"uuid":"12","equipmentName":"equipment2","equipmentType":"ZB01"},{"uuid":"13","equipmentName":"equipment3","equipmentType":"ZB01"}]
     */

    private String dicValue;
    private List<EquipmentsBean> equipments;

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public List<EquipmentsBean> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentsBean> equipments) {
        this.equipments = equipments;
    }

}
