package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/1/12.
 * 订单评价页面
 */
public class OrderCommentActivity extends MyBaseActivity implements HttpDemo.HttpCallBack{

    String TAG ="ordercomment" ;
    RatingBar ratingBar;
    EditText editText;

    HttpDemo demo;
    List<Pairs> pairsList;

    String endUserId;
    String orderId;//订单id
    String objectId;//评论对象ID
    int commentType;//评论类型 0：专业 ，1： 公开课 2：微专业
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ordercomment);
        init();
        initView();
    }

    private void init() {
        demo=new HttpDemo(this);
        intent=getIntent();
        endUserId=intent.getStringExtra("endUserId");
        orderId=intent.getStringExtra("orderId");
        objectId=intent.getStringExtra("objectId");
        commentType=intent.getIntExtra("commentType",1);
    }

    private void initView() {
        ratingBar= (RatingBar) findViewById(R.id.ordercomment_ratingBar);
        editText= (EditText) findViewById(R.id.ordercomment_EditView);
    }

    public void onclick(View v){
        switch (v.getId()){
            case R.id.ordercomment_back:
                finish();
                break;
            case R.id.ordercomment_tijiao:
                sendHttpForTiJiao();
                break;
        }
    }

    private void sendHttpForTiJiao() {
        int stars= (int) ratingBar.getRating();
        String content=editText.getText().toString();
        if(stars==0){
            Toast.makeText(this,"请评分",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(content)){
            Toast.makeText(this,"请输入评价内容",Toast.LENGTH_SHORT).show();
            return;
        }
        if(content.length()>300){
            Toast.makeText(this,"字数太多了",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(objectId) || TextUtils.isEmpty(objectId)){
            Toast.makeText(this,"参数为空",Toast.LENGTH_SHORT).show();
            return;
        }
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("order/addOrderComment");
        pairsList.add(new Pairs("orderId",orderId));//订单id
        pairsList.add(new Pairs("objectId",objectId));//评论对象ID
        pairsList.add(new Pairs("commentType",commentType+""));//评论类型 0：专业 ，1： 公开课 2：微专业
        pairsList.add(new Pairs("endUserId",endUserId));
        pairsList.add(new Pairs("stars",stars+""));
        pairsList.add(new Pairs("content",content));
        demo.doHttpPostLoading(this,url,pairsList,0);
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        try{
            JSONObject object=new JSONObject(msg);
            if(object.getBoolean("success")){
                Log.i(TAG, "setMsg: "+"提交成功");
                Toast.makeText(this,"评论成功",Toast.LENGTH_SHORT).show();
                Intent intents = new Intent(this,MyOrderActivity.class);
                setResult(1,intents);
                finish();
            }else{
                Toast.makeText(this,object.getString("message"),Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e(TAG, "setMsg: "+e.toString());
        }
    }
}
