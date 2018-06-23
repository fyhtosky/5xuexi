package com.sj.yinjiaoyun.xuexi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sj.yinjiaoyun.xuexi.domain.DbTiMu;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 * 作业考试 学生作答时间缓存
 */
public class DbOperatorJob {

    String TAG="dbJobs";
    MySQLHelper helper;
    SQLiteDatabase db;
    Context context;
    //构造函数建表
    public DbOperatorJob(Context context){
        helper=new MySQLHelper(context);
        db=helper.getReadableDatabase();
        this.context=context;
    }


    /**
     * 增加题目  (如果该id存在，则删除原来的数据，添加现在的新数据)
     * @param tiMu  考试测试题目
     */
    public void insertLogin(DbTiMu tiMu){
     //   Log.i(TAG, "insertLogin: ");
        if(tiMu==null){
            return ;
        }
        if(queryIsExists(tiMu.getTmId())){//存在该id ，则删除
            deleteLoginFromId(tiMu.getTmId());
        }
        ContentValues values=new ContentValues();
        values.put("tmId",tiMu.getTmId());
        values.put("qid",tiMu.getQid());
        values.put("answer",tiMu.getAnswer());
        db.insert(MyConfig.TB_JOBS,null,values);
        Log.i(TAG, "insertLogin: "+"插入数据成功："+tiMu.toString());
    }

    /**
     * 根据 用户id 判断对象是否是在表中存在
     * @param id 要判断的对象
     * @return  true存在 false不存在
     */
    private boolean queryIsExists(String id){
        if(id.equals("")){
            return false;
        }
        Cursor cursor=db.query(MyConfig.TB_JOBS,null,"tmId=?",
                new String[]{id},null,null,null);//查看此对象对应的游标
        int len=cursor.getCount();
        cursor.close();//关闭游标
      //  Log.i(TAG, "queryIsExists: 该用户id 判断对象是否是在表中存在"+(len>0));
        return len>0;
    }
    /**
     * 根据 用户endUserId  删除表中的数据(账户和密码为指定的都会被删除)
     * @param tmId   用户id
     */
    private void deleteLoginFromId(String tmId){
        if(tmId.equals("")){
            return ;
        }
        db.delete(MyConfig.TB_JOBS,"tmId=?",new String[]{tmId});
       // Log.i(TAG, "deleteLogin: "+"成功删除："+tmId+":"+tmId);
    }

    /**
     * 根据 用户endUserId  删除表中的数据(账户和密码为指定的都会被删除)
     * @param
     */
    public void deleteAllDate(){
        db.delete(MyConfig.TB_JOBS,null,null);
        Log.i(TAG, "deleteLogin: "+"成功删除：");
    }


    /**
     * 根据id查询题目信息
     * @param tmId
     * @return 题目
     */
    public DbTiMu getDbTiMuFromId(String tmId){
        // Log.i(TAG, "数据库拿数据  根据id查看用户资料信息: "+loginId);
        if(tmId.equals("")){
            return null;
        }
        DbTiMu tm=null;
        Cursor cursor=db.query(MyConfig.TB_JOBS,null,"tmId = ?",new String[]{tmId },null,null,null);
        int len=cursor.getCount();//获取对应的列数
      //  Log.i(TAG, "该id下的用户个数: "+len);
        if(len>0) {
            while (cursor.moveToNext()) {//判断是否有下一个游标
                tm = new DbTiMu();
                String tnId = cursor.getString(cursor.getColumnIndex("tmId"));//根据列数获取对象值加入list
                tm.setTmId(tnId);
                tm.setQid(cursor.getInt(cursor.getColumnIndex("qid")));
                tm.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
                Log.i(TAG, "getDbTiMu: " + tmId+"查询题目数据：" + tm.toString());
            }
            cursor.close();
        }
        return tm;
    }


    /**
     * 获取所有的题目信息
     * @return
     */
    public List<DbTiMu> getDbTiMuList(){
        List<DbTiMu> list=new ArrayList<>();
        Cursor cursor=db.query(MyConfig.TB_JOBS,null,null,null,null,null,null);
        int len=cursor.getCount();
        DbTiMu tm;
        if(len>0){
            while (cursor.moveToNext()){
                int cursorIndex=cursor.getColumnIndex("tmId");//获取name对应的列数
                String tmId=cursor.getString(cursorIndex);//根据列数获取对象值加入list
                int qid=cursor.getInt(cursor.getColumnIndex("qid"));
                String answer=cursor.getString(cursor.getColumnIndex("answer"));
                tm=new DbTiMu(tmId,qid,answer);
                list.add(tm);
                Log.i(TAG, "getUnUserInfo: "+"获取专业信息： "+tm.toString());
            }
        }
        cursor.close();
        return  list;
    }



}
