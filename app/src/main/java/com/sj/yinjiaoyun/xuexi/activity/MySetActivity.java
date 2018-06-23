package com.sj.yinjiaoyun.xuexi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.db.DbOperatorLogin;
import com.sj.yinjiaoyun.xuexi.domain.LoginInfo;
import com.sj.yinjiaoyun.xuexi.entry.LoginStatusToHttp;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.jpush.MyJPushUitl;
import com.sj.yinjiaoyun.xuexi.manager.DateclearManager;
import com.sj.yinjiaoyun.xuexi.manager.UpdateManager;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.view.MeView;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/2.
 *  主页- 我fragment- 设置 页面
 */
public class MySetActivity extends MyBaseActivity implements HttpDemo.HttpCallBack{

    UpdateManager updateManager;//检查更新工具类
    MeView mark;//检车跟新
    MeView qingchu;
    TitleBarView titleBarView;
    String versionName;
    //标示用户是否登录
    private boolean loginState;
    private Button btExit;
    private String endUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myset);
        endUserId = PreferencesUtils.getSharePreStr(MyApplication.getContext(),"username");
        updateManager=new UpdateManager(this);
        loginState=PreferencesUtils.getSharePreBoolean(MySetActivity.this, Const.LOGIN_STATE);
        mark= (MeView) findViewById(R.id.myset_check);
        btExit= (Button) findViewById(R.id.myset_exit);
        qingchu= (MeView) findViewById(R.id.myset_clean);
        titleBarView= (TitleBarView) findViewById(R.id.myset_titleBarView);
        versionName=updateManager.getVersionName();
        mark.setValuesForMark("V"+versionName);
        try {
            String appSize= DateclearManager.getTotalCacheSize(this);
            qingchu.setValuesForMark(appSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        titleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //未登录择不显示退出登录按钮
        if(!loginState){
            btExit.setVisibility(View.GONE);
        }else{
            btExit.setVisibility(View.VISIBLE);
        }
    }


    public void onclick(View view){
        switch(view.getId()) {
            case R.id.myset_clean://清除缓存
                claenCache();
                break;
            case R.id.myset_check://检查更新
                updateManager.checkHttpVersion(this);
                break;
            case R.id.myset_about://关于软件
                Intent inte=new Intent(this,AboutActivity.class);
                inte.putExtra("versionName",versionName);
                startActivity(inte);
                break;
            case R.id.myset_exit://退出登录
                //退出XMpp
                if(MyApplication.xmppConnection!=null ){
                    if(MyApplication.xmppConnection.isAuthenticated()){
                        MyApplication.xmppConnection.disconnect();
                        MyApplication.xmppConnection=null;
                        MyApplication.groupsList=null;
                        MyApplication.list=null;

                    }
                }
                //设置登录状态下线
                new LoginStatusToHttp().userUnderLine(endUserId);
                PreferencesUtils.putSharePre(getApplicationContext(),Const.LOGIN_STATE,false);
                Intent intent=new Intent(MySetActivity.this,MainActivity.class);
                startActivity(intent);
                MyJPushUitl.stopPush(this);
                MyJPushUitl.setClearNotification(this);
                ActiveActUtil.getInstance().exit();

                break;
            case R.id.myset_share:
                Intent intent1=new Intent(this,ShareActivity.class);
                startActivity(intent1);
                break;
        }
    }




    //清除缓存
    private void claenCache() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认清除缓存");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                DateclearManager.clearAllCache(MySetActivity.this);
                try {
                    String appSize= DateclearManager.getTotalCacheSize(MySetActivity.this);
                    qingchu.setValuesForMark(appSize);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i("UpDate", "parserUpdate: "+msg);
        try {
            JSONObject object=new JSONObject(msg);
            JSONObject data=object.getJSONObject("data");
            String apkUrl=data.getString("url");
            String version=data.getString("version");
            updateManager.checkIsUpdate(Integer.parseInt(version),apkUrl,1);//检查是否需要更新
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
