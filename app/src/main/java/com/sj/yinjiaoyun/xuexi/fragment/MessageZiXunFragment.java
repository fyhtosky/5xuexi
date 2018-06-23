package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.MessageAdapter;

import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Notices;
import com.sj.yinjiaoyun.xuexi.domain.NoticesRows;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParserMessage;
import com.sj.yinjiaoyun.xuexi.domain.ParserMessageDate;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.widget.AutoListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/1/22.
 * 我的消息里面 ，3 咨询
 */
public class MessageZiXunFragment extends Fragment implements HttpDemo.HttpCallBack,AutoListView.OnRefreshListener,AutoListView.OnLoadListener  {

    String TAG="message";
    String endUserId;
    AutoListView listView;

    List<Pairs> pairsList;
    HttpDemo demo;

    int contentType=3; // 0：讨论组信息  1：答疑  2：批改作业  3：咨询  4：新建讨论组   （默认值为咨询）
    List<NoticesRows> totalList;//总的消息列表,所有的消息列表
    List<NoticesRows> resultList;//访问一次网络，请求返回的的数据（即加载的新数据，准备添加进给总的）
    List<Long> idList;//点击阅读或者全部加载为已读的 id 集合

    MessageAdapter adapter;
    Boolean isRefreshSuccess=false;//是否刷新成功
    int page=1;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<NoticesRows> result = (List<NoticesRows>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    totalList.clear();
                    totalList.addAll(result);
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    totalList.addAll(result);
                    break;
            }
            listView.setResultSize(totalList.size());
            adapter.refresh(totalList);
        }
    };


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View convertView=inflater.inflate(R.layout.fragment_comment,container,false);
            initView(convertView);
            initEvent();
            return convertView;
        }

    private void initView(View convertView) {
        idList=new ArrayList<>();
        demo=new HttpDemo(this);
        listView= (AutoListView) convertView.findViewById(R.id.comment_listView);
        totalList=new ArrayList<>();
        adapter=new MessageAdapter(totalList,getActivity());
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        loadData(AutoListView.REFRESH);
    }

    private void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    idList=new ArrayList<>();
                    Byte a=1;
                    totalList.get(position-1).setIsRead(a);
                    idList.add(totalList.get(position-1).getId());
                    sendIsReadToHttp(idList);
                    adapter.refresh(totalList);
                }catch (Exception e){
                    Log.e(TAG, "咨询onItemClick: "+e.toString());
                }
            }
        });
    }

    //消息已读的条数,反馈到网络上去
    private void sendIsReadToHttp(List<Long> idList){
        if(idList==null){
            Log.i(TAG, "sendIsReadToHttp: " + "idList是否为空：");
            return;
        }
        int a=idList.size();
        if(a==0){
            Log.i(TAG, "sendIsReadToHttp: "+"没有未读消息");
            return;
        }
        pairsList=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<a;i++){
            if((a-1)==i){
                sb.append(idList.get(i));
            }else{
                sb.append(idList.get(i)).append(",");
            }
        }
        Log.i(TAG, "sendIsReadToHttp: "+sb.toString());
        String url= MyConfig.getURl("my/updateNoticeStatusToRead");
        pairsList.add(new Pairs("ids",sb.toString()));
        demo.doHttpPost(url,pairsList,3);//发送是否阅读到网络上去
    }

    /**
     * 设置全部标为已读
     */
    public void setAllIsReadToHttp(){
        Log.i(TAG, "setAllIsReadToHttp: "+(totalList==null));
        if(totalList==null){
            return;
        }
        idList=new ArrayList<>();
        Byte a=1;//0 表示未读，1 表示已读
        // 遍历list的长度，将MyAdapter中的map值全部设为true
        for (int i = 0; i < totalList.size(); i++) {
            NoticesRows noticesRows=totalList.get(i);
            if(noticesRows.getIsRead()==0){
                noticesRows.setIsRead(a);//把所有的项改成已经阅读
                idList.add(noticesRows.getId());
            }
        }
        Log.i(TAG, "onClick:已选标为为已读的条数 "+idList.size());
        //通知listView刷新
        sendIsReadToHttp(idList);
        adapter.refresh(totalList);
    }



    public void setDateFromMessageActivity(String endUserId){
           this.endUserId=endUserId;
    }

    Message pullMsg;
    private void loadData(final int what) {
        // 这里模拟从网络上器获取数据
        if(what== AutoListView.LOAD){
            getDateForHttp(page,1);//加载
        }else{
            getDateForHttp(1,2);//刷新时候，页码永远为1
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isRefreshSuccess){
                    Log.i(TAG, "---: "+"刷新成功，添加加载数据");
                    pullMsg= handler.obtainMessage();
                    pullMsg.what = what;
                    pullMsg.obj=resultList;
                    handler.sendMessage(pullMsg);
                }
            }
        }).start();
    }

    //下拉加载
    @Override
    public void onLoad() {
        Log.i(TAG, "onLoad: ");
        loadData(AutoListView.LOAD);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh: ");
        loadData(AutoListView.REFRESH);
    }


    /**
     *  从网络上获取消息总列表信息
     * @param page  消息页数
     * @param request  0表示第一次进来时候访问网络  其他数字 表示刷新加载 访问网络
     */
    private  void getDateForHttp(int page,int request){
        Log.i(TAG, "getDateForHttp: 获取 讨论组 页码为："+page);
        isRefreshSuccess=false;//设置访问网络状态初始化为false
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("my/myNotice");
        pairsList.add(new Pairs("receiveId",endUserId));//用户ID
        //pairsList.add(new Pairs("isRead",1+""));//是否已读  1表示已读  0未读
        pairsList.add(new Pairs("contentType",contentType+""));
        pairsList.add(new Pairs("notice_type","0"));//消息类型 0：提醒 1：通知
        pairsList.add(new Pairs("page",page+""));//分页index，默认1
        pairsList.add(new Pairs("rows",10+""));//每一页数量默认10、
        demo.doHttpGet(url,pairsList,request);
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        isRefreshSuccess=true;
        try {
            switch (requestCode) {
                case 1://加载
                    page++;
                    parserMessage(msg);
                    break;
                case 2://刷新时候，页码永远为1
                    page = 2;//刷新的时候把页面设为1
                    parserMessage(msg);
                    break;
                case 3://消息反馈结果
                    parserMessageFanKui(msg);
                    break;
            }
        }catch (Exception e){
            Log.e(TAG, "setMsg: "+e.toString());
        }
    }



    /**
     * 解析列表条数接口
     * @param msg
     * @throws Exception
     */
   private void parserMessage(String msg) {
       isRefreshSuccess = true;
       Gson gson = new Gson();
       ParserMessage parserMessage = gson.fromJson(msg, ParserMessage.class);
       ParserMessageDate data = parserMessage.getData();
       Notices notices = data.getNotices();
       resultList = notices.getRows();
   }

    private void parserMessageFanKui(String msg) throws Exception{
        JSONObject obj=new JSONObject(msg);
        if(obj.getBoolean("success")){
            Log.i(TAG, "setMsg: "+"消息阅读反馈成功");
            idList=new ArrayList<>();//重置阅读的集合统计
        }else{
            Log.e(TAG, "setMsg: "+"消息阅读反馈失败");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
