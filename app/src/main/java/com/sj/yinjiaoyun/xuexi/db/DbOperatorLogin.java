package com.sj.yinjiaoyun.xuexi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.sj.yinjiaoyun.xuexi.domain.LoginInfo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/23.
 * 保存用户登录部分信息数据库（目前只保存 状态 用户id  用户名/电话 密码）
 */
public class DbOperatorLogin {
    String TAG="db_login";
    MySQLHelper helper;
    SQLiteDatabase db;
    Context context;
    //构造函数建表
    public DbOperatorLogin(Context context){
        helper=new MySQLHelper(context);
        db=helper.getReadableDatabase();
        this.context=context;
    }

    /**
     * 增加用户用户  (如果该id存在，则删除原来的数据，添加现在的新数据)
     * @param info 用户
     */
    public void insertLogin(LoginInfo info){
        Log.i(TAG, "insertLogin: ");
        if(info==null){
            return ;
        }
        if(queryIsExists(info.getEndUserId())){//存在该id ，则删除
            deleteLoginFromId(info.getEndUserId());
        }
        deleteLoginFromState(MyConfig.LOGIN_TRUE);//删除所有登录状态的用户
        ContentValues values=new ContentValues();
        values.put("state",info.getState());
        values.put("flag",info.getFlag());
        values.put("endUserId",info.getEndUserId());
        values.put("param",info.getParam());
        values.put("pwd",info.getPwd());
        values.put("image",info.getImage());
        values.put("realName",info.getRealName());
        db.insert(MyConfig.TB_LOGIN,null,values);
        Log.i(TAG, "insertLogin: "+"插入数据成功："+info.getState()+":"+info.getFlag()+":"+info.getEndUserId()+":"+info.getParam()+":"+info.getPwd()+":"+info.getImage()+":"+info.getRealName());
    }


    /**
     * 根据用户id  修改用户登录状态
     * @param state  要修改为的状态
     * @param endUserId  用户的id
     */
    public void changeLineState(String state,String endUserId){
        if(endUserId.equals("")&& state.equals("")){
            return ;
        }
        Cursor cursor=db.query(MyConfig.TB_LOGIN,null,"endUserId=?",new String[]{endUserId},null,null,null);
        int len=cursor.getCount();
        Log.i(TAG, "changeLineState: "+"id为"+endUserId+"的用户个数:"+len);
        if(len>0){
            String param;
            while(cursor.moveToNext()){
                //删除原始数据
                deleteLoginFromIdAndState(MyConfig.LOGIN_TRUE,endUserId);
                //添加已经改好的新数据
                param=cursor.getString(cursor.getColumnIndex("param"));
                ContentValues values=new ContentValues();
                values.put("state",state);
                values.put("flag",cursor.getString(cursor.getColumnIndex("flag")));
                values.put("endUserId",cursor.getString(cursor.getColumnIndex("endUserId")));
                values.put("param",param) ;
                values.put("pwd",cursor.getString(cursor.getColumnIndex("pwd")));
                values.put("image",cursor.getString(cursor.getColumnIndex("image")));
                values.put("realName",cursor.getString(cursor.getColumnIndex("realName")));
                db.insert(MyConfig.TB_LOGIN,null,values);
                Log.i(TAG, "changeLineState: "+"成功修改登录状态的信息为未登录："+ MyConfig.LOGIN_FALSE+":"
                        +cursor.getString(cursor.getColumnIndex("flag"))+cursor.getString(cursor.getColumnIndex("endUserId"))+
                        ":"+cursor.getString(cursor.getColumnIndex("param"))
                        +":"+cursor.getString(cursor.getColumnIndex("pwd"))+":"+cursor.getString(cursor.getColumnIndex("image")));
            }
        }else{
            Toast.makeText(context,"用户不存在",Toast.LENGTH_SHORT).show();
        }
        cursor.close();//关闭游标
    }


    /**
     * 获取登录状态的用户信息
     * @return 登录状态的用户
     */
    public LoginInfo getLoginEndUserInfo(){
        Cursor cursor=db.query(MyConfig.TB_LOGIN,null,"state=?",new String[]{MyConfig.LOGIN_TRUE},null,null,null);
        int len=cursor.getCount();
        LoginInfo info=null;
        Log.i(TAG, "getUserInfo: "+"获取登录在线的用户个数"+len);
        if(len>0){
            while (cursor.moveToNext()){
                int cursorIndex=cursor.getColumnIndex("endUserId");//获取name对应的列数
                String userId=cursor.getString(cursorIndex);//根据列数获取对象值加入list
                String flag=cursor.getString(cursor.getColumnIndex("flag"));
                String param=cursor.getString(cursor.getColumnIndex("param"));
                String pwd=cursor.getString(cursor.getColumnIndex("pwd"));
                String image=cursor.getString(cursor.getColumnIndex("image"));
                String realName=cursor.getString(cursor.getColumnIndex("realName"));
                info=new LoginInfo(MyConfig.LOGIN_TRUE,flag,userId,param,pwd,image,realName);
                Log.i(TAG, "getUserInfo: "+"登录在线状态的用户信息： "+ MyConfig.LOGIN_TRUE+":"+flag+":"+userId+":"+param+":"+pwd+":"+image);
            }
        }
        cursor.close();
        return info;
    }

    /**
     * 获取未登录状态的用户信息
     * @return 未登录状态的用户集合
     */
    public List<LoginInfo> getUnLoginEndUserInfo(){
        List<LoginInfo> loginInfoList =new ArrayList<>();
        Cursor cursor=db.query(MyConfig.TB_LOGIN,null,"state=?",new String[]{MyConfig.LOGIN_FALSE},null,null,null);
        int len=cursor.getCount();
        LoginInfo info;
        Log.i(TAG, "getUnUserInfo: "+"获取不在线的用户个数"+len);
        if(len>0){
            while (cursor.moveToNext()){
                int cursorIndex=cursor.getColumnIndex("endUserId");//获取name对应的列数
                String userId=cursor.getString(cursorIndex);//根据列数获取对象值加入list
                String flag=cursor.getString(cursor.getColumnIndex("flag"));
                String param=cursor.getString(cursor.getColumnIndex("param"));
                String pwd=cursor.getString(cursor.getColumnIndex("pwd"));
                String image=cursor.getString(cursor.getColumnIndex("image"));
                String realName=cursor.getString(cursor.getColumnIndex("realName"));
                info=new LoginInfo(MyConfig.LOGIN_FALSE,flag,userId,param,pwd,image,realName);
                loginInfoList.add(info);
                Log.i(TAG, "getUnUserInfo: "+"不在线状态的用户信息： "+ MyConfig.LOGIN_FALSE+":"+userId+":"+param+":"+pwd+":"+image);
            }
        }
        cursor.close();
        return loginInfoList;
    }


    /**
     * 根据 用户endUserId  删除表中的数据(账户和密码为指定的都会被删除)
     * @param endUserId   用户id
     */
    private void deleteLoginFromIdAndState(String state,String endUserId){
        if(endUserId.equals("")&&state.equals("")){
            return ;
        }
        db.delete(MyConfig.TB_LOGIN,"state=? and endUserId=?",new String[]{state,endUserId});
        Log.i(TAG, "deleteLogin: "+"成功删除："+state+":"+endUserId+"的用户信息");
    }


    /**
     * 根据 用户endUserId  删除表中的数据(账户和密码为指定的都会被删除)
     * @param endUserId   用户id
     */
    private void deleteLoginFromId(String endUserId){
        if(endUserId.equals("")){
            return ;
        }
        db.delete(MyConfig.TB_LOGIN,"endUserId=?",new String[]{endUserId});
        Log.i(TAG, "deleteLogin: "+"成功删除："+endUserId+":"+endUserId);
    }


    /**
     * 根据 state  删除表中的数据(账户和密码为指定的都会被删除)
     * @param state   用户状态
     */
    public void deleteLoginFromState(String state){
        if(state.equals("")){
            return ;
        }
        db.delete(MyConfig.TB_LOGIN,"state=?",new String[]{state});
        Log.i(TAG, "deleteLogin: "+"删除状态为"+state+"的信息");
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
        Cursor cursor=db.query(MyConfig.TB_LOGIN,null,"endUserId=?",
                new String[]{id},null,null,null);//查看此对象对应的游标
        int len=cursor.getCount();
        cursor.close();//关闭游标
        Log.i(TAG, "queryIsExists: 该用户id 判断对象是否是在表中存在"+(len>0));
        return len>0;
    }


}
