package com.sj.yinjiaoyun.xuexi.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.GroupInfoAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroupVO;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.entry.GroupUserInfo;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.entry.ParseGroupMembers;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.widget.NewGridView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 群组信息Activity
 */

public class GroupInforActivity extends MyBaseActivity implements View.OnClickListener,HttpDemo.HttpCallBack{

    private static final String TAG = "aaaaa";
    ImageView groupchatinfoBack;
    TextView groupchatinfoName;
    NewGridView gridView;
    TextView chatinfoBottomTotal;
    TextView chatinfoBottomCollege;
    TextView chatinfoBottomGroupName;
    CheckBox chatinfoBottomMessageNo;
    CheckBox chatinfoBottomChatTop;
    TextView chatinfoBottomNickname;

    private LayoutInflater inflater;
    View bootomView;
    HttpDemo demo;
    List<Pairs> pairsList;
    GroupInfoAdapter groupInfoAdapter;
    List<GroupUserInfo> list;
    //群成员的页数
    private  int page=1;
    private  int row=20;
    private String receiver;
    private String jid;
    private boolean isCheck=false;
    private int wh;
    //该群一共多少人
    private int groupTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_infor);
        ButterKnife.bind(this);
        //获取设备的宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        wh=display.getWidth();
        inflater = LayoutInflater.from(this);
        demo = new HttpDemo(this);
        // bootomView = inflater.inflate(R.layout.groupchatinfo_bottom, null);
        initView();
        initEvent();
        //设置免打扰状态
        setIsNotDisturb();
        //获取群主信息
        getHttpGroupChatInfo(receiver);
        //获取群主成员列表
        getHttpGroupMembers(receiver);
    }
    /**
     *  设置免打扰状态
     */
    private void  setIsNotDisturb(){
        //设置免打扰
        if(MyApplication.groupsList!=null &&MyApplication.groupsList.size()>0){
            for (TigaseGroups tigaseGroups:MyApplication.groupsList){
                if(receiver.equals(tigaseGroups.getTigaseGroupVO().getGroupId())){
                    //开启了免打扰
                    //未开启免打扰
                    isCheck = 1 == tigaseGroups.getTigaseGroupVO().getIsNotDisturb();
                }else{
                    if(tigaseGroups.getChildTigaseGroupVOs()!=null){
                        if(tigaseGroups.getChildTigaseGroupVOs().size()>0){
                            for (TigaseGroupVO tigaseGroupVO:tigaseGroups.getChildTigaseGroupVOs()){
                                if(receiver.equals(tigaseGroupVO.getGroupId())){
                                    //开启了免打扰
                                    //未开启免打扰
                                    isCheck = 1 == tigaseGroupVO.getIsNotDisturb();
                                }
                            }
                        }
                    }
                }
            }
        }else{
            isCheck=false;
        }
        //是免打扰状态
        if(isCheck){
            chatinfoBottomMessageNo.setChecked(true);
        }else {
            chatinfoBottomMessageNo.setChecked(false);
        }
    }
    /**
     * 设置免打扰
     * @param isCheck
     */
    private void  setNotDisturb(boolean isCheck){
        HashMap<String,String>hashMap=new HashMap<>();
        hashMap.put("jid",jid);
        hashMap.put("withJid",receiver);
        if(isCheck){
            hashMap.put("isNotDisturb",String.valueOf(1));
        }else{
            hashMap.put("isNotDisturb",String.valueOf(0));
        }
        hashMap.put("msgType","1");

        HttpClient.post(this, Api.SET_NOTDISTURB, hashMap, new CallBack<ReturnBean>() {
           @Override
           public void onSuccess(ReturnBean result) {
               ToastUtil.showShortToast(GroupInforActivity.this,result.getMessage());

           }
       });

    }

    private void initEvent() {
        //适配器
        groupInfoAdapter = new GroupInfoAdapter(this, list,wh);
        gridView.setAdapter(groupInfoAdapter);
    }

    private void initView() {
        groupchatinfoBack = (ImageView) findViewById(R.id.groupchatinfo_back);
        groupchatinfoName = (TextView) findViewById(R.id.groupchatinfo_name);
        gridView = (NewGridView) findViewById(R.id.groupchatinfo_recycleView);
        chatinfoBottomTotal = (TextView)findViewById(R.id.chatinfo_bottom_total);
        chatinfoBottomCollege = (TextView)findViewById(R.id.chatinfo_bottom_college);
        chatinfoBottomGroupName = (TextView)findViewById(R.id.chatinfo_bottom_groupName);
        chatinfoBottomMessageNo = (CheckBox)findViewById(R.id.chatinfo_bottom_messageNo);
        chatinfoBottomChatTop = (CheckBox)findViewById(R.id.chatinfo_bottom_chatTop);
        chatinfoBottomNickname = (TextView)findViewById(R.id.chatinfo_bottom_nickname);
        groupchatinfoBack.setOnClickListener(this);
        chatinfoBottomTotal.setOnClickListener(this);
        chatinfoBottomMessageNo.setOnClickListener(this);
        chatinfoBottomChatTop.setOnClickListener(this);
        jid = "5f_"+ PreferencesUtils.getSharePreStr(this, "username");

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            receiver = bundle.getString(Const.GROUP_ID,"");
        }

    }


    /**
     * 群 –基本信息<br>
     *
     * @param groupJid 群主id
     */
    private void getHttpGroupChatInfo(String groupJid) {
        pairsList = new ArrayList<>();
        pairsList.add(new Pairs("groupJid", groupJid));
        demo.doHttpGet(Api.GET_GROUP_INFO, pairsList, 0);
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
    }


    @Override
    public void setMsg(String msg, int requestCode) {

        if (requestCode == 0) {
            try {
                parseGroupChatInfo(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 1) {
            //获取群成员的数据
            parseGroupNumbers(msg);
        }

    }

    private void parseGroupNumbers(String msg) {
        Log.i(TAG, "setMsg: " + msg);
        ParseGroupMembers parseGroupMembers = new Gson().fromJson(msg, ParseGroupMembers.class);
        ParseGroupMembers.DataBean data = parseGroupMembers.getData();
        ParseGroupMembers.DataBean.TigaseGroupUsersBean usersBean = data.getTigaseGroupUsers();
        groupTotal=usersBean.getTotal();
        groupchatinfoName.setText("群组信息("+usersBean.getTotal()+")");
        String realName=PreferencesUtils.getSharePreStr(GroupInforActivity.this,"realName");
        String uesrName=PreferencesUtils.getSharePreStr(GroupInforActivity.this,"Name");
        Logger.d("GroupInforActivity:"+realName+"=="+uesrName);
        if(!"null".equals(realName)){
            chatinfoBottomNickname.setText(""+realName);
        }else {
            chatinfoBottomNickname.setText(""+uesrName);
        }

        list = exangeRowsBean(usersBean.getRows());
        if(usersBean.getTotal()>20){
            chatinfoBottomTotal.setVisibility(View.VISIBLE);
        }else{
            chatinfoBottomTotal.setVisibility(View.GONE);
        }
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
            switch (rowsBean.getUserType()==null ? 0:rowsBean.getUserType()) {
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
                         if (backendOperatorVOBean.getRealName()!=null&&TextUtils.isEmpty(backendOperatorVOBean.getRealName())) {
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

    private void parseGroupChatInfo(String msg) throws Exception {
        // Log.i("aaaaa", "setMsg: " + msg);
        JSONObject jsonbject = new JSONObject(msg);
        JSONObject data = jsonbject.getJSONObject("data");
        JSONObject groupChat = data.getJSONObject("groupChat");
        String businessName = groupChat.getString("businessName");
        String collegeName = groupChat.getString("collegeName");
        chatinfoBottomGroupName.setText(businessName);
        chatinfoBottomCollege.setText(collegeName);
        // Log.i("aaaaa", businessName + ":" + collegeName);
    }

    public void onClick(View view) {
        switch (view.getId()) {
              //查看全部成员
            case R.id.chatinfo_bottom_total:
                Intent intent=new Intent(GroupInforActivity.this,GroupMemberActivity.class);
                intent.putExtra(Const.GROUP_ID,receiver);
                intent.putExtra(Const.GROUP_TOTAL,groupTotal);
                startActivity(intent);
                break;
            //消息免打扰
            case R.id.chatinfo_bottom_messageNo:
               isCheck=!isCheck;
                if(isCheck){
                    chatinfoBottomMessageNo.setChecked(true);
                }else{
                    chatinfoBottomMessageNo.setChecked(false);
                }
                setNotDisturb(isCheck);
                break;
            //置顶消息
            case R.id.chatinfo_bottom_chatTop:
                break;
            //返回
            case R.id.groupchatinfo_back:
                finish();
                break;
        }
    }
}
