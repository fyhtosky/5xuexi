package com.sj.yinjiaoyun.xuexi.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.MyRecycleAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.DataBean;
import com.sj.yinjiaoyun.xuexi.bean.Test;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.view.GrayDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanzhiying on 2017/3/2.
 * 群组列表Fragment
 */
public class MessageGroupListFragment extends Fragment {


    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    //用户登录的id
    //3435
    private String userid;
    private String jid;
    //准备群组的数据源
    private List<TigaseGroups>list=new ArrayList<>();
   //适配器
    private MyRecycleAdapter myRecycleAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_group_list, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        //获取userid
        userid= PreferencesUtils.getSharePreStr(getContext(), "username");
        jid="5f_"+userid;
        recycleview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycleview.addItemDecoration(new GrayDecoration());
        myRecycleAdapter=new MyRecycleAdapter(list,getContext());
        recycleview.setAdapter(myRecycleAdapter);


    }

    @Override
    public void onResume() {
        initData();
        super.onResume();


    }

    /**
     * 获取群聊的信息
     */
    private void initData() {
              HashMap<String ,String>map=new HashMap<>();
              map.put("endUserId",userid);
              map.put("userType","0");
              HttpClient.postStr(this, Api.GET_GROUP_LIST, map, new CallBack<String>() {
                  @Override
                  public void onSuccess(String result) {
                      if(result==null){
                          return;
                      }
                      Gson gson=new Gson();
                      Test a=gson.fromJson(result, Test.class);
                      DataBean dataBean = a.getData();
                      list.clear();
                      Logger.d("群列表的数据源"+dataBean.getTigaseGroups().toString());
                      list.addAll(dataBean.getTigaseGroups());
                      recycleview.getAdapter().notifyDataSetChanged();
                      //赋值给全局变量
                      MyApplication.groupsList=list;

                  }
              });



    }


}


