package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.domain.TiMu;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/5.
 * 作答报告  里面的GridAdapter
 */
public class AnswerGridViewAdapter extends BaseAdapter {


   private List<TiMu> tmList;
   private Context context;
    public AnswerGridViewAdapter(List<TiMu> tm, Context context) {
        this.tmList = tm;
        this.context = context;
    }

    public void onFresh(List<TiMu> tmList){
        this.tmList=tmList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tmList==null?0:tmList.size();
    }

    @Override
    public Object getItem(int position) {
        return tmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TiMu tm=tmList.get(position);
        TextView tv=new TextView(context);
        int a=tm.getQid();
        tv.setPadding(10,10,10,10);
        tv.setGravity(Gravity.CENTER);
        tv.setText(a+"");
        int flag=tm.getAnswerFlag();
        switch (flag){
            case 0://未做
                tv.setBackgroundResource(R.drawable.bg_large_gray);
                break;
            case 1://正确
                tv.setBackgroundResource(R.drawable.bg_large_green);
                break;
            case 2://错误
                tv.setBackgroundResource(R.drawable.bg_large_orange);
                break;
        }
        return tv;
    }

    //比对答案是否正确
    private int compareTiMu(TiMu tm){

        String studentAnswer=tm.getStudentAnswer();
        if(studentAnswer==null || studentAnswer.equals("")){//未做
            return 0;
        }
        int a=-1;
        if(tm.getQuestionType()==1 || tm.getQuestionType()==3){//单选 判断
            List<String> answerList=tm.getQuestionAnswerList();
            if(studentAnswer.equals(answerList.get(0))){
                a=1;//正确
            }else{
                a= 2;//错误
            }
        }else if(tm.getQuestionType()==2){  //多选,多选答案为json 串，故此分开判断
            if(studentAnswer.equals(tm.getQuestionAnswer())){
                a= 1;//正确
            }else{
                a= 2;//错误
            }
        }
        return a;
    }

}
