package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.AppConfigBean;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.manager.UpdateManager;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import java.util.HashMap;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/21.
 * 欢迎页面、启动页面（每次进来的前置页面）
 */
public class WelcomeActivity extends MyBaseActivity {
    //标示第一次进来
    private Handler handler=new Handler();
    private ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_beforeone);
        imageView= (ImageView) findViewById(R.id.before_img);
        AlphaAnimation animation=new AlphaAnimation(0,1);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
        getAppConfig();
    }
    /**
     * 获取网路集体配置
     */
    private void getAppConfig() {
        try {
            HashMap<String,String>map=new HashMap<>();
            map.put("appType","1");
            map.put("version",String.valueOf(getPackageManager().getPackageInfo(getPackageName(),0).versionCode));
            HttpClient.post(this, Api.GET_APP_URI_CONFIG, map, new CallBack<AppConfigBean>() {
                @Override
                public void onSuccess(AppConfigBean result) {
                    if(result==null){
                        return;
                    }
                    if(result.isSuccess()){
                        //重新部署服务器
                        Api.XMPP_HOST=result.getData().getProperties().getTigaseUrl();
                        Api.Host=result.getData().getProperties().getXuexiUrl();
                        Api.PHOTO_HOST=result.getData().getProperties().getPicroomUrl();
                        Api.TOKEN_HOST=result.getData().getProperties().getIxueUrl();
                    }
                    mainDoor();
                }
            });

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void mainDoor() {
        //延时启动
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!PreferencesUtils.getSharePreBoolean(getApplicationContext(), Const.ISFIRST)){
                    Intent intent=new Intent(WelcomeActivity.this,GuideActivity.class);
                    startActivity(intent);
                    PreferencesUtils.putSharePre(getApplicationContext(),Const.ISFIRST,true);
                }else{
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                }
                finish();
            }


        }, 3000);
    }

}
