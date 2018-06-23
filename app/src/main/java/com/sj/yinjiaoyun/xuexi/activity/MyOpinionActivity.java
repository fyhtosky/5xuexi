package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/2.
 * 主页- 我fragment- 意见反馈页面
 */
public class MyOpinionActivity extends MyBaseActivity implements HttpDemo.HttpCallBack{

    String TAG="opinion";
    EditText etText;
    EditText etContact;
    EditText etTitle;//标题
    List<Pairs> pairsList;
    String content;//反馈意见
    String contact;//联系方式
    String title;//标题

    HttpDemo demo;
    Intent intent;

    String endUserId;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myopinion);
        etText= (EditText) findViewById(R.id.opinion_text);
        etContact= (EditText) findViewById(R.id.opinion_Contact);
        etTitle= (EditText) findViewById(R.id.opinion_title);
        inflater=LayoutInflater.from(this);
    }

    public void onclick(View view){
        switch(view.getId()) {
            case R.id.opinion_back://返回
                finish();
                break;
            case R.id.opinion_tijiao://提交
                sendMyOpinionToHttp();
                break;
        }
    }

    public void sendMyOpinionToHttp(){
        demo=new HttpDemo(this);
        title=etTitle.getText().toString();
        content=etText.getText().toString();
        contact=etContact.getText().toString();
        if(title.equals("")){
            Toast.makeText(this,"请填写反馈标题",Toast.LENGTH_SHORT).show();
            return;
        }
        if(content.equals("")){
            Toast.makeText(this,"请填写具体内容",Toast.LENGTH_SHORT).show();
            return;
        }
        if(contact.equals("")){
            Toast.makeText(this,"请填写联系方式",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!setMacther(contact)){
            Toast.makeText(this,"请填写合法联系方式",Toast.LENGTH_SHORT).show();
            return;
        }
        pairsList=new ArrayList<>();
        Log.i(TAG, "sendMyOpinionToHttp: "+endUserId+":"+content+":"+contact+":"+title);
        String url= MyConfig.getURl("aboutUs/addFeedBack");
//        pairsList.add(new Pairs("userId",endUserId));
        pairsList.add(new Pairs("title",title));
        pairsList.add(new Pairs("content",content));
        pairsList.add(new Pairs("contact",contact));
        demo.doHttpPostLoading(this,url,pairsList,0);
    }

    //匹配正则  QQ 手机号  email
    public Boolean setMacther(String text){
        String phone="^(((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))+\\\\d{8})$";
        String qq="^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$";
        String email="[1-9][0-9]{4,14}";
        if(text.matches(phone)){
            return true;
        }
        if(text.matches(qq)){
            return true;
        }
        return text.matches(email);
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        try {
            JSONObject obj=new JSONObject(msg);
            if(obj.getBoolean("success")){
                Toast.makeText(this,"提交成功",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this,"提交失败",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
