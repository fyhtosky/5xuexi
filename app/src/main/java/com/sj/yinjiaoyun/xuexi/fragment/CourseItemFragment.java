package com.sj.yinjiaoyun.xuexi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.OptionalCourseActivity;
import com.sj.yinjiaoyun.xuexi.adapter.CourseItemAdapter;
import com.sj.yinjiaoyun.xuexi.bean.CoureseBean;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/20.
 */
public class CourseItemFragment extends Fragment {
    private static final String YEAR = "year";
    private static final String ENROLL_PLAY_ID = "enrollPlanId";
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.defaultContainer)
    LinearLayout defaultContainer;
    @BindView(R.id.ll_choose_course)
    LinearLayout llChooseCourse;
    //标识的用户id
    private String endUserId = "";
    private int year;
    //招生计划的id
    private String enrollPlanId;
    //课程的数据源
    private List<CoureseBean.DataBean.CourseSchedulesBean> list = new ArrayList<>();

    //适配器
    private CourseItemAdapter courseItemAdapter;
    private int status;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_item_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        endUserId = PreferencesUtils.getSharePreStr(getContext(), "username");
        courseItemAdapter = new CourseItemAdapter(getContext(), list);
        listView.setAdapter(courseItemAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            year = bundle.getInt(YEAR, 1);
            enrollPlanId = bundle.getString(ENROLL_PLAY_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requstCourse();
    }

    /**
     * 获取学时下的课程
     */
    private void requstCourse() {
        String params = "?userId=" + endUserId + "&enrollPlanId=" + enrollPlanId + "&theFewYear=" + String.valueOf(year);
        HttpClient.get(this, Api.FIND_COURSE_LIST + params, new CallBack<CoureseBean>() {
            @Override
            public void onSuccess(CoureseBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    status=result.getData().getStatus();
                    list.clear();
                    list.addAll(result.getData().getCourseSchedules());
                    courseItemAdapter.notifyDataSetChanged();
                    //是否有课程做相应的界面展示
                    if(list.size()>0){
                        defaultContainer.setVisibility(View.GONE);
                    }else {
                        defaultContainer.setVisibility(View.VISIBLE);
                    }
                    //显示是否需要选课
                    if (result.getData().getStatus() == 2 || result.getData().getStatus()==5) {
                        llChooseCourse.setVisibility(View.VISIBLE);
                    } else {
                        llChooseCourse.setVisibility(View.GONE);
                    }
                }
            }

        });
    }

    public static CourseItemFragment newInstance(int year, String enrollPlanId) {
        CourseItemFragment fragment = new CourseItemFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, year);
        args.putString(ENROLL_PLAY_ID, enrollPlanId);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.bt_choose_course)
    public void onClick() {
        if(status==2||status==5){
           OptionalCourseActivity.StartActivity(getContext(),year,enrollPlanId);
        }
    }
}
