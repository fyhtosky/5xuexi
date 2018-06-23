package com.sj.yinjiaoyun.xuexi.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/28.
 * 公开课 课程预览页面、课程详情 视频页面的评价 微专业 评价
 */
public class EvaluateFragment extends Fragment implements HttpDemo.HttpCallBack{

    String TAG="evaluate";
    HttpDemo demo;
    List<Pairs> pairsList;

   // static  Long objectId;//课程id
    Long objectId;//课程id
    int commentType;
    RadioGroup group;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;

    android.support.v4.app.FragmentTransaction tran;
    android.support.v4.app.FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView=inflater.inflate(R.layout.fragment_valuate,container,false);
        Log.i("fragment", "onCreateView: "+"GradeFragment");
        initView(convertView);
        getHttpDateForCounter(commentType);
        return convertView;
    }

    /**
     *
     * @param objectId   对象ID
     * @param commentType   评论类型  评价对象类型 0：专业 1公开课 2:微专业
     */
    public void setDateFromActivity(Long objectId,int commentType){
        this.objectId=objectId;
        this.commentType=commentType;
        Log.i(TAG, "setDateFromActivity: "+objectId);
    }


    int type=0;//获取数据类型
    @SuppressLint("CommitTransaction")
    private void initView(View convertView) {
        demo=new HttpDemo(this);
        group = (RadioGroup) convertView.findViewById(R.id.evaluate_radioGroup);
        rb1= (RadioButton) convertView.findViewById(R.id.evaluate_rb1);
        rb2= (RadioButton) convertView.findViewById(R.id.evaluate_rb2);
        rb3= (RadioButton) convertView.findViewById(R.id.evaluate_rb3);
        rb4= (RadioButton) convertView.findViewById(R.id.evaluate_rb4);
        manager=getFragmentManager();
        tran=manager.beginTransaction();
        CommentTotalFragment total=new CommentTotalFragment();
        total.setDateFromEvaluate(commentType,objectId,0);
        tran.add(R.id.evaluate_container,total);
        tran.commit();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.evaluate_rb1://全部
                        CommentTotalFragment total=new CommentTotalFragment();
                        total.setDateFromEvaluate(commentType,objectId,0);
                        replaceFragemnt(total);
                        break;
                    case R.id.evaluate_rb2://好评
                        CommentGoodFragment good=new CommentGoodFragment();
                        good.setDateFromEvaluate(commentType,objectId,1);
                        replaceFragemnt(good);
                        break;
                    case R.id.evaluate_rb3://中评
                        CommentOkayFragment okey=new CommentOkayFragment();
                        okey.setDateFromEvaluate(commentType,objectId,2);
                        replaceFragemnt(okey);
                        break;
                    case R.id.evaluate_rb4://差评
                        CommentBadFragment bag=new CommentBadFragment();
                        bag.setDateFromEvaluate(commentType,objectId,3);
                        replaceFragemnt(bag);
                        break;
                }
            }
        });

    }

    @SuppressLint("CommitTransaction")
    private void replaceFragemnt(Fragment f){
        tran=manager.beginTransaction();
        tran.replace(R.id.evaluate_container,f);
        tran.commit();
    }

    /**
     * 获取条数信息
     */
    private void getHttpDateForCounter(int commentType){
        if(objectId==null){
            return;
        }
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("order/orderCommentCounter");
        pairsList.add(new Pairs("objectId",String.valueOf(objectId)));//专业ID
        pairsList.add(new Pairs("commentType",commentType+""));//评价对象类型 0：专业 1公开课 2:微专业
        demo.doHttpPost(url,pairsList,4);
    }



    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+requestCode+":"+msg);
        switch (requestCode){
            case 0://全部
                break;
            case 1://好评
                break;
            case 2://中评数据
                break;
            case 3://差评数据
                break;
            case 4://获取条数
                parserCounter(msg);
                break;
        }
    }

    //解析各类型条数
    public void parserCounter(String msg){
        try {
            JSONObject object=new JSONObject(msg);
            JSONObject data=object.getJSONObject("data");
            JSONObject comments=data.getJSONObject("comments");
            int total=comments.getInt("total");
            int good=comments.getInt("good");
            int okay=comments.getInt("okay");
            int bad=comments.getInt("bad");
            Log.i(TAG, "parserCounter: "+total);
            rb1.setText("全部评价\n"+total);
            rb2.setText("好评\n"+good);
            rb3.setText("中评\n"+okay);
            rb4.setText("差评\n"+bad);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
