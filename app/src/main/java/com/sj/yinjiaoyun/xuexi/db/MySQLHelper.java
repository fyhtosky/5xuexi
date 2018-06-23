package com.sj.yinjiaoyun.xuexi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sj.yinjiaoyun.xuexi.http.MyConfig;


/**
 * 作者：Created by ${开发者鼻祖 沈军} on 2016/7/4 0004.
 * 邮箱：961784535@qq.com
 * 数据库创建，工具类
 */
public class MySQLHelper extends SQLiteOpenHelper{

    String TAG="result";
    String sql_logins = MyConfig.SQL_CREATE+" "+ MyConfig.TB_LOGIN+"(id integer primary key autoincrement," +
            "state varchar(20),flag varchar(20),endUserId varchar(20),realName varchar(20),param varchar(30),pwd varchar(30),image varchar(30))";//state表示登录状态（1,0），endUserId表示用户id
    String sql_userinfo= MyConfig.SQL_CREATE+" "+ MyConfig.TB_USERINFO+"(id integer primary key autoincrement," +"loginId varchar(20),"+
            "endUserId varchar(20),userName varchar(20),realName varchar(20),sex varchar(20),phone varchar(20),idCard varchar(50)," +
            "email varchar(50),middleSchoolCertificate varchar(50),collegeSpecializCertificate varchar(50)," +
            "collegeSchoolCertificate varchar(50)," + "bachelorCertificate varchar(50),userImg varchar(100)," +
            "address varchar(50),politicsStatus varchar(20),nation varchar(20),fixPhone varchar(20),postalCode varchar(20))";//个人信息储存
    String sql_productvo = MyConfig.SQL_CREATE+" "+ MyConfig.TB_PRODUCT+"(id integer primary key autoincrement," +
            "flag varchar(20),_id varchar(30),productName varchar(20),enrollPlanId varchar(30))";//state表示选中状态（1,0），
    String sql_intro= MyConfig.SQL_CREATE+" "+ MyConfig.TB_INTRO+"(id integer primary key autoincrement," +
            "learnBehaviorScore integer,usualScore integer,finalScore integer,totalScore integer)";//数据缓存

    String sql_jobs= MyConfig.SQL_CREATE+" "+ MyConfig.TB_JOBS+"(id integer primary key autoincrement," +
            "tmId varchar(20),qid integer(20),answer varchar(2000))";//作业考试临时数据缓存

    public MySQLHelper(Context context) {
        super(context, MyConfig.NAME, null, MyConfig.SQL_VERSION);
    }

    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql_logins);
        sqLiteDatabase.execSQL(sql_userinfo);
        sqLiteDatabase.execSQL(sql_productvo);
    }

    //版本号改变，数据库重构
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
