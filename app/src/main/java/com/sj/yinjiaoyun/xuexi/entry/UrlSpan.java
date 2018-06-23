package com.sj.yinjiaoyun.xuexi.entry;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.callback.Span;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/11/13.
 */
public class UrlSpan extends ForegroundColorSpan implements Span<String> {
    private String mUrl;

    public UrlSpan(String url) {
        super(Color.parseColor("#01af63"));
        mUrl = url;
    }

    @Override
    public String getValue() {
        return mUrl;
    }
}