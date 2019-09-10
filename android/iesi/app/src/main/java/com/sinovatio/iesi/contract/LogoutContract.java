package com.sinovatio.iesi.contract;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;
import com.sinovatio.iesi.model.entity.TargetBean;

import java.util.List;

import io.reactivex.Flowable;

public interface LogoutContract {

    interface Model {
        Flowable<BaseArrayBean<Object>> logout(String account);
        //获取绑定目标名称数据
        Flowable<BaseArrayBean<Object>> getTargetUser(String account, String missionId, String queryType);

        //获取当前工作状态
        Flowable<BaseArrayBean<Object>> getWorkingState(String account, String missionId);
    }

    interface View extends BaseView<Presenter>{
        void onSuccess(BaseArrayBean<Object> bean);
        void onQuerySuccess(List<TargetBean> bean);
        void onError(Throwable throwable);
        void onWorkingState(int isLeader,String workingState);
    }

    interface Presenter extends BasePresenter{
        void logout(String account);
        void getTargetUser(String account, String missionId,String queryType);
        void getWorkingState(String account, String missionId);
    }
}
