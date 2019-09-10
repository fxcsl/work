package com.sinovatio.iesi.model;

import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.EquipmentInforContract;
import com.sinovatio.iesi.http.RetrofitClient;
import com.sinovatio.iesi.model.entity.Equipment_Lv_entity;
import com.sinovatio.iesi.model.entity.Equipment_recycler_entity;
import com.sinovatio.iesi.model.entity.EquipmentsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

import static com.sinovatio.iesi.Myapplication.context;

/**
 * 携带装备m层
 */
public class EquipmentInforModel implements EquipmentInforContract.Model {

//    private int[] icon_name = {R.mipmap.qiuji_icon, R.mipmap.z_camera, R.mipmap.s_camera, R.mipmap.gps_blue, R.mipmap.talkie_blue, R.mipmap.arms_blue, R.mipmap.safety_blue, R.mipmap.computer_blue};
//    String[] items = context.getResources().getStringArray(R.array.equipment_list);


//    @Override
//    public List<Equipment_Lv_entity> getVEquipList() {
//        List<Equipment_Lv_entity> list=new ArrayList<>();
//        for (int i=0;i<icon_name.length;i++){
//            Equipment_Lv_entity equi=new Equipment_Lv_entity();
//            equi.setIv_icon(icon_name[i]);
//            equi.setTitle(items[i]);
//            equi.setId(i);
//            list.add(equi);
//        }
//        return list;
//    }

    @Override
    public Flowable<BaseArrayBean<Object>> equipmentTypes() {
        return RetrofitClient.getInstance().getInterface().equipmentTypes();
    }

    @Override
    public List<Equipment_Lv_entity> getListBean(List json) {
        List<Equipment_Lv_entity> list = new ArrayList<>();
        for (int i = 0; i < json.size(); i++) {
            LinkedTreeMap<String, Object> linkmap_1 = (LinkedTreeMap<String, Object>) json.get(i);
            Equipment_Lv_entity ele = new Equipment_Lv_entity();
            ele.setDicValue((String) linkmap_1.get("dicValue"));
//                ele.setDicValue("type0"+(i+1));
            ele.setDicPorperty((String) linkmap_1.get("dicProperty"));
            List list_c = (List) linkmap_1.get("equipmentInfoVOS");
            List<EquipmentsBean> list_bean = new ArrayList<>();
            for (int t = 0; t < list_c.size(); t++) {
                LinkedTreeMap<String, Object> linkmap_c = (LinkedTreeMap<String, Object>) list_c.get(t);
                EquipmentsBean eb = new EquipmentsBean();
                eb.setUuid((String) linkmap_c.get("uuid"));
                eb.setEquipmentName((String) linkmap_c.get("equipmentName"));
//                  eb.setEquipmentType(jb_bean.getString("equipmentType"));
                eb.setType(0);
                list_bean.add(eb);
            }
            ele.setEquipments(list_bean);
            list.add(ele);
        }

        return list;
    }

    @Override
    public Flowable<BaseArrayBean<Object>> accountEquipment(String missionId, String account) {
        return RetrofitClient.getInstance().getInterface().accountEquipment(missionId, account);
    }

    //
    @Override
    public List<Equipment_recycler_entity> getRecyclerBean(LinkedTreeMap<String, String> json_map) {
        List<Equipment_recycler_entity> list = new ArrayList<>();
//    LinkedTreeMap<String,String>  linktree=
        if (json_map.get("lastEquipmentId") != "" && json_map.get("lastEquipmentId") != ",") {
            String[] lastEquipmentId = json_map.get("lastEquipmentId").split(",");
            String[] dicPorperty = json_map.get("dicProperty").split(",");
            String[] equmentName = json_map.get("equipmentName").split(",");
            String[] equmentType = json_map.get("equipmentType").split(",");
//            String[] equmentType = {"APPZB01", "APPZB01", "APPZB01", "APPZB01", "APPZB01"};
            for (int i = 0; i < lastEquipmentId.length; i++) {
                Equipment_recycler_entity equi = new Equipment_recycler_entity();
                equi.setType(0);
                equi.setDicPorperty(dicPorperty[i]);
                equi.setEqumentName(equmentName[i]);
                equi.setEqumentType(equmentType[i]);
                equi.setLastEquipmentId(lastEquipmentId[i]);
                list.add(equi);
            }
        }
        return list;
    }

    @Override
    public Flowable<BaseArrayBean<Object>> deleteLastEquipment(String lastEquipmentId, String missionId, String account) {
        return RetrofitClient.getInstance().getInterface().deleteLastEquipment(lastEquipmentId, missionId, account);
    }

    @Override
    public Flowable<BaseArrayBean<Object>> updateLastEquipment(String lastEquipmentId, String missionId, String account) {
        return RetrofitClient.getInstance().getInterface().updateLastEquipment(lastEquipmentId, missionId, account);
    }
}
