package com.sj.yinjiaoyun.xuexi.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wanzhiying on 2017/4/5.
 */
public class ProcessImageView extends ImageView {
    private Paint mPaint;// 画笔
    int width = 0;
    int height = 0;
    Context context = null;
    int progress = 0;
    private Rect rect;

    public ProcessImageView(Context context) {
       this(context,null,0);
    }

    public ProcessImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessImageView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
        rect = new Rect();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.FILL);


        mPaint.setColor(Color.parseColor("#70000000"));// 半透明
        canvas.drawRect(0, 0, getWidth(), getHeight()- getHeight() * progress
                / 100, mPaint);

        mPaint.setColor(Color.parseColor("#00000000"));// 全透明
        canvas.drawRect(0, getHeight() - getHeight() * progress / 100,
                getWidth(), getHeight(), mPaint);

        mPaint.setTextSize(30);
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setStrokeWidth(2);
        mPaint.getTextBounds("100%", 0, "100%".length(), rect);// 确定文字的宽度
        canvas.drawText(progress + "%", getWidth() / 2 - rect.width() / 2,
                getHeight() / 2, mPaint);

    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

}
