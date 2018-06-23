package com.sj.yinjiaoyun.xuexi.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.utils.DensityUtils;

/**
 * Created by wanzhiying on 2017/3/9.
 */
public class GrayDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = DensityUtils.dp2px(parent.getContext(),1);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Drawable divider = parent.getContext().getResources().getDrawable(R.drawable.shape_bg_gray);
        for (int i = 0; i < parent.getChildCount();i++){
            View child = parent.getChildAt(i);
            int left = child.getLeft();
            int right = child.getRight();
            int top = child.getBottom();
            int bottom = child.getBottom() + parent.getLayoutManager().getBottomDecorationHeight(child);
            if(divider!=null){
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }
}
