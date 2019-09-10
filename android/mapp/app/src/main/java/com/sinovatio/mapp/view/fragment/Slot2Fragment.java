package com.sinovatio.mapp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sinovatio.mapp.R;
import com.sinovatio.mapp.adapter.RecyclerViewAdapter;
import com.sinovatio.mapp.base.BaseLazyLoadFragment;

import java.util.List;

public class Slot2Fragment extends BaseLazyLoadFragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private RecyclerView card_slot_view;
    private RecyclerView operator_view;
    private RecyclerView service_area_view;
    private RecyclerView singal_intensity_view;
    private RecyclerView adjacent_cell_view;
    private List<String> Title1;
    private List<String> Text1;
    private RecyclerViewAdapter adapter;

    public Slot2Fragment() {
    }

    public static Slot2Fragment getInstance(String param1) {
        Slot2Fragment fragment = new Slot2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    protected void initView(View view) {

    }


    public View initViews(LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_slot, null);
//        TextView card_title_tv = (TextView) v.findViewById(R.id.card_title_tv);
//        initData();
//        card_slot_view = v.findViewById(R.id.card_slot_view);
//        //设置为垂直的样式
//        card_slot_view.setLayoutManager(new LinearLayoutManager(getActivity()));
//        //设置适配器
//        card_slot_view.setAdapter(adapter=new BaseAdapter(getActivity(),Title1,Text1));
        return v;
    }

//    protected void initData() {
//        Title1 = new ArrayList<>();
//        Text1 = new ArrayList<>();
//        JSONArray ja = new JSONArray();
////        bTitle = new ArrayList<>();
////        bText = new ArrayList<>();
////        sTitle = new ArrayList<>();
////        sText = new ArrayList<>();
//
//        TelephonyManager tel = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//        try {
//            ja = SimUtils.getAllSimInfo(tel);
//            if(ja.length()>1){
//                Title1.add("IMEI");
//                Text1.add(SimUtils.getIMEI(ja,1));
//                Title1.add("SIM IMSI");
//                Text1.add(SimUtils.getIMSI(ja,1));
//                Title1.add("ICCID");
//                Text1.add(SimUtils.getICCID(ja,1));
//                Title1.add("ICCID信息");
//                Text1.add(SimUtils.getICCIDInfo(ja,1));
//            }
//            else {
//                Title1.add("IMEI");
//                Text1.add(DeviceUtil.getDeviceID(MyApplication.getContext(),2));
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_slot;
    }
}
