package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.OpenCourseVO;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domains.CouseFuse;
import com.sj.yinjiaoyun.xuexi.domains.OrderCommentCounterVO;
import com.sj.yinjiaoyun.xuexi.domains.ParseOpenIntro;
import com.sj.yinjiaoyun.xuexi.domains.ParseOpenIntroDate;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/16.
 * 公开课详情 简介
 */
public class OpenIntroFragment extends Fragment implements HttpDemo.HttpCallBack{

    String TAG="openintro";
    CouseFuse couseFuse;
    HttpDemo demo;
    OpenIntroHttpCallBack callback;

    Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_openintro,container,false);
        initView(view);
        getHttpDate();
        return view;
    }

    TextView couseName;
    TextView collegeName;
    TextView couseAttention;
    TextView couseGoodRate;
    TextView cousePrice;
    TextView couseIntro;
    View container;
    private void initView(View view) {
        container=view.findViewById(R.id.openintro_container);
        couseName= (TextView) view.findViewById(R.id.openintro_couseName);
        collegeName= (TextView) view.findViewById(R.id.openintro_collegeName);
        couseAttention= (TextView) view.findViewById(R.id.openintro_attention);
        couseGoodRate= (TextView) view.findViewById(R.id.openintro_goodrate);
        cousePrice= (TextView) view.findViewById(R.id.openintro_price);
        couseIntro= (TextView) view.findViewById(R.id.openintro_jianjie);
    }

    /**
     * 根据课程id和公开课类型查询公开课详细信息
     * @param
     */
    private void getHttpDate(){
        demo=new HttpDemo(this);
        String url= MyConfig.getURl("openCourse/findOpenCourseByCourseIdAndType");
        List<Pairs> pairsList=new ArrayList<>();
        pairsList.add(new Pairs("courseId",String.valueOf(couseFuse.getCourseId())));//课程ID
        pairsList.add(new Pairs("opencourseType",String.valueOf(couseFuse.getCourseType())));//公开课类型
        demo.doHttpGet(url,pairsList, MyConfig.CODE_GET_COURSEINFO);
    }

    /**
     * 从activity传递过来的数据
     * @param search      104 订单完成       101 订单未完成
     * @param
     */
    public void setDateFromActivity(CouseFuse search, OpenIntroHttpCallBack callback){
        this.couseFuse=search;
        this.callback=callback;
        Log.i(TAG, "setDateFromActivity: "+":"+search.toString() );
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        try {
            parseDate(msg);
        } catch (Exception e) {
            Log.e(TAG, "setMsg: "+e.toString());
        }
    }

    private void parseDate(String msg) throws Exception{
        Log.i(TAG, "setMsg: "+msg);
        Gson gson=new Gson();
        ParseOpenIntro parserOpenIntro=gson.fromJson(msg, ParseOpenIntro.class);
        ParseOpenIntroDate data=parserOpenIntro.getData();
        OrderCommentCounterVO vo=data.getOrderCommentCounterVO();
        OpenCourseVO opencouse=data.getOpenCourse();
        setMessageToView(opencouse,vo);
    }

    private void setMessageToView(OpenCourseVO opencouse, OrderCommentCounterVO vo) {
        if(callback!=null){
            callback.deliveryIdAndName(opencouse.getId(),opencouse.getCourseName());
        }
        couseName.setText(opencouse.getCourseName());
        collegeName.setText(opencouse.getCollegeName());
        couseGoodRate.setText("好评率："+vo.getGoodRate()+"%");
        couseAttention.setText(opencouse.getNumber()+"");
        cousePrice.setText(saveTwoScale(opencouse.getPrice()));
        couseIntro.setText(Html.fromHtml(opencouse.getCourseDesc()));
        container.setVisibility(View.VISIBLE);
    }

    public  String saveTwoScale(Double d){
        DecimalFormat df=new DecimalFormat("0.00");
        if(d !=null && d > 0){
            cousePrice.setTextColor(activity.getResources().getColor(R.color.colorRed));
            return "¥"+df.format(d);
        }
        cousePrice.setTextColor(activity.getResources().getColor(R.color.colorGreen));
        return "免费";
    }

    public interface OpenIntroHttpCallBack{
        void deliveryIdAndName(Long id,String courseName);
    }

}
