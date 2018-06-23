package com.sj.yinjiaoyun.xuexi.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import com.sj.yinjiaoyun.xuexi.entry.FakeImageSpan;
import com.sj.yinjiaoyun.xuexi.entry.ImageSpan;
import com.sj.yinjiaoyun.xuexi.view.RichEditText;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/7/17.
 * 富文本的工具类
 */
public class RichTextUtil {
    private static SpannableStringBuilder spBuilder;
    private static ForegroundColorSpan span;

    /**
     *
     * @param wholeStr 全部文字
     * @param highlightStr 改变颜色的文字
     * @param color 颜色
     */
    public static SpannableStringBuilder fillColor(String wholeStr, String highlightStr, int color) {
        span = new ForegroundColorSpan(color);
        try {
            if (!TextUtils.isEmpty(wholeStr) && !TextUtils.isEmpty(highlightStr)) {
                if (wholeStr.contains(highlightStr)) {
                /*
                 *  返回highlightStr字符串wholeStr字符串中第一次出现处的索引。
                 */
                    int start = wholeStr.indexOf(highlightStr);
                    int end = start + highlightStr.length();
                    spBuilder = new SpannableStringBuilder(wholeStr);
                    spBuilder.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    return spBuilder;
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return null;

    }

    /**
     * 设置Edittext的内容
     * @param richText
     * @return
     */
    public static Spanned convertRichTextToSpanned(String richText) {
        return RichTextConvertor.fromRichText(richText);
    }

    /**
     * 将富文本转成html的格式
     * @param spanned
     * @return
     */
    public static String convertSpannedToRichText(Spanned spanned) {
        List<CharacterStyle> spanList =
                Arrays.asList(spanned.getSpans(0, spanned.length(), CharacterStyle.class));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spanned);
        for (CharacterStyle characterStyle : spanList) {
            int start = stringBuilder.getSpanStart(characterStyle);
            int end = stringBuilder.getSpanEnd(characterStyle);
            if (start >= 0) {
                String htmlStyle = handleCharacterStyle(characterStyle,
                        stringBuilder.subSequence(start, end).toString());
                if (htmlStyle != null) {
                    stringBuilder.replace(start, end, htmlStyle);
                }
            }
        }
        return stringBuilder.toString();
    }
    private static String handleCharacterStyle(CharacterStyle characterStyle, String text) {
        if (characterStyle instanceof FakeImageSpan) {
            FakeImageSpan span = (FakeImageSpan) characterStyle;
            return String.format("<img src=\"%s\" />", span.getValue());
        } else if (characterStyle instanceof ImageSpan) {
            ImageSpan span = (ImageSpan) characterStyle;
            return String.format("<img src=\"%s\" />", TextUtils.isEmpty(span.getUrl()) ?
                    span.getFilePath() : span.getUrl());
        }
        return null;
    }

    /**
     * 设置edittext 富文本的内容
     * @param mEditTextInput
     * @param richText
     */
    public  static void setRichTextContent(final RichEditText mEditTextInput , String richText) {
        mEditTextInput.setRichText(richText);
        FakeImageSpan[] imageSpans = mEditTextInput.getFakeImageSpans();
        if (imageSpans == null || imageSpans.length == 0) {
            return;
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (final FakeImageSpan imageSpan : imageSpans) {
            final String src = imageSpan.getValue();
            if (src!=null &&src.startsWith("http")) {
                // web images
                new AsyncTask<String, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(String... params) {
                        try {
                            InputStream is = new URL(src).openStream();
                            return BitmapFactory.decodeStream(is);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap == null) {
                            return;
                        }
                        mEditTextInput.replaceDownloadedImage(imageSpan, bitmap, src);
                    }
                }.executeOnExecutor(executorService, src);
            } else {
                // local images
                mEditTextInput.replaceLocalImage(imageSpan, src);
            }
        }
    }
}
