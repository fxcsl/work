package com.sinovatio.iesi.model;

import com.sinovatio.iesi.R;
import com.sinovatio.iesi.contract.EquipmentDetailsContract;
import com.sinovatio.iesi.model.entity.Equipment_recycler_entity;

import java.util.ArrayList;
import java.util.List;

public class EquipmentDetailsModel implements EquipmentDetailsContract.Model {
    @Override
    public List<Equipment_recycler_entity> getEquipInforList() {
        List<Equipment_recycler_entity> list=new ArrayList<>();

        return list;
    }
}
