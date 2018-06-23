package com.sj.yinjiaoyun.xuexi.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.LruCache;

import com.sj.yinjiaoyun.xuexi.app.MyApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wanzhiying on 2017/3/17.
 */
public class BitmapUtils {
    public LruCache<String, Bitmap> mMemoryCache;
    public  DiskLruCache mDiskLruCache = null;
    private static BitmapUtils mInstance;
    public BitmapUtils() {
       init();
    }

    private void init() {
        initMemoryCache();
        initDiskCache();
    }

    public static synchronized BitmapUtils getInstance() {
        if(mInstance == null) {
            mInstance = new BitmapUtils();
        }
        return mInstance;
    }
    /**
     * 初始化内存缓存
     */
    public void initMemoryCache() {

        // Set up memory cache
        if (mMemoryCache != null) {
            try {
                clearMemoryCache();
            } catch (Throwable e) {
                e.getLocalizedMessage();
            }
        }
        // find the max memory size of the system
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                if (bitmap == null) return 0;
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }
    /**
     * 初始化磁盘缓存
     */
    private void initDiskCache() {
        try {
            File cacheDir = OtherUtils.getDiskCacheDir(MyApplication.getContext(), "images");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, OtherUtils.getAppVersion(MyApplication.getContext()),
                    1, 15 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空内存缓存
     */
    public void clearMemoryCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }


    public Bitmap display(String path) {
        if (TextUtils.isEmpty(path) ) {
            throw new IllegalArgumentException("args may not be null");
        }
        return decodeSampledBitmapFromFile(path,400,600);
    }


    /**
     * 根据计算的inSampleSize，得到压缩后图片
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }
    /**
     * 计算inSampleSize，用于压缩图片
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的宽度
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        // int min = Math.min(width, height);
        // int maxReq = Math.max(reqWidth, reqHeight);

        // if(min > maxReq) {
        //     inSampleSize = Math.round((float) min / (float) maxReq);
        // }

        // 通过之前的计算方法，在加载类似400*4000这种长图时会内存溢出
        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);

            inSampleSize = Math.max(widthRadio, heightRadio);
        }

        return inSampleSize;
    }

    /**
     * 解析准确尺寸的图片
     * @param path
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap decodeScaleImage(String path, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        if (options.outWidth <= 0 || options.outHeight <= 0) {
            // 保护策略，防止下面出现崩溃
            return bitmap;
        }
        Bitmap decodeBitmap = createScaledBitmap(bitmap, options.outWidth, options.outHeight
                , maxWidth, maxHeight);
        int degree = readPictureDegree(path);
        if (decodeBitmap != null && degree != 0) {
            bitmap = rotateBitmap(degree, decodeBitmap);
            decodeBitmap.recycle();
            return bitmap;
        } else {
            return decodeBitmap;
        }
    }


    /**
     * 图片裁剪
     * @param bitmap
     * @param width
     * @param height
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap createScaledBitmap(Bitmap bitmap, int width, int height
            , float maxWidth, float maxHeight) {
        Matrix matrix = new Matrix();
        float scale = calculateScale(width, height, maxWidth, maxHeight);
        matrix.postScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0
                , width, height, matrix, false);
        return newBitmap;
    }

    /**
     * 设置裁剪的比例
     * @param outWidth
     * @param outHeight
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static float calculateScale(int outWidth, int outHeight, float maxWidth, float maxHeight) {
        float ratioH = maxHeight / (float) outHeight;
        float ratioW = maxWidth / (float) outWidth;
        float scale = ratioH < ratioW ? ratioH : ratioW;
        return scale;
    }
    public static int readPictureDegree(String path) {
        short degree = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt("Orientation", 1);
            switch (orientation) {
                case 3:
                    degree = 180;
                case 4:
                case 5:
                case 7:
                default:
                    break;
                case 6:
                    degree = 90;
                    break;
                case 8:
                    degree = 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }
    public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
