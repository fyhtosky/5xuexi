package com.sj.yinjiaoyun.xuexi.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/6/20 0020.
 * 网络请求操作类
 */
public class HttpDemo {
    String TAG="result";
    HttpUtils httpUtils;
    Handler handler;
    HttpCallBack callBack;
    public interface HttpCallBack{
        void setMsg(String msg, int requestCode);
    }
    public HttpDemo(){
        httpUtils=new HttpUtils();
        handler=new Handler();
    }
    public HttpDemo(HttpCallBack callBack){
        httpUtils=new HttpUtils();
        handler=new Handler();
        this.callBack=callBack;
    }


    /**
     * get请求
     * @param url  完整接口
     * @param pairsList  键值对集合
     */
    public void doHttpGet(final String url, List<Pairs> pairsList, final int requestCode) {
        Log.i(TAG, "进入doHttpGet: ");
        httpUtils.doGet(url, pairsList, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "doHttpGet失败: "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(! HttpClient.onIntercept(response)){
                    return;
                }
                final String msg = response.body().string();
                Log.i(TAG, "doHttpGet成功: "+msg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: "+requestCode+msg);
                        callBack.setMsg(msg,requestCode);
                    }
                });
            }
        });
    }


    /**
     * get请求
     * @param url  完整接口
     * @param pairsList  键值对集合
     */
    public void doHttpGetUser(final String url, List<Pairs> pairsList, final int requestCode) {
        Log.i(TAG, "doHttpGetUser: ");
        Log.i("fragmentintro", "doHttpGetUser: "+"IntroFragment");
        httpUtils.doGetUser(url, pairsList, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "doHttpGetUser失败: "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(! HttpClient.onIntercept(response)){
                    return;
                }
                final String msg = response.body().string();
                Log.i(TAG, "doHttpGetUser成功: "+requestCode+msg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.setMsg(msg,requestCode);
                    }
                });
            }
        });
    }


    /**
     * post请求
     * @param url  完整接口
     * @param pairsList  键值对集合
     */
   public void doHttpPost(final String url, List<Pairs> pairsList, final int requestCode){
        Log.i(TAG, "doHttpPost ");
        httpUtils.doPost(url,pairsList, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "doHttpPost失败: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(! HttpClient.onIntercept(response)){
                    return;
                }
                final String msg=response.body().string();
                Log.i("login", "doHttpPost"+msg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.setMsg(msg,requestCode);
                    }
                });
            }
        });
    }


    /**
     * 视频信息反馈 post请求
     * @param url  完整接口
     * @param pairsList  键值对集合
     */
    public void doHttpPostVideo(final String url, List<Pairs> pairsList, final int requestCode){
        httpUtils.doPostVideo(url,pairsList, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("videos", "doHttpPostVideo失败: "+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(! HttpClient.onIntercept(response)){
                    return;
                }
                final String msg=response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.setMsg(msg,requestCode);
                    }
                });
            }
        });
    }





    /**
     * get请求  有缓冲条
     * @param url  完整接口
     * @param pairsList  键值对集合
     */
    public void doHttpGetLoading(Context context, final String url, List<Pairs> pairsList, final int requestCode){
        Log.i(TAG, "doHttpPost ");
        httpUtils.doGet(url,pairsList, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "doHttpPost失败: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(! HttpClient.onIntercept(response)){
                    return;
                }
                final String msg=response.body().string();
                Log.i(TAG, "doHttpPost成功: "+requestCode+"+"+msg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.setMsg(msg,requestCode);
                    }
                });
            }
        });
    }

    /**
     * post请求   有缓冲条
     * @param url  完整接口
     * @param pairsList  键值对集合
     */
    public void doHttpPostLoading(Context context, final String url, List<Pairs> pairsList, final int requestCode){
        Log.i(TAG, "doHttpPost ");
        Logger.d("URL:"+url+";params:"+pairsList.toString());
        httpUtils.doPost(url,pairsList, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "doHttpPost失败: "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(! HttpClient.onIntercept(response)){
                    return;
                }
                final String msg=response.body().string();
                Log.i("login", "doHttpPost"+msg);
                Log.i(TAG, "doHttpPost成功: "+requestCode+"+"+msg);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.setMsg(msg,requestCode);
                    }
                });
            }
        });
    }
}
