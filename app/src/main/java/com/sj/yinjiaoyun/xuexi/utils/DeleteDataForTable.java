package com.sj.yinjiaoyun.xuexi.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sj.yinjiaoyun.xuexi.db.MySQLHelper;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/9/8.
 * 删除数据库表格的工具类
 */
public class DeleteDataForTable {
    MySQLHelper helper;
    SQLiteDatabase db;
    Context context;

    public DeleteDataForTable(Context context) {
        helper=new MySQLHelper(context);
        db=helper.getReadableDatabase();
        this.context = context;
    }

    /**
     * 删除所有表中的数据
     */
    public void deleteAllData(){
        Log.i("login", "删除该表中数据: "+ MyConfig.TB_USERINFO+":"+ MyConfig.TB_PRODUCT+":");
        db.delete(MyConfig.TB_USERINFO,null,null);
        db.delete(MyConfig.TB_PRODUCT,null,null);
    }

    /**
     * 删除所有表中的数据
     */
    public void deleteTbIntro(){
        Log.i("login", "删除该表中数据: "+ MyConfig.TB_INTRO);
        db.delete(MyConfig.TB_INTRO,null,null);
    }
}
