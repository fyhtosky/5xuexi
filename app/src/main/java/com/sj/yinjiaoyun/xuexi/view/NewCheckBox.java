package com.sj.yinjiaoyun.xuexi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/12.
 * 失去点击事件选中未选中的checkBox
 */
public class NewCheckBox  extends CheckBox{

    public NewCheckBox(Context context) {
        super(context);
    }

    public NewCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        //super.toggle();
    }
}
