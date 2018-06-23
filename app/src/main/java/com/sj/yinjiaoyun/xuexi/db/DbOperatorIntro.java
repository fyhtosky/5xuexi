package com.sj.yinjiaoyun.xuexi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.Score;


/**
 * Created by ${沈军 961784535@qq.com} on 2016/9/14.
 * 视频详情页 我的分数 数据缓存
 */
public class DbOperatorIntro {

    String TAG="dbintro";
    MySQLHelper helper;
    SQLiteDatabase db;
    Context context;
    //构造函数建表
    public DbOperatorIntro(Context context){
        helper=new MySQLHelper(context);
        db=helper.getReadableDatabase();
        this.context=context;
    }

    public void insertScore(Score score){
        Log.i(TAG, "存数据进数据库: "+":" +score.toString());
        if(context==null){
            return ;
        }
        if(queryIsExists(score)){
            deleteScore(score);//删除所有的用户信息
        }
        ContentValues values=new ContentValues();
        values.put("learnBehaviorScore",score.getLearnBehaviorScore());
        values.put("usualScore",score.getUsualScore());
        values.put("finalScore",score.getFinalScore());
        values.put("totalScore",score.getTotalScore());
        db.insert(MyConfig.TB_INTRO,null,values);
        Log.i(TAG, "insertUserInfo: "+"插入数据成功："+score.toString());
    }



    /**
     * 删除此分数
     * @param score
     */
    private void deleteScore(Score score) {
        if(score==null){
            return;
        }
        db.delete(MyConfig.TB_INTRO,"learnBehaviorScore=? and usualScore=? and finalScore=?and totalScore=?",
                new String[]{score.getLearnBehaviorScore()+"",score.getUsualScore()+"",score.getFinalScore()+"",
                        score.getTotalScore()+""});
        Log.i(TAG, "deleteScore: "+"删除："+score.toString()+"的信息");
    }

    /**
     *  判断分数是否存在
     * @param score
     * @return
     */
    private boolean queryIsExists(Score score) {
        if(score==null){
            return false;
        }
        Cursor cursor=db.query(MyConfig.TB_INTRO,null,"learnBehaviorScore=? and usualScore=? and finalScore=?and totalScore=?",
                new String[]{score.getLearnBehaviorScore()+"",score.getUsualScore()+"",score.getFinalScore()+"",
                        score.getTotalScore()+""},null,null,null);//查看此对象对应的游标
        int len=cursor.getCount();
        cursor.close();//关闭游标
        Log.i(TAG, "queryIsExists: 判断对象是否是在表中存在 "+(len>0));
        return len>0;
    }

    //获取数据库的信息
    public Score getIntroScore(){
        Score score = null;
        Cursor cursor=db.query(MyConfig.TB_INTRO,null,null,null,null,null,null);
        int len=cursor.getCount();
        Log.i(TAG, "getIntroScore: "+"获取数据条数"+len);
        if(len>0){
            while (cursor.moveToNext()){
                int cursorIndex=cursor.getColumnIndex("learnBehaviorScore");//获取name对应的列数
                int learnBehaviorScore=cursor.getInt(cursorIndex);//根据列数获取对象值加入list
                int usualScore=cursor.getInt(cursor.getColumnIndex("usualScore"));
                int finalScore=cursor.getInt(cursor.getColumnIndex("finalScore"));
                int totalScore=cursor.getInt(cursor.getColumnIndex("totalScore"));
                score=new Score(learnBehaviorScore,usualScore,finalScore,totalScore);
            }

            cursor.close();
        }
        Log.i(TAG, "getIntroScore: "+"返回出去书否为空： "+(score==null));
        return score;
    }


    public void deleteTable(){
        Log.i(TAG, "deleteTable: "+"删除表中数据");
        db.delete(MyConfig.TB_INTRO,null,null);
    }
}
