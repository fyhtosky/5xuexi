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
 * 引导页面：第三个
 */
public class PaperThreeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.what_new_three,container,false);
        ImageView ivBg= (ImageView) view.findViewById(R.id.guide_bg_c);
        ImageUtil.viewByBackground(ivBg, R.mipmap.guide_c);
        return view;
       // return inflater.inflate(R.layout.what_new_three,container,false);
    }
}
