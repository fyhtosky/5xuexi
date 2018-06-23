package com.sj.yinjiaoyun.xuexi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.UserInfo;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/9/5.
 * 用户个人信息缓存入数据库
 */
public class DbOperatorUserInfo {

    String TAG="meActivity";
    MySQLHelper helper;
    SQLiteDatabase db;
    Context context;
    //构造函数建表
    public DbOperatorUserInfo(Context context){
        helper=new MySQLHelper(context);
        db=helper.getReadableDatabase();
        this.context=context;
    }

    /**
     * 增加用户个人信息
     * @param info 用户个人信息
     * @param loginid 用户
     */
    public void insertUserInfo(UserInfo info, String loginid){
        if(info==null){
            return ;
        }
        Log.i(TAG, "存数据进数据库: "+loginid+":" +info.toString());
        if(queryIsExists(loginid)){
            deleteLoginInfo(loginid);//删除所有的用户信息
        }
        ContentValues values=new ContentValues();
        values.put("loginId",loginid);
        values.put("endUserId",String.valueOf(info.getEndUserId()));
        values.put("userName",info.getUserName());
        values.put("realName",info.getRealName());
        Log.i("meActivity", "insertUserInfo--: "+String.valueOf(info.getSex()));
        values.put("sex",info.getSex());
        values.put("phone",info.getPhone());
        values.put("idCard",info.getIdCard());
        values.put("email",info.getEmail());
        values.put("middleSchoolCertificate",info.getMiddleSchoolCertificate());
        values.put("collegeSpecializCertificate",info.getCollegeSpecializCertificate());
        values.put("collegeSchoolCertificate",info.getCollegeSchoolCertificate());
        values.put("bachelorCertificate",info.getBachelorCertificate());
        values.put("userImg",info.getUserImg());
        values.put("address",info.getAddress());

        values.put("politicsStatus",info.getPoliticsStatus());
        values.put("nation",info.getNation());
        values.put("fixPhone",info.getFixPhone());
        values.put("postalCode",info.getPostalCode());

        db.insert(MyConfig.TB_USERINFO,null,values);
        Log.i(TAG, "insertUserInfo: "+"插入数据成功："+loginid+"："+info.toString());
    }

    /**
     * 根据 用户id 判断对象是否是在表中存在
     * @param loginId 要判断的对象
     * @return  true存在 false不存在
     */
    private boolean queryIsExists(String loginId){
        if(loginId.equals("")){
            return false;
        }
        Cursor cursor=db.query(MyConfig.TB_USERINFO,null,"loginId=?",
                new String[]{loginId},null,null,null);//查看此对象对应的游标
        int len=cursor.getCount();
        cursor.close();//关闭游标
        Log.i(TAG, "queryIsExists: 该用户id 判断对象是否是在表中存在"+(len>0));
        return len>0;
    }

    //删除所有用户信息
    private void deleteLoginInfo(String loginId) {
        if(loginId.equals("")){
            return;
        }
        db.delete(MyConfig.TB_USERINFO,"loginId=?",new String[]{loginId});
        Log.i(TAG, "deleteUserInfo: "+"删除表单id为："+loginId+"的信息");
    }

    /**
     * 根据id查询用户信息
     * @param loginId
     * @return 用户信息
     */
    public UserInfo LookUserInfoFromId(String loginId){
       // Log.i(TAG, "数据库拿数据  根据id查看用户资料信息: "+loginId);
        if(loginId.equals("")){
            return null;
        }
        UserInfo info=null;
        Cursor cursor=db.query(MyConfig.TB_USERINFO,null,"loginId = ?",new String[]{loginId },null,null,null);
        int len=cursor.getCount();//获取对应的列数
        Log.i(TAG, "该id下的用户个数: "+len);
        if(len>0) {
            while (cursor.moveToNext()) {//判断是否有下一个游标
                info = new UserInfo();
                String endUserId = cursor.getString(cursor.getColumnIndex("endUserId"));//根据列数获取对象值加入list
                Log.i(TAG, "LookUserInfoFromId: "+endUserId+" ");
                if(endUserId.equals("")){
                    info.setEndUserId(Long.parseLong(endUserId.trim()));
                }
                info.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                info.setRealName(cursor.getString(cursor.getColumnIndex("realName")));
                String sex=cursor.getString(cursor.getColumnIndex("sex"));
                if(sex==null){
                    info.setSex(null);
                }else{
                    info.setSex(Byte.valueOf(sex));
                }
                info.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                info.setIdCard(cursor.getString(cursor.getColumnIndex("idCard")));
                info.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                info.setMiddleSchoolCertificate(cursor.getString(cursor.getColumnIndex("middleSchoolCertificate")));
                info.setCollegeSpecializCertificate(cursor.getString(cursor.getColumnIndex("collegeSpecializCertificate")));
                info.setCollegeSchoolCertificate(cursor.getString(cursor.getColumnIndex("collegeSchoolCertificate")));
                info.setBachelorCertificate(cursor.getString(cursor.getColumnIndex("bachelorCertificate")));
                info.setUserImg(cursor.getString(cursor.getColumnIndex("userImg")));
                info.setAddress(cursor.getString(cursor.getColumnIndex("address")));

                info.setPoliticsStatus(cursor.getString(cursor.getColumnIndex("politicsStatus")));
                info.setNation(cursor.getString(cursor.getColumnIndex("nation")));
                info.setFixPhone(cursor.getString(cursor.getColumnIndex("fixPhone")));
                info.setPostalCode(cursor.getString(cursor.getColumnIndex("postalCode")));
                Log.i(TAG, "getAllUserInfo: " + "获取id为 " + loginId + " 的用户信息数据：" + info.toString());
            }
            cursor.close();
        }
        return info;
    }


}
