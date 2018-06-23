package com.sj.yinjiaoyun.xuexi.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.sj.yinjiaoyun.xuexi.R;
import com.squareup.picasso.Picasso;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/7.
 * 网络图片加载例子,首页轮播图框架的使用工具类
 */
public class ImageHolderNetworkView implements Holder<String> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        if(data.equals("1")){
            Picasso.with(context).load(R.mipmap.banner1).error(R.mipmap.error).into(imageView);
        }else{
            Picasso.with(context).load(data).error(R.mipmap.error).into(imageView);
        }

    }
}
