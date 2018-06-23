package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/8.
 * 学籍切换，编辑备注
 */
public class SchoolMarkActivity extends MyBaseActivity {

    TextView textView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_schoolmark);
        textView= (TextView) findViewById(R.id.schoolmark_edit);
        intent=getIntent();
        textView.setText(intent.getStringExtra("beizhu"));
    }

    public void onclick(View view){
        switch(view.getId()){
            case R.id.schoolmark_back://返回
                finish();
                break;
        }
    }
}
