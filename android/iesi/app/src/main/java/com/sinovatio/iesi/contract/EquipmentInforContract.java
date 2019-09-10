package com.sinovatio.iesi.contract;

import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;
import com.sinovatio.iesi.model.entity.Equipment_Lv_entity;
import com.sinovatio.iesi.model.entity.Equipment_recycler_entity;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EquipmentInforContract {

    interface Model{
        //获取装备类别列表数据
        Flowable<BaseArrayBean<Object>> equipmentTypes();
        //数据实体转换
        List<Equipment_Lv_entity>  getListBean(List json);
        //获取已穿戴装备列表数据
        Flowable<BaseArrayBean<Object>> accountEquipment(String missionId,String account);
        //数据实体转换
        List<Equipment_recycler_entity> getRecyclerBean(LinkedTreeMap<String,String> json);
//删除设备
        Flowable<BaseArrayBean<Object>> deleteLastEquipment(String lastEquipmentId, String missionId, String account);

        //上传已分配设备

        Flowable<BaseArrayBean<Object>> updateLastEquipment(String lastEquipmentId,String missionId, String account);
    }

    interface View extends BaseView<Presenter> {

        //网络访问失败
        void onError(Throwable throwable);
        //装备类别 访问成功
        void equipmentTypesSuccess(List<Equipment_Lv_entity> el);
        //获取已穿戴设备列表
        void setREquipList( List<Equipment_recycler_entity> list_recycler);
        //删除设备
        void deleteLastEquipmentSuccess();
        //上传设备
        void updateLastEquipmentSuccess();
    }

    interface Presenter extends BasePresenter {

        //初始化设备列表数据
        void initLvList();
        //初始化已穿戴设备列表数据
        void initRcList(String missionId,String account);

        //删除设备
        void deleteLastEquipment(String lastEquipmentId, String missionId, String account);

        //上传设备
        void updateLastEquipment(String lastEquipmentId, String missionId, String account);
    }
}
