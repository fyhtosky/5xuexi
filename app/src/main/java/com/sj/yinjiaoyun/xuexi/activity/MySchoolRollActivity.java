package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.SchoolMenuAdapter;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParseStudentNumbersDate;
import com.sj.yinjiaoyun.xuexi.domain.ParserStudentNumbers;
import com.sj.yinjiaoyun.xuexi.domain.StudentNumbers;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.view.InfoView;
import com.sj.yinjiaoyun.xuexi.view.MeView;
import com.sj.yinjiaoyun.xuexi.view.ScheduleTitleView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/6.
 * 我的 学籍信息 学籍列表
 */
public class MySchoolRollActivity extends MyBaseActivity implements HttpDemo.HttpCallBack {

    String TAG="school";

    ScheduleTitleView scheduleTitleView;
    View container;
    LayoutInflater inflater;
    ListView lv;//pop里面的listView
    PopupWindow pop;
    SchoolMenuAdapter adapter;
    View menuView;

    List<Pairs> pairsList;
    String endUserId;
    List<StudentNumbers> studentNumbersList;//接口返回的学籍信息集合
    StudentNumbers studentNumbers;

    HttpDemo demo;

    ViewHolder holder;
    String beizhu;//备注信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_schoolroll);
        init();
        initView();
        getHttpDate(endUserId);
    }

    private void init() {
        inflater=LayoutInflater.from(this);
        endUserId=  PreferencesUtils.getSharePreStr(MySchoolRollActivity.this, "username");
    }

    private void initView() {
        holder=new ViewHolder();
        scheduleTitleView= (ScheduleTitleView) findViewById(R.id.schoolroll_scheduleTitleView);
        container=findViewById(R.id.schoolroll_fragment);
        holder.tvhint= (TextView) findViewById(R.id.schoolroll_hint);
        holder.userHead= (ImageView) findViewById(R.id.scholl_derail_userHead);
        holder.applicationNumber= (InfoView) findViewById(R.id.scholl_derail__applicationNumber);
        holder.studentCode= (InfoView) findViewById(R.id.scholl_derail__studentCode);
        holder.collegeCode= (InfoView) findViewById(R.id.scholl_derail__collegeCode);
        holder.collegeName= (InfoView) findViewById(R.id.scholl_derail__collegeName);
        holder.productCode= (InfoView) findViewById(R.id.scholl_derail__productCode);

        holder.productName= (InfoView) findViewById(R.id.scholl_derail__productName);
        holder.productType= (InfoView) findViewById(R.id.scholl_derail__productType);
        holder.productGradation= (InfoView) findViewById(R.id.scholl_derail__productGradation);
        holder.learningModality= (InfoView) findViewById(R.id.scholl_derail__learningModality);
        holder.productLearningLength= (InfoView) findViewById(R.id.scholl_derail__productLearningLength);

        holder.theYear= (InfoView) findViewById(R.id.scholl_derail__theYear);
        holder.theSeason= (InfoView) findViewById(R.id.scholl_derail__theSeason);
        holder.enrollDate= (InfoView) findViewById(R.id.scholl_derail__enrollDate);
        holder.teachingCenterName= (InfoView) findViewById(R.id.scholl_derail__teachingCenterName);
        holder.graduateTime= (InfoView) findViewById(R.id.scholl_derail__graduateTime);

        holder.remark= (MeView) findViewById(R.id.scholl_derail__remark);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b=data.getExtras();  //data为B中回传的Intent
                String mark=b.getString("mark");
                Log.i(TAG, "onActivityResult: "+mark);
                holder.remark.setValuesForMark(mark);
                break;
            default:
                break;
        }
    }

    //设置下拉菜单
    private void setPopMenu(){
        menuView=inflater.inflate(R.layout.fragment_pop_menu,null);
        lv= (ListView) menuView.findViewById(R.id.menu_pop);
        adapter=new SchoolMenuAdapter(this,studentNumbersList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: "+position);
                scheduleTitleView.isChoice().setChecked(false);
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                SchoolMenuAdapter.ViewHolder viewHolder=(SchoolMenuAdapter.ViewHolder)view.getTag();
                viewHolder.cb.toggle();// 把CheckBox的选中状态改为当前状态的反,gridview确保是单一选中
                studentNumbers=studentNumbersList.get(position);
                scheduleTitleView.setTextTitle(studentNumbers.getCollegeName()+"-"+studentNumbers.getProductName());
                Log.i(TAG, "onItemClick: "+studentNumbers.getProductName());
                setSchoolRoll(studentNumbers);
            }
        });
        pop = new PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//第一个参数表示要加载的目标view，后面表示大小
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(getResources().getDrawable(R.color.colorGrayish));
        scheduleTitleView.isChoice().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(pop!=null) {
                    if (isChecked) {
                        if(pop.isShowing()){
                            Log.i(TAG, "onCheckedChanged: "+"选中"+"正在显示。设置消失");
                            pop.dismiss();
                        }else{
                            Log.i(TAG, "onCheckedChanged: "+"选中"+"并显示");
                            container.setAlpha(0.7f);//pop显示时给列表项设置变灰
                            pop.showAsDropDown(scheduleTitleView, 0, 0);//这里的view表示在其下显示，后面两个参数表示偏移
                        }
                        scheduleTitleView.setTextColor(getResources().getColor(R.color.colorGreen));//设置主题颜色值
                    } else {
                        Log.i(TAG, "onCheckedChanged: "+"未选中");
                        container.setAlpha(1.0f);//pop消失时给列表项设置复原
                        pop.dismiss();
                        scheduleTitleView.setTextColor(getResources().getColor(R.color.colorHomeitem));//设置主题颜色值
                    }
                }
            }
        });
        scheduleTitleView.clickView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleTitleView.isChoice().toggle();
            }
        });
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                scheduleTitleView.isChoice().setChecked(false);
            }
        });
    }

    //给地下fragmeng设置值
    private void setSchoolRoll(StudentNumbers studentNumbers) {
        if(studentNumbers.getUserHead()!=null){
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(R.color.colorWrite)
                    .borderWidthDp(0.1f)
                    .cornerRadiusDp(2)
                    .oval(false)
                    .build();
            Picasso.with(this)
                    .load(studentNumbers.getUserHead())
                    .resize(70,70)
                    .error(R.mipmap.default_userhead).centerCrop()
                    .transform(transformation)
                    .into(holder.userHead);
        }
        holder.applicationNumber.setValues(studentNumbers.getApplicationNumber());
        holder.studentCode.setValues(studentNumbers.getStudentCode());
        holder.collegeCode.setValues(studentNumbers.getCollegeCode());
        holder.collegeName.setValues(studentNumbers.getCollegeName());
        holder.productCode.setValues(studentNumbers.getProductCode());

        holder.productName.setValues(studentNumbers.getProductName());
        String productType= (String) MyConfig.productType().get(Byte.toString(studentNumbers.getProductType()));
        holder.productType.setValues(productType);
        String c= (String) MyConfig.productGradation().get(Byte.toString(studentNumbers.getProductGradation()));
        holder.productGradation.setValues(c);//层次
        holder.learningModality.setValues((String) MyConfig.learningModality().get(studentNumbers.getLearningModality()));//学习形式
        if(studentNumbers.getLearningLength()!=null){//学制
            Log.i(TAG, "setSchoolRoll: 学制"+studentNumbers.getLearningLength());
            holder.productLearningLength.setValues(studentNumbers.getLearningLength());
        }
        holder.theYear.setValues(studentNumbers.getTheYear()+"级");//年级
        String b=(String) MyConfig.theSeason().get(String.valueOf(studentNumbers.getTheSeason()));//入学季
        Log.i(TAG, "setSchoolRoll: 入学季:"+b);
        holder.theSeason.setValues(b);//入学季

        holder.enrollDate.setValues(studentNumbers.getEnrollDate());//入学日期
        holder.teachingCenterName.setValues(studentNumbers.getTeachingCenterName());
        holder.graduateTime.setValues(studentNumbers.getGraduateTime());
        beizhu=studentNumbers.getRemark();
        Log.i(TAG, "setSchoolRoll: "+beizhu);
        if(beizhu!=null && beizhu.length()>5){
            holder.remark.setValuesForMark(beizhu.substring(0,5));
        }else{
            holder.remark.setValuesForMark(beizhu);
        }
    }



    //获取学籍列表信息网络接口
    public void getHttpDate(String endUserId){
        if(endUserId==null){
            Log.i(TAG, "getHttpDate: "+"endUserId为空");
            return;
        }
        demo=new HttpDemo(this);
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("studentNumber/findStudentNumberByUserId");
        pairsList.add(new Pairs("endUserId",endUserId));
        demo.doHttpGetLoading(this,url,pairsList,0);
    }


    public void onclick(View view){
        switch(view.getId()) {
            case R.id.schoolbar_back://返回
                finish();
                break;
            case R.id.scholl_derail__remark://备注
                Log.i(TAG, "onclick: "+(beizhu==null));
                if(beizhu!=null){
                    Intent intent=new Intent(MySchoolRollActivity.this,SchoolMarkActivity.class);
                    intent.putExtra("beizhu",beizhu);
                    startActivityForResult(intent,0);
                }
                break;
        }
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        try {
            parseDate(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDate(String msg) {
        Log.i(TAG, "parseDate: "+msg);
        Gson gson=new Gson();
        ParserStudentNumbers parserStudentNumbers=gson.fromJson(msg, ParserStudentNumbers.class);
        ParseStudentNumbersDate data=parserStudentNumbers.getData();
        studentNumbersList=data.getStudentNumbers();
        if(studentNumbersList==null){
            scheduleTitleView.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
            holder.tvhint.setVisibility(View.VISIBLE);
            return;
        }
        if(studentNumbersList.size()==1){
            container.setVisibility(View.VISIBLE);
            scheduleTitleView.setVisibility(View.GONE);
            holder.tvhint.setVisibility(View.GONE);
            StudentNumbers s=studentNumbersList.get(0);
            setSchoolRoll(s);
        }else {
            container.setVisibility(View.VISIBLE);
            scheduleTitleView.setVisibility(View.VISIBLE);
            holder.tvhint.setVisibility(View.GONE);
            StudentNumbers s=studentNumbersList.get(0);
            scheduleTitleView.setTextTitle(s.getCollegeName()+"-"+
                    s.getProductName());
            setSchoolRoll(s);
            setPopMenu();
        }
    }


    class ViewHolder{
        TextView tvhint;//无学籍信息时候的提示
        ImageView userHead;//录取图片
        InfoView applicationNumber;//成人高考报名号
        InfoView studentCode;//学号
        InfoView collegeCode;//院校代码
        InfoView collegeName;//院校名称
        InfoView productCode;//专业代码
        InfoView productName;//专业名称
        InfoView productType;//教育类别
        InfoView productGradation;//层次
        InfoView learningModality;//学习形式
        InfoView productLearningLength;//学制
        InfoView theYear;//年级
        InfoView theSeason;//入学季
        InfoView enrollDate;//入学日期
        InfoView teachingCenterName;//教学站点
        InfoView graduateTime;//预计毕业时间
        MeView remark;//备注
    }

}
