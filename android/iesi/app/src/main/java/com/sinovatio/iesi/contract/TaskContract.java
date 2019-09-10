package com.sinovatio.iesi.contract;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;


import io.reactivex.Flowable;

public interface TaskContract {

    interface Model {
        //获取任务列表数据
        public Flowable<BaseArrayBean<Object>> getTaskList(String account, Integer missionState);
    }

    interface View extends BaseView<Presenter>{
        public void onSuccess(BaseArrayBean<Object> bean);
        public void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter{
        public void getTaskList(String account, Integer missionState);
    }
}

