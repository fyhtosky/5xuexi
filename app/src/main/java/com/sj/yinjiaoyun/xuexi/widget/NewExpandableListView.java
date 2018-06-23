package com.sj.yinjiaoyun.xuexi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/9/1.
 * 自定义expandListView适应ScrollView，解决expandListView与ScrollView滑动的冲突
 */
public class NewExpandableListView extends ExpandableListView{

    public NewExpandableListView(Context context) {
        super(context);
    }

    public NewExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
