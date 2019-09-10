package com.sinovatio.iesi.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.TargetInfoQueryContract;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.TargetInfoQueryModel;
import com.sinovatio.iesi.model.entity.TargetBean;
import com.sinovatio.iesi.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class TargetInfoQueryPresenter implements TargetInfoQueryContract.Presenter {
    @NonNull
    private TargetInfoQueryContract.View view;
    private TargetInfoQueryContract.Model model;
    private List<TargetBean> list = new ArrayList<>();
    public TargetInfoQueryPresenter(@NonNull TargetInfoQueryContract.View view){
        this.view = view;
        this.model = new TargetInfoQueryModel();
    }

    @Override
    public void getTargetUser(String account, String missionId,String queryType) {
        view.showLoading();
        model.getTargetUser(account, missionId,queryType)
                .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                .as(view.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                    @Override
                    public void accept(BaseArrayBean<Object> bean) throws Exception {
                        if (bean.getCode().equals("200")) {
                            list.clear();
                            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                            TargetBean targetBean = new TargetBean();
                            List listTarget = bean.getResult();
                            for (int i = 0; i < listTarget.size(); i++) {
                                LinkedTreeMap<String, Object> tmTarget = (LinkedTreeMap<String, Object>) bean.getResult().get(i);
                                String jsonString = gson.toJson(tmTarget);
                                targetBean = gson.fromJson(jsonString,TargetBean.class);
//                                targetBean.setTargetId((String) tmTarget.get("targetId"));
//                                targetBean.setName((String) tmTarget.get("name"));
//                                targetBean.setIdentityCode((String) tmTarget.get("identityCode"));
//                                targetBean.setRefFlag((String) tmTarget.get("refFlag"));
                                list.add(targetBean);
                            }
                        }
                        view.onSuccess(list);
                        view.hideLoading();
                    }
                },new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                view.hideLoading();
                ToastUtil.toast("目标信息查询失败");
                view.onError(throwable);
            }
        });

    }

    @Override
    public void refTargetUser(String account, String missionId, String targetId, String name, String identityCode, String bdType) {
        view.showLoading();
        model.refTargetUser(account,missionId, targetId, name, identityCode, bdType)
                .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                .as(view.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                    @Override
                    public void accept(BaseArrayBean<Object> bean) throws Exception {
                        view.onbdTypeSuccess(bean);
                        view.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.onError(throwable);
                        view.hideLoading();
                    }
                });
    }
}
