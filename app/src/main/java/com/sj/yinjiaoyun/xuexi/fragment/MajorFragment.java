package com.sj.yinjiaoyun.xuexi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.MajorAdapter;
import com.sj.yinjiaoyun.xuexi.bean.MajorBean;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/19.
 */
public class MajorFragment extends Fragment {
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.defaultContainer)
    LinearLayout defaultContainer;
    private String endUserId;
    private List<MajorBean.DataBean.SoaProductVOsBean> list = new ArrayList<>();
    private MajorAdapter majorAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_major, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        endUserId = PreferencesUtils.getSharePreStr(getActivity(), "username");
        majorAdapter = new MajorAdapter(getContext(), list);
        listview.setAdapter(majorAdapter);
        listview.setDividerHeight(20);


    }

    @Override
    public void onResume() {
        super.onResume();
        requstMajor();
    }

    private void requstMajor() {
        String params = "?endUserId=" + endUserId;
        HttpClient.get(this, Api.FIND_MY_PRODUCT_FOR_CHOOSE + params, new CallBack<MajorBean>() {
            @Override
            public void onSuccess(MajorBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    list.clear();
                    list.addAll(result.getData().getSoaProductVOs());
                    majorAdapter.notifyDataSetChanged();
                    if(list.size()>0){
                        defaultContainer.setVisibility(View.GONE);
                    }else {
                        defaultContainer.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}
