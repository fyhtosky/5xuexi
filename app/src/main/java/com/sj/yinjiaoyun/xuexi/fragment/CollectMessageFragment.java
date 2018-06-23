package com.sj.yinjiaoyun.xuexi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.NotifyEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.CollectMessageAdapter;
import com.sj.yinjiaoyun.xuexi.bean.CollectMessageBean;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/4/17.
 */
public class CollectMessageFragment extends Fragment {


    @BindView(R.id.listview)
    ListView listview;
    private String createTime;
    private Context mContext;
    private String userid;
    private int row=15;
    private int total;
    private List<CollectMessageBean.DataBean.MsgEnshrineBean.RowsBean>list=new ArrayList<>();
    private CollectMessageAdapter adapter;
    private  View viewP;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       viewP = inflater.inflate(R.layout.fragment_collect_message, container, false);
        ButterKnife.bind(this, viewP);
        init();
        return viewP;
    }


    @Override
    public void onStart() {
        //注册EventBus
        EventBus.getDefault().register(this);
        super.onStart();

    }

    @Override
    public void onStop() {
        //反注册EventBus
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyEvent notifyEvent){
        onResume();
    }
    private void init() {
        mContext = getActivity();
        userid="5f_"+ PreferencesUtils.getSharePreStr(mContext, "username");
        adapter=new CollectMessageAdapter(list,getActivity(),getActivity());
        listview.setAdapter(adapter);
        //给listView添加监听器
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_IDLE){
                    if(total>list.size()&& list.size()>0){
                        initData(true);
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    @Override
    public void onResume() {
        createTime="";
        initData(false);
        super.onResume();
    }

    private void initData(final boolean isLoadMore) {
        HashMap<String,String>hashmap=new HashMap<>();
        hashmap.put("jid",userid);
        hashmap.put("updateTime",createTime);
        hashmap.put("rows",String.valueOf(row));
        HttpClient.post(this, Api.GET_COLLECT_MESSAGE_LIST, hashmap, new CallBack<CollectMessageBean>() {
            @Override
            public void onSuccess(CollectMessageBean result) {
                  if(result==null){
                      return;
                  }
                if(200==result.getState()){
                    if(!isLoadMore){
                        total=result.getData().getMsgEnshrine().getTotal();
                        list.clear();
                    }
                    if(total>list.size()){
                        list.addAll(result.getData().getMsgEnshrine().getRows());
                    }
                    createTime=String.valueOf(list.get((list.size()-1)).getUpdateTime());
                    adapter.notifyDataSetChanged();
                    Logger.d("收藏列表的数量："+list.size());
                }
            }
        });

    }

}
