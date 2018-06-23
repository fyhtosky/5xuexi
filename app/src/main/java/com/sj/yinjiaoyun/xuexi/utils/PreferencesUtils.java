package com.sj.yinjiaoyun.xuexi.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wanzhiying on 2017/3/6.
 */
public class PreferencesUtils {
    /**
     * 普通字段存放地址
     */
    public static String  QQXMPP="5xuexi";


    public static String getSharePreStr(Context mContext, String field){
        SharedPreferences sp= mContext.getSharedPreferences(QQXMPP, 0);
        //如果该字段没对应值，则取出字符串0
        return sp.getString(field,"");
    }
    //取出whichSp中field字段对应的int类型的值
    public static int getSharePreInt(Context mContext,String field){
        SharedPreferences sp= mContext.getSharedPreferences(QQXMPP, 0);
        //如果该字段没对应值，则取出0
        return sp.getInt(field,0);
    }
    //取出whichSp中field字段对应的Long类型的值
    public static long getSharePreLong(Context mContext,String field){
        SharedPreferences sp= mContext.getSharedPreferences(QQXMPP, 0);
        //如果该字段没对应值，则取出0
        return sp.getLong(field,0);
    }

    //取出whichSp中field字段对应的boolean类型的值
    public static boolean getSharePreBoolean(Context mContext,String field){
        SharedPreferences sp= mContext.getSharedPreferences(QQXMPP, 0);
        //如果该字段没对应值，则取出0
        return sp.getBoolean(field, false);
    }
    //保存string类型的value到whichSp中的field字段
    @SuppressLint("CommitPrefEdits")
    public static void putSharePre(Context mContext, String field, String value){
        SharedPreferences sp= mContext.getSharedPreferences(QQXMPP, 0);
        sp.edit().putString(field, value).commit();
    }

    //保存int类型的value到whichSp中的field字段
    @SuppressLint("CommitPrefEdits")
    public static void putShareInt(Context mContext, String field, int value){
        SharedPreferences sp= mContext.getSharedPreferences(QQXMPP, 0);
        sp.edit().putInt(field, value).commit();
    }
    //保存Long类型的value到whichSp中的field字段
    @SuppressLint("CommitPrefEdits")
    public static void putSharePre(Context mContext, String field, long value){
        SharedPreferences sp= mContext.getSharedPreferences(QQXMPP, 0);
        sp.edit().putLong(field, value).commit();
    }

    //保存boolean类型的value到whichSp中的field字段
    @SuppressLint("CommitPrefEdits")
    public static void putSharePre(Context mContext, String field, Boolean value){
        SharedPreferences sp= mContext.getSharedPreferences(QQXMPP, 0);
        sp.edit().putBoolean(field, value).commit();
    }
    //清空保存的数据
    @SuppressLint("CommitPrefEdits")
    public static void clearSharePre(Context mContext){
        try {
            SharedPreferences sp= mContext.getSharedPreferences(QQXMPP, 0);
            sp.edit().clear().commit();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}
