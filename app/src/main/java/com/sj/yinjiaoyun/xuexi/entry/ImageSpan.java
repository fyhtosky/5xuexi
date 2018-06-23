package com.sj.yinjiaoyun.xuexi.entry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.sj.yinjiaoyun.xuexi.callback.Span;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/11/10.
 */
public class ImageSpan extends android.text.style.ImageSpan implements Span<String> {
    private String mFilePath;
    private String mUrl;

    public ImageSpan(Context context, Bitmap bitmap, String filePath) {
        super(context, bitmap);
        mFilePath = filePath;
    }

    public ImageSpan(Drawable drawable) {
        super(drawable);
    }

    public String getFilePath() {
        return mFilePath;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String getValue() {
        return null;
    }
}
