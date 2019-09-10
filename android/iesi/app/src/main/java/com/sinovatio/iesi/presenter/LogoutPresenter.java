package com.sinovatio.iesi.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.LoginContract;
import com.sinovatio.iesi.contract.LogoutContract;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.LogoutModel;
import com.sinovatio.iesi.model.entity.NowStateBean;
import com.sinovatio.iesi.model.entity.TargetBean;
import com.sinovatio.iesi.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.functions.Consumer;

public class LogoutPresenter implements LogoutContract.Presenter {

    @NonNull
    private LogoutContract.View view;
    private LogoutContract.Model model;

    private List<TargetBean> list = new ArrayList<>();

    public LogoutPresenter(@NonNull LogoutContract.View view){
        this.view = view;
        this.model = new LogoutModel();
    }

    @Override
    public void logout(String account) {
        view.showLoading();
        model.logout(account)
                .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                .as(view.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                    @Override
                    public void accept(BaseArrayBean<Object> bean) throws Exception {
                        view.onSuccess(bean);
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
                            List listTarget = bean.getResult();
                            TargetBean targetBean = new TargetBean();
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
                        view.onQuerySuccess(list);
                        view.hideLoading();
                    }
                },new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.hideLoading();
                        ToastUtil.toast("绑定目标信息查询失败");
                        view.onError(throwable);
                    }
                });

    }

    @Override
    public void getWorkingState(String account, String missionId) {
        model.getWorkingState(account,missionId).compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                .as(view.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                    @Override
                    public void accept(BaseArrayBean<Object> objectBaseArrayBean) throws Exception {
                        if(objectBaseArrayBean.getCode().equals("200")) {
                            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                            NowStateBean targetBean = new NowStateBean();
                            LinkedTreeMap<String, Object> tmTarget = (LinkedTreeMap<String, Object>) objectBaseArrayBean.getResult().get(0);
                            String jsonString = gson.toJson(tmTarget);
                            targetBean = gson.fromJson(jsonString,NowStateBean.class);
                            view.onWorkingState(targetBean.getIsLeader(),targetBean.getWorkingState());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.onError(throwable);
                    }
                });
    }
}
