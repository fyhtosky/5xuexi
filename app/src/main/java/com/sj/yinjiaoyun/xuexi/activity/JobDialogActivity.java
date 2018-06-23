package com.sj.yinjiaoyun.xuexi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.db.DbOperatorLogin;
import com.sj.yinjiaoyun.xuexi.domain.Coursewares;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.widget.SaveCheckBox;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/20.
 * 视频详情   作业  考试   答疑  弹出对话框
 *
 */
public class JobDialogActivity extends Activity {

    String TAG="dialog";
    SaveCheckBox sCb1;
    SaveCheckBox sCb2;
    SaveCheckBox sCb3;
    Coursewares coursewares;
    String courseScheduleId;//课程id 554
    String examPaperReleaseId;//作业id
    String testExamPaperReleaseId;//测试id
    int groupPosition;    //章节
    int childPosition;    //课时

    String endUserId;
    Byte examState;       //	是	完成测验状态  0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期
    Byte homeworkState;   //	是	完成作业状态  0：没有作业  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_course);
        setDialog();
        initView();
    }

    private void initView(){
        endUserId = PreferencesUtils.getSharePreStr(this,"username");
        Intent intent=getIntent();
        coursewares =intent.getParcelableExtra("Coursewares");
        courseScheduleId=intent.getStringExtra("CourseScheduleId");
        groupPosition=intent.getIntExtra("groupPosition",0);
        childPosition=intent.getIntExtra("childPosition",0);
        examPaperReleaseId=intent.getStringExtra("ExamPaperReleaseId");
        testExamPaperReleaseId=intent.getStringExtra("TestExamPaperReleaseId");
        examState=coursewares.getExamState();
        homeworkState=coursewares.getHomeworkState();
        Log.i(TAG, "initView: 状态是咧"+"0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期");
        Log.i(TAG, "initView: 测验完成状态"+examState);
        Log.i(TAG, "initView:作业完成状态 "+homeworkState);
        Log.i(TAG, "initView: 课程id "+courseScheduleId);
        Log.i(TAG, "onCreate: "+testExamPaperReleaseId+":"+coursewares.toString());
        sCb1 = (SaveCheckBox) findViewById(R.id.dialog_course_rb1);
        sCb2 = (SaveCheckBox) findViewById(R.id.dialog_course_rb2);
        sCb3 = (SaveCheckBox) findViewById(R.id.dialog_course_rb3);
        sCb1.setChecked(true);
        if(homeworkState==1){
            sCb2.setChecked(true);
        }else{
            sCb2.setChecked(false);
        }
        if(examState==1){
            sCb3.setChecked(true);
        }else{
            sCb3.setChecked(false);
        }
        sCb1.setOnClickListener(listener);
        sCb2.setOnClickListener(listener);
        sCb3.setOnClickListener(listener);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.dialog_course_rb1://答疑
                    Log.i(TAG, "onClick: "+"答疑");
                    Intent intent1=new Intent(JobDialogActivity.this,DaYiActivity.class);
                    intent1.putExtra("coursewareId",String.valueOf(coursewares.getId()));
                    intent1.putExtra("CourseScheduleId",courseScheduleId);
                    Log.i(TAG, "onClick: "+String.valueOf(courseScheduleId));
                    intent1.putExtra("groupPosition",groupPosition);
                    intent1.putExtra("childPosition",childPosition);
                    startActivity(intent1);
                    break;
                case R.id.dialog_course_rb2://作业
                    Log.i(TAG, "onClick: "+"作业");
                        judgeState(homeworkState,2);
                    break;
                case R.id.dialog_course_rb3://测试
                    Log.i(TAG, "onClick: "+"测试");
                        judgeState(examState,3);
                    break;
            }
        }
    };


    StringBuilder sb;
    /**
     * 根据状态判断是否进入题目界面
     * @param state  作业状态码 0：没有测验  1：未做   4：尚未开始考试  5：已过期";  [ 2：已做未批改  3、已做已批改]进入到成绩页面
     * @param examType   2作业 3测试 类型
     */
    private void judgeState(Byte state,int examType){
        // 完成状态 0：没有测验  1：未做   4：尚未开始考试  5：已过期";  [ 2：已做未批改  3、已做已批改]进入到成绩页面
        switch(state){//状态位判断
            case 0:     // 0：没有测验
                break;
            case 1://未做   //////////////////////////////////////////
                Log.i(TAG, "judgeState: "+"未做");//进入方框引导
                Intent intent1=new Intent(JobDialogActivity.this,JobHintActivity.class);
                if(examType==2){//作业
                    intent1.putExtra("ExamPaperReleaseId",examPaperReleaseId);
                }else{
                    intent1.putExtra("ExamPaperReleaseId",testExamPaperReleaseId);
                }
                intent1.putExtra("CourseScheduleId",courseScheduleId);
                intent1.putExtra("groupPosition",groupPosition);
                intent1.putExtra("childPosition",childPosition);
                intent1.putExtra("examType",examType);
                Log.i(TAG, "传递数据dialog judgeState: "+courseScheduleId+" testExamPaperReleaseId"+testExamPaperReleaseId+" examPaperReleaseId"+examPaperReleaseId);
                startActivity(intent1);
                break;
            case 4://尚未开始考试    //进入方框引导
                Log.i(TAG, "judgeState: "+"尚未开始考试");
                Intent intent4=new Intent(JobDialogActivity.this,JobHintActivity.class);
                if(examType==2){//作业
                    intent4.putExtra("ExamPaperReleaseId",examPaperReleaseId);
                }else{
                    intent4.putExtra("ExamPaperReleaseId",testExamPaperReleaseId);
                }
                intent4.putExtra("CourseScheduleId",courseScheduleId);
                intent4.putExtra("groupPosition",groupPosition);
                intent4.putExtra("childPosition",childPosition);
                Log.i(TAG, "传递数据dialog judgeState: "+courseScheduleId+" testExamPaperReleaseId"+testExamPaperReleaseId+" examPaperReleaseId"+examPaperReleaseId);
                startActivity(intent4);
                break;
            case 5:// 5：已过期
                Log.i(TAG, "judgeState: "+5+"未做");//进入方框引导
                Intent intent5=new Intent(JobDialogActivity.this,JobHintActivity.class);
                if(examType==2){//作业
                    intent5.putExtra("ExamPaperReleaseId",examPaperReleaseId);
                }else{
                    intent5.putExtra("ExamPaperReleaseId",testExamPaperReleaseId);
                }
                intent5.putExtra("CourseScheduleId",courseScheduleId);
                intent5.putExtra("groupPosition",groupPosition);
                intent5.putExtra("childPosition",childPosition);
                Log.i(TAG, "传递数据dialog judgeState: "+courseScheduleId+" testExamPaperReleaseId"+testExamPaperReleaseId+" examPaperReleaseId"+examPaperReleaseId);
                startActivity(intent5);
                break;

            case 2://已做未批改
                Log.i(TAG, "judgeState: "+2);
                Intent intent2=new Intent(this,AnswerReportActivity.class);
                intent2.putExtra("CourseScheduleId",courseScheduleId);
                if(examType==2){//作业
                    intent2.putExtra("ExamPaperReleaseId",examPaperReleaseId);
                }else{
                    intent2.putExtra("ExamPaperReleaseId",testExamPaperReleaseId);
                }
                intent2.putExtra("EndUserId",endUserId);
                Log.i(TAG, "传递数据dialog judgeState: "+courseScheduleId+" testExamPaperReleaseId"+testExamPaperReleaseId+" examPaperReleaseId"+examPaperReleaseId);
                startActivity(intent2);
                break;
            case 3://3、已做已批改
                Log.i(TAG, "judgeState: "+3);
                Intent intent3=new Intent(this,AnswerReportActivity.class);
                intent3.putExtra("CourseScheduleId",courseScheduleId);
                if(examType==2){//作业
                    intent3.putExtra("ExamPaperReleaseId",examPaperReleaseId);
                }else{
                    intent3.putExtra("ExamPaperReleaseId",testExamPaperReleaseId);
                }
                intent3.putExtra("EndUserId",endUserId);
                Log.i(TAG, "传递数据dialog judgeState: "+courseScheduleId+" testExamPaperReleaseId"+testExamPaperReleaseId+" examPaperReleaseId"+examPaperReleaseId);
                startActivity(intent3);
                break;
        }

    }


    private void setDialog(){
        //////设置为true点击区域外消失
        setFinishOnTouchOutside(true);

        // 设置dialog位置
        Window dialog = getWindow();
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWindowAnimations(R.style.MyDialogStyle);
        // 以下6行代码设置dialog 的宽高
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.12); // 高度设置为屏幕的0.5
        p.width = (int) (d.getWidth() * 0.98); // 宽度设置为屏幕的0.98
        p.y =10;//设置dialog 上下偏移量，这里设为100，开发完成要测试不同分辨率手机，再调整
        getWindow().setAttributes(p);
    }


}
