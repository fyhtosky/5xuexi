package com.sj.yinjiaoyun.xuexi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.RichTextEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RichTextActivity extends AppCompatActivity {
    private static final int START_CHANGE_PICTURE=2 ;
    private static final String TI_MU_ID="timuid";
    @BindView(R.id.webView)
    WebView webView;
    //表示题目的题号
    private int tiMuId;
    private String index="<input type=\"hidden\" id=\"x_u_e_i_x_@#$_x_u_e_i_x\" />";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_text);
        ButterKnife.bind(this);
        //支持js
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/richTextEditor.html");
        //添加客户端支持
        webView.setWebChromeClient(new WebChromeClient());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tiMuId=bundle.getInt(TI_MU_ID);
        }
    }

    @OnClick({R.id.iv_back, R.id.add_image,R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //启动相机
            case R.id.add_image:
                startActivityForResult(new Intent(RichTextActivity.this,UploadImageActivity.class),START_CHANGE_PICTURE);
                break;
            //保存
            case R.id.tv_save:
                if(tiMuId!=0){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        webView.evaluateJavascript("getHtml()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {

                                Logger.d("html的返回值："+value.replace("\\u003C","<").replaceAll("\\\\",""));
                                if(value!=null && !TextUtils.isEmpty(value)){
                                    EventBus.getDefault().post(new RichTextEvent(tiMuId,value.replace("\\u003C","<").replaceAll("\\\\","")));
                                    //关闭Activity
                                    RichTextActivity.this.finish();
                                }


                            }
                        });
                    }
                }else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        webView.evaluateJavascript("getHtml()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                Logger.d("html的返回值："+value.replace("\\u003C","<").replaceAll("\\\\",""));
                                if(value!=null && !TextUtils.isEmpty(value)){
                                    //数据是使用Intent返回
                                    Intent intent = new Intent();
                                    //把返回数据存入Intent
                                    intent.putExtra("result", value.replace("\\u003C","<").replaceAll("\\\\",""));
                                    //设置返回数据
                                    RichTextActivity.this.setResult(RESULT_OK, intent);
                                    //关闭Activity
                                    RichTextActivity.this.finish();
                                }


                            }
                        });
                    }
                }


                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 相机拍照完成后，返回图片路径
        if(requestCode==START_CHANGE_PICTURE){
            if (resultCode == Activity.RESULT_OK) {
                if(data!=null){
                    final String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                    Logger.d("相册回来的url:"+result);
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:insertImage(' ', '"+result+" ')");
                        }
                    });
                }
            }
        }
    }

    /**
     * 启动Activity
     */
    public static void StartActivity(Context context, int id ) {
        Intent intent = new Intent(context, RichTextActivity.class);
        intent.putExtra(TI_MU_ID, id);
        context.startActivity(intent);

    }

}
