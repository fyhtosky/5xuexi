package com.sj.yinjiaoyun.xuexi.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.OpenCouseItemActivity;
import com.sj.yinjiaoyun.xuexi.adapter.OpenCouseAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.OpenCourseVO;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParseOpenCourse;
import com.sj.yinjiaoyun.xuexi.domain.ParseOpenCourseData;
import com.sj.yinjiaoyun.xuexi.domains.CouseFuse;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.widget.AutoListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/1.
 * 课程表里面的 - 公开课fragment
 */
public class OpenCouseFragment extends Fragment implements HttpDemo.HttpCallBack,AutoListView.OnRefreshListener{

    String TAG="openfragment";
    HttpDemo demo;
    String endUserId;//用户id
    List<Pairs> pairsList;
    CouseFuse search;


    AutoListView listView;
    List<OpenCourseVO> openList;
    List<OpenCourseVO> resultList;
    OpenCouseAdapter adapter;
    View courseContainer;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<OpenCourseVO> result = (List<OpenCourseVO>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    Log.i(TAG, "handleMessage: "+"刷新");
                    openList.clear();
                    if(result!=null){
                        openList.addAll(result);
                    }
                    break;
            }
            Log.i(TAG, "handleMessage: "+openList.size());
            listView.setResultSize(openList.size());
            adapter.onFresh(openList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        OpenCourseVO openCourseVO = openList.get(position-1);
                        search = exangeDate(openCourseVO);
                        Intent intent = new Intent(getActivity(), OpenCouseItemActivity.class);
                        Log.i(TAG, "parserIsBuy: " + search.toString());
                        intent.putExtra("CouseFuse", search);
                        intent.putExtra("EndUserId", endUserId);
                        intent.putExtra("CourseScheduleId", String.valueOf(openCourseVO.getCourseScheduleId()));
                        getActivity().startActivity(intent);
                    }catch (Exception e){
                        Log.e(TAG, "onItemClick: "+e.toString());
                    }
                }
            });

            if (openList == null || openList.size()==0) {
                courseContainer.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                courseContainer.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("showAndHidden", "onCreate:创建 "+"openCouseFragment");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_schedule_open,container,false);
        initView(view);
        return view;
    }

    Message pullMsg;
    Boolean isRefreshSuccess=false;//是否刷新成功
    private void loadData(final int what) {
        // 这里模拟从网络上器获取数据
        if(what== AutoListView.LOAD){
            //加载
        }else{
            getHttpDate();//刷新时候，页码永远为1
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "run: "+isRefreshSuccess);
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

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh: ");
        loadData(AutoListView.REFRESH);
    }


    private void getHttpDate(){
        isRefreshSuccess=false;//设置访问网络状态初始化为false
        demo=new HttpDemo(this);
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("openCourse/findOpenCourseByEnduserId");
        pairsList.add(new Pairs("endUserId",endUserId));
        demo.doHttpGetLoading(getActivity(),url,pairsList,0);
    }


    public void initView(View view){
        endUserId = PreferencesUtils.getSharePreStr(MyApplication.getContext(),"username");
        openList=new ArrayList<>();
        listView= (AutoListView) view.findViewById(R.id.open_listView);
        courseContainer=view.findViewById(R.id.open_defaultContainer);
        adapter=new OpenCouseAdapter(getActivity(),openList);
        listView.setAdapter(adapter);
        listView.setLoadEnable(false);
        listView.setOnRefreshListener(this);
        listView.setDividerHeight(1);
        loadData(AutoListView.REFRESH);
    }

    private CouseFuse exangeDate(OpenCourseVO openCourse) {
            CouseFuse s=new CouseFuse();
            s.setCollegeName(openCourse.getCollegeName());
            s.setCourseLogo(openCourse.getCourseLogo());
            s.setPrice(String.valueOf(openCourse.getPrice()));
            s.setCourseName(openCourse.getCourseName());
            s.setNumber(openCourse.getNumber());
            s.setCourseId(openCourse.getCourseId());
            s.setOpencourseId(openCourse.getId());
            byte a=0;
            s.setIsFree(a);
            s.setCourseType(openCourse.getOpenCourseType());
        return s;
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        if(requestCode==0){
            try {
                prserOpenCouse(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //解析数据
    private void prserOpenCouse(String msg) {
            Gson gson = new Gson();
            ParseOpenCourse openCourse = gson.fromJson(msg, ParseOpenCourse.class);
            ParseOpenCourseData data = openCourse.getData();
            resultList = data.getOpenCourses();
            isRefreshSuccess=true;
//            Log.i(TAG, "prserOpenCouse: "+resultList.size());
    }


}
