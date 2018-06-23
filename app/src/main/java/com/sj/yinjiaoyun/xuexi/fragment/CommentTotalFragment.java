package com.sj.yinjiaoyun.xuexi.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.CommentAdapter;
import com.sj.yinjiaoyun.xuexi.domain.CoRows;
import com.sj.yinjiaoyun.xuexi.domain.Comments;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParserCommentData;
import com.sj.yinjiaoyun.xuexi.domain.ParserComments;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.widget.AutoListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/29.
 * 课程详情 视频播放页面  评论接口 -全部评价
 */
public class CommentTotalFragment extends Fragment implements HttpDemo.HttpCallBack ,AutoListView.OnRefreshListener,AutoListView.OnLoadListener {

    String TAG="comment";
    HttpDemo demo;
    List<Pairs> pairsList;
    CommentAdapter adapter;
     Long courseId;
    AutoListView listView;
    int type=0;//获取数据类型
    List<CoRows> totalList;
    List<CoRows> resultList;//访问一次网络，请求返回的的数据（即加载的新数据，准备添加进给总的）

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<CoRows> result = (List<CoRows>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH://刷新
                    listView.onRefreshComplete();
                    if(totalList!=null){
                        totalList.clear();
                        if(result!=null){
                            totalList.addAll(result);
                        }
                    }

                    break;
                case AutoListView.LOAD://加载
                    listView.onLoadComplete();
                    totalList.addAll(result);
                    break;
            }
            if(totalList!=null){
                listView.setResultSize(totalList.size());
                adapter.refresh(totalList);
            }

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView=inflater.inflate(R.layout.fragment_comment,container,false);
        initView(convertView);
        return convertView;
    }

    private void initView(View convertView) {
        demo=new HttpDemo(this);
        totalList=new ArrayList<>();
        listView= (AutoListView) convertView.findViewById(R.id.comment_listView);
        adapter=new CommentAdapter(getActivity(),totalList);
        listView.setAdapter(adapter);
        listView.setLoadEnable(false);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        loadData(AutoListView.REFRESH);
    }

    Message pullMsg;
    int page=1;//加载的页数
    Boolean isRefreshSuccess=false;//是否刷新成功
    private void loadData(final int what) {
        // 这里模拟从服务器获取数据
        if(what== AutoListView.LOAD){
            getHttpDate(page,1);//加载
        }else{
            page=1;
            getHttpDate(page,2);//刷新时候，页码永远为1
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


    int commentType;
    /**
     *
     * @param objectId   对象ID
     * @param commentType   评论类型  评价对象类型 0：专业 1公开课 2:微专业
     * @param type         0全部评论   1好评  2中评 3差评
     */
    public void setDateFromEvaluate(int commentType,Long objectId,int type){
        this.commentType=commentType;
        this.courseId=objectId;
        this.type=type;
    }

   // //下拉加载
    @Override
    public void onLoad() {
        Log.i(TAG, "onLoad: ");
        loadData(AutoListView.LOAD);
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh: ");
        loadData(AutoListView.REFRESH);
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        isRefreshSuccess=true;
        switch (requestCode){
            case 1://加载
                page++;
                parserComments(msg);
                break;
            case 2://刷新时候，页码永远为1
                page=1;//刷新的时候把页面设为1
                parserComments(msg);
                break;
        }
    }



    /**
     * 获取评论信息
     * @param page  页数
     * @param flag   0 表示刷新  1表示加载
     */
    private void getHttpDate(int page,int flag){
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("order/orderComment");
        pairsList.add(new Pairs("objectId",String.valueOf(courseId)));
        Log.i(TAG, "getHttpDate: "+courseId);
        pairsList.add(new Pairs("commentType",commentType+""));//  0：专业 1公开课 2:微专业
        if(type>0){
            pairsList.add(new Pairs("type",type+""));//null: 全部 1: 好评 - 评星 4-5    * 2: 中评 - 评星 2-3  3: 差评 - 评星 1
        }
        pairsList.add(new Pairs("page",page+""));//分页index，默认1
        pairsList.add(new Pairs("rows",20+""));//每一页数量默认10、
        demo.doHttpGet(url,pairsList,flag);
    }


    //解析评论接口
    public void parserComments(String msg) {
            try{
                Gson gson=new Gson();
                ParserComments a=gson.fromJson(msg, ParserComments.class);
                ParserCommentData data=a.getData();
                Comments comments=data.getComments();
                resultList=comments.getRows();
            }catch (Exception e) {
                     e.getLocalizedMessage();
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
