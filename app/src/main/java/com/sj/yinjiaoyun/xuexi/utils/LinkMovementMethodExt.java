package com.sj.yinjiaoyun.xuexi.utils;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.activity.HtmlImageActivity;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/27.
 * 拦截<Img></>标签图片的点击时间
 */
public class LinkMovementMethodExt extends LinkMovementMethod {
    private static LinkMovementMethod sInstance;
    private  Class spanClass = null;
    private static Context context;
    public static MovementMethod getInstance(Context con, Class _spanClass) {
        context=con;
        if (sInstance == null) {
            sInstance = new LinkMovementMethodExt();
            ((LinkMovementMethodExt)sInstance).spanClass = _spanClass;
        }

        return sInstance;
    }



    int x1;
    int x2;
    int y1;
    int y2;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            x1 = (int) event.getX();
            y1 = (int) event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = (int) event.getX();
            y2 = (int) event.getY();

            if (Math.abs(x1 - x2) < 10 && Math.abs(y1 - y2) < 10) {

                x2 -= widget.getTotalPaddingLeft();
                y2 -= widget.getTotalPaddingTop();

                x2 += widget.getScrollX();
                y2 += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y2);
                int off = layout.getOffsetForHorizontal(line, x2);
                /**
                 * get you interest span
                 */
                Object[] spans = buffer.getSpans(off, off, spanClass);
                if (spans.length != 0) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(spans[0]),
                            buffer.getSpanEnd(spans[0]));
                    for(Object span:spans){
                        if(span instanceof ImageSpan){
                            String picUrl=((ImageSpan) span).getSource();
                            Intent intent=new Intent(context, HtmlImageActivity.class);
                            intent.putExtra("picUrl",picUrl);
                            context.startActivity(intent);
                        }
                    }
                    return false;
                }
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }

    public boolean canSelectArbitrarily() {
        return true;
    }

    public boolean onKeyUp(TextView widget, Spannable buffer, int keyCode,
                           KeyEvent event) {
        return false;
    }
}
