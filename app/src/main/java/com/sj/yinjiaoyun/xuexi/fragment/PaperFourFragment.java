package com.sj.yinjiaoyun.xuexi.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.MainActivity;
import com.sj.yinjiaoyun.xuexi.utils.ImageUtil;


/**
 * Created by Administrator on 2016/5/30 0030.
 * 引导页面：第四个
 */
public class PaperFourFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.what_new_four,container,false);
       // ImageView iv= (ImageView) view.findViewById(R.id.guide_button);
        ImageView ivBg= (ImageView) view.findViewById(R.id.guide_bg_d);
        ImageUtil.viewByBackground(ivBg, R.mipmap.guide_d);
        ivBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }
}
