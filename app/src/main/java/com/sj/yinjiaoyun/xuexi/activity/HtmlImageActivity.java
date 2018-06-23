package com.sj.yinjiaoyun.xuexi.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
@SuppressLint("SetJavaScriptEnabled")
public class HtmlImageActivity extends AppCompatActivity {

    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.webView)
    WebView webView;
    //展示图片的url
    private String picUrl;
    private String picName;
    private BitmapFactory.Options options ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_image);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            picUrl = bundle.getString("picUrl");
            if (picUrl != null && !TextUtils.isEmpty(picUrl)) {
                Logger.d("展示图片：" + picUrl.substring(picUrl.indexOf("http"),picUrl.length()));
//                picName= FileUtils.getFileName(picUrl);
//                File file = new File(Environment.getExternalStorageDirectory(), picName);
//                if(file.exists()) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
//                    ivImg.setImageBitmap(bitmap);
//                }
//                Picasso.with(HtmlImageActivity.this)
//                        .load(file.getAbsolutePath()).error(R.mipmap.logo).into(ivImg);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setBlockNetworkImage(false);
//                webView.loadUrl(picUrl);
                webView.loadDataWithBaseURL(null, "<img width=\"100%\" src=\""+picUrl.substring(picUrl.indexOf("http"),picUrl.length()).trim()+"\">", "text/html", "utf-8", null);

            }
        }
    }

    @OnClick(R.id.rl)
    public void onClick() {
        finish();
    }
}
