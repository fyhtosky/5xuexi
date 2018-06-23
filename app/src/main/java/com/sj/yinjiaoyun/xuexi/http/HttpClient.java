package com.sj.yinjiaoyun.xuexi.http;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebSettings;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.DownApkEvent;
import com.sj.yinjiaoyun.xuexi.activity.LoginActivity;
import com.sj.yinjiaoyun.xuexi.activity.MainActivity;
import com.sj.yinjiaoyun.xuexi.activity.MySetActivity;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.jpush.MyJPushUitl;
import com.sj.yinjiaoyun.xuexi.manager.UpdateManager;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import org.greenrobot.eventbus.EventBus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
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
 * Created by wanzhiying on 2017/3/6.
 */
public class HttpClient {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    private static Handler handler = new Handler(Looper.getMainLooper());
    public static final String EDU_TOKEN="edu_token";
    public static final String EDU_AUTH="edu_auth";
    public static final String EDU_VERSION_BACK="edu_app_version_auth";
    public static final String EDU_VERSION_URL="edu_app_update_url";
    public static final String EDU_VERSION="version";
    public static final String EDU_TYPE="appType";
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
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

    /**
     * 上传多张或者单张图片
     *
     * @param tag
     * @param url
     * @param key
     * @param map
     * @param paths
     * @param callBack
     */
    public static void postImage(Object tag, String url, String key, HashMap<String, String> map, List<String> paths, final CallBack callBack) {

        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            callBack.onFailure("网络开小差了！！");
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (String path : paths) {
            builder.addFormDataPart(key, path, RequestBody.create(MEDIA_TYPE_PNG, new File(path)));
        }
        for (String s : map.keySet()) {
            builder.addFormDataPart(s, map.get(s));
        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String finalJson =response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callBack.onSuccess(new Gson().fromJson(finalJson, callBack.type));
                        } catch (Exception e) {
                            callBack.onFailure(e.getLocalizedMessage());
                        }

                    }
                });

            }
        });
    }

    public static void UploadImage(Object tag, String url, String key, HashMap<String, String> map, String paths, final CallBack callBack) {

        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            callBack.onFailure("网络开小差了！！");
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
            builder.addFormDataPart(key, paths, RequestBody.create(MEDIA_TYPE_PNG, new File(paths)));
        for (String s : map.keySet()) {
            builder.addFormDataPart(s, map.get(s));
        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getLocalizedMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String finalJson = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callBack.onSuccess(new Gson().fromJson(finalJson, callBack.type));
                        } catch (Exception e) {
                            callBack.onFailure(e.getLocalizedMessage());
                        }

                    }
                });

            }
        });
    }
    /**
     * post请求
     *
     * @param tag
     * @param url
     * @param map
     * @param callBack
     */
    public static void post(Object tag, final String url, HashMap<String, String> map, final CallBack callBack) {

        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            callBack.onFailure("网络开小差了！！");
            Logger.d("network error");
            return;
        }
        FormBody.Builder builder = new FormBody.Builder();
        StringBuilder postDataStr = new StringBuilder();
        if (map != null) {
            for (String s : map.keySet()) {
                builder.add(s, map.get(s));
                postDataStr.append("[ key = ").append(s).append(",").append("value=").append(map.get(s)).append("]");
            }
        }
        RequestBody requestBody = builder.build();

        final Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .post(requestBody)
                .build();
        Logger.d("post:url:" + url + " postData:" + postDataStr.toString());
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getLocalizedMessage() + "error url:"
                                + url);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                /**
                 * 拦截处理
                 */
               if(!onIntercept(response)){
                   return;
               }
                String json = response.body().string();
                Logger.json(json);
                final String finalJson = json;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("success");
                            callBack.onSuccess(new Gson().fromJson(finalJson, callBack.type));
                            Logger.d("parse json success");
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onFailure(e.getLocalizedMessage() + "error url:"
                                    + url);
                        }

                    }
                });

            }
        });
    }

    /**
     * 获取图片的验证码
     * @param tag
     * @param url
     * @param map
     * @param callBack
     */
    public static void postImgCode(Object tag, final String url, HashMap<String, String> map, final CallBack callBack) {

        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            callBack.onFailure("网络开小差了！！");
            Logger.d("network error");
            return;
        }
        FormBody.Builder builder = new FormBody.Builder();
        StringBuilder postDataStr = new StringBuilder();
        if (map != null) {
            for (String s : map.keySet()) {
                builder.add(s, map.get(s));
                postDataStr.append("[ key = ").append(s).append(",").append("value=").append(map.get(s)).append("]");
            }
        }
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .removeHeader("User-Agent").addHeader("User-Agent",
                        getUserAgent())
                .post(requestBody)
                .build();
        Logger.d("post:url:" + url + " postData:" + postDataStr.toString());
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getLocalizedMessage() + "error url:"
                                + url);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Logger.json(json);
                final String finalJson = json;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("success");
                            callBack.onSuccess(new Gson().fromJson(finalJson, callBack.type));
                            Logger.d("parse json success");
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onFailure(e.getLocalizedMessage() + "error url:"
                                    + url);
                        }

                    }
                });

            }
        });
    }

    /**
     * 发送post请求返回需要json字符串
     * @param tag
     * @param url
     * @param map
     * @param callBack
     */
    public static void postStr(Object tag, final String url, HashMap<String, String> map, final CallBack callBack) {

        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            callBack.onFailure("网络开小差了！！");
            Logger.d("network error");
            return;
        }
        FormBody.Builder builder = new FormBody.Builder();
        StringBuilder postDataStr = new StringBuilder();
        if (map != null) {
            for (String s : map.keySet()) {
                builder.add(s, map.get(s));
                postDataStr.append("[ key = ").append(s).append(",").append("value=").append(map.get(s)).append("]");
            }
        }
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .post(requestBody)
                .build();
        Logger.d("post:url:" + url + " postData:" + postDataStr.toString());
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getLocalizedMessage() + "error url:"
                                + url);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                System.out.println("json = " + json);
                Logger.json(json);
                final String finalJson = json;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("success");
                            callBack.onSuccess(finalJson);
                            Logger.d("parse json success");
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.onFailure(e.getLocalizedMessage() + "error url:"
                                    + url);
                        }

                    }
                });

            }
        });
    }

    public static void get(final Object tag, final String url, final CallBack callBack) {
        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            callBack.onFailure("网络开小差了！！");
            Logger.d("network error");
            return;
        }

        Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .build();
        Logger.d("get:url:" + url);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getLocalizedMessage() + "error url:"
                                + url);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                /**
                 * 拦截处理
                 */
                if(!onIntercept(response)){
                    return;
                }
                String json = response.body().string();
                final String finalJson = json;
                Logger.json(json);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Logger.d("type:" + callBack.type.getClass().getSimpleName());
                            callBack.onSuccess(new Gson().fromJson(finalJson, callBack.type));
                            Logger.d("parse json success");
                        } catch (Exception e) {
                            callBack.onFailure(e.getLocalizedMessage() + "  error url:"
                                    + url);
                        }
                    }
                });

            }
        });



    }

    public static boolean onIntercept(Response response) {
        String  header=response.header(EDU_AUTH);
        String appVersion=response.header(EDU_VERSION_BACK);
        Logger.d("EDU_AUTH："+header);
        Logger.d("EDU_VERSION_BACK:"+appVersion);
        if("401".equals(header)){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //退出XMpp
                    onStartActivity();
                }
            });
        }
        if("30001".equals(appVersion)){
            Api.DOWN_APK_URL=response.header(EDU_VERSION_URL);
            Logger.d("下载地址："+Api.DOWN_APK_URL);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onStartMainActivity();
                    EventBus.getDefault().post(new DownApkEvent(Api.DOWN_APK_URL));
                }
            });
        }
        return true;
    }


    public static void getStr(final Object tag, final String url,final CallBack callBack){
        if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
            callBack.onFailure("网络开小差了！！");
            Logger.d("network error");
            return;
        }

        Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .build();
        Logger.d("get:url:" + url);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getLocalizedMessage() + "error url:"
                                + url);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String resultStr = response.body().string();
                final String finalStr = resultStr;
                Logger.d(resultStr);
                Logger.xml(resultStr);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callBack.onSuccess(finalStr);

                        } catch (Exception e) {
                            callBack.onFailure(e.getLocalizedMessage() + "error url:"
                                    + url);
                        }
                    }
                });

            }
        });

    }
    public static void  postArray1(final String portName, final String content, final CallBack callBack) {
        new Thread(){
            @Override
            public void run() {
                try {
                    String encodeUrl = encodeUrl(portName);
                    // String path = htmlName + portName;
                    Logger.d("连接："+content);
                    URL url = new URL(encodeUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.setUseCaches(false);
                    conn.setInstanceFollowRedirects(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.connect();
                    Logger.d("响应数据:"+"網絡以鏈接");
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.write(content.getBytes());
                    out.flush();
                    out.close();

                    InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                    char[] c = new char[1024];
                    final StringBuffer result = new StringBuffer();
                    int length ;
                    while ((length = reader.read(c)) != -1) {
                        result.append(c, 0, length);
                    }
                    reader.close();
                    Logger.d("响应数据："+result.toString()+"响应码："+conn.getResponseCode());
                    //// 判断请求是否成功
                    if(conn.getResponseCode()==200){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(result);
                            }
                        });
                    }else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailure("請求數據失敗");
                            }
                        });

                    }


                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }.start();

    }
    /**
     * 将URL地址中的中文转化为ENCODE
     *
     * @param url
     *            访问地址
     * @return
     */
    public static String encodeUrl(String url) {
        StringBuilder sb = new StringBuilder();
        if (url != null && url.length() > 0 && url.contains("?")) {
            // 截取?之前的不需要转换的
            int pos = url.indexOf("?");
            sb.append(url.substring(0, pos + 1));
            url = url.substring(pos + 1, url.length());
            // 根据&符号分组
            String[] str = url.split("&");
            for(String sr:str){
                // name=0%￥啊 0 对等号后面的字符串进行编码
                String r = sr.substring(0, sr.indexOf("=") + 1);
                String s = sr.substring(sr.indexOf("=") + 1, sr.length());
                try {
                    s = java.net.URLEncoder.encode(s, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                sb.append(r);
                sb.append(s);
                sb.append("&");
            }

            // 去掉最后一个&
            if (str.length > 0) {
                sb.delete(sb.toString().length() - 1, sb.toString().length());
            }
        } else {
            String part1 = url != null ? url.substring(0, (url != null ? url.lastIndexOf("/") : 0) + 1) : null;
            String part2 = url.substring(url.lastIndexOf("/") + 1);
            Log.d("part1 " , part1);
            Log.d("part2 " , part2);
            try {
                url = part1 + java.net.URLEncoder.encode(part2, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return url;
        }
        return sb.toString();
    }

    /**
     * 获取User-Agent并设置给Okhttp
     * @return
     */
    private static String getUserAgent() {
        String userAgent ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(MyApplication.getContext());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void onStartActivity(){
        //如果已经是退出状态则不调跳转登录界面
        if(!PreferencesUtils.getSharePreBoolean(MyApplication.getContext(),Const.LOGIN_STATE)){
            return;
        }
        //退出XMpp
        if(MyApplication.xmppConnection!=null ){
            if(MyApplication.xmppConnection.isAuthenticated()){
                MyApplication.xmppConnection.disconnect();
                MyApplication.xmppConnection=null;
                MyApplication.groupsList=null;
                MyApplication.list=null;

            }
        }
        //设置登录状态
        PreferencesUtils.putSharePre(MyApplication.getContext(),Const.LOGIN_STATE,false);
        Intent intent=new Intent(MyApplication.getContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.putExtra(Const.IS_TOKEN_LOST,true);
        MyApplication.getContext().startActivity(intent);
        MyJPushUitl.stopPush(MyApplication.getContext());
        MyJPushUitl.setClearNotification(MyApplication.getContext());
        ActiveActUtil.getInstance().exit();
    }

    public static void onStartMainActivity(){
        //如果已经是退出状态则不调跳转登录界面
        if(!PreferencesUtils.getSharePreBoolean(MyApplication.getContext(),Const.LOGIN_STATE)){
            return;
        }
        //退出XMpp
        if(MyApplication.xmppConnection!=null ){
            if(MyApplication.xmppConnection.isAuthenticated()){
                MyApplication.xmppConnection.disconnect();
                MyApplication.xmppConnection=null;
                MyApplication.groupsList=null;
                MyApplication.list=null;

            }
        }
        //设置登录状态
        PreferencesUtils.putSharePre(MyApplication.getContext(),Const.LOGIN_STATE,false);
        Intent intent=new Intent(MyApplication.getContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.putExtra(Const.IS_TOKEN_LOST,true);
        MyApplication.getContext().startActivity(intent);
        MyJPushUitl.stopPush(MyApplication.getContext());
        MyJPushUitl.setClearNotification(MyApplication.getContext());
        ActiveActUtil.getInstance().exit();
    }

}
