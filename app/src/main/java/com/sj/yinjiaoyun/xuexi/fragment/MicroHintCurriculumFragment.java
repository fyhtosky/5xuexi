package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.CurriculumAdapter;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domains.ParseTrainingCourse;
import com.sj.yinjiaoyun.xuexi.domains.ParseTrainingCourseData;
import com.sj.yinjiaoyun.xuexi.domains.TrainingCourse;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/9.
 * 微专业预览页面 课程体系
 */
public class MicroHintCurriculumFragment extends Fragment implements HttpDemo.HttpCallBack{

    private  final String TAG = "microhintcurriculum";
    ListView listView;
    View  viewHead;
    Long trainingId;
    String trainingItemId;

    Activity activity;

    HttpDemo demo;
    List<Pairs> pairsList;

    CurriculumAdapter adapter;
    List<TrainingCourse> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_micro_hint_curriculum,container,false);
        viewHead=inflater.inflate(R.layout.head_micro_hint_curriculum,null);
        initView(view,viewHead);
        return view;
    }

    /**
     * 从microhintactivity中拿到数据
     * @param trainingId       微专业id
     */
    public void setTrainingIdForActivity(Long trainingId){
         this.trainingId=trainingId;
    }

    /**
     * 从microhintFragment中拿到分期id
     * @param trainingItemId   微专业分期id
     */
    public void setTrainingItemIdForActivity(String trainingItemId){
        Log.i(TAG, "setTrainingItemIdForActivity: "+trainingItemId);
        this.trainingItemId = trainingItemId;
        if(listView!=null){
            getHttpDate(trainingItemId);
        }
    }


    private void initView(View view ,View head) {
        listView= (ListView) view.findViewById(R.id.micro_hint_curriculum_listView);
        listView.addHeaderView(head);
        adapter=new CurriculumAdapter(list,activity);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);


    }


    public void getHttpDate(String trainingItemId){
        Log.i(TAG, "getHttpDate: "+trainingItemId);
        if(TextUtils.isEmpty(trainingItemId)){
            Toast.makeText(activity,"微专业课程体系里面分期id为空",Toast.LENGTH_LONG).show();
            return;
        }
        demo=new HttpDemo(this);
        String url= MyConfig.getURl("training/findTrainingCourse");
        pairsList=new ArrayList<>();
        pairsList.add(new Pairs("trainingItemId",trainingItemId));
        demo.doHttpGet(url,pairsList,0);
    }



    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        try {
            parseDate(msg);
        } catch (Exception e) {
            Log.e(TAG, "setMsg: "+e.toString());
        }
    }

    /**
     * 解析数据
     * @param msg
     */
    private void parseDate(String msg) {
        Gson gson=new Gson();
        ParseTrainingCourse parseTrainingCourse=gson.fromJson(msg, ParseTrainingCourse.class);
        ParseTrainingCourseData data=parseTrainingCourse.getData();
        List<TrainingCourse> listTrainingCourse=data.getTrainingCourse();
        adapter.onRefreshs(listTrainingCourse);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            Log.i(TAG, "onHiddenChanged: ");
        }
    }
}
