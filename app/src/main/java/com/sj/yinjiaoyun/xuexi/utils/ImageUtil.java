package com.sj.yinjiaoyun.xuexi.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/25.
 * 操作图片的工具类
 */
public class ImageUtil {

    private static final String TAG = "ImageUtils";

    /**
     * 给图片设置src

     * @param imageView

     * @param resId 资源id
     */

    public static void imageViewBySrc(ImageView imageView, int resId){
        if(imageView==null) return;

        BitmapDrawable bitmap = getResourcesImg(imageView.getContext(),resId);

        if(bitmap!=null){
            imageView.setImageDrawable(bitmap);
        }
    }


    /**
     * 给图片设置src

     * @param imageView

     * @param bitmap

     */

    public static void imageViewBySrc(ImageView imageView,BitmapDrawable bitmap){

        if(imageView==null||bitmap==null)return;


        imageView.setImageDrawable(bitmap);

    }


    /**
     * 给图片设置src

     * @param imageView

     * @param defaultResId

     * @param selectedResId

     * @param pressedResId

     * @param checkedResId

     */

    public static void imageViewBySrc(ImageView imageView,int defaultResId,int selectedResId,int pressedResId,int checkedResId){

        if(imageView==null)   return;


        StateListDrawable stateListDrawable = getStateListDrawable(imageView.getContext(),defaultResId,selectedResId,pressedResId,checkedResId);
        if(stateListDrawable!=null){
            imageView.setImageDrawable(stateListDrawable);
        }
    }

    /**
     * 获取点击效果的时候的图片
     * @param context
     * @param defaultResId
     * @param selectedResId
     * @param pressedResId
     * @param checkedResId
     * @return
     */
    public static StateListDrawable getStateListDrawable(Context context, int defaultResId, int selectedResId, int pressedResId, int checkedResId){
        StateListDrawable drawable = new StateListDrawable();
        if(defaultResId!=0&&defaultResId!=-1){
            drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                    getResourcesImg(context,defaultResId));
        }
        if(selectedResId!=0&&selectedResId!=-1){
            drawable.addState(new int[]{android.R.attr.state_selected}, getResourcesImg(context,selectedResId));
        }
        if(pressedResId!=0&&pressedResId!=-1){
            drawable.addState(new int[]{android.R.attr.state_pressed}, getResourcesImg(context,pressedResId));

        }

        if(checkedResId!=0&&checkedResId!=-1){

            drawable.addState(new int[]{android.R.attr.state_checked}, getResourcesImg(context,checkedResId));

        }

        return drawable;
    }




    /**
     * 给图片设置background
     * @param view
     * @param checkedResId 资源id
     */
    @SuppressLint("NewApi")
    public static void viewByBackground(View view, int defaultResId, int selectedResId, int pressedResId, int checkedResId){
        if(view==null)return;

        StateListDrawable stateListDrawable = getStateListDrawable(view.getContext(),defaultResId,selectedResId,pressedResId,checkedResId);
        if(stateListDrawable!=null){
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN){
                view.setBackground(stateListDrawable);
            }else{
                view.setBackgroundDrawable(stateListDrawable);
            }
        }
    }

    /**
     * 给图片设置background
     * @param view
     * @param resId 资源id
     */
    @SuppressLint("NewApi")
    public static void viewByBackground(View view,int resId){
        if(view==null)return;

        BitmapDrawable bitmap = getResourcesImg(view.getContext(),resId);
        if(bitmap!=null){
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN){
                view.setBackground(bitmap);
            }else{
                view.setBackgroundDrawable(bitmap);
            }
        }
    }

    /**
     * 设置背景
     * @param view
     * @param bitmap
     */
    @SuppressLint("NewApi")
    public static void viewByBackground(View view,BitmapDrawable bitmap){
        if(view==null||bitmap==null)return;

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN){
            view.setBackground(bitmap);
        }else{
            view.setBackgroundDrawable(bitmap);
        }
    }

    /**
     * 获取资源图片
     * @param context
     * @param resId
     * @return
     */
    public static BitmapDrawable getResourcesImg(Context context,int resId){
        try{
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;	//要比888还要小
            /**
             * inPurgeable 设为True的话表示使用BitmapFactory创建的Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收，
             * 在应用需要再次访问Bitmap的Pixel时（如绘制Bitmap或是调用getPixel），
             * 系统会再次调用BitmapFactory decoder重新生成Bitmap的Pixel数组。
             * 为了能够重新解码图像，bitmap要能够访问存储Bitmap的原始数据。
             * inPurgeable 是控制 Bitmap 对象是否使用软引用机制, 在系统需要的时候可以回收该对象, 如果在此后, 程序又需要使用该对象, 则系统重新 decode 该对象.
             */
            opt.inPurgeable = true;
            /**
             * inInputShareable  和 inPurgeable 组合使用的,
             * 是控制是否复制 inputfile 对象的引用, 如果不复制,
             * 那么要实现 inPurgeable 机制就需要复制一份 file 数据,
             * 才能在系统需要 decode 的时候创建一个 bitmap 对象.
             */
            opt.inInputShareable = true;

            InputStream is = context.getResources().openRawResource(resId);
            /**
             * decodeStream无论是时间上还是空间上，都比decodeResource方法更优秀
             */
            Bitmap bitmap = BitmapFactory.decodeStream(is,null, opt);
            is.close();

            return new BitmapDrawable(context.getResources(),bitmap);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    /**
     * Stores an image on the storage
     *
     * @param image
     *            the image to store.
     * @param pictureFile
     *            the file in which it must be stored
     */
    public static void storeImage(Bitmap image, File pictureFile) {
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    public static Bitmap readBitmap(String file) {
        return BitmapFactory.decodeFile(file);
    }


}
