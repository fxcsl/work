package com.sinovatio.iesi.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.TargetProfileQueryContract;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.TargetProfileQueryModel;
import com.sinovatio.iesi.model.entity.ProfileBean;
import com.sinovatio.iesi.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class TargetProfileQueryPresenter implements TargetProfileQueryContract.Presenter {

    @NonNull
    private TargetProfileQueryContract.View view;
    private TargetProfileQueryContract.Model model;
    private List<ProfileBean> list = new ArrayList<>();
    public TargetProfileQueryPresenter(@NonNull TargetProfileQueryContract.View view){
        this.view = view;
        this.model = new TargetProfileQueryModel();
    }

    @Override
    public void getTargetList(String account, String missionId) {
        view.showLoading();
        model.getTargetList(account, missionId)
                .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
                .as(view.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                    @Override
                    public void accept(BaseArrayBean<Object> bean) throws Exception {
                        if (bean.getCode().equals("200")) {
                            list.clear();
                            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                            ProfileBean profileBean = new ProfileBean();
                            List listTarget = bean.getResult();
                            for (int i = 0; i < listTarget.size(); i++) {
                                LinkedTreeMap<String, Object> tmTarget = (LinkedTreeMap<String, Object>) bean.getResult().get(i);
                                String jsonString = gson.toJson(tmTarget);
                                profileBean = gson.fromJson(jsonString,ProfileBean.class);
//                                profileBean.setId((String) tmTarget.get("id"));
//                                profileBean.setName((String) tmTarget.get("name"));
//                                profileBean.setCaseCode((String) tmTarget.get("caseCode"));
//                                profileBean.setHeadPic((String) tmTarget.get("headPic"));
                                list.add(profileBean);
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
}
