package com.sinovatio.iesi.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.TargetArchivesInfoContract;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.TargetArchivesInfoModel;
import com.sinovatio.iesi.model.entity.TargetArchivesInfo_entity;
import com.sinovatio.iesi.view.TargetArchivesInfoActivity;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 目标详情mvp p层
 */
public class TargetArchivesInfoPresenter implements TargetArchivesInfoContract.Presenter {
    TargetArchivesInfoContract.View v;
    TargetArchivesInfoModel model;

    public TargetArchivesInfoPresenter(@NonNull TargetArchivesInfoContract.View view) {
        v = view;
        model = new TargetArchivesInfoModel();
    }

    @Override
    public void getCaseInfo(String account, String caseId) {
        v.showLoading();
        model.getCaseInfo(account,caseId)
                .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                .as(v.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                    @Override
                    public void accept(BaseArrayBean<Object> objectBaseArrayBean) throws Exception {
                        v.hideLoading();
                        if(objectBaseArrayBean.getCode().equals("200")) {
                            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                            LinkedTreeMap<String, String> linkmap_1 = (LinkedTreeMap<String, String>) objectBaseArrayBean.getResult().get(0);
                            TargetArchivesInfo_entity entity = new TargetArchivesInfo_entity();
                            String jsonString = gson.toJson(linkmap_1);
                            entity = gson.fromJson(jsonString,TargetArchivesInfo_entity.class);
//                            entity.setBeforeCase(linkmap_1.get("beforeCase"));
//                            entity.setCaseCode(linkmap_1.get("caseCode"));
//                            entity.setHeadPic(linkmap_1.get("headPic"));
//                            entity.setId(linkmap_1.get("id"));
//                            entity.setLicense(linkmap_1.get("license"));
//                            entity.setName(linkmap_1.get("name"));
//                            entity.setNickName(linkmap_1.get("nickName"));
//                            entity.setOldname(linkmap_1.get("oldname"));
//                            entity.setPhone(linkmap_1.get("phone"));
                            v.getCaseInfoSuccess(entity);
                        }else {
                            v.getError(objectBaseArrayBean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        v.hideLoading();
                        v.getError("获取数据失败");
                    }
                });
    }
}
