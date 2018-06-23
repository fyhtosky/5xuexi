package com.sj.yinjiaoyun.xuexi.utils;

import android.content.Context;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;

import com.sj.yinjiaoyun.xuexi.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by wanzhiying on 2017/3/9.
 * Picasso封装加载圆角图片的工具类
 */
public class PicassoUtils {
    static Transformation transformation = new RoundedTransformationBuilder()
            .borderColor(R.color.colorWrite)
            //.borderWidthDp(0.1f)
            .cornerRadiusDp(2)
            .oval(false)
            .build();
    public static void LoadPathCorners(Context context , String path, ImageView imageView){
        Picasso.with(context).load(path).resize(75,75).centerCrop().transform(transformation).into(imageView);

    }
    public static void LoadPathCorners(Context context , String path,int width,int height, ImageView imageView){
        Picasso.with(context).load(path).resize(width,height).centerCrop().transform(transformation).into(imageView);

    }
    public static void LoadCorners(Context context , int  id, ImageView imageView){
        Picasso.with(context).load(id).resize(75,75).centerCrop().transform(transformation).into(imageView);

    }
    public static void LoadCorners(Context context , int  id,int width,int height ,ImageView imageView){
        Picasso.with(context).load(id).resize(width,height).centerCrop().transform(transformation).into(imageView);

    }
}
