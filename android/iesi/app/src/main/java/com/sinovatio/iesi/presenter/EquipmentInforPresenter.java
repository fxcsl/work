package com.sinovatio.iesi.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.EquipmentInforContract;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.EquipmentInforModel;
import com.sinovatio.iesi.model.entity.Equipment_Lv_entity;
import com.sinovatio.iesi.model.entity.EquipmentsBean;
import com.sinovatio.iesi.tools.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class EquipmentInforPresenter implements EquipmentInforContract.Presenter {

    @NonNull
    private EquipmentInforContract.View v;
    private EquipmentInforContract.Model model;

    public EquipmentInforPresenter(@NonNull EquipmentInforContract.View view) {
        v = view;
        model = new EquipmentInforModel();
    }


    @Override
    public void initLvList() {
        v.showLoading();
        model.equipmentTypes()
                .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                .as(v.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                               @Override
                               public void accept(BaseArrayBean<Object> objectBaseArrayBean) throws Exception {
                                   if (objectBaseArrayBean.getCode().equals("200")) {
                                       v.equipmentTypesSuccess(model.getListBean(objectBaseArrayBean.getResult()));
                                   } else {
                                       ToastUtil.toast(objectBaseArrayBean.getMessage());
                                   }
                                   v.hideLoading();
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                v.onError(throwable);
                                v.hideLoading();
                                ToastUtil.toast("数据获取失败");
                            }
                        });
    }

    @Override
    public void initRcList(String missionId,String account) {
        v.showLoading();
        model.accountEquipment(missionId,account)
                .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                .as(v.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                    @Override
                    public void accept(BaseArrayBean<Object> stringBaseArrayBean) throws Exception {
                        //{"code":"200","message":"success!","result":[{"lastEquipmentId":"63,51","dicPorperty":"单警设备,通讯设备","equmentName":"equipment3,equipment1","equmentType":"ZB06,ZB05"}]}
//                        Log.i("httpssssss",stringBaseArrayBean.getResult().get(0).toString());
                        if (stringBaseArrayBean.getCode().equals("200")) {
                            v.setREquipList(model.getRecyclerBean((LinkedTreeMap<String,String>)stringBaseArrayBean.getResult().get(0)));
                        } else {
                            ToastUtil.toast(stringBaseArrayBean.getMessage());
                        }
                        v.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        v.onError(throwable);
                        ToastUtil.toast("数据获取失败");
                        v.hideLoading();
                    }
                });
    }

    @Override
    public void deleteLastEquipment(String lastEquipmentId, String missionId, String account) {
        v.showLoading();
        model.deleteLastEquipment(lastEquipmentId,missionId,account)
                .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                .as(v.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                    @Override
                    public void accept(BaseArrayBean<Object> objectBaseArrayBean) throws Exception {
                        if(objectBaseArrayBean.getCode().equals("200")){
                            v.deleteLastEquipmentSuccess();
                            ToastUtil.showToast(objectBaseArrayBean.getMessage(),R.mipmap.success_icon);
                        }else {
                            ToastUtil.toast(objectBaseArrayBean.getMessage());
                        }
                        v.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        v.hideLoading();
                        ToastUtil.toast("设备删除失败");
                        v.onError(throwable);
                    }
                });
    }

    @Override
    public void updateLastEquipment(String lastEquipmentId, String missionId, String account) {
         v.showLoading();
         model.updateLastEquipment(lastEquipmentId,missionId,account)
                 .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                 .as(v.<BaseArrayBean<Object>>bindAutoDispose())
                 .subscribe(new Consumer<BaseArrayBean<Object>>() {
                     @Override
                     public void accept(BaseArrayBean<Object> objectBaseArrayBean) throws Exception {
                         if(objectBaseArrayBean.getCode().equals("200")){
                             v.updateLastEquipmentSuccess();
                             ToastUtil.showToast(objectBaseArrayBean.getMessage(),R.mipmap.success_icon);
                         }else {
                             ToastUtil.showToast(objectBaseArrayBean.getMessage(),R.mipmap.error_icon);
                         }
                         v.hideLoading();
                     }
                 }, new Consumer<Throwable>() {
                     @Override
                     public void accept(Throwable throwable) throws Exception {
                         v.hideLoading();
                         ToastUtil.toast("设备上报失败");
                         v.onError(throwable);
                     }
                 });
    }
}
