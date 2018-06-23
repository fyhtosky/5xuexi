package com.sj.yinjiaoyun.xuexi.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * Created by wanzhiying on 2017/3/17.
 */
public class AsyncHttpUtil {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void PostImage(String url, HashMap<String,String>map, final CallBack callBack){
            RequestParams params = new RequestParams();
            for (String s : map.keySet()){
                params.put(s,map.get(s));
            }
            client.post(url,params,new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, final String content) {
                    Logger.d("AsyncHttpClient"+content);
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           callBack.onSuccess(new Gson().fromJson(content, callBack.type));
                       }
                   });
                }

                @Override
                public void onFailure(final Throwable e, String data){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure(e.getLocalizedMessage());
                        }
                    });

                }
            });


    }
}
