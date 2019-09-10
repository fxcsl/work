package com.sinovatio.mapp.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity( createInDb=false)
public class DeviceFactoryEntity {
    @Id(autoincrement = true)
    private Long id;
    private String threeByteMac= "";
    private String factory= "";
    @Generated(hash = 1858385809)
    public DeviceFactoryEntity(Long id, String threeByteMac, String factory) {
        this.id = id;
        this.threeByteMac = threeByteMac;
        this.factory = factory;
    }
    @Generated(hash = 900585758)
    public DeviceFactoryEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getThreeByteMac() {
        return this.threeByteMac;
    }
    public void setThreeByteMac(String threeByteMac) {
        this.threeByteMac = threeByteMac;
    }
    public String getFactory() {
        return this.factory;
    }
    public void setFactory(String factory) {
        this.factory = factory;
    }


}
