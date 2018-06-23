package com.sj.yinjiaoyun.xuexi.http;

import android.content.pm.PackageManager;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.xiaopan.android.net.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



/**
 * Created by Administrator on 2016/6/20 0020.
 * 网络请求 工具类
 */
public class  HttpUtils {
    private static final String EDU_TOKEN="edu_token";
    private static final String EDU_AUTH="edu_auth";
    public static final String EDU_VERSION="version";
    public static final String EDU_VERSION_BACK="edu_app_version_auth";
    public static final String EDU_TYPE="appType";
    String TAG="https";
    OkHttpClient client;
    public HttpUtils(){
        client=new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        // 为请求附加 token
                        Request authorised = null;
                        try {
                            authorised = originalRequest.newBuilder()
                                    .header(EDU_TOKEN, PreferencesUtils.getSharePreStr(MyApplication.getContext(), Const.TOKEN))
                                    .addHeader(EDU_VERSION,String.valueOf(MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(),0).versionCode))
                                    .addHeader(EDU_TYPE,"1")
                                    .build();
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        Logger.d("TOKEN:"+PreferencesUtils.getSharePreStr(MyApplication.getContext(), Const.TOKEN));
                        return chain.proceed(authorised);

                    }
                })
        .build();

    }

    /**
     * get请求
     * @param url 完整的借口
     * @param pairsList 键值对
     * @param callback  回调
     */
    public void doGet(String url, List<Pairs> pairsList, Callback callback) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        if (pairsList!= null) {
            for (Pairs pairs : pairsList) {
                if (isFirst) {
                    sb.append("?");
                    isFirst = false;
                } else {
                    sb.append("&");
                }
              //  Log.i(TAG, "@doGet: "+pairs.getKey()+":"+pairs.getValue());
                sb.append(pairs.getKey()).append("=").append(pairs.getValue());
            }
        }
        url=url+sb.toString();
        Log.i(TAG, "拼接url为: "+url);
        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            Log.e(TAG,"网络开小差了！！");
            return;
        }
        Request request=new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }



    //用户信息特定
    public void doGetUser(String url, List<Pairs> pairsList, Callback callback) {
        Log.i(TAG, "doGetUser: ");
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        if (pairsList!= null) {
            for (Pairs pairs : pairsList) {
                if (isFirst) {
                    sb.append("?");
                    isFirst = false;
                } else {
                    sb.append("&");
                }
                sb.append(pairs.getKey()).append("=").append(pairs.getEndUserId());
            }
        }
        url=url+sb.toString();
        Log.i(TAG, "doGetUser拼接url为:-- "+url);
        Log.i("fragmentintro", "拼接url为: "+url);
        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            Log.e(TAG,"网络开小差了！！");
            return;
        }
        Request request=new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }


    /**
     *  post请求
     * @param url 完整的借口
     * @param pairsList 键值对
     * @param callback  回调
     */
    public void doPost(String url, List<Pairs> pairsList, Callback callback){
        Log.i(TAG, "doPost " +url);
        FormBody.Builder builder=new FormBody.Builder();
        if(pairsList!=null){
            for (Pairs pairs:pairsList){
                builder.add(pairs.getKey(),pairs.getValue());
                Log.i(TAG, "doPost: "+pairs.getKey()+"="+pairs.getValue());
            }
        }
        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            Log.e(TAG,"网络开小差了！！");
            return;
        }
        FormBody body=builder.build();
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }


    /**
     *  视频反馈接口 post请求
     * @param url 完整的借口
     * @param pairsList 键值对
     * @param callback  回调
     */
    public void doPostVideo(String url, List<Pairs> pairsList, Callback callback){
        Log.i("videos", "doPostVideo: ");
        FormBody.Builder builder=new FormBody.Builder();
        if(pairsList!=null){
            for (Pairs pairs:pairsList){
                builder.add(pairs.getKey(),String.valueOf(pairs.getValue()));
            }

        }
        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            Log.e(TAG,"网络开小差了！！");
            return;
        }
        FormBody body=builder.build();
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 上传文件 post请求
     * @param url
     * @param pairsList
     * @param file
     * @param callback
     */
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    public void doPostFile(String url, List<Pairs> pairsList, List<String> mImgUrls, Callback callback) {
        Log.i("opinion", "doPostFile utils: ");
        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i <mImgUrls.size() ; i++) {
            Log.i("opinion", "doPostFile: "+mImgUrls.get(i));
            String  imgPath=mImgUrls.get(i);
            File f=new File(imgPath);
            if (f!=null) {
                Log.i("opinion", "doPostFile: "+f.getName());
                builder.addFormDataPart("img", f.getName(), RequestBody.create(MEDIA_TYPE_JPG, f));
            }
        }
        //添加其它信息
        if(pairsList!=null){
            for (Pairs pairs:pairsList){
                builder.addFormDataPart(pairs.getKey(),String.valueOf(pairs.getValue()));
            }
        }
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();
        //client.newCall(request).enqueue(callback);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("opinion", "上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("opinion", "上传照片成功：response = " + response.body().string());
            }
        });

    }
}
