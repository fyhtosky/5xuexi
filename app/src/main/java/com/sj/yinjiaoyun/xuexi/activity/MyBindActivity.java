package com.sj.yinjiaoyun.xuexi.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.CheckInfoBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.loginCommon.LoginBindListener;
import com.sj.yinjiaoyun.xuexi.loginCommon.ThirdLoginManager;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.view.MeView;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyBindActivity extends MyBaseActivity {

    @BindView(R.id.mysafe_titleBarView)
    TitleBarView mysafeTitleBarView;
    @BindView(R.id.bind_weixin)
    MeView bindWeixin;
    @BindView(R.id.bind_qq)
    MeView bindQq;

    private boolean IsQQBind;
    private boolean IsWeixinBind;

    private String endUserId;

    private ThirdLoginManager thirdLoginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bind);
        ButterKnife.bind(this);
        thirdLoginManager=new ThirdLoginManager(MyBindActivity.this);
        init();
        setDateRequest();
    }

    private void init() {
        mysafeTitleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        endUserId = PreferencesUtils.getSharePreStr(MyApplication.getContext(), "username");
    }

    private void setDateRequest() {
        String params = "?id=" + endUserId;
        HttpClient.get(this, Api.GET_USER_INFO + params, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
              if(result.isSuccess()){
                    if( !TextUtils.isEmpty(result.getData().getUser().getQqOpenid()) || !TextUtils.isEmpty(result.getData().getUser().getQqUuid())){
                        bindQq.getEtIcon().setImageResource(R.mipmap.qq);
                        bindQq.setValuesForText("QQ("+result.getData().getUser().getQqName()+")");
                        bindQq.setValuesForMark("解绑");
                    }else {
                        IsQQBind=true;
                        bindQq.getEtIcon().setImageResource(R.mipmap.qq_gray);
                        bindQq.setValuesForText("QQ");
                        bindQq.setValuesForMark("绑定");
                    }
                  if( !TextUtils.isEmpty(result.getData().getUser().getWxOpenid()) || !TextUtils.isEmpty(result.getData().getUser().getWxUuid())){
                      bindWeixin.getEtIcon().setImageResource(R.mipmap.weixin);
                      bindWeixin.setValuesForText("微信("+result.getData().getUser().getWxName()+")");
                      bindWeixin.setValuesForMark("解绑");
                  }else {
                      IsWeixinBind=true;
                      bindWeixin.getEtIcon().setImageResource(R.mipmap.weixin_gray);
                      bindWeixin.setValuesForText("微信");
                      bindWeixin.setValuesForMark("绑定");
                  }
              }
            }
        });
    }

    AlertDialog dialog;//免费公开课提示
    @OnClick({R.id.bind_weixin, R.id.bind_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bind_weixin:
                if(IsWeixinBind){
                    //绑定
                    thirdLoginManager.WeixinLogin(new LoginBindListener() {
                        @Override
                        public void doComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                            bindThirdLogin(share_media,map);
                        }
                    });

                }else {
                    final AlertDialog.Builder builder=new AlertDialog.Builder(this);
//                    builder.setTitle("提示");
                    builder.setMessage("解绑后，将无法通过微信直接登录当前i学令牌账户，确定要解绑吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //解绑
                                    UnbindThridLogin(2);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog=builder.create();
                    dialog.show();

                }

                break;
            case R.id.bind_qq:
                if(IsQQBind){
                    //绑定
                    thirdLoginManager.qqLogin(new LoginBindListener() {
                        @Override
                        public void doComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                            bindThirdLogin(share_media,map);
                        }
                    });

                }else {
                    final AlertDialog.Builder builder=new AlertDialog.Builder(this);
//                    builder.setTitle("提示");
                    builder.setMessage("解绑后，将无法通过QQ直接登录当前i学令牌账户，确定要解绑吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //解绑
                                    UnbindThridLogin(1);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog=builder.create();
                    dialog.show();

                }

                break;
        }
    }

    /**
     * 绑定QQ、微信的登录
     */
    private void bindThirdLogin(final SHARE_MEDIA share_media, final Map<String, String> map) {
        HashMap<String,String>info=new HashMap<>();
        info.put("token",PreferencesUtils.getSharePreStr(MyApplication.getContext(), Const.TOKEN));
        String loginType = "";
        switch (share_media){
            case QQ:
                loginType="1";
                break;
            case WEIXIN:
                loginType="2";
                break;
        }
        info.put("type",loginType);
        //2为微信1为QQ
        info.put("openid",map.get("openid"));
        info.put("unionid",map.get("unionid"));
        info.put("name",map.get("name"));
        info.put("figureurl",map.get("iconurl"));
        HttpClient.post(this, Api.BIND_THIRD_LOGIN, info, new CallBack<CheckInfoBean>() {
            @Override
            public void onSuccess(CheckInfoBean result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
                    //刷新Token
                    PreferencesUtils.putSharePre(getApplicationContext(),Const.TOKEN,result.getData().getUser().getToken());
                    //有账号则登录操作
                    switch (share_media){
                        case QQ:
                            IsQQBind=false;
                            bindQq.getEtIcon().setImageResource(R.mipmap.qq);
                            bindQq.setValuesForText("QQ("+map.get("name")+")");
                            bindQq.setValuesForMark("解绑");
                            ToastUtil.showShortToast(MyApplication.getContext(),"QQ账号绑定成功");
                            break;
                        case WEIXIN:
                            IsWeixinBind=false;
                            bindWeixin.getEtIcon().setImageResource(R.mipmap.weixin);
                            bindWeixin.setValuesForText("微信("+map.get("name")+")");
                            bindWeixin.setValuesForMark("解绑");
                            ToastUtil.showShortToast(MyApplication.getContext(),"微信账号绑定成功");
                            break;
                    }
                }else {
                    ToastUtil.showShortToast(MyApplication.getContext(),result.getMessage());
                    if(result.getState()==406){
                        HttpClient.onStartActivity();
                    }
                }
            }

        });
    }

    /**
     * 解除QQ、微信的登录状态
     * @param i
     */
    private void UnbindThridLogin(final int i) {
        HashMap<String,String>map=new HashMap<>();
        map.put("type",String.valueOf(i));
        map.put("token",PreferencesUtils.getSharePreStr(MyApplication.getContext(), Const.TOKEN));
        HttpClient.post(this, Api.UNBIND_THIRD_LOGIN, map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
                    switch (i){
                        case 1:
                            IsQQBind=true;
                            bindQq.getEtIcon().setImageResource(R.mipmap.qq_gray);
                            bindQq.setValuesForText("QQ");
                            bindQq.setValuesForMark("绑定");
                            ToastUtil.showShortToast(MyApplication.getContext(),"QQ账号解绑成功");
                            break;
                        case 2:
                            IsWeixinBind=true;
                            bindWeixin.getEtIcon().setImageResource(R.mipmap.weixin_gray);
                            bindWeixin.setValuesForText("微信");
                            bindWeixin.setValuesForMark("绑定");
                            ToastUtil.showShortToast(MyApplication.getContext(),"微信账号解绑成功");
                            break;

                    }
                }else {
                    if(result.getState()==406){
                         HttpClient.onStartActivity();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}
