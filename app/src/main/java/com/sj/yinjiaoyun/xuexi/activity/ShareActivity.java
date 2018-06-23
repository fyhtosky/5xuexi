package com.sj.yinjiaoyun.xuexi.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/1/5.
 * 设置 分享给朋友
 */
public class ShareActivity extends MyBaseActivity{

    private ImageView ivBack;
    private ImageView ivshare;
    private String ShareUrl="http://a.app.qq.com/o/simple.jsp?pkgname=com.sj.yinjiaoyun.xuexi";
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_share);
        ivBack= (ImageView) findViewById(R.id.iv_back);
        ivshare= (ImageView) findViewById(R.id.iv_share);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //申请权限
        if(Build.VERSION.SDK_INT>=23){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        123);
            }
        }
        final UMWeb web = new UMWeb(ShareUrl);
        web.setTitle("打造招生、学习、就业生态圈");//标题
        web.setThumb(new UMImage(ShareActivity.this, R.mipmap.logo));  //缩略图
        web.setDescription("5xuexi在线学习平台是集“课时资源服务、在线学习、网校管理、网校招生、在线学习社区”为一体，" +
                "提供便利的网上学历与非学历教育的网络学习平台，聚合以高等院校为主");//描述
        //设置分享面板的样式
        final ShareBoardConfig config = new ShareBoardConfig();
        //设置分享面板的位置
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);
       // 设置item背景形状
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
        //设置分享面板title文本内容
        config.setTitleText("分享到");
        //设置指示器的显示状态
        config.setIndicatorVisibility(false);
        //设置取消按钮是否显示
        config.setCancelButtonVisibility(false);
        ivshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //授权按钮登录
//                UMShareAPI  mShareAPI = UMShareAPI.get( ShareActivity.this );
//                mShareAPI.doOauthVerify(ShareActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                //打开面板分享
                new ShareAction(ShareActivity.this)
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener).open(config);
            }
        });
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            if(platform.equals(SHARE_MEDIA.WEIXIN)){
                //分享开始的回调
                if( !UMShareAPI.get(ShareActivity.this).isInstall(ShareActivity.this, SHARE_MEDIA.WEIXIN)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(ShareActivity.this,"请安装微信");
                        }
                    });
                }
            }else if(platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
                //分享开始的回调
                if( !UMShareAPI.get(ShareActivity.this).isInstall(ShareActivity.this, SHARE_MEDIA.WEIXIN)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(ShareActivity.this,"请安装微信");
                        }
                    });
                }
            }else{
                if(!UMShareAPI.get(ShareActivity.this).isInstall(ShareActivity.this, SHARE_MEDIA.QQ)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(ShareActivity.this,"请安装qq");
                        }
                    });
                }
            }

        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
                 Logger.d("platform"+platform);


        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(t!=null){
                Logger.d("throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

        }
    };
    /**
     * 点击授权登录的回调
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
