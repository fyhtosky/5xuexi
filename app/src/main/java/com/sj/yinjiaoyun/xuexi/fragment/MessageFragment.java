package com.sj.yinjiaoyun.xuexi.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.MyViewPagerAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.DataBean;
import com.sj.yinjiaoyun.xuexi.bean.Test;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.xmppmanager.XmppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xiaopan.android.net.NetworkUtils;

/**
 * Created by wanzhiying on 2017/3/1.
 * 消息Fragment
 */
public class MessageFragment extends Fragment {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.vp)
    ViewPager vp;
    private List<String> list = Arrays.asList("消息", "群组","收藏");
    private List<Fragment> fragmentList = new ArrayList<>();
    private MessageListFragment messageListFragment;
    private MessageGroupListFragment messageGroupListFragment;
    private CollectMessageFragment collectMessageFragment;
    //准备群组的数据源
    private List<TigaseGroups>groupsList=new ArrayList<>();
    //用户登录的id
    private String userid;
    //5f_3435
    private String jid;
    //单线程化的线程池
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;

    }
    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    private void init() {
        //获取userid
        userid= PreferencesUtils.getSharePreStr(getContext(), "username");
        jid="5f_"+userid;
        messageListFragment=new MessageListFragment();
        messageGroupListFragment=new MessageGroupListFragment();
        collectMessageFragment=new CollectMessageFragment();
        fragmentList.clear();
        fragmentList.add(messageListFragment);
        fragmentList.add(messageGroupListFragment);
        fragmentList.add(collectMessageFragment);
        vp.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(),fragmentList,list));
        vp.setOffscreenPageLimit(fragmentList.size());
        tablayout.setupWithViewPager(vp);
    }

    Runnable joinGroup=new Runnable() {
        @Override
        public void run() {
            // 获取所有的群id加入群聊天室
            if(groupsList.size()>0) {
                MyApplication.list=null;
                MyApplication.list= XmppUtil.getMultiChatList(groupsList,jid);
            }
        }
    };
    /**
     * 获取群聊的信息
     */
    private void initData() {
        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            ToastUtil.showShortToast(getActivity(),"网络连接不可用，请检查网络设置");
            return;
        }
        HashMap<String ,String> map=new HashMap<>();
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
                groupsList.clear();
                Logger.d("群列表的数据源"+dataBean.getTigaseGroups().toString());
                groupsList.addAll(dataBean.getTigaseGroups());
                //赋值给全局变量
                MyApplication.groupsList=groupsList;
                if(MyApplication.xmppConnection!=null&& MyApplication.xmppConnection.isAuthenticated()){
                    singleThreadExecutor.execute(joinGroup);
                }

            }
        });


    }
}
