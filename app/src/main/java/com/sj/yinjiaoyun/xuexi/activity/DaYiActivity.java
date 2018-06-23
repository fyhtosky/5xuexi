package com.sj.yinjiaoyun.xuexi.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.DaYiAdapter;
import com.sj.yinjiaoyun.xuexi.db.DbOperatorLogin;
import com.sj.yinjiaoyun.xuexi.domain.DaYi;
import com.sj.yinjiaoyun.xuexi.domain.Discussion;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParserDiscussion;
import com.sj.yinjiaoyun.xuexi.domain.ParserDiscussionData;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;
import com.sj.yinjiaoyun.xuexi.widget.AutoListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/4.
 * 答疑 列表
 */
public class DaYiActivity extends MyBaseActivity implements HttpDemo.HttpCallBack,AutoListView.OnRefreshListener,AutoListView.OnLoadListener{

    String TAG="dayiaaaa";
    Intent intent;
    int groupPosition;//章节
    int childPosition;//课时
    String coursewareId;//课件ID
    String courseScheduleId;//教学计划

    TitleBarView titlebar;
    AutoListView listView;
    DaYiAdapter adapter;
    TextView tvBottom;//我要提问
    Button btn;   //dailog里面的
    Dialog dialog;
    EditText editView;//dailog里面的

    String endUserId;//用户id

    HttpDemo demo;
    List<Pairs> pairsList;

    List<DaYi> daYiList;
    List<DaYi> resultList;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<DaYi> result = (List<DaYi>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    if(daYiList!=null){
                        daYiList.clear();
                        daYiList.addAll(result);
                    }
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    daYiList.addAll(result);
                    break;
            }
            try {
                if(result==null){
                    listView.setResultSize(0);
                }else{
                    listView.setResultSize(result.size());
                }
                adapter.refresh(daYiList);
                Log.i(TAG, "handleMessage: " + "完成操作");
            }catch (Exception e){
                Log.e(TAG, "handleMessage: "+e.toString());
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dayi);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();
        initView();
        initEvent();
    }



    //数据准备
    private void init() {
        endUserId = PreferencesUtils.getSharePreStr(this,"username");
        intent=getIntent();
        demo=new HttpDemo(this);
        coursewareId=intent.getStringExtra("coursewareId");
        courseScheduleId=intent.getStringExtra("CourseScheduleId");
        groupPosition=intent.getIntExtra("groupPosition",0);
        childPosition=intent.getIntExtra("childPosition",0);
        Log.i(TAG, "init: "+" endUserId"+endUserId+" coursewareId"+coursewareId+" courseScheduleId"+courseScheduleId);
    }


    //初始化控件
    private void initView() {
        titlebar= (TitleBarView) findViewById(R.id.dayi_title);
        listView= (AutoListView) findViewById(R.id.dayi_listView);
        tvBottom= (TextView) findViewById(R.id.dayi_tiwen);
        titlebar.getRightTitleView().setText("第" + groupPosition + "章" + "第" + childPosition + "课时" + "答疑");
    }


    private void initEvent() {
        daYiList=new ArrayList<>();
        adapter=new DaYiAdapter(daYiList,this);
        listView.setAdapter(adapter);
        listView.setDividerHeight(3);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        titlebar.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog();
            }
        });
        loadData(AutoListView.REFRESH);
    }

    private void  getDateForHttp(int page,int requestCode){
        isRefreshSuccess=false;
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("learn/findDiscussionOnline");
        pairsList.add(new Pairs("coursewareId",coursewareId));
        pairsList.add(new Pairs("courseScheduleId",courseScheduleId));
        pairsList.add(new Pairs("page",page+""));//分页index，默认1
        pairsList.add(new Pairs("rows",20+""));//每一页数量默认10、
        demo.doHttpGet(url,pairsList,requestCode);
    }

    private void  setDialog(){
        View view=LayoutInflater.from(this).inflate(R.layout.dialog_dayi,null);
        btn= (Button) view.findViewById(R.id.dialog_dayi_btn);
        editView= (EditText) view.findViewById(R.id.dialog_dayi_editView);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setIcon(android.R.drawable.ic_dialog_info)
                .setView(view);
        dialog = builder.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        btn.setFocusable(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: ---");
                String msg=editView.getText().toString();
                if(msg.length()>300){
                    Toast.makeText(DaYiActivity.this,"请输入内容（不超过300字）",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(msg.equals(""))){
                    setQuestionsToHttp(msg);
                }
            }
        });
   }


    //发送问题去网络
    private void setQuestionsToHttp(String question){
        Log.i(TAG, "setQuestionsToHttp: ");
        dialog.dismiss();
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("learn/addDiscussionOnline");
        pairsList.add(new Pairs("endUserId",endUserId));//
        pairsList.add(new Pairs("question",question));//疑问
        pairsList.add(new Pairs("coursewareId",coursewareId));//课件id
        pairsList.add(new Pairs("courseScheduleId",courseScheduleId));//课
        // 程表id
        demo.doHttpPostLoading(this,url,pairsList,0);
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        if(requestCode==1){//加载
            try {
                page=page+1;
                parserDate(msg);
            } catch (Exception e) {
                listView.onRefreshComplete();
                Log.e(TAG, "parserDate: "+e.toString());
            }
        }else if(requestCode==2){//刷新
            try {
                parserDate(msg);
                page=2;
            } catch (Exception e) {
                listView.onLoadComplete();
                Log.e(TAG, "parserDate: "+e.toString());
            }
        } else{//发送问题反馈
            try {
                JSONObject obj=new JSONObject(msg);
                Boolean isSuccess=obj.getBoolean("success");
                if(isSuccess){
                    Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();
                    loadData(AutoListView.REFRESH);
                }else{
                    Toast.makeText(this,"提交失败，请检查网络",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parserDate(String msg) {
            Gson gson=new Gson();
            ParserDiscussion parserDiscussion=gson.fromJson(msg,ParserDiscussion.class);
            ParserDiscussionData data=parserDiscussion.getData();
            Discussion discussion=data.getDiscussion();
            isRefreshSuccess=true;
            resultList=discussion.getRows();
    }

    Message pullMsg;
    int page=1;//加载的页数
    Boolean isRefreshSuccess=false;//是否刷新成功
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

    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
    }

    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
