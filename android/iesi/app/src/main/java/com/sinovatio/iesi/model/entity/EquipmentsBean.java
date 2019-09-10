package com.sinovatio.iesi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public  class EquipmentsBean implements Parcelable {
    /**
     * uuid : 11
     * equipmentName : equipment1
     * equipmentType : ZB01
     */

    private String uuid;
    private String equipmentName;
    private String equipmentType;
    private int type=0;
    private int idChoice=0;

    public EquipmentsBean() {
    }

    protected EquipmentsBean(Parcel in) {
        uuid = in.readString();
        equipmentName = in.readString();
        equipmentType = in.readString();
        type = in.readInt();
        idChoice = in.readInt();
    }

    public static final Creator<EquipmentsBean> CREATOR = new Creator<EquipmentsBean>() {
        @Override
        public EquipmentsBean createFromParcel(Parcel in) {
            return new EquipmentsBean(in);
        }

        @Override
        public EquipmentsBean[] newArray(int size) {
            return new EquipmentsBean[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIdChoice() {
        return idChoice;
    }

    public void setIdChoice(int idChoice) {
        this.idChoice = idChoice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(equipmentName);
        dest.writeString(equipmentType);
        dest.writeInt(type);
        dest.writeInt(idChoice);
    }
}