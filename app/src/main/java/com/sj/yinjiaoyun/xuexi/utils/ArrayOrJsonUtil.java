package com.sj.yinjiaoyun.xuexi.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 */
public class ArrayOrJsonUtil {


    private static String TAG = "aaaaa";
    //把实体类转化为Jsson
    public static  String toJson(Object obj) {
        //字段是首字母小写，其余单词首字母大写
        Gson gson = new Gson();
        String obj2 = gson.toJson(obj);
        Log.i(TAG, "toJson: "+obj2);
        return obj2;
    }

    //把数组json转化为数组
    public static List<String> getList(String jsonString) {
        //先转jsonArray
        JsonArray jsonArray = new JsonParser().parse(jsonString).getAsJsonArray();
        Log.i(TAG, "getList: "+jsonArray);
        List<String> list=new ArrayList<>();
        try {
            for(int i=0;i<jsonArray.size();i++){
                Log.i(TAG, "getList: "+jsonArray.get(i));
                list.add(jsonArray.get(i).toString());
            }
            Log.i(TAG, "getList: "+list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //把数组json转化为数组
    public static List<Integer> jsonToList(String jsonString) {
        //先转化成json
        StringBuilder sb=new StringBuilder();
        sb.append("{data:").append(jsonString).append("}");
        Log.i(TAG, "jsonToList: "+sb.toString());
        List<Integer> list = new ArrayList<>();
        try {
            JSONObject obj=new JSONObject(sb.toString());
            JSONArray data=obj.getJSONArray("data");
            for(int i=0;i<data.length();i++){
                Log.i(TAG, "jsonToList: "+data.get(i));
                int a=(Integer) data.get(i);
                list.add(a);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i(TAG, "jsonToList: --"+list.size());
        return list;
    }

    //把数组json转化为数组
    public static List<String> jsonToListStr(String jsonString) {
        //先转化成json
        StringBuilder sb=new StringBuilder();
        sb.append("{data:").append(jsonString).append("}");
        Log.i(TAG, "jsonToList: "+sb.toString());
        List<String> list = new ArrayList<>();
        try {
            JSONObject obj=new JSONObject(sb.toString());
            JSONArray data=obj.getJSONArray("data");
            for(int i=0;i<data.length();i++){
                String a= (String) data.get(i);
                list.add(a);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }



    //把map类转化为Jsson
    public static  String mapToJson(Map<String,Object> map) {
        //字段是首字母小写，其余单词首字母大写
        Gson gson = new Gson();
        String obj2 = gson.toJson(map);
        Log.i(TAG, "toJson: "+obj2);
        return obj2;
    }

}
