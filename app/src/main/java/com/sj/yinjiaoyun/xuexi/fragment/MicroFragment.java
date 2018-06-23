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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.MicroActivity;
import com.sj.yinjiaoyun.xuexi.adapter.MicroAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.MicroMenuAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domains.CourseScheduleVO;
import com.sj.yinjiaoyun.xuexi.domains.MicroCouse;
import com.sj.yinjiaoyun.xuexi.domains.TrainingItemVO;
import com.sj.yinjiaoyun.xuexi.domains.ParseMicroCourse;
import com.sj.yinjiaoyun.xuexi.domains.ParseMicroCourseData;
import com.sj.yinjiaoyun.xuexi.domains.ParseMicroTraning;
import com.sj.yinjiaoyun.xuexi.domains.ParseTraningData;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.domains.TrainingVO;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.view.ScheduleTitleView;
import com.sj.yinjiaoyun.xuexi.widget.NewListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/2/9.
 * 主Activity - 课程表tab下 Fragment里面 -  微专业tab下的fragment
 */
public class MicroFragment extends Fragment implements HttpDemo.HttpCallBack {


    String TAG = "microfragment";
    ScheduleTitleView titleBarView;//标题切换
    TextView tvCollege;//院校
    TextView tvTutionWay;//开课方式
    TextView tvRegisterTime;//报名时间
    ProgressBar progressBar;//进度条
    TextView progressMark;//进度显示
    NewListView listView;//

    String endUserId;
    List<Pairs> pairsList;
    HttpDemo demo;
    MicroMenuAdapter microMenuAdapter;
    MicroAdapter microAdapter;
    List<CourseScheduleVO> voList;

    View popView;
    Activity activity;

    View defaultContainer;//缺省值集合
    View courseContainer;//课程集合

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_micro, container, false);
        popView = inflater.inflate(R.layout.fragment_pop_menu, null);
        initView(view);
        initPopMenu(popView);
        return view;
    }



    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        endUserId = PreferencesUtils.getSharePreStr(MyApplication.getContext(),"username");
        demo = new HttpDemo(this);
        titleBarView = (ScheduleTitleView) view.findViewById(R.id.micro_scheduleTitleView);
        defaultContainer = view.findViewById(R.id.micro_defaultContainer);
        courseContainer = view.findViewById(R.id.micro_courseContainer);
        tvCollege = (TextView) view.findViewById(R.id.micro_college);
        tvTutionWay = (TextView) view.findViewById(R.id.micro_tutionWay);
        tvRegisterTime = (TextView) view.findViewById(R.id.micro_registerTime);
        progressBar = (ProgressBar) view.findViewById(R.id.micro_progressbar);
        progressMark = (TextView) view.findViewById(R.id.micro_progressMark);
        listView = (NewListView) view.findViewById(R.id.micro_listView);
        microAdapter = new MicroAdapter(voList, activity);
        listView.setAdapter(microAdapter);
        listView.setOnItemClickListener(itemclick);
    }



    ListView popListView;
    List<TrainingVO> traningList;
    TrainingVO trainingVO;//pop选中的项
    PopupWindow pop;

    //设置pop下拉菜单
    private void initPopMenu(View view) {
        popListView = (ListView) view.findViewById(R.id.menu_pop);
        microMenuAdapter = new MicroMenuAdapter(getActivity(), traningList);
        popListView.setAdapter(microMenuAdapter);

        //第一个参数表示要加载的目标view，后面表示大小,（screenHeight2/2表示listView条数多时，设置其最大高度显示为屏幕高度的一半）
      //  pop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, screenHeight2/2);
        pop=new PopupWindow(popView);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);//设置pop宽度填满屏幕
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(getResources().getDrawable(R.color.colorGrayish));
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                titleBarView.isChoice().setChecked(false);
            }
        });
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: " + position);
                try {
                    titleBarView.isChoice().setChecked(false);
                    // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                    MicroMenuAdapter.ViewHolder viewHolder = (MicroMenuAdapter.ViewHolder) view.getTag();
                    viewHolder.cb.toggle();// 把CheckBox的选中状态改为当前状态的反,gridview确保是单一选中
                    trainingVO = traningList.get(position);
                    titleBarView.setTextTitle(trainingVO.getTrainingName() + "");
                    getHttpTrainingCourse(trainingVO);
                } catch (Exception e) {
                    Log.e(TAG, "onItemClick: " + e.toString());
                }
            }
        });
        titleBarView.isChoice().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pop != null) {
                    if (isChecked) {
                        pop.setFocusable(true);
                        if (pop.isShowing()) {
                            //Log.i(TAG, "onCheckedChanged: "+"选中"+"正在显示。设置消失");
                            pop.dismiss();
                        } else {
                            //Log.i(TAG, "onCheckedChanged: "+"选中"+"并显示");
                            pop.showAsDropDown(titleBarView, 0, 0);//这里的view表示在其下显示，后面两个参数表示偏移
                        }
                        titleBarView.setTextColor(getActivity().getResources().getColor(R.color.colorGreen));//设置主题颜色值
                    } else {
                        // Log.i(TAG, "onCheckedChanged: "+"未选中");
                        pop.dismiss();
                        titleBarView.setTextColor(getActivity().getResources().getColor(R.color.colorHomeitem));//设置主题颜色值
                    }
                }
            }
        });
        titleBarView.clickView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleBarView.isChoice().toggle();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getHttpMyTraining();
    }

    /**
     * 获取顶部微专业切花列表
     */
    private void getHttpMyTraining() {
        if(TextUtils.isEmpty(endUserId)){
            return;
        }
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("training/findMyTraining");//36.2根据用户名获取我的微专业
        pairsList.add(new Pairs("userId", endUserId));
        demo.doHttpGet(url, pairsList, 0);
    }

    /**
     * 获取顶部微专业对应的课程列表信息          38. 根据用户ID 微专业id 分期id 查询微专业课程表
     *
     * @param trainingVO
     */
    private void getHttpTrainingCourse(TrainingVO trainingVO) {
        String trainingId = String.valueOf(trainingVO.getId());
        String trainingItemId = String.valueOf(trainingVO.getTrainingItemId());
        if (TextUtils.isEmpty(trainingId) && TextUtils.isEmpty(trainingItemId)) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
            return;
        }
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("training/findMyTrainingCourse");
        pairsList.add(new Pairs("endUserId", endUserId));
        pairsList.add(new Pairs("trainingId", trainingId));
        pairsList.add(new Pairs("trainingItemId", trainingItemId));
        demo.doHttpGetLoading(activity,url, pairsList, 1);
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        try {
            if (requestCode == 0) {//切换顶部微专业的列表
                parsefindMyTraining(msg);
            } else if (requestCode == 1) {//获取顶部微专业对应的课程列表信息
                parseTrainingCourse(msg);
            }
        } catch (Exception e) {
            Log.e(TAG, "setMsg: " + e.toString());
        }
    }

    /**
     * 获取顶部微专业对应的课程列表信息
     *
     * @param msg
     */
    private void parseTrainingCourse(String msg) //throws Exception
    {
        Log.i(TAG, "parseTrainingCourse: " + msg);
        Gson gson = new Gson();
        ParseMicroCourse parseMicroCourse = gson.fromJson(msg, ParseMicroCourse.class);
        ParseMicroCourseData data = parseMicroCourse.getData();
        MicroCouse microCouse = data.getTrainingCourse();
        courseId = String.valueOf(microCouse.getId());
        collegeName = microCouse.getCollegeName();
        tvCollege.setText(microCouse.getCollegeName());
        tvTutionWay.setText(MyConfig.tutionWay().get(microCouse.getTutionWay() + "").toString());
        TrainingItemVO trainingItemVO = microCouse.getTrainingItemVO();
        if (microCouse.getTutionWay() == 0) {//随到随学
            tvRegisterTime.setVisibility(View.GONE);
        } else {
            tvRegisterTime.setVisibility(View.VISIBLE);
            tvRegisterTime.setText("开课时间：" + MyUtil.getTime(trainingItemVO.getEnteredStartTime(), "yyyy-MM-dd")
                    + " " + MyUtil.getTime(trainingItemVO.getEnteredEndTime(), "yyyy-MM-dd"));
        }
        progressMark.setText(trainingItemVO.getTrainingPercent());
        progressBar.setProgress(MyUtil.transFormation(trainingItemVO.getTrainingPercent()));
        List<CourseScheduleVO> list = trainingItemVO.getCourseScheduleVOs();
        voList = new ArrayList<>();
        Log.i(TAG, "parseTrainingCourse: "+list.size());
        for (int i = 0; i < list.size(); i++) {
            CourseScheduleVO a = list.get(i);
            if (a != null) {
                voList.add(a);
            }
        }
        Log.i(TAG, "parseTrainingCourse: "+voList.size());
        microAdapter.onfresh(voList);
    }

    /**
     * 解析我的微专业（顶部列表切换）
     *
     * @param msg
     */
    private void parsefindMyTraining(String msg) {
        Log.i(TAG, "parsefindMyTraining: " + msg);
        Gson gson = new Gson();
        ParseMicroTraning parseMicroTraning = gson.fromJson(msg, ParseMicroTraning.class);
        ParseTraningData data = parseMicroTraning.getData();
        traningList = data.getTraining();
        if (traningList != null) {
            courseContainer.setVisibility(View.VISIBLE);
            defaultContainer.setVisibility(View.GONE);
            microMenuAdapter.fresh(traningList);
            trainingVO = traningList.get(0);
            titleBarView.setTextTitle(trainingVO.getTrainingName());
            if(traningList.size()>8){//当popwindow的listView条数过多是，设置其高度为固定值，否则其高度自适应
                pop.setHeight(500);
            }else {
                pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            getHttpTrainingCourse(trainingVO);
        } else {
            defaultContainer.setVisibility(View.VISIBLE);
            courseContainer.setVisibility(View.GONE);
        }
    }


    String collegeName;
    String courseId;//课程id
    //列表点击事件
    AdapterView.OnItemClickListener itemclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CourseScheduleVO vo = voList.get(position);
            Intent intent = new Intent(activity, MicroActivity.class);
            intent.putExtra("CourseScheduleId", String.valueOf(vo.getId()));//课程表ID
            intent.putExtra("collegeName", collegeName);
            intent.putExtra("endUserId", endUserId);
            intent.putExtra("courseId", courseId);
            intent.putExtra("courseName", vo.getCourseName());
            if (TextUtils.isEmpty(vo.getTeacherName())) {
                intent.putExtra("isHaveTeacher", false);
            }
            startActivity(intent);
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            Log.i("onhiddenchanged", "onHiddenChanged:微专业 ");
            if(trainingVO !=null) {
                Log.i("onhiddenchanged", "onHiddenChanged:微专业访问网络 ");
                getHttpMyTraining();
            }
        }
    }


}
