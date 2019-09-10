package com.sinovatio.iesi.tools;

import com.sinovatio.iesi.model.entity.Equipment_recycler_entity;
import com.sinovatio.iesi.model.entity.EquipmentsBean;

import java.util.List;

/**
 * EventBus实体类
 */
public class EventBusCarrier {

    private String eventType; //区分事件的类型

    private Object object; //事件的实体类
    List<Equipment_recycler_entity> list;//装备集合

    public List<Equipment_recycler_entity> getList() {
        return list;
    }

    public void setList(List<Equipment_recycler_entity> list) {
        this.list = list;
    }


    public String getEventType() {
        return eventType;
    }


    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getObject() {
        return object;
    }



    public void setObject(Object object) {
        this.object = object;
    }

}
