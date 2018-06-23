package com.sj.yinjiaoyun.xuexi.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.ImageBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImgPageActivity extends AppCompatActivity {


    @BindView(R.id.vp)
    ViewPager vp;
    private Long id;
    private int index=0;
    private ArrayList<ImageBean> imageList;
    private List<ImageView> views;
    private ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_page);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        views = new ArrayList<>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getLong("id");
            imageList = (ArrayList<ImageBean>) bundle.getSerializable("imageList");

        }
        if (imageList!=null && imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                if(id.longValue()==imageList.get(i).getId().longValue()){
                    index=i;
                }
                final ImageView iv = new ImageView(this);
                iv.setLayoutParams(mParams);
                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.with(ImgPageActivity.this).load(imageList.get(i).getUrl()).placeholder(R.drawable.ic_photo_loading).error(R.mipmap.ic_default_adimage).into(iv);
//                 PhotoUtils.decodeUriAsBitmapFromNet(imageList.get(i).getUrl(), new CallBack<Bitmap>() {
//                    @Override
//                    public void onSuccess(Bitmap result) {
//                        Bitmap bitmap =  result;
//                        iv.setImageBitmap(bitmap);
//                    }
//                });

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                views.add(iv);
            }
            Logger.d("imageList数量"+imageList.size()+imageList.toString()+"时间："+id+"下标："+index);
            pagerAdapter=new ViewPagerAdapter(views);
            vp.setAdapter(pagerAdapter);
            vp.setCurrentItem(index);
        }

    }



     class ViewPagerAdapter extends PagerAdapter {

        //界面列表
        private List<ImageView> views;

        public ViewPagerAdapter(List<ImageView> views) {
            this.views = views;
        }

        //销毁arg1位置的界面
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        //获得当前界面数
        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }

            return 0;
        }


        //初始化arg1位置的界面
        @Override
        public Object instantiateItem(View arg0, int arg1) {

            ((ViewPager) arg0).addView(views.get(arg1), 0);

            return views.get(arg1);
        }

        //判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }


}
