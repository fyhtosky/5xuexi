package com.sj.yinjiaoyun.xuexi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/29.
 * 只能代码中设置状态 并且点击按钮时候 状态不改变
 */
public class SaveCheckBox extends CheckBox {
    public SaveCheckBox(Context context) {
        super(context);
    }

    public SaveCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SaveCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
       // super.toggle();
    }
}
