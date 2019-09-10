package com.sinovatio.iesi.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.StatusContract;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.StatusModel;
import com.sinovatio.iesi.model.entity.StatusBean;
import com.sinovatio.iesi.model.entity.TargetArchivesInfo_entity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class StatusPresenter implements StatusContract.Presenter {
    StatusContract.View view;
    StatusContract.Model model;

    public StatusPresenter(StatusContract.View view) {
        this.view = view;
        model = new StatusModel();
    }

    @Override
    public void changeGroupWorkingState(String account, String missionId, String dicValue) {
        view.showLoading();
        model.changeGroupWorkingState(account, missionId, dicValue).compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main()).as(view.<BaseArrayBean<Object>>bindAutoDispose()).subscribe(new Consumer<BaseArrayBean<Object>>() {
            @Override
            public void accept(BaseArrayBean<Object> objectBaseArrayBean) throws Exception {
                if(objectBaseArrayBean.getCode().equals("200")){
                    view.updateStateSuccess("更新成功！");
                }else {
                    view.httpError(1,objectBaseArrayBean.getMessage());
                }
                view.hideLoading();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                view.httpError(0,"数据提交失败");
                view.hideLoading();
            }
        });
    }

    @Override
    public void getWorkingStateDic() {
//        view.showLoading();
        model.getWorkingStateDic().compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main()).as(view.<BaseArrayBean<Object>>bindAutoDispose())
                .subscribe(new Consumer<BaseArrayBean<Object>>() {
                    @Override
                    public void accept(BaseArrayBean<Object> objectBaseArrayBean) throws Exception {
                        if(objectBaseArrayBean.getCode().equals("200")) {
                            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                            List<StatusBean> list = new ArrayList<>();
                            for (int i = 0; i < objectBaseArrayBean.getResult().size(); i++) {
                                StatusBean statusBean = new StatusBean();
                                LinkedTreeMap<String, String> linkmap_1 = (LinkedTreeMap<String, String>) objectBaseArrayBean.getResult().get(i);
                                String jsonString = gson.toJson(linkmap_1);
                                statusBean = gson.fromJson(jsonString, StatusBean.class);
                                list.add(statusBean);
                            }
                            view.getStateDicSuccess(list);
                        }else {
                            view.httpError(2,objectBaseArrayBean.getMessage());
                        }
//                        view.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.httpError(2,"数据获取失败");
//                        view.hideLoading();
                    }
                });
    }
}
