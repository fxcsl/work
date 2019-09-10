package com.sinovatio.iesi.contract;

import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;
import com.sinovatio.iesi.model.entity.Equipment_recycler_entity;
import com.sinovatio.iesi.model.entity.EquipmentsBean;

import java.util.List;

public interface EquipmentDetailsContract {

    interface Model{
        //获取装备列表数据
        List<Equipment_recycler_entity> getEquipInforList();
    }

    interface View extends BaseView<Presenter> {

        //本地装备列表数据
//        void setEquipInforList( List<EquipmentsBean> list);
    }

    interface Presenter extends BasePresenter {

        //初始化设备列表数据
//        void initList();
    }
}
