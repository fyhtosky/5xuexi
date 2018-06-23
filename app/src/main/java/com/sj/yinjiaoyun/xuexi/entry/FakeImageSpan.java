package com.sj.yinjiaoyun.xuexi.entry;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

import com.sj.yinjiaoyun.xuexi.callback.Span;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/11/10.
 */
public class FakeImageSpan extends CharacterStyle implements Span<String> {
    private String mUrl;

    public FakeImageSpan(String url) {
        mUrl = url;
    }
    @Override
    public void updateDrawState(TextPaint tp) {

    }

    @Override
    public String getValue() {
        return mUrl;
    }
}
