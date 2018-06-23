package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.ScheduleItemActivity;
import com.sj.yinjiaoyun.xuexi.adapter.MajorExpandableAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.MajorListAdapter;
import com.sj.yinjiaoyun.xuexi.domain.CourseVO;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.Parent;
import com.sj.yinjiaoyun.xuexi.domain.ParseCourses;
import com.sj.yinjiaoyun.xuexi.domain.ParseCoursesDate;
import com.sj.yinjiaoyun.xuexi.domain.SoaProductVO;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.widget.NewExpandableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/10.
 * 课程表  专业fragment  里面的躯体所选的课程列表显示
 * (选完方向 课程时候的显示)
 */
public class TabulationFragment extends Fragment implements HttpDemo.HttpCallBack{

    String TAG="tabulation";
    List<Parent> parentList;//填充ExpandableListView的控件
    List<CourseVO> couselist;//填充ListView的控件
    NewExpandableListView expandableListView;//专业类型    0：网校 1：成教 2：自考 5 : 选修 的时候
    ListView listView;//专业类型  3：培训 4：考证
    MajorExpandableAdapter expandAdapter;//   0：网校 1：成教 2：自考 5 : 选修 的时候
    MajorListAdapter listAdapter;//3：培训 4：考证
    SoaProductVO soaProductVO;//某个专业信息封装类

    View ivDefault;//没有选课时出现的值

    HttpDemo demo;
    Activity context;
    CallBackFromTabulation callBack;

    String enrollPlanId;//招生计划id
    String productId;//专业id
    String endUserId;//用户id

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("showAndHidden", "onCreate: "+"3 TabulationFragment");
        this.context=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_tabulation,container,false);
        initView(v);
        getTabForHttp(endUserId,enrollPlanId,productId);
        return v;
    }


    private void initView(View v) {
        ivDefault= v.findViewById(R.id.tab_defaultContainer);
        expandableListView= (NewExpandableListView) v.findViewById(R.id.tab_expandableListView);
        listView= (ListView) v.findViewById(R.id.tab_listView);
    }


    /**
     * 从专业传过来的方向数据
     * @param endUserId    用户id
     * @param enrollPlanId  招生季换id
     * @param productId     专业id
     */
    public void setTabDateFromMajor(CallBackFromTabulation callBack, String endUserId, Long enrollPlanId, Long productId){
        this.callBack=callBack;
        this.endUserId=endUserId;
        this.enrollPlanId=String.valueOf(enrollPlanId);
        this.productId=String.valueOf(productId);
    }

    /**
     * 根据招生专业ID+招生计划ID + 用户ID获取课程信息（教学计划信息）
     * @param endUserId  用户ID
     * @param enrollPlanId  招生计划ID
     * @param productId   招生专业ID
     */
    private void getTabForHttp(String endUserId,String enrollPlanId,String productId){
        if(enrollPlanId==null || productId==null)
            return;
        demo=new HttpDemo(this);
        Log.i(TAG, "getHttpCourse: "+endUserId+":"+enrollPlanId+":"+productId);
        String url= MyConfig.getURl("course/queryCourses");
        List<Pairs> list=new ArrayList<>();
        list.add(new Pairs("endUserId",endUserId+""));
        list.add(new Pairs("enrollPlanId",enrollPlanId+""));
        list.add(new Pairs("productId",productId+""));
        demo.doHttpPostLoading(getActivity(),url,list, MyConfig.CODE_POST_COURSES);
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        try {
            parserList(msg);
        }catch (Exception e){
            Log.e(TAG, "setMsg: "+e.toString());
        }
    }

    /**
     * 解析列表中的数据
     * @param msg
     */
    private void parserList(String msg) throws Exception
    {
        Log.i(TAG, "parserList: "+msg);
        Gson gson=new Gson();
        ParseCourses parseCourses=gson.fromJson(msg, ParseCourses.class);
        if(!(parseCourses.getSuccess())){////////////////////
            Log.i(TAG, "setMsg: "+parseCourses.getMessage());
            ivDefault.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
        }else{//data不为空
            ivDefault.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            ParseCoursesDate data=parseCourses.getData();
            //Log.i(TAG, "parserList: "+(data==null));
            soaProductVO=data.getSoaProductVO();
            //Log.i(TAG, "parserList: "+(soaProductVO==null));
            String majorPercent=soaProductVO.getMajorPercent();//专业进度
            Log.i(TAG, "setHeadMessage:学习进度为 "+majorPercent);
            if(TextUtils.isEmpty(soaProductVO.getMajorPercent())){
                callBack.deliveryProgress("0%");
            }else{
                callBack.deliveryProgress(majorPercent);
            }
            //判断是培训考证还是正常学习
            Map<String,List<CourseVO>> couseMap=soaProductVO.getTeachingPlanMap();
            Map<String,Boolean>	stateMap=soaProductVO.getStateMap();
            Log.i(TAG, "setMsg:专业类型 ："+soaProductVO.getProductType());
            Log.i(TAG, "couseMap: "+couseMap.size());
            if(couseMap.size()==0){////付费了但是没选专业的情况////////////
                ivDefault.setVisibility(View.VISIBLE);
                expandableListView.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
            }else{
                ivDefault.setVisibility(View.GONE);
                switch(soaProductVO.getProductType()){
                    case 3://专业类型 培训
                        listView.setVisibility(View.VISIBLE);
                        expandableListView.setVisibility(View.GONE);
                        setListViewMessage(couseMap);
                        break;
                    case 4://专业类型 考证
                        listView.setVisibility(View.VISIBLE);
                        expandableListView.setVisibility(View.GONE);
                        setListViewMessage(couseMap);
                        break;
                    default://专业类型    0：网校 1：成教 2：自考 5 : 选修
                        listView.setVisibility(View.GONE);
                        expandableListView.setVisibility(View.VISIBLE);
                        Log.i("showAndHidden", "onHiddenChanged: "+"3 TabulationFragment"+"显示");
                        setExpandableMessage(couseMap,stateMap);
                        break;
                }
            }
            callBack.smoothScrollToTop();//设置scrollView 专业头部和列表从上至下置顶
        }
    }

    /**
     *  培训考证时  给下拉列表子控件设置值
     * @param   map 数据解析出来的集合
     */
    private void setListViewMessage(Map<String,List<CourseVO>> map) {
        couselist=map.get("1");
        Log.i(TAG, "setListViewMessage: "+couselist.size());
        if(listAdapter==null){
            listAdapter=new MajorListAdapter(context,couselist);
            listView.setAdapter(listAdapter);
            listView.setDividerHeight(1);
        }else{
            listAdapter.refresh(couselist);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseVO courseVO =couselist.get(position);
                Intent intent=new Intent(getActivity(), ScheduleItemActivity.class);
                intent.putExtra("CourseVO", courseVO);
                intent.putExtra("SoaProductVO",soaProductVO);
                intent.putExtra("endUserId", endUserId);
                startActivity(intent);
                NewMajorFragment.isScrollViewToTop=false;//此种状态设置其滑动不到顶
            }
        });
    }


    /**
     *  非培训考证时    给下拉列表子控件设置值
     * @param   map        数据解析出来的集合
     * @param   stateMap    该学生的缴费情况
     */
    private void setExpandableMessage(Map<String,List<CourseVO>> map, Map<String,Boolean> stateMap) {
        Log.i(TAG, "setExpandableMessage: ");
        Parent parent;
        parentList=new ArrayList<>();
        String parentName;
        for (String key : map.keySet()) {
            // Log.i(TAG, "Parent: "+"key= " + key  +"是否付费:"+stateMap.get(key)+ " and value= "+ map.get(key).size());
            parentName= (String) MyConfig.xueNian().get(key);
            if(stateMap.get(key)) {
                parent = new Parent(parentName, stateMap.get(key), map.get(key));
                parentList.add(parent);
            }
        }
        expandAdapter=new MajorExpandableAdapter(context,parentList);
        expandableListView.setAdapter(expandAdapter);
        expandableListView.setGroupIndicator(null);//设置圆圈为空
        expandableListView.setDividerHeight(1);
        //设置首次加载张开
        for (int i = 0; i < parentList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        //设置ExpandableListView不能收缩
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        // 设置ExpandableListView子项点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CourseVO courseVO =parentList.get(groupPosition).getChild().get(childPosition);
                Intent intent=new Intent(getActivity(), ScheduleItemActivity.class);
                intent.putExtra("CourseVO", courseVO);
                intent.putExtra("SoaProductVO",soaProductVO);
                intent.putExtra("endUserId", endUserId);
                startActivity(intent);
                NewMajorFragment.isScrollViewToTop=false;//此种状态设置其滑动不到顶
                return true;
            }
        });
    }

    public interface CallBackFromTabulation{
        void deliveryProgress(String progress);
        void smoothScrollToTop();
    }


}
