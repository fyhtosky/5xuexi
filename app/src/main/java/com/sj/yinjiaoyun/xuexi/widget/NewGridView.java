package com.sj.yinjiaoyun.xuexi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/4.
 * 课程表   方向选择  方向信息
 */
public class NewGridView extends GridView {

    public NewGridView(Context context) {
        super(context);
    }

    public NewGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
