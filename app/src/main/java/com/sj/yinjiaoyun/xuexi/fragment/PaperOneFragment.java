package com.sj.yinjiaoyun.xuexi.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.utils.ImageUtil;


/**
 * Created by Administrator on 2016/5/30 0030.
 * 引导页面：第一个
 */
public class PaperOneFragment extends Fragment {

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.what_new_one,container,false);
        init();
        return view;
    }
    private void init(){
        ImageView iv= (ImageView) view.findViewById(R.id.guide_bg_a);
        ImageUtil.viewByBackground(iv, R.mipmap.guide_a);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    private void setWait(){
         new Thread(){
             @Override
             public void run() {
                 super.run();
                 try {
                     sleep(2000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }.start();
    }


}
