package com.sj.yinjiaoyun.xuexi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/25.
 */
public class NetworkImageGetter implements Html.ImageGetter {
    private static final String TAG ="NetworkImageGetter" ;
    private String picName;
    TextView tvText;
    Drawable defaultDrawable;
    private BitmapFactory.Options options ;
//    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean isOriginalShow=false;


    public NetworkImageGetter(Context context, TextView tvText) {
        this.tvText = tvText;
        defaultDrawable=context.getResources().getDrawable(R.mipmap.logo);
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
    }
    public NetworkImageGetter(Context context) {
        defaultDrawable=context.getResources().getDrawable(R.mipmap.logo);
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
    }

    public void setTvText(TextView tvText) {
        this.tvText = tvText;
    }

    public void setOriginalShow(boolean originalShow) {
        isOriginalShow = originalShow;
    }

    @Override
    public Drawable getDrawable(String source) {
        source=source.substring(source.indexOf("http"),source.length());
        // 封装路径
        picName= FileUtils.getFileName(source);
        File file = new File(Environment.getExternalStorageDirectory(), picName);
        // 判断是否以http开头
        if(source.startsWith("http")) {
            // 判断路径是否存在
            if(file.exists()) {
                // 存在即获取drawable
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
               Drawable  locaDrawable = new BitmapDrawable(bitmap);
                if(isOriginalShow){
                    locaDrawable.setBounds(0, 0, locaDrawable.getIntrinsicWidth(), locaDrawable.getIntrinsicHeight());
                }else {
                    locaDrawable.setBounds(0, 0, 60, 60);
                }
                return locaDrawable;
            } else {
//                 不存在即开启异步任务加载网络图片
                URLDrawable urlDrawable = new URLDrawable(defaultDrawable,isOriginalShow);
                AsyncLoadNetworkPic networkPic = new AsyncLoadNetworkPic(isOriginalShow,urlDrawable);
                networkPic.execute(source);
//                networkPic.executeOnExecutor(executorService, source);
                return  urlDrawable;

            }
        }
        return defaultDrawable;
    }

    /**
     * 加载网络图片异步类
     * @author Susie
     */
    private final class AsyncLoadNetworkPic extends AsyncTask<String, Integer, Drawable> {
        private URLDrawable _drawable;
        private boolean isOriginalShow;

        public AsyncLoadNetworkPic(boolean isOriginalShow, URLDrawable _drawable) {
            this.isOriginalShow = isOriginalShow;
            this._drawable = _drawable;
        }

        public AsyncLoadNetworkPic(URLDrawable _drawable) {
            this._drawable = _drawable;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            // 加载网络图片
            loadNetPic(params);
            String source = params[0];
            Drawable drawable;
            try {
                Logger.d("url下载流："+source);
                Bitmap bitmap = BitmapFactory.decodeStream(new URL(source).openStream(), null, options);
                drawable = new BitmapDrawable(bitmap);
                if(!this.isOriginalShow){
                   drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                }else {
                   drawable.setBounds(0, 0, 60, 60);
                }
                return  drawable;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if(result!=null){
                _drawable.setDrawable(result);
                // 当执行完成后再次为其设置一次
                tvText.setText(tvText.getText());
//                executorService.shutdown();
            }

        }
        /**加载网络图片*/
        private void loadNetPic(String... params) {
            String path = params[0];

            File file = new File(Environment.getExternalStorageDirectory(), picName);

            InputStream in = null;

            FileOutputStream out = null;

            try {
                URL url = new URL(path);

                HttpURLConnection connUrl = (HttpURLConnection) url.openConnection();

                connUrl.setConnectTimeout(5000);

                connUrl.setRequestMethod("GET");

                if(connUrl.getResponseCode() == 200) {

                    in = connUrl.getInputStream();

                    out = new FileOutputStream(file);

                    byte[] buffer = new byte[1024];

                    int len;

                    while((len = in.read(buffer))!= -1){
                        out.write(buffer, 0, len);
                    }
                } else {
                    Log.i(TAG, connUrl.getResponseCode() + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if(in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public class URLDrawable extends BitmapDrawable {
        private Drawable drawable;
        private boolean isOriginalShow;
        public URLDrawable(Drawable defaultDraw,boolean isOriginalShow){
            this.isOriginalShow=isOriginalShow;
            setDrawable(defaultDraw);
        }
        public URLDrawable(Drawable defaultDraw){
            setDrawable(defaultDraw);
        }
        private void setDrawable(Drawable ndrawable){
            drawable = ndrawable;
            if(this.isOriginalShow){
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }else {
                drawable.setBounds(0, 0, 60, 60);
                setBounds(0,0,60,60);
            }
        }
        @Override
        public void draw(Canvas canvas) {
            drawable.draw(canvas);
        }
    }
}