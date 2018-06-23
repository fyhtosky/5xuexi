package com.sj.yinjiaoyun.xuexi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.ColleageEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.College;
import com.sj.yinjiaoyun.xuexi.fragment.ContactsFragment;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by ${沈军 961784535@qq.com} on 2016/10/13.
 * 学号登录/发现的 的院校选择 界面
 */
public class SelectCollegeActivity extends Activity implements ContactsFragment.ContactsFragemntCallBack{

    String TAG="result";
    Intent  phoneIntent;//返回数据回到学号登录页面
    ContactsFragment contactsFragment;
    private College college;
    private int requestCode=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_student_login);
         init();
        if (savedInstanceState == null) {
            contactsFragment= new ContactsFragment();
            contactsFragment.setActivity(this);
            getFragmentManager().beginTransaction().add(R.id.container,contactsFragment ).commit();
        }
    }

    private void init() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            requestCode=bundle.getInt("requestCode",0);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        final int action = event.getAction();
        switch(action){
            case  MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //各个控件的点击事件
    public void onclick(View v){
        switch(v.getId()){
            case R.id.studentLogin_back://返回按钮
                if(college!=null){
                    phoneIntent = new Intent();
                    phoneIntent.putExtra("College",college);
                    setResult(requestCode,phoneIntent);
                }
                finish();
                break;
        }
    }


    @Override
    public void deliverValue(College college) {
        this.college=college;
        Logger.d("deliverValue:"+college.toString());
        phoneIntent = new Intent(this,LoginActivity.class);
        phoneIntent.putExtra("College",college);
         setResult(requestCode,phoneIntent);
        if(requestCode==0){
            EventBus.getDefault().post(new ColleageEvent(college.getId(),college.getOrganizationName()));
        }
        finish();
    }
}
