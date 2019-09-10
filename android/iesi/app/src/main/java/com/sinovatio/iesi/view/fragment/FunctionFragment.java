package com.sinovatio.iesi.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinovatio.iesi.BaseFragment;
import com.sinovatio.iesi.R;
import com.sinovatio.iesi.view.TargetInfoQueryActivity;
import com.sinovatio.iesi.view.TargetProfileQueryActivity;
import com.sinovatio.iesi.view.TaskActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 功能
 */
public class FunctionFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.tv_target)
    TextView tvTarget;
    @BindView(R.id.tv_archives)
    TextView tvArchives;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_knowledge)
    TextView tvKnowledge;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_map)
    TextView tvMap;
    @BindView(R.id.tv_early)
    TextView tvEarly;

    @Override
    protected void initView(View view) {
        titleTv.setText("功能");
        titleBack.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_function;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_target,R.id.tv_archives, R.id.tv_info, R.id.tv_knowledge, R.id.tv_start, R.id.tv_map, R.id.tv_early})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_target:
                Intent intentTarget =new Intent(getActivity(), TargetInfoQueryActivity.class);
                startActivity(intentTarget);
                break;
            case R.id.tv_archives:
                Intent intentProfile = new Intent(getActivity(), TargetProfileQueryActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.tv_info:
                break;
            case R.id.tv_knowledge:
                break;
            case R.id.tv_start:
                break;
            case R.id.tv_map:
                break;
            case R.id.tv_early:
                break;
        }

    }

}
