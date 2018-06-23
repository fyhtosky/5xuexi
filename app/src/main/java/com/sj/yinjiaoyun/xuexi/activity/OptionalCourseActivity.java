package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.OptionalCourseAdapter;
import com.sj.yinjiaoyun.xuexi.bean.OptionalCourseBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选修课程的界面
 */
public class OptionalCourseActivity extends AppCompatActivity {

    @BindView(R.id.listview)
    ListView listview;
    //标识的用户id
    private String endUserId = "";
    private int year;
    //招生计划的id
    private String enrollPlanId;
    //选修课的数据源
    private List<OptionalCourseBean.DataBean.NeedSelectCoursesBean> list = new ArrayList<>();
    private OptionalCourseAdapter optionalCourseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optional_course);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        endUserId = PreferencesUtils.getSharePreStr(this, "username");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            enrollPlanId = bundle.getString("enrollPlanId");
            year=bundle.getInt("year");
            requstCourse();
        }
        optionalCourseAdapter=new OptionalCourseAdapter(this,list);
        listview.setAdapter(optionalCourseAdapter);
    }

    /**
     * 获取学时下的选修课的列表
     */
    private void requstCourse() {
        String params = "?userId=" + endUserId + "&enrollPlanId=" + enrollPlanId + "&theFewYear=" + String.valueOf(year);
        HttpClient.get(this, Api.FIND_COURSE_OPTIONAL + params, new CallBack<OptionalCourseBean>() {
            @Override
            public void onSuccess(OptionalCourseBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    list.clear();
                    list.addAll(result.getData().getNeedSelectCourses());
                    optionalCourseAdapter.notifyDataSetChanged();

                }
            }

        });
    }
    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
               StringBuilder sb=new StringBuilder();
                if(optionalCourseAdapter.getCheckedData().size()==0){
                    ToastUtil.showShortToast(OptionalCourseActivity.this,"请选择选修课程");
                    return;
                }else {
                    for (int i=0;i<optionalCourseAdapter.getCheckedData().size();i++){
                        if(i==list.size()-1){
                            //当循环到最后一个的时候 就不添加逗号
                            sb.append(optionalCourseAdapter.getCheckedData().get(i).getId());
                        }else {
                            sb.append(optionalCourseAdapter.getCheckedData().get(i).getId());
                            sb.append(",");
                        }
                    }
                    saveCourse(sb.toString());
                }
                break;
        }
    }

    private void saveCourse(String idlist) {
        HashMap<String,String>map=new HashMap<>();
        map.put("endUserId",endUserId);
        map.put("enrollmentId",enrollPlanId);
        map.put("teachingPlanIds",idlist);
        map.put("theFewYear",String.valueOf(year));
        HttpClient.post(this, Api.ADD_COURSE_SCHEDULES, map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
                    finish();
                }else {
                    ToastUtil.showShortToast(OptionalCourseActivity.this,result.getMessage());
                }
            }
        });

    }

    /**
     * 启动Activity
     */
    public static void StartActivity(Context context,int year, String enrollPlanId) {
        Intent intent = new Intent(context, OptionalCourseActivity.class);
        intent.putExtra("year",year);
        intent.putExtra("enrollPlanId", enrollPlanId);
        context.startActivity(intent);

    }
}
