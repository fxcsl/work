package com.sinovatio.iesi.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.EquipmentDetailsContract;
import com.sinovatio.iesi.model.entity.Equipment_recycler_entity;
import com.sinovatio.iesi.model.entity.EquipmentsBean;
import com.sinovatio.iesi.presenter.EquipmentDetailsPresenter;
import com.sinovatio.iesi.tools.EventBusCarrier;
import com.sinovatio.iesi.view.adapter.EquipDetail_Adapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EquipmentDetailsActivity extends BaseActivity implements EquipmentDetailsContract.View, EquipDetail_Adapter.CheckBoxChangeed {
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.top_view)
    View topView;
    @BindView(R.id.lv)
    ListView lv;

    @BindView(R.id.tv_ok)
    TextView tvOk;
    private EquipmentDetailsContract.Presenter presenter;
    EquipDetail_Adapter equipDetail_adapter;
    List<EquipmentsBean> list = new ArrayList<>();
    List<EquipmentsBean> list_event = new ArrayList<>();

    String dicPorperty="";//类别名
    String divValue="";//类别id
    String choice="";//已选择id
    public int p_id;//装备类别id

    @Override
    public int getLayoutId() {
        return R.layout.activity_equipmentdetails;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        windowColor();
        presenter = new EquipmentDetailsPresenter(this);
//        presenter.initList();
        equipDetail_adapter = new EquipDetail_Adapter(context, list, this);
        lv.setAdapter(equipDetail_adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).getType() == 0) {
                    list.get(position).setType(1);
                } else {
                    list.get(position).setType(0);
                }
                equipDetail_adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dicPorperty=getIntent().getStringExtra("DicPorperty");
        divValue=getIntent().getStringExtra("DicValue");
        choice=getIntent().getStringExtra("Choice");
        Bundle bundle=getIntent().getBundleExtra("bundle");
        List<EquipmentsBean> list_1=bundle.getParcelableArrayList("list");
        listTo(list_1);//状态转换

        equipDetail_adapter.notifyDataSetChanged();
    }

    //状态转换
    public void listTo(List<EquipmentsBean> list_1){
        list.clear();
        String[] ids=choice.split(",");
        for (int i=0;i<list_1.size();i++){
            for (int t=0;t<ids.length;t++){
                if(ids[t].equals(list_1.get(i).getUuid())){
                    list_1.get(i).setIdChoice(1);
                    break;
                }
            }
        }
        list.addAll(list_1);
    }

    @OnClick({R.id.tv_exit, R.id.top_view,R.id.tv_ok})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_exit:
            case R.id.top_view:
                finishs();
                break;
            case R.id.tv_ok:
                List<Equipment_recycler_entity> list_now=new ArrayList<>();
                for (int i=0;i<list_event.size();i++){
                    Equipment_recycler_entity equipment_recycler_entity=new Equipment_recycler_entity();
                    equipment_recycler_entity.setLastEquipmentId(list_event.get(i).getUuid());
                    equipment_recycler_entity.setEqumentType(divValue);
                    equipment_recycler_entity.setDicPorperty(dicPorperty);
                    equipment_recycler_entity.setEqumentName(list_event.get(i).getEquipmentName());
                    equipment_recycler_entity.setIsNew(1);
                    list_now.add(equipment_recycler_entity);
                }

                EventBusCarrier eventBusCarrier = new EventBusCarrier();
                eventBusCarrier.setEventType("1");
                eventBusCarrier.setList(list_now);
                EventBus.getDefault().post(eventBusCarrier); //普通事件发布 //
                finishs();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishs();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void finishs() {
        finish();
        overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
    }


    public void windowColor() {
        Window window = getWindow();
        //取消设置Window半透明的Flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏为透明
        window.setStatusBarColor(getResources().getColor(R.color.color_4c686969));
    }


    @Override
    public void checkBoxChange(int position) {
//        Toast.makeText(context,pos+"",Toast.LENGTH_SHORT).show();
        if (list.get(position).getType() == 0) {
            list_event.add(list.get(position));
            list.get(position).setType(1);
        } else {
            for (int i = 0; i < list_event.size(); i++) {
                if (list.get(position).getUuid().equals(list_event.get(i).getUuid())) {
                    list_event.remove(i);
                    break;
                }
            }
            list.get(position).setType(0);
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
}
