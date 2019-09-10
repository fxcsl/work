package com.sinovatio.mapp.model.db;

import com.sinovatio.mapp.entity.DeviceFactoryEntity;
import com.sinovatio.mapp.greendao.DaoSession;
import com.sinovatio.mapp.greendao.DeviceFactoryEntityDao;


public class DeviceFactoryUtils {

    /**
     * 查询设备名称
     * @param daoSession
     * @param mac
     * @return
     */
    public static String getFactoryByMac(DaoSession daoSession, String mac){
        String factory = "UNKNOW";
        String key = mac.toUpperCase().replaceAll(":|-","").substring(0,6);
        DeviceFactoryEntity dfe = daoSession.getDeviceFactoryEntityDao().queryBuilder().where
                (DeviceFactoryEntityDao
                        .Properties.ThreeByteMac.eq(key)).unique();
        if(dfe!=null){
            factory = dfe.getFactory();
        }
        //LogUtil.d("current mac:"+mac+";current factory："+factory);
        return factory;
    }

    public static void main(String[] args){
        String a = "b0:d5:9d:cd:e2:a7";
        String b = "b0-d5-9d-cd-e2-a7";
        String c = "b0d59dcde2a7";
        String result = b.toUpperCase().replaceAll(":|-","").substring(0,6);

        System.out.println("aaaa:"+result);



    }
}
