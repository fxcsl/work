package com.sinovatio.iesi.presenter;

import android.support.annotation.NonNull;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.LoginContract;
import com.sinovatio.iesi.contract.TaskContract;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.LoginModel;
import com.sinovatio.iesi.model.TaskModel;

import io.reactivex.functions.Consumer;

public class TaskPresenter implements TaskContract.Presenter {
    @NonNull
    private TaskContract.View view;
    private TaskContract.Model model;

    public TaskPresenter(@NonNull TaskContract.View view){
        this.view = view;
        this.model = new TaskModel();
    }

    @Override
    public void getTaskList(String account, Integer missionState) {
        view.showLoading();
        model.getTaskList(account, missionState)
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
}
