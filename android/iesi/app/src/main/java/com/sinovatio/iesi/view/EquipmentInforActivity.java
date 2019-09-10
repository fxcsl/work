package com.sinovatio.iesi.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.EquipmentInforContract;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.model.entity.Equipment_Lv_entity;
import com.sinovatio.iesi.model.entity.Equipment_recycler_entity;
import com.sinovatio.iesi.model.entity.EquipmentsBean;
import com.sinovatio.iesi.presenter.EquipmentInforPresenter;
import com.sinovatio.iesi.surfaceview.SpacesItemDecoration;
import com.sinovatio.iesi.tools.EventBusCarrier;
import com.sinovatio.iesi.tools.ToastUtil;
import com.sinovatio.iesi.view.adapter.Equipment_Lv_Adapter;
import com.sinovatio.iesi.view.adapter.Equipment_Recyler_Adapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * 装备详情
 */

public class EquipmentInforActivity extends BaseActivity implements EquipmentInforContract.View, Equipment_Recyler_Adapter.DeteleImageClick {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.imag_detele)
    ImageView imagDetele;
    @BindView(R.id.recycle)
    android.support.v7.widget.RecyclerView recycle;
    @BindView(R.id.recycle_null_tv)
    TextView recycleNullTv;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.tv_update)
    TextView tv_update;


    private EquipmentInforContract.Presenter presenter;
    List<Equipment_recycler_entity> list_recycler = new ArrayList<>();
    Equipment_Recyler_Adapter equipment_recyler_adapter;
    UserInfo userInfo;
    String missionId = "";//任务id
    boolean mIsRefreshing=false;//解决recycler 滑动bug

    @Override
    public int getLayoutId() {
        return R.layout.activity_equipment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        userInfo = LitePal.findFirst(UserInfo.class);//获取用户信息
        if(userInfo!=null) {
            SharedPreferences sh = context.getSharedPreferences("taskId", 0);//获取任务信息
            missionId = sh.getString("MissionId", "0");
            Log.i("https_taskID", missionId);
            presenter = new EquipmentInforPresenter(this);
            presenter.initLvList();//获取设备种类列表
            presenter.initRcList(missionId, userInfo.getAccount());//获取已穿戴设备
            titleTv.setText(R.string.arms_infor);
        }
        EventBus.getDefault().register(this);  //事件的注册

        //设置布局管理器
        //瀑布流
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器
        //垂直分布和横向分布  LinearLayoutManager.HORIZONTAL  VERTICAL
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycle.setLayoutManager(layoutManager);
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));  //相比于第一种，第二种为SlideInUpAnimator添加插值器
        recycle.setItemAnimator(animator);
        //设置动画时长
        recycle.getItemAnimator().setAddDuration(500);
        recycle.getItemAnimator().setRemoveDuration(500);
//        recycle.getItemAnimator().setMoveDuration(1000);
//        recycle.getItemAnimator().setChangeDuration(1000);
        recycle.addItemDecoration(new SpacesItemDecoration(30));
        equipment_recyler_adapter = new Equipment_Recyler_Adapter(context, list_recycler, this);
        recycle.setAdapter(equipment_recyler_adapter);
        recycle.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mIsRefreshing) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this); //解除注册
        super.onDestroy();
    }

    @OnClick({R.id.title_back, R.id.imag_detele, R.id.tv_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.imag_detele:
                for (int i = 0; i < list_recycler.size(); i++) {
                    if (list_recycler.get(i).getType() == 0) {
                        list_recycler.get(i).setType(1);
                    } else {
                        list_recycler.get(i).setType(0);
                    }
                }
                equipment_recyler_adapter.notifyDataSetChanged();
                break;
            case R.id.tv_update:

                presenter.updateLastEquipment(listToString(), missionId, userInfo.getAccount());
                break;
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    //list id 转,String
    public String listToString(){
        String lastEuipmentId = "";
        List<Equipment_recycler_entity> list_re = removeDuplicate(list_recycler);//去重
        for (int i = 0; i < list_re.size(); i++) {
            lastEuipmentId += list_re.get(i).getLastEquipmentId() + ",";
        }
        if (lastEuipmentId.length() > 0)
            lastEuipmentId = lastEuipmentId.substring(0, lastEuipmentId.length() - 1);
        return lastEuipmentId;
    }

    @Override
    public void equipmentTypesSuccess(final List<Equipment_Lv_entity> list_eq) {
        listview.setAdapter(new Equipment_Lv_Adapter(context, list_eq));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(context, EquipmentDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list_eq.get(position).getEquipments()); //传递list对象
                in.putExtra("bundle", bundle);
                in.putExtra("DicPorperty", list_eq.get(position).getDicPorperty());
                in.putExtra("DicValue", list_eq.get(position).getDicValue());
                in.putExtra("Choice",listToString());
                startActivity(in);
                overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);//activity 开启结束动画
            }
        });
    }

    @Override
    public void setREquipList(List<Equipment_recycler_entity> list) {
        mIsRefreshing=true;
        list_recycler.clear();
        list_recycler.addAll(list);
        equipment_recyler_adapter.notifyDataSetChanged();
        ImageType();//处理删除按钮的显示
        mIsRefreshing=false;
    }

    @Override
    public void deleteLastEquipmentSuccess() {
        for (int i = 0; i < list_recycler.size(); i++) {
            if (list_recycler.get(i).getLastEquipmentId() == nowId) {
                list_recycler.remove(i);
                equipment_recyler_adapter.notifyItemRemoved(i);
                break;
            }
        }
        ImageType();
    }

    @Override
    public void updateLastEquipmentSuccess() {
        tv_update.setVisibility(View.GONE);
        presenter.initRcList(missionId, userInfo.getAccount());//获取/刷新已穿戴设备
    }

    public void ImageType() {
        if (list_recycler.size() == 0) {
            imagDetele.setVisibility(View.GONE);
            recycleNullTv.setVisibility(View.VISIBLE);
        } else {
            imagDetele.setVisibility(View.VISIBLE);
            recycleNullTv.setVisibility(View.GONE);
        }
    }

    private String nowId = "";

    @Override
    public void deteleImageClick(String id,int isNew) {
        nowId = id;
        if(isNew==1){
            for (int i = 0; i < list_recycler.size(); i++) {
                if (list_recycler.get(i).getLastEquipmentId() == nowId) {
                    list_recycler.remove(i);
                    equipment_recyler_adapter.notifyItemRemoved(i);
                    break;
                }
            }
            ImageType();
        }else {
            presenter.deleteLastEquipment(id, missionId, userInfo.getAccount());
        }
    }

    // EventBus普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventBusCarrier carrier) {
        List<Equipment_recycler_entity> list_event = carrier.getList();
        if (list_event.size() > 0)
            tv_update.setVisibility(View.VISIBLE);
        list_recycler.addAll(list_event);
        list_change();
        equipment_recyler_adapter.notifyDataSetChanged();
        ImageType();
    }

    //数据统一化
    public void list_change() {
        for (int i = 0; i < list_recycler.size(); i++) {
            list_recycler.get(i).setType(0);
        }
    }


    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }


    //集合去重
    public List<Equipment_recycler_entity> removeDuplicate(List<Equipment_recycler_entity> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).getLastEquipmentId().equals(list.get(i).getLastEquipmentId())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }
}
