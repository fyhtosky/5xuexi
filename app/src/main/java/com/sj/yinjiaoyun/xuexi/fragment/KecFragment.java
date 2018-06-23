package com.sj.yinjiaoyun.xuexi.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.KecExpandAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.KecListAdapter;
import com.sj.yinjiaoyun.xuexi.domain.Kec;
import com.sj.yinjiaoyun.xuexi.domain.KecParent;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParserKec;
import com.sj.yinjiaoyun.xuexi.domain.ParserKecDate;
import com.sj.yinjiaoyun.xuexi.domain.SoaProductVOs;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.widget.NewExpandableListView;
import com.sj.yinjiaoyun.xuexi.widget.NewListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/10.
 *  课程表  课程选择导航
 */
public class KecFragment extends Fragment implements HttpDemo.HttpCallBack{

    String TAG="kecheng";

    Button button;
    View footerView;
    View defaultContainer;
    TextView tvTitle;//标题
    HttpDemo demo;
    List<Pairs> pairsList;
    CallBackFromKec callBackFromKec;
    String endUserId;
    String enrollPlanId;
    SoaProductVOs soa;
    Boolean isFirst;

    NewListView newListView;
    NewExpandableListView newExpandableListView;
    List<KecParent> dateExpand;//填充课程adapter的集合(非培训考证类型的课程选择)
    KecExpandAdapter expandAdapter;
    List<Kec> dateList;//填充课程adapter的集合(培训考证类型的课程选择)
    KecListAdapter listAdapter;
    List<Long> teachingPlanIdsList;//选择的教学计划id集合


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_kec,container,false);
        footerView=inflater.inflate(R.layout.footer_kec,null);
        initView(v,footerView);
        initEvent();
        getKecForHttp();
        return v;
    }

    public void initView(View v,View footerView){
        isFirst=true;
        tvTitle= (TextView) v.findViewById(R.id.kec_tv_titlePlace);
        defaultContainer=v.findViewById(R.id.kec_defaultContainer);
        newExpandableListView= (NewExpandableListView)v.findViewById(R.id.major_kec_expand);
        newListView= (NewListView) v.findViewById(R.id.major_kec_list);
        button= (Button) footerView.findViewById(R.id.btnCouse);
        button.setText("移动端此功能尚未开放，请于网页端操作");
        button.setBackgroundResource(R.drawable.btn_sure_gray);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                if(isFirst){
//                  //  callBackFromKec.replaceFragment(2);//表示专业已选择,切换为已选列表页面
//                    sendBaoCunToHttp();//保存课程接口
//                }
//            }
//        });
    }

    private void initEvent() {
        demo=new HttpDemo(this);
        if(soa.getProductType() ==3 || soa.getProductType() ==4){//培训 考证 类型时候选课
            //一节列表显示
            newListView.setVisibility(View.VISIBLE);
            newExpandableListView.setVisibility(View.GONE);
            listAdapter=new KecListAdapter(getActivity(),dateList);
            newListView.setAdapter(listAdapter);
            newListView.setEnabled(false);
        }else{
            //二级列表显示
            newListView.setVisibility(View.GONE);
            newExpandableListView.setVisibility(View.VISIBLE);
        }
    }



    //从网络上获取选课表数据信息
    private void getKecForHttp(){
        if(soa==null || endUserId==null || enrollPlanId==null)
            return;
        Log.i(TAG, "getKecForHttp: "+endUserId+":"+enrollPlanId+":");
        String url= MyConfig.getURl("myProduct/findMyTeachingPlansByEnrollPlanId");
        pairsList=new ArrayList<>();
        pairsList.add(new Pairs("userId",endUserId+""));
        pairsList.add(new Pairs("enrollPlanId",enrollPlanId+""));
        if(soa.getProductType()==3 || soa.getProductType()==4){
            demo.doHttpPostLoading(getActivity(),url,pairsList,1);//为 培训 考证
        }else{
            demo.doHttpPostLoading(getActivity(),url,pairsList,2);//0：网校 1：成教 2：自考
        }
    }




    //保存所选专业按钮
    private void sendBaoCunToHttp() {
        Log.i(TAG, "保存: ");
        teachingPlanIdsList=new ArrayList<>();
        //获取所选课程的id集合
        if(soa.getProductType() ==3 || soa.getProductType() ==4) {//培训 考证 类型时候选课
            for (Map.Entry<Integer, Boolean> entry : listAdapter.getSelectMap().entrySet()) {
                Log.i(TAG, "sendBaoCunToHttp: "+"Key = " + entry.getKey() + ", Value = " + entry.getValue());
                if(entry.getValue()){
                    Kec a=dateList.get(entry.getKey());
                    teachingPlanIdsList.add(a.getId());
                }
            }
        }else{
            for (Map.Entry<Long, Boolean> entry : expandAdapter.getExpandSelectMap().entrySet()) {
                Log.i(TAG, "sendBaoCunToHttp: "+"Key = " + entry.getKey().toString() + ", Value = " + entry.getValue());
                if(entry.getValue()){
                    teachingPlanIdsList.add(entry.getKey());
                }
            }
        }
        if(teachingPlanIdsList.size()<0){
            Log.i(TAG, "sendBaoCunToHttp: ");
            return;
        }
        StringBuilder sb=new StringBuilder();
        int a=teachingPlanIdsList.size();
        for(int i=0;i<teachingPlanIdsList.size();i++){
            Log.i(TAG, "sendBaoCunToHttp: "+teachingPlanIdsList.get(i));
            if((a-1)==i){
                sb.append(teachingPlanIdsList.get(i));
            }else{
                sb.append(teachingPlanIdsList.get(i)).append(",");
            }
        }
        Log.i(TAG, "sendBaoCunToHttp: "+endUserId+":"+enrollPlanId+":"+"teachingPlanIds"+sb.toString());
        String url= MyConfig.getURl("myProduct/addCourseSchedules");
        pairsList=new ArrayList<>();
        pairsList.add(new Pairs("endUserId",endUserId+""));
        pairsList.add(new Pairs("teachingPlanIds",sb.toString()));//选择的教学计划id集合
        pairsList.add(new Pairs("enrollmentId",enrollPlanId+""));
        demo.doHttpPostLoading(getActivity(),url,pairsList,0);
    }


    //从专业传过来的方向集合
    public void setKecDateFromMajor(KecFragment.CallBackFromKec callBackFromFangx, String endUserId, SoaProductVOs soa){
        this.callBackFromKec=callBackFromFangx;
        this.endUserId=endUserId;
        this.soa=soa;
        this.enrollPlanId=String.valueOf(soa.getEnrollPlanId());
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+requestCode+":"+msg);
        try {
            if (requestCode == 0) {//保存所选课程接口
                    JSONObject obj = new JSONObject(msg);
                    if (obj.getBoolean("success")) {
                        isFirst = false;
                        Log.i(TAG, "setMsg: " + "保存专业成功");
                        callBackFromKec.replaceFragment(2);//表示专业已选择,切换为已选列表页面
                    } else {
                        String message = obj.getString("message");
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
            } else if (requestCode == 1) {//获取选择的课程信息  //为 培训 考证
                parserKecList(msg);
            } else if (requestCode == 2) {//获取选择的课程信息  //网校 成教 自考
                parserKecExpand(msg);
            }
        }catch (Exception e){
            Log.i(TAG, "setMsg: "+e.toString());
        }
    }

    //专业内容为培训考证，直接显示一级选课菜单（不选方向）
    private void parserKecList(String msg) {
            Gson gson=new Gson();
            ParserKec kec=gson.fromJson(msg, ParserKec.class);
            ParserKecDate data=kec.getData();
            dateList=data.getData();
            if(dateList.size()==0){
                footerView.setVisibility(View.GONE);
                tvTitle.setVisibility(View.GONE);
                defaultContainer.setVisibility(View.VISIBLE);
                return;
            }else{
                footerView.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
                defaultContainer.setVisibility(View.GONE);
            }
            Log.i(TAG, "parserKecList: "+dateList.size());
            listAdapter.refresh(dateList);
            newListView.addFooterView(footerView);
    }

    //所选方向对应的选课 为二级菜单
    private void parserKecExpand(String msg) throws Exception{
        Gson gson=new Gson();
        ParserKec kec=gson.fromJson(msg, ParserKec.class);
        ParserKecDate data=kec.getData();
        List<Kec> list=data.getData();
        if(list.size()==0){
            Toast.makeText(getActivity(),"无课程数据",Toast.LENGTH_SHORT).show();
            return;
        }
        exangeDate(list);
        if(dateExpand==null || dateExpand.size()==0){
            footerView.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
            defaultContainer.setVisibility(View.VISIBLE);
            return;
        }else{
            footerView.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            defaultContainer.setVisibility(View.GONE);
        }
        expandAdapter=new KecExpandAdapter(getActivity(),dateExpand);
        newExpandableListView.setAdapter(expandAdapter);
        newExpandableListView.addFooterView(footerView);

        newExpandableListView.setGroupIndicator(null);//设置圆圈为空
        newExpandableListView.setDividerHeight(1);
        //设置首次加载张开
        for (int i = 0; i < dateExpand.size(); i++) {
            newExpandableListView.expandGroup(i);
        }
        //设置ExpandableListView不能收缩
        newExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        newExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.i(TAG, "onChildClick: ");
                return true;
            }
        });
        callBackFromKec.smoothScrollToTop();//设置scrollView 专业头部和列表从上至下置顶
    }


   //转化课程集合
    private List<KecParent> exangeDate(List<Kec> list) {
        Log.i(TAG, "exangeDate: "+"转化集合");
        int[] theYear={1,2,3,4,5,6,7,8,9,10,11};
        dateExpand=new ArrayList<>();//父集合
        String xueQi;
        List<Kec> kecChild;//子集合
        Kec kec;
        for (int j:theYear){
            kecChild = new ArrayList<>();
            xueQi = "第" +j + "学期";
            for(int i=0;i<list.size();i++) {
                kec = list.get(i);
                if (kec.getTheFewYear()!=null && kec.getTheFewYear() == j) {//第j学期
                    kecChild.add(kec);//添加自子集合
                }
            }
            if(kecChild.size()>0){
                dateExpand.add(new KecParent(xueQi, kecChild));//添加父集合
            }
        }
        return dateExpand;
    }

    //接口回调
    public interface CallBackFromKec{
        void replaceFragment(int flag);
        void smoothScrollToTop();
    }


}
