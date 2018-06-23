package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.ChooseReplyAdapter;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.entry.ParseGroupMembers;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseReplyActivity extends MyBaseActivity {



    @BindView(R.id.iv_name)
    TextView ivName;
    @BindView(R.id.listview)
    ListView listview;

    //群成员的页数
    private  int page=1;
    private  int row=20;
    private String groupJid;
    private int total;
    //适配器
    private ChooseReplyAdapter adapter;
    private String receiver;
    private boolean isLastRow=false;
    //@的内容封装
    private String content;
    private String jid;

    //数据源
   private List<ParseGroupMembers.DataBean.TigaseGroupUsersBean.RowsBean> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_reply);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        receiver = "5f_"+ PreferencesUtils.getSharePreStr(ChooseReplyActivity.this, "username");
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            groupJid = bundle.getString(Const.GROUP_ID,"");
        }
        adapter=new ChooseReplyAdapter(list,ChooseReplyActivity.this);
        listview.setAdapter(adapter);
        //获取群主成员列表
        isLastRow=false;
        RequeseData(false);
        //listview的点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                returnData(position);
            }
        });
       listview.setOnScrollListener(new AbsListView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(AbsListView view, int scrollState) {
               if(scrollState==SCROLL_STATE_IDLE){
                   if(total>list.size()){
                       RequeseData(true);
                   }

               }
           }

           @Override
           public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

           }
       });
    }

    private void returnData(int position) {
        //携带数据
        if(list.get(position).getUserType()==0){
//            content="#@_"+list.get(position).getJid()+"_!@_"+list.get(position).getEndUserVO().getRealName()+"_@#";
            if(list.get(position).getEndUserVO().getRealName()!=null){
                content=list.get(position).getEndUserVO().getRealName();
            }else{
                content=list.get(position).getEndUserVO().getUserName();
            }


        }else if(list.get(position).getUserType()==1){
//            content="#@_"+list.get(position).getJid()+"_!@_"+list.get(position).getBackendOperatorVO().getRealName()+"_@#";
            if(list.get(position).getBackendOperatorVO().getRealName()!=null){
                content=list.get(position).getBackendOperatorVO().getRealName();
            }else{
                content=list.get(position).getBackendOperatorVO().getUserName();
            }

        }
        jid=list.get(position).getJid();
        if(receiver.equals(jid)){
            ToastUtil.showShortToast(ChooseReplyActivity.this,"不能@自己");
            return;
        }
        Intent data=new Intent();
        data.putExtra(Const.CHOOSE_REPLY,content);
        data.putExtra(Const.CHOOSE_JID,jid);
        setResult(RESULT_OK,data);
        finish();
    }

    private void RequeseData(final boolean onLoadMore) {
        HashMap<String,String>map=new HashMap<>();
        map.put("groupId",groupJid);
        map.put("page",String.valueOf(page));
        map.put("rows",String.valueOf(row));
        HttpClient.post(this, Api.POST_GROUP_MEMBERS, map, new CallBack<ParseGroupMembers>() {
            @Override
            public void onSuccess(ParseGroupMembers result) {
                if(result==null){
                    return;
                }
                if(!onLoadMore){
                    list.clear();
                    total=result.getData().getTigaseGroupUsers().getTotal();
                }
                page++;
                list.addAll(result.getData().getTigaseGroupUsers().getRows());
                adapter.notifyDataSetChanged();
            }
        });
    }


    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }


}
