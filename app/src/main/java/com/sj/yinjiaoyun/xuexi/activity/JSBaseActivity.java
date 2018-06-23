package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.JsReturnBean;
import com.sj.yinjiaoyun.xuexi.jpush.MyJPushUitl;
import com.sj.yinjiaoyun.xuexi.jsforandroid.ForgetPwdObject;
import com.sj.yinjiaoyun.xuexi.jsforandroid.JSforAndroidListener;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class JSBaseActivity extends AppCompatActivity implements JSforAndroidListener {

    @BindView(R.id.webView)
    WebView webView;
    //webView添加cookie
    protected String cookies ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_js_base);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 加载指定url
     * @return
     */
    @NonNull
    protected abstract String getJsUrl() ;

    /**
     * 获取操作之后的处理
     * @param result
     */
    protected abstract void onResult(String result);

    /**
     * 布局初始化
     */
    protected  void init(){
        cookies=PreferencesUtils.getSharePreStr(JSBaseActivity.this,Const.TOKEN);
        WebSettings webSettings = webView.getSettings();
        //是否支持js
        webSettings.setJavaScriptEnabled(true);
        //设置是否使用WebView推荐使用的窗口
        webSettings.setUseWideViewPort(true);
        //设置加载页面的模式
        webSettings.setLoadWithOverviewMode(true);
        //设置是否支持缩放
        webSettings.setSupportZoom(true);
        webView.addJavascriptInterface(new ForgetPwdObject(this), "iXueLingPaiJs");
        if(getJsUrl()!=null){
            syncCookie();
            webView.loadUrl(getJsUrl());
            Logger.d(getJsUrl());
            webView.setWebViewClient(new WebViewClient() {
                // 在当前WebView中打开新连接，不跳转到系统浏览器
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    webView.loadUrl(url);
                    return true;
                }

                // 重写此方法可以让webview处理https请求
                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
//                super.onReceivedSslError(view, handler, error);
                }
            });
            //添加google自带的浏览器
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                }

                @Override
                public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                    return super.onJsPrompt(view, url, message, defaultValue, result);
                }
            });

        }

    }

    /**
     * 设置webView cookie
     */
    protected  void syncCookie(){

    }
    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
        webView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        //恢复pauseTimers状态
        webView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.setWebViewClient(null);
            webView.setWebChromeClient(null);
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((RelativeLayout) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
            Logger.d("WebView销毁");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }

    }

    @Override
    public void getStatus(String status) {
        Logger.d("js回调的结果："+status);
        JsReturnBean jsReturnBean= new Gson().fromJson(status, JsReturnBean.class);
        switch (jsReturnBean.getStatus()){
            case 200:
                onResult(jsReturnBean.getData());
                break;
            case 401:
                //退出XMpp
                if(MyApplication.xmppConnection!=null ){
                    if(MyApplication.xmppConnection.isAuthenticated()){
                        MyApplication.xmppConnection.disconnect();
                        MyApplication.xmppConnection=null;
                        MyApplication.groupsList=null;
                        MyApplication.list=null;

                    }
                }
                //设置登录状态
                PreferencesUtils.putSharePre(JSBaseActivity.this, Const.LOGIN_STATE,false);
                Intent intent=new Intent(JSBaseActivity.this,LoginActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
                MyJPushUitl.stopPush(JSBaseActivity.this);
                MyJPushUitl.setClearNotification(JSBaseActivity.this);
                ActiveActUtil.getInstance().exit();
                break;
            case 500:
                ToastUtil.showShortToast(JSBaseActivity.this,jsReturnBean.getData());
                break;

        }
    }
}
