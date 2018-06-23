package com.sj.yinjiaoyun.xuexi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.ProductVO;
import com.sj.yinjiaoyun.xuexi.domain.ProductVOcache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/9/8.
 * reportfragment 专业详情  顶部 数据缓存
 */
public class DbOperatorProductVO {

    String TAG="home";
    MySQLHelper helper;
    SQLiteDatabase db;
    Context context;
    //构造函数建表
    public DbOperatorProductVO(Context context){
        helper=new MySQLHelper(context);
        db=helper.getReadableDatabase();
        this.context=context;
    }

    /**
     * 增加用户用户  (如果该id存在，则删除原来的数据，添加现在的新数据)
     * @param productVO 用户  productVO集合里面子项
     */
    public void insertLogin(ProductVO productVO){
        Log.i(TAG, "insertLogin: ");
        if(productVO==null){
            return ;
        }
        if(queryIsExists(String.valueOf(productVO.getId()))){//存在该id ，则删除
            deleteFromId(String.valueOf(productVO.getId()));
        }
        ContentValues values=new ContentValues();
        values.put("flag", MyConfig.LOGIN_FALSE);
        values.put("_id",productVO.getId());
        values.put("productName",productVO.getProductName());
        values.put("enrollPlanId",productVO.getEnrollPlanId());
        db.insert(MyConfig.TB_PRODUCT,null,values);
        Log.i(TAG, "insertLogin: "+"插入数据成功："+productVO.toString());
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
        Cursor cursor=db.query(MyConfig.TB_PRODUCT,null,"_id=?",
                new String[]{id},null,null,null);//查看此对象对应的游标
        int len=cursor.getCount();
        cursor.close();//关闭游标
        Log.i(TAG, "queryIsExists: 该用户id 判断对象是否是在表中存在"+(len>0));
        return len>0;
    }


    /**
     * 根据 用户id 删除该表中的数据
     * @param id   用户id
     */
    private void deleteFromId(String id){
        if(id.equals("")){
            return ;
        }
        db.delete(MyConfig.TB_PRODUCT,"_id=?",new String[]{id});
        Log.i(TAG, "deleteLogin: "+"根据id成功删除："+id+":");
    }

    /**
     * 根据用户id  修改显示专业的状态
     * @param flag  要修改为的状态
     * @param vo
     */
    public void changeLineState(String flag,ProductVO vo){
        if(vo==null && flag.equals("")){
            return ;
        }
        String _id=String.valueOf(vo != null ? vo.getId() : null);
        String productName=vo.getProductName();
        String enrollPlanId=String.valueOf(vo.getEnrollPlanId());
        Cursor cursor=db.query(MyConfig.TB_PRODUCT,null,"_id=? and productName=? and enrollPlanId=?",new String[]{_id,productName,enrollPlanId},null,null,null);
        int len=cursor.getCount();
        if(len>0){
            while(cursor.moveToNext()){
                //删除原始数据
                Log.i(TAG, "changeLineState: "+"删除原始数据");
                db.delete(MyConfig.TB_USERINFO,"_id=?and productName=? and enrollPlanId=?",new String[]{_id,productName,enrollPlanId});
                //添加已经改好的新数据
                ContentValues values=new ContentValues();
                values.put("flag",flag);
                values.put("_id",_id) ;
                values.put("productName",productName);
                values.put("enrollPlanId",enrollPlanId);
                db.insert(MyConfig.TB_LOGIN,null,values);
                Log.i(TAG, "changeLineState: "+"成功修改登录状态的信息为未登录："+ MyConfig.LOGIN_FALSE+":"+cursor.getString(cursor.getColumnIndex("endUserId"))+":"+cursor.getString(cursor.getColumnIndex("param"))
                        +":"+cursor.getString(cursor.getColumnIndex("pwd")));
            }
        }else{
                Log.i(TAG,"用户不存在");
        }
        cursor.close();//关闭游标
    }

    /**
     *
     * @return  返回数据集
     */
    public List<ProductVOcache> getAllProductVO(){
        List<ProductVOcache> list =new ArrayList<>();
        Cursor cursor=db.query(MyConfig.TB_PRODUCT,null,null,null,null,null,null);
        int len=cursor.getCount();
        ProductVO vo;
        ProductVOcache cache;
        if(len>0){
            while (cursor.moveToNext()){
                String flag=cursor.getString(cursor.getColumnIndex("flag"));
                int cursorIndex=cursor.getColumnIndex("_id");//获取name对应的列数
                String _id=cursor.getString(cursorIndex);//根据列数获取对象值加入list
                String productName=cursor.getString(cursor.getColumnIndex("productName"));
                String enrollPlanId=cursor.getString(cursor.getColumnIndex("enrollPlanId"));
                vo=new ProductVO(Long.parseLong(_id),productName,Long.parseLong(enrollPlanId));
                cache=new ProductVOcache(flag,vo);
                list.add(cache);
                Log.i(TAG, "getUnUserInfo: "+"获取专业信息： "+vo.toString());
            }
        }
        cursor.close();
        return list;
    }

}
