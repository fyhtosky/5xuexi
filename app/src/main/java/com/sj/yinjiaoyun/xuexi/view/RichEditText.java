package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.EditText;

import com.sj.yinjiaoyun.xuexi.entry.FakeImageSpan;
import com.sj.yinjiaoyun.xuexi.entry.ImageSpan;
import com.sj.yinjiaoyun.xuexi.utils.BitmapUtils;
import com.sj.yinjiaoyun.xuexi.utils.RichTextUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/11/10.
 */
public class RichEditText extends EditText {
    private  int IMAGE_MAX_WIDTH ;
    private int IMAGE_MAX_HEIGHT ;
    private Context mContext;

    public RichEditText(Context context) {
        super(context,null);

    }

    public RichEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        IMAGE_MAX_WIDTH=mContext.getResources().getDisplayMetrics().widthPixels;
        IMAGE_MAX_HEIGHT=IMAGE_MAX_WIDTH;
    }

    public void setRichText(String richText) {
        setText(RichTextUtil.convertRichTextToSpanned(richText));
    }
    public String getRichText() {
        return RichTextUtil.convertSpannedToRichText(getText());
    }

    /**
     * 添加本地文件夹内的图片
     * @param filePath
     */
    public void addImage(String filePath) {
        SpannableString spannable = new SpannableString("\n<img src=\"" + filePath + "\"/>");
        Bitmap bitmap = BitmapUtils.decodeScaleImage(filePath, IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);
        if (bitmap == null) {
            return;
        }
        ImageSpan span = new ImageSpan(mContext, bitmap, filePath);
        spannable.setSpan(span, 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getText().insert(getSelectionStart(), spannable);
    }


    /**
     * 替换网络图片
     * @param span
     * @param bitmap
     * @param url
     */
    public void replaceDownloadedImage(FakeImageSpan span, Bitmap bitmap, String url) {
        Bitmap scaledBitmap = BitmapUtils.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);

        SpannableString spannable = new SpannableString("<img />");
        ImageSpan imageSpan = new ImageSpan(mContext, scaledBitmap, null);
        imageSpan.setUrl(url);
        spannable.setSpan(imageSpan, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getText().replace(getText().getSpanStart(span), getText().getSpanEnd(span), spannable);
    }


    /**
     * 替换本地文件的图片
     * @param span
     * @param filePath
     */
    public void replaceLocalImage(FakeImageSpan span, String filePath) {
        Bitmap bitmap = BitmapUtils.decodeScaleImage(filePath, IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);
        if (bitmap == null) {
            return;
        }
        SpannableString spannable = new SpannableString("<img src=\"" + filePath + "\"/>");
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap, filePath);
        spannable.setSpan(imageSpan, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getText().replace(getText().getSpanStart(span), getText().getSpanEnd(span), spannable);
    }

    public FakeImageSpan[] getFakeImageSpans() {
        return getText().getSpans(0, getText().length(), FakeImageSpan.class);
    }
}
