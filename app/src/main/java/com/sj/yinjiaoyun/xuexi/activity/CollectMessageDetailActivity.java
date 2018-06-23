package com.sj.yinjiaoyun.xuexi.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.NotifyEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.bean.CollectMessageBean;
import com.sj.yinjiaoyun.xuexi.utils.ExpressionUtil;
import com.sj.yinjiaoyun.xuexi.utils.MyPopWindows;
import com.sj.yinjiaoyun.xuexi.utils.TimeRender;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectMessageDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_couse_type)
    TextView tvCouseType;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.tv_collect_time)
    TextView tvCollectTime;
    @BindView(R.id.ll_CollectMessageDetailActivity)
    LinearLayout llCollectMessageDetailActivity;
    private CollectMessageBean.DataBean.MsgEnshrineBean.RowsBean rowsBean;
    private String rgx = "*$img_";
    private MyPopWindows popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_message_detail);
        //注册EventBus
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        popupWindow=new MyPopWindows(CollectMessageDetailActivity.this,CollectMessageDetailActivity.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            rowsBean = (CollectMessageBean.DataBean.MsgEnshrineBean.RowsBean) bundle.getSerializable("rowbean");
            if(rowsBean==null){
                return;
            }
            Logger.d("接受的实体类：" + rowsBean.toString());
            if (rowsBean.getSenderLogo() != null && !TextUtils.isEmpty(rowsBean.getSenderLogo())) {
                Picasso.with(CollectMessageDetailActivity.this).load(rowsBean.getSenderLogo()).placeholder(R.drawable.progressbar_landing).error(R.mipmap.ic_default_adimage).into(ivIcon);
            } else {
                ivIcon.setImageResource(R.mipmap.default_userhead);
            }
            //私聊
            if (rowsBean.getMsgType() == 0) {
                tvName.setText(rowsBean.getSenderName());
                tvCouseType.setVisibility(View.GONE);
            } else {
                tvCouseType.setVisibility(View.VISIBLE);
                tvName.setText(rowsBean.getSenderName() + "-" + rowsBean.getBusinessName());
                //父节点
                if (0 == rowsBean.getIsLef()) {
                    // //招生专业类型
                    if (0 == rowsBean.getType()) {
                        if (0 == rowsBean.getProductType()) {
                            tvCouseType.setText(rowsBean.getEnrollPlanName() + "  网教");
                        } else if (1 == rowsBean.getProductType()) {
                            tvCouseType.setText(rowsBean.getEnrollPlanName() + "  成教");
                        } else if (2 == rowsBean.getProductType()) {
                            tvCouseType.setText(rowsBean.getEnrollPlanName() + "  自考");
                        } else if (3 == rowsBean.getProductType()) {
                            tvCouseType.setText(rowsBean.getEnrollPlanName() + "  培训");
                        } else if (4 == rowsBean.getProductType()) {
                            tvCouseType.setText(rowsBean.getEnrollPlanName() + "  考证");
                        } else if (5 == rowsBean.getProductType()) {
                            tvCouseType.setText(rowsBean.getEnrollPlanName() + "  统招");
                        }
                        //公开课类型课程类型
                    } else if (1 == rowsBean.getType()) {
                        if (0 == rowsBean.getOpenCourseType()) {
                            tvCouseType.setText(rowsBean.getBusinessName() + "  免费");
                        } else if (1 == rowsBean.getOpenCourseType()) {
                            tvCouseType.setText(rowsBean.getBusinessName() + "  付费");
                        }
                        //微专业类型
                    } else if (2 == rowsBean.getType()) {
                        if (0 == rowsBean.getTrainingType()) {
                            tvCouseType.setText( "随到随学");
                        } else if (1 == rowsBean.getTrainingType()) {
                            tvCouseType.setText( "定期开课");
                        }
                    }

                } else {
                    //子节点
                    tvCouseType.setText("所属专业：" + rowsBean.getParentName());
                }
            }


            if (rowsBean.getMsgContent() != null) {
                //图片类型
                if (rowsBean.getMsgContent().startsWith("*$img_") && rowsBean.getMsgContent().endsWith("_gmi$*") ) {
                    tvContent.setVisibility(View.GONE);
                    ivPicture.setVisibility(View.VISIBLE);
                    final String url = rowsBean.getMsgContent().
                            substring(rgx.length(), rowsBean.getMsgContent().length() - rgx.length());
                    if (url != null) {
                        Picasso.with(CollectMessageDetailActivity.this).load(url).placeholder(R.drawable.progressbar_landing).into(ivPicture);
                    } else {
                        ivPicture.setImageResource(R.mipmap.logo);
                    }
                } else {
                    tvContent.setVisibility(View.VISIBLE);
                    ivPicture.setVisibility(View.GONE);
                    //@功能
                    if (rowsBean.getMsgContent().contains("_!@_")) {
                        String content = ExpressionUtil.RecursiveQuery(CollectMessageDetailActivity.this, rowsBean.getMsgContent(), 0);
                        tvContent.setText(content);
                    } else {
                        //对内容做处理
                        tvContent.setText(ExpressionUtil.prase(CollectMessageDetailActivity.this, tvContent, rowsBean.getMsgContent()));
                    }
                }
            }
            //设置时间
            tvCollectTime.setText("收藏于" + TimeRender.getFormat(rowsBean.getUpdateTime()));
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyEvent notifyEvent){
        finish();
    }
    @OnClick({R.id.iv_back, R.id.iv_detel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_detel:
               popupWindow.pop(llCollectMessageDetailActivity,rowsBean,Gravity.BOTTOM);
                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }
}
