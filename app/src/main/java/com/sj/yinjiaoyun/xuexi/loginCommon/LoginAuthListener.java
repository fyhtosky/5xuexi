package com.sj.yinjiaoyun.xuexi.loginCommon;

import android.app.Activity;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * 作者：VanHua on 2018/5/7 18:02
 * <p>登录授权的回调
 * 邮箱：1373516909@qq.com
 */
public abstract class LoginAuthListener implements UMAuthListener {
    private Activity mContext;


    public LoginAuthListener(Activity mContext) {
        this.mContext = mContext;

    }

    @Override
    public void onStart(SHARE_MEDIA platform) {
        if(platform.equals(SHARE_MEDIA.WEIXIN)){
            //分享开始的回调
            if( !UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.WEIXIN)){
                ToastUtil.showShortToast(mContext,"请安装微信");
            }
        }else{
            if(!UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.QQ)){
                ToastUtil.showShortToast(mContext,"请安装QQ");
        }
    }
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        doComplete(share_media,i,map);
    }

    protected abstract void doComplete(SHARE_MEDIA share_media, int i, Map<String, String> map);

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        if(throwable!=null){
            Logger.e("throw:"+throwable.getMessage());
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        Logger.d("throw:"+i);
    }
}
