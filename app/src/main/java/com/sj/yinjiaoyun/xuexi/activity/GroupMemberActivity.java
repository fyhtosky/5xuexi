package com.sj.yinjiaoyun.xuexi.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.GroupInfoAdapter;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.entry.GroupUserInfo;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.entry.ParseGroupMembers;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群成员界面
 */
public class GroupMemberActivity extends MyBaseActivity implements HttpDemo.HttpCallBack {
    private static final String TAG = "GroupMemberActivity";
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.gridview)
    GridView gridview;
    HttpDemo demo;
    List<Pairs> pairsList;
    //群成员的页数
    private  int page=1;
    private  int row=50;
    private int total;
    List<GroupUserInfo> list=new ArrayList<>();
    GroupInfoAdapter groupInfoAdapter;
    private String receiver;
    private int wh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        ButterKnife.bind(this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        wh=display.getWidth();
        demo = new HttpDemo(this);
        //适配器
        groupInfoAdapter = new GroupInfoAdapter(this, list,wh);
        gridview.setAdapter(groupInfoAdapter);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            receiver = bundle.getString(Const.GROUP_ID,"");
            total=bundle.getInt(Const.GROUP_TOTAL,0);
        }
        //获取群主成员列表
        getHttpGroupMembers(receiver);
        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_IDLE){
                    if(total>list.size()&& list.size()>0){
                        page++;
                        getHttpGroupMembers(receiver);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    /**
     * -群 -成员列表<br>
     *
     * @param groupJid 群主id
     */
    private void getHttpGroupMembers(String groupJid) {
        pairsList = new ArrayList<>();
        pairsList.add(new Pairs("groupId", groupJid));
        pairsList.add(new Pairs("page", String.valueOf(page)));
        pairsList.add(new Pairs("rows", String.valueOf(row)));//默认十条
        demo.doHttpPost(Api.POST_GROUP_MEMBERS, pairsList, 1);
         Logger.d("groupId:+"+groupJid+"page:"+page+",rows:"+row);
    }
    /**
     * 返回json回调
     * @param msg
     * @param requestCode
     */
    @Override
    public void setMsg(String msg, int requestCode) {
        //获取群成员的数据
        parseGroupNumbers(msg);
    }
    private void parseGroupNumbers(String msg) {
        Log.i(TAG, "setMsg: " + msg);
        ParseGroupMembers parseGroupMembers = new Gson().fromJson(msg, ParseGroupMembers.class);
        ParseGroupMembers.DataBean data = parseGroupMembers.getData();
        ParseGroupMembers.DataBean.TigaseGroupUsersBean usersBean = data.getTigaseGroupUsers();
        tvName.setText("群组成员("+usersBean.getTotal()+")");
        list.addAll(exangeRowsBean(usersBean.getRows())) ;
        for(int i=0;i<list.size();i++){
            Log.e(TAG, "parseGroupNumbers: "+list.get(i).toString());
        }
        groupInfoAdapter.freshData(list);
    }

    private List<GroupUserInfo> exangeRowsBean(List<ParseGroupMembers.DataBean.TigaseGroupUsersBean.RowsBean> list) {
        List<GroupUserInfo> infoList = new ArrayList<>();
        GroupUserInfo info;
        String img = "";
        for (int i = 0; i < list.size(); i++) {
            ParseGroupMembers.DataBean.TigaseGroupUsersBean.RowsBean rowsBean = list.get(i);
            switch (rowsBean.getUserType()) {
                case 0://前台
                    ParseGroupMembers.DataBean.TigaseGroupUsersBean.RowsBean.EndUserVOBean endUserVOBean = rowsBean.getEndUserVO();
                    if(endUserVOBean!=null){
                        try {
                            //  Log.i(TAG, "exangeRowsBean: " + endUserVOBean.toString());
                            if (endUserVOBean.getUserImg() == null) {
                                img = "";
                            } else {
                                img = endUserVOBean.getUserImg();
                            }
                        } catch (Exception e) {
                            Log.i(TAG, "exangeRowsBean: " + e.toString());
                        }
                        if (TextUtils.isEmpty(endUserVOBean.getRealName())) {
                            info = new GroupUserInfo(img, endUserVOBean.getUserName(),rowsBean.getJid());
                        } else {
                            info = new GroupUserInfo(img, endUserVOBean.getRealName(),rowsBean.getJid());
                        }
                        infoList.add(info);
                    }

                    break;
                case 1://后台
                    ParseGroupMembers.DataBean.TigaseGroupUsersBean.RowsBean.BackendOperatorVOBean backendOperatorVOBean=rowsBean.getBackendOperatorVO();
                     if(backendOperatorVOBean!=null){
                         try {
                             // Log.i(TAG, "exangeRowsBean: " + backendOperatorVOBean.toString());
                             if (backendOperatorVOBean.getPhoto()== null) {
                                 img = "";
                             } else {
                                 img = backendOperatorVOBean.getPhoto();
                             }
                         } catch (Exception e) {
                             Log.i(TAG, "exangeRowsBean: " + e.toString());
                         }
                         if (TextUtils.isEmpty(backendOperatorVOBean.getRealName())) {
                             info = new GroupUserInfo(backendOperatorVOBean.getPhoto(), backendOperatorVOBean.getUserName(),rowsBean.getJid());
                         } else {
                             info = new GroupUserInfo(backendOperatorVOBean.getPhoto(), backendOperatorVOBean.getRealName(),rowsBean.getJid());
                         }
                         infoList.add(info);
                     }

                    break;
            }

        }
        Log.e(TAG, "parseGroupNumbers: " + infoList.size());
        return infoList;
    }
}
