package com.sinovatio.iesi.presenter;

import android.support.annotation.NonNull;

import com.sinovatio.iesi.BaseBean;
import com.sinovatio.iesi.contract.MainContract;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.MainModel;
import com.sinovatio.iesi.model.entity.VersionBean;

import io.reactivex.functions.Consumer;

public class MainPresenter implements MainContract.Presenter {

    @NonNull
    private MainContract.View v;
    private MainContract.Model model;

    public MainPresenter(@NonNull MainContract.View view){
        v=view;
        model=new MainModel();
    }

    @Override
    public void insert_select_adb(String s) {

//        LitePal.deleteAll(UserInfo.class);//清空表
//        UserInfo u=new UserInfo();
//        u.setIntroduction("who we are？");
//        u.setNames("zhjp");
////        u.save();//保存数据
//        if( u.save()) {
//            UserInfo us = LitePal.findFirst(UserInfo.class);//查询
//            v.success(us);
//        }else {
//            v.fail("没有数据！");
//        }


        model.checkAPKVersion()
                .compose(RxScheduler.<BaseBean<VersionBean>>Flo_io_main())
                .subscribe(new Consumer<BaseBean<VersionBean>>() {
                    @Override
                    public void accept(BaseBean<VersionBean> bean) throws Exception {
//                        VersionBean version =  bean.getResult();
//                        int versionCode = version.getVersionCode();
                        v.HttpSuccess(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}
