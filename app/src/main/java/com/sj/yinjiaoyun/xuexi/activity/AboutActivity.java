package com.sj.yinjiaoyun.xuexi.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/1/5.
 * 设置里面 关于软件信息
 */
public class AboutActivity extends MyBaseActivity {

    private ImageView ivback;
    private String versionName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        Intent intent = getIntent();
        versionName= intent.getStringExtra("versionName");
        initView();
       ivback. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //申请权限
        if(Build.VERSION.SDK_INT>=23){
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        123);
            }
        }
    }
    public void  initView(){
        ivback= (ImageView) findViewById(R.id.view_titlebar_left);
        ImageView ivshare = (ImageView) findViewById(R.id.iv_share);
        ImageView imageView = (ImageView) findViewById(R.id.about_icon);
        TextView textView = (TextView) findViewById(R.id.about_hint);
        TextView tvText = (TextView) findViewById(R.id.about_text);
        textView.setText("我学习 V"+versionName);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(5)
                .oval(false)
                .build();
        Picasso.with(this)
                .load(R.mipmap.logo)
                .resize(150,150)
                .error(R.mipmap.default_userhead).centerCrop().transform(transformation).into(imageView);
        String text = " “我学习”互联网教育平台，聚合以各高等院校为主导的包含培训机构和教学资源供应商的广大教育机构，提供完善便利的网上学历教育和非学历教育网络学习平台，为用户提供“平台+内容+社区+服务”的一站式互联网职业教育服务。致力于打造招生、学习、就业、再培训的终身学习生态圈，促进互联网共享经济在成人继续教育领域的生根发芽和快速成长。";
        tvText.setText("  "+ text);
        String ShareUrl="http://a.app.qq.com/o/simple.jsp?pkgname=com.sj.yinjiaoyun.xuexi";
        final UMWeb web = new UMWeb(ShareUrl);
        web.setTitle("打造招生、学习、就业生态圈");//标题
        web.setThumb(new UMImage(AboutActivity.this, R.mipmap.logo));  //缩略图
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
                new ShareAction(AboutActivity.this)
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
                if( !UMShareAPI.get(AboutActivity.this).isInstall(AboutActivity.this, SHARE_MEDIA.WEIXIN)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(AboutActivity.this,"请安装微信");
                        }
                    });
                }
            }else if(platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
                //分享开始的回调
                if( !UMShareAPI.get(AboutActivity.this).isInstall(AboutActivity.this, SHARE_MEDIA.WEIXIN)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(AboutActivity.this,"请安装微信");
                        }
                    });
                }
            }else{
                if(!UMShareAPI.get(AboutActivity.this).isInstall(AboutActivity.this, SHARE_MEDIA.QQ)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShortToast(AboutActivity.this,"请安装qq");
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
