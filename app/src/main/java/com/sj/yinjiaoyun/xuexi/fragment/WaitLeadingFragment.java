package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sj.yinjiaoyun.xuexi.R;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/11.
 * 正在缓冲时候的fragment
 */
public class WaitLeadingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leading,container,false);
    }
}
