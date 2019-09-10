package com.sinovatio.iesi.presenter;

import com.sinovatio.iesi.contract.EquipmentDetailsContract;
import com.sinovatio.iesi.model.EquipmentDetailsModel;

public class EquipmentDetailsPresenter implements EquipmentDetailsContract.Presenter {
    private EquipmentDetailsContract.View view;
    private EquipmentDetailsContract.Model model;

    public EquipmentDetailsPresenter(EquipmentDetailsContract.View v){
        view=v;
        model=new EquipmentDetailsModel();
    }

//    @Override
//    public void initList() {
//       view.setEquipInforList(model.getEquipInforList());
//    }
}
