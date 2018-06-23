package com.sj.yinjiaoyun.xuexi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.entry.Photo;
import com.sj.yinjiaoyun.xuexi.utils.ImageLoader;
import com.sj.yinjiaoyun.xuexi.utils.OtherUtils;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/5/15.
 */
public class SinglePhotoAdapter extends BaseAdapter {
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_PHOTO = 1;

    private List<Photo> mDatas;
    private Context mContext;
    private int mWidth;
    private boolean mIsShowCamera=false;


    public SinglePhotoAdapter(Context context, List<Photo> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
        int screenWidth = OtherUtils.getWidthInPx(mContext);
        mWidth = (screenWidth - OtherUtils.dip2px(mContext, 4))/3;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    public void setIsShowCamera(boolean isShowCamera) {
        this.mIsShowCamera = isShowCamera;
        if (mIsShowCamera) {
            Photo camera = new Photo(null);
            camera.setIsCamera(true);
            mDatas.add(0, camera);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(position==0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PHOTO;
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Photo getItem(int position) {
        if(mDatas == null || mDatas.size() == 0){
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDatas.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == TYPE_CAMERA) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_camera_layout, parent,false);
            convertView.setTag(null);
            //设置高度等于宽度
            GridView.LayoutParams lp = new GridView.LayoutParams(mWidth, mWidth);
            convertView.setLayoutParams(lp);
        } else {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_photo_layout, parent,false);
                holder.photoImageView = (ImageView) convertView.findViewById(R.id.imageview_photo);
                holder.selectView = (ImageView) convertView.findViewById(R.id.checkmark);
                holder.maskView = convertView.findViewById(R.id.mask);
                holder.wrapLayout = (FrameLayout) convertView.findViewById(R.id.wrap_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.photoImageView.setImageResource(R.drawable.ic_photo_loading);
            Photo photo = getItem(position);
            holder.selectView.setVisibility(View.INVISIBLE);
            holder.photoImageView.setTag(photo.getPath());
            ImageLoader.getInstance().display(photo.getPath(), holder.photoImageView,
                    mWidth, mWidth);

        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView photoImageView;
        private ImageView selectView;
        private View maskView;
        private FrameLayout wrapLayout;
    }


}
