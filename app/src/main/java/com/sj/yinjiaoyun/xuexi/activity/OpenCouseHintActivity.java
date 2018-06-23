package com.sj.yinjiaoyun.xuexi.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.Event.ViedoCompleteEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.FragmentAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.OrderReturnBean;
import com.sj.yinjiaoyun.xuexi.bean.VideoAddressBean;
import com.sj.yinjiaoyun.xuexi.domain.Coursewares;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domains.CouseFuse;
import com.sj.yinjiaoyun.xuexi.fragment.EvaluateFragment;
import com.sj.yinjiaoyun.xuexi.fragment.OpenDirectoryHintFragment;
import com.sj.yinjiaoyun.xuexi.fragment.OpenIntroFragment;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.CXAESUtil;
import com.sj.yinjiaoyun.xuexi.utils.CheckNetworkConnect;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.TimeCutTamp;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.view.PLView.PLPlayView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.squareup.picasso.Picasso;
import com.universalvideoview.UniversalVideoView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/19.
 * 公开课 预览页面
 * 订完未完成下公开课预览界面
 */
public class OpenCouseHintActivity extends PlayBaseActivity implements UniversalVideoView.VideoViewCallback,
        HttpDemo.HttpCallBack, OpenDirectoryHintFragment.OpenDirectoryCallBack, OpenIntroFragment.OpenIntroHttpCallBack {

    /////////////////////////////视频控件的设置/////////////////////////////////////////////////////
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static int mSeekPosition;//当前视屏播放的当前位置
    String TAG = "opencouse";
    Long limitPosition = 300000L;//视屏免费播放时间
    Long timeInterval = 30000L;//检查视屏是否播放到限制位子时间间隔
    Coursewares wares;//课程详情-章节课时列表及学习情况
    CouseFuse couseFuse;
    String courseScheduleId = "";//课程表id
    Intent intent;
    int orderStatus;//定单的状况 104 订单完成      101 订单未完成  120 订单不存在
    String orderCode;//101状态订单未完成的订单号
    LayoutInflater inflater;
    ViewPager viewPager;
    RadioGroup group;//简介 目录 评论
    TextView openGoTo;//立即参加
    FragmentAdapter fragmentAdapter;
    List<Fragment> listFragment;
    OpenIntroFragment openIntroFragment;//简介
    OpenDirectoryHintFragment openDirectoryFragment;//目录
    EvaluateFragment openEvaluateFragment;//评论
    View flashContainer;//下划线的父
    HttpDemo demo;
    List<Pairs> pairsList;//网络请求时候传递参数
    String productid;//解析人大视频url解析出来的变量
    String videoCode;//解析人大视频url解析出来的变量
    //    UniversalVideoView mVideoView;//视频播放控件
//    UniversalMediaController mMediaController;//视频控制控件
    ImageView mCourseIcon;//视屏没播放时的标题
    View mBottomLayout;//非视频播放纵容其的其他的所有的控件容器
    View mVideoLayout;// <!--视屏播放器总的容器-->
    long totalTime;//Directoryitem项里面成绩的中总时长
    AlertDialog dialog;//免费公开课提示
    String openCourseId;
    RadioButton rb1;
    RadioButton rb2;
    boolean isDialogShow = true;
    Message pullMsg;
    Boolean isLimitTime = true;//需要时间限制
    private String endUserId;//用户id
    private String VIDEO_URL;//视频播放地址
    private Boolean isTotalPaly = false;//当前视屏播放的总时间段
    private long totalPlayTime = 0;//记录用户观看视屏用了多长时间
    private int cachedHeight;
    private boolean isFullscreen;//是否是满屏
    private boolean loginState;
    private int isAudit;   //是否需要審核表示
    ////////////////////////////////视频的操作///////////////////////////////////////////////////////////////
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long position = (long) msg.obj;
            // zhangcz
            Log.i(TAG, "handleMessage: " + position);
            if (position > limitPosition) {
                isLimitTime = false;
                mVideoView.pausePlay();
//                mMediaController.setEnabled(false);
                setDialog();
            } else {
//                mMediaController.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveActUtil.getInstance().addActivity(this);
        init();
        initView();
        initEventView();
        initEventVideo();

    }

    @Override
    protected int layoutID() {
        return R.layout.layout_opencouse_hint;
    }

    //////////////////////Activity的生命周期////////////////////////////////////////////////////////////
    //数据准备
    private void init() {
        inflater = LayoutInflater.from(this);
        intent = getIntent();
        demo = new HttpDemo(this);
        endUserId = PreferencesUtils.getSharePreStr(OpenCouseHintActivity.this, "username");
        loginState = PreferencesUtils.getSharePreBoolean(OpenCouseHintActivity.this, Const.LOGIN_STATE);
        couseFuse = intent.getParcelableExtra("CouseFuse");
        courseScheduleId = intent.getStringExtra("CourseScheduleId");
        orderStatus = intent.getIntExtra("orderStatus", 0);
        isAudit = intent.getIntExtra("isAudit", 0);
        Log.i(TAG, "进入预览页面：");
        Log.i(TAG, "onCreate:用户id： " + endUserId);
        Log.i(TAG, "onCreate:专业信息： " + couseFuse.toString());
        Log.i(TAG, "init: 订单状况" + orderStatus);
        if (orderStatus == 101 || orderStatus == 100 || orderStatus == 109) {//101 订单存在已付款等待审核，100订单存在未付款，109订单存在查实取消状态
            orderCode = intent.getStringExtra("orderCode");
            Log.i(TAG, "setGoToPayOrBug: 订单号" + orderCode);
        }
    }

    private void initView() {
        openGoTo = (TextView) findViewById(R.id.opencourse_hint_goto);
        flashContainer = findViewById(R.id.opencouse_underLineC);
        group = (RadioGroup) findViewById(R.id.open_item_head_group);
        rb1 = (RadioButton) findViewById(R.id.open_rb1);
        rb2 = (RadioButton) findViewById(R.id.open_rb2);
        viewPager = (ViewPager) findViewById(R.id.item_opencourse_viewpager);
        mCourseIcon = (ImageView) findViewById(R.id.open_Image);
        mVideoLayout = findViewById(R.id.open_video_layout);
        mBottomLayout = findViewById(R.id.open_bottom_layout);
//        mVideoView = (UniversalVideoView) findViewById(R.id.open_videoView);
//        mMediaController = (UniversalMediaController) findViewById(R.id.open_media_controller);
        setUnderLine();//设置下划线
        //;

        //未登录点击跳转界面如果登录则给对话框提示
        openGoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginState) {
                    startActivity(new Intent(OpenCouseHintActivity.this, LoginActivity.class));
                } else {
                    setGoToPayOrBug();
                }

            }
        });
        try {
            if (couseFuse.getCourseLogo().equals("")) {
                Picasso.with(this).load(R.mipmap.error).resize(350, 200).into(mCourseIcon);
            } else {
                Picasso.with(this).load(couseFuse.getCourseLogo()).resize(350, 200).into(mCourseIcon);
            }
            if (!loginState) {
                openGoTo.setText("点击登录");
                return;
            }
            if (couseFuse.getIsFree() == 1) {//免费
                openGoTo.setText("立即参加");
                openGoTo.setClickable(true);
                if (orderStatus == 100) {//100订单存在未付款状态
                    openGoTo.setText("查看订单");
                    openGoTo.setClickable(false);
                } else if (orderStatus == 101) {//101 订单存在已付款等待审核状态
                    openGoTo.setText("审核中");
                    openGoTo.setClickable(false);
                } else if (orderStatus == 120 || orderStatus == 109) {//120订单不存在  109订单已取消   （此种情况要冲洗创建订单号）
                    openGoTo.setText("立即参加");
                    openGoTo.setClickable(true);
                } else if (orderStatus == 101 || orderStatus == 102) {
                    openGoTo.setText("审核中");
                    openGoTo.setClickable(false);
                }
            } else {//付费
                if (orderStatus == 100) {//100订单存在未付款状态
                    openGoTo.setText("去付款");
                    openGoTo.setClickable(false);
                } else if (orderStatus == 101) {//101 订单存在已付款等待审核状态
                    openGoTo.setText("审核中");
                    openGoTo.setClickable(false);
                } else if (orderStatus == 120 || orderStatus == 109) {//120订单不存在  109订单已取消   （此种情况要冲洗创建订单号）
                    openGoTo.setText("立即购买");
                    openGoTo.setClickable(true);
                } else if (orderStatus == 101 || orderStatus == 102) {
                    openGoTo.setText("审核中");
                    openGoTo.setClickable(false);
                }
            }
        } catch (Exception e) {
            Log.i(TAG, "initView: " + e.toString());
        }
    }


    /**
     * 如果公开课免费 dialog提示免费可以观看   如果公开课付费 分两种情况：
     * 1 没有创建订单      没有付款
     * 2 已创建有订单      没有付款
     */
    public void setGoToPayOrBug() {
        Log.i(TAG, "setGoToPayOrBug: ");
        if (couseFuse.getIsFree() == 1) { //免费公开课，可以直接学习
            if (isAudit == 1) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("该公开课需要审核")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setApplyNameToHttp();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
            } else {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("该公开课免费")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setApplyNameToHttp();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        } else {
            if (isAudit == 1) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("该公开课需要审核")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                createOrder();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
            } else {
                createOrder();
            }

        }
    }

    /**
     * 创建订单
     */
    private void createOrder() {
        if (orderStatus == 100) {//100订单存在未付款状态
            if (!TextUtils.isEmpty(orderCode)) {
                Intent inte = new Intent(this, ConfirmOpenOrderActivity.class);
                inte.putExtra("orderCode", orderCode);
                inte.putExtra("search", couseFuse);
                inte.putExtra("endUserId", endUserId);
                inte.putExtra("CourseScheduleId", courseScheduleId);
                startActivity(inte);
                ActiveActUtil.getInstance().addOrderActivity(this);
            }
        } else if (orderStatus == 120 || orderStatus == 109) {//订单不存在,创建订单
            createOrderCode(endUserId, openCourseId);
        }
    }

    /**
     * 创建订单接口
     *
     * @param endUserId    用户id
     * @param openCourseId 公开课id
     */
    private void createOrderCode(String endUserId, String openCourseId) {
        if (TextUtils.isEmpty(endUserId) || TextUtils.isEmpty(openCourseId)) {
            Toast.makeText(this, "用户id或公开课为空", Toast.LENGTH_SHORT).show();
            return;
        }
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("order/createOrder");
        pairsList.add(new Pairs("endUserId", endUserId));//用户 id
        pairsList.add(new Pairs("orderType", 1 + ""));//订单类型0：专业，1：公开课
        pairsList.add(new Pairs("openCourseId", openCourseId));
        demo.doHttpPostLoading(this, url, pairsList, 1);
    }

    //立即报名 网络请求
    private void setApplyNameToHttp() {
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("order/createOrder");
        pairsList.add(new Pairs("endUserId", endUserId));
        pairsList.add(new Pairs("orderType", 1 + ""));//订单类型0：专业，1：公开课2:微专业
        pairsList.add(new Pairs("openCourseId", openCourseId));
        demo.doHttpPostLoading(this, url, pairsList, 0);
    }

    //非视频控件的事件绑定
    private void initEventView() {
        createFragmentData();
        Log.i(TAG, "initEventView: " + listFragment.size() + " orderStatus:" + orderStatus);
        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), listFragment);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        isLimitTime = true;//是否限时   限时
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton button = (RadioButton) group.getChildAt(position % 4);
                button.setChecked(true);//
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.open_rb1://简介
                        changeAnimation(0);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.open_rb2://目录
                        changeAnimation(1);
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.open_rb3://评价
                        changeAnimation(2);
                        viewPager.setCurrentItem(2);
                        break;
                }
            }
        });
    }

    //创建 简介 目录 评价 的fragment数据
    private void createFragmentData() {
        listFragment = new ArrayList<>();
        openIntroFragment = new OpenIntroFragment();
        openIntroFragment.setDateFromActivity(couseFuse, OpenCouseHintActivity.this);//选课表id

        openDirectoryFragment = new OpenDirectoryHintFragment();
        openDirectoryFragment.setDateFromActivity(couseFuse.getCourseId(), OpenCouseHintActivity.this, 0);

        openEvaluateFragment = new EvaluateFragment();
        openEvaluateFragment.setDateFromActivity(couseFuse.getOpencourseId(), 1);

        listFragment.add(openIntroFragment);
        listFragment.add(openDirectoryFragment);
        listFragment.add(openEvaluateFragment);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Activty onRestart: " + mSeekPosition);
        mVideoView.getmPLVideoView().start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Activty onStop: " + mSeekPosition);
//        mVideoView.pausePlay();
        mVideoView.setLayerType(View.LAYER_TYPE_NONE, null);//解除硬件加速
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pausePlay();
        Log.d(TAG, "Activity暂停 mSeekPosition=" + mSeekPosition);
    }

    /**
     * 给下划线设置宽
     */
    private void setUnderLine() {
        // 获取屏幕密度（方法2）
        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        int screenWidth2 = dm2.widthPixels;      // 屏幕宽（像素，如：480px）
        //设置动画图片的图片宽度
        RelativeLayout.LayoutParams laParams = (RelativeLayout.LayoutParams) flashContainer.getLayoutParams();
        laParams.width = screenWidth2 / 3;
        flashContainer.setLayoutParams(laParams);
        changeAnimation(0);
    }

    //下划线切换时候的动画效果
    private void changeAnimation(int arg0) {
        switch (arg0) {
            case 0:
                Animation animationL = AnimationUtils.loadAnimation(this, R.anim.translate_three_o);
                flashContainer.startAnimation(animationL);//开始动画
                animationL.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
            case 1://表示动画从左到右
                Animation animationR = AnimationUtils.loadAnimation(this, R.anim.translate_three_t);
                flashContainer.startAnimation(animationR);//开始动画
                animationR.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
            case 2://表示动画初始化（登录进来时候）
                Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.translate_three_th);
                flashContainer.startAnimation(animation3);//开始动画
                animation3.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
        }
    }

    /////////////////////////fragment数据传递/////////////////////////////////////////////////////////////////
    @Override
    public void deliveryUri(Coursewares coursewares, int currentTime, int totalPlayTime, int playCount) {
        wares = coursewares;
        Log.i(TAG, "deliveryUri: " + coursewares.toString());
        mVideoView.pausePlay();//设置正在播放的视频暂停
        mSeekPosition = currentTime;//把该课程正在播放的位子设置为pc段时候的位子
        this.totalPlayTime = totalPlayTime;
        isTotalPaly = false;//设置该课程视屏没有完全播放一次
        if (coursewares.getIsLink() == null) {
//            this.VIDEO_URL=coursewares.getCoursewareVideoUrl();
            getVideoAddress(coursewares);
            Log.i(TAG, "为7牛或者默认本地的视屏: " + VIDEO_URL);
        } else if (coursewares.getIsLink() == 2) {//为外链   人大视屏网址
            Log.i(TAG, "人大视屏: " + coursewares.getCoursewareVideoUrl() + ":");
            getRenDaPramFromUrl(coursewares.getCoursewareVideoUrl());//获取人大视频参数接口
        } else {//为7牛或者默认本地的视屏
//            this.VIDEO_URL=coursewares.getCoursewareVideoUrl();
            getVideoAddress(coursewares);

        }
    }

    private void getVideoAddress(final Coursewares coursewares) {
        Long time = new Date().getTime();
        String Token = PreferencesUtils.getSharePreStr(MyApplication.getContext(), Const.TOKEN);
        if (TextUtils.isEmpty(Token)) {
            Token = "uuid";
        }
        if (TextUtils.isEmpty(endUserId)) {
            endUserId = "0";
        }
        String Text = endUserId + "_" + coursewares.getId() + "_" + time + "_3" + Token;
        String key = CXAESUtil.encryptString(Api.AESKEY, Text);
        com.orhanobut.logger.Logger.d("明文：" + Text);
        com.orhanobut.logger.Logger.d("密文：" + key);
        String params = Api.GET_VIDEO_ADDRESS + "?courseId=" + coursewares.getCourseId() + "&cursewareId=" + coursewares.getId() + "&time=" + time + "&key=" + key + "&platform=1";
        HttpClient.get(this, params, new CallBack<VideoAddressBean>() {
            @Override
            public void onSuccess(VideoAddressBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                    VIDEO_URL = result.getData().getCourseware().getCoursewareVideoUrl();
                    Log.i(TAG, "为7牛或者默认本地的视屏: " + VIDEO_URL);
                    setVideoPlayerBefore(coursewares.getCoursewareName(), VIDEO_URL);
                }
            }
        });
    }

    //简介数据回调
    @Override
    public void deliveryIdAndName(Long id, String courseName) {
        this.openCourseId = String.valueOf(id);
    }

    ////////////////////////网络数据//////////////////////////////////////////////////////////////////
    @Override
    public void setMsg(String msg, int requestCode) {
        try {
            if (requestCode == MyConfig.CODE_POST_GETPATH) {//人大获取视频前需要的参数接口
                parseRenDaParam(msg);
            } else if (requestCode == MyConfig.CODE_POST_GETURL) {//获取人大视频参数接口后，再获取视频网址的接口返回信息
                Log.i(TAG, "setMsg: " + "人大获取视频网址返回信息");
                cutVideoUrl(msg);
            } else if (requestCode == 1) { //创建公开课订单
                parserCreateOrderCode(msg);
            } else if (requestCode == 0) {//立即报名网络接口
                parserApplyName(msg);
            } else if (requestCode == MyConfig.CODE_POST_VIDEO) {
                try {
                    JSONObject obj = new JSONObject(msg);
                    if (obj.getBoolean("success")) {
                        Log.i("videos", "setMsg: " + "=====视屏反馈成功");
                        totalPlayTime = 0;//设置用户观看时间为0
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "setMsg: " + e.toString());
        }
    }

    //立即报名网络解析
    private void parserApplyName(String msg) throws Exception {
        Log.i(TAG, "setMsg:立即报名网络接口 " + msg);
        OrderReturnBean orderReturnBean = new Gson().fromJson(msg, OrderReturnBean.class);
//            JSONObject jsonObject=new JSONObject(msg);
        if (orderReturnBean.isSuccess()) {
//                JSONObject object=jsonObject.getJSONObject("data");
//                Long courseScheduleId=object.getLong("courseScheduleId");
            int courseScheduleId = orderReturnBean.getData().getCourseScheduleId();
            dialog.dismiss();
            Intent inte = new Intent(OpenCouseHintActivity.this, OpenCouseItemActivity.class);
            inte.putExtra("CouseFuse", couseFuse);
            inte.putExtra("EndUserId", endUserId);
            inte.putExtra("CourseScheduleId", courseScheduleId);
            Log.i(TAG, "setMsg:课程id " + courseScheduleId);
            startActivity(inte);
            finish();
        } else {
//                Toast.makeText(this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
            Toast.makeText(this, orderReturnBean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //创建公开课订单号  接口返回数据解析
    private void parserCreateOrderCode(String msg) throws Exception {
        Log.i(TAG, "parserCreateOrderCode: " + msg);
        JSONObject jsonObject = new JSONObject(msg);
        Boolean success = jsonObject.getBoolean("success");
        if (success) {
            JSONObject object = jsonObject.getJSONObject("data");
            String orderCode1 = object.getString("orderCode");
            Log.i(TAG, "parserCreateOrderCode: " + orderCode1);
            if (!TextUtils.isEmpty(orderCode1)) {
                Intent inte = new Intent(this, ConfirmOpenOrderActivity.class);
                inte.putExtra("orderCode", orderCode1);
                inte.putExtra("search", couseFuse);
                inte.putExtra("endUserId", endUserId);
                startActivity(inte);
            }
        } else {
            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    //提示框 试看5分钟
    public void setDialog() {
        if (isDialogShow) {
            Log.i(TAG, "setDialog: ");
            MyUtil.setDailog(this, "提示", "您还未购买该课程，只能试看五分钟", "确认", "");
            isDialogShow = false;
        }
    }

    //关于视频控件事件的绑定
    private void initEventVideo() {

        setVideoAreaSize();

        if (VIDEO_URL == null) {
            Log.i(TAG, "initVideo: 视频网址为空");
//            mMediaController.setVisibility(View.GONE);
            mVideoView.hiddleControlView();
            mCourseIcon.setVisibility(View.VISIBLE);
        } else {
            try {
                setVideoPlayerBefore(wares.getCoursewareName(), VIDEO_URL);
            } catch (Exception e) {
                Log.i(TAG, "initEventVideo: ");
            }
        }
    }

    private void limitTime() {
        Log.i(TAG, "limitTime: ");
        isDialogShow = true;
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while (isLimitTime) {
                        sleep(timeInterval);
                        pullMsg = handler.obtainMessage();
                        pullMsg.obj = mVideoView.getmPLVideoView().getCurrentPosition();
                        Log.i(TAG, "run: " + "发送");
                        handler.sendMessage(pullMsg);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                //cachedHeight = (int) (width * 3f / 4f);
                //cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                if (VIDEO_URL != null) {
                    mCourseIcon.setVisibility(View.GONE);
                    mVideoView.setVoideUrl(VIDEO_URL);
                    mVideoView.startPlay();
                } else {
                    Log.i(TAG, "设置视频大小: 视频网址为空：" + null);
                    mCourseIcon.setVisibility(View.VISIBLE);
                }
                mVideoView.requestFocus();
            }
        });
    }

    /**
     * 设置视频播放前 检查网络信息 Wifi直接播放  非wifi直接Dialog提示框
     *
     * @param title 视频标题
     * @param video 视频地址
     */
    private void setVideoPlayerBefore(final String title, final String video) {
        Log.i(TAG, "setVideoPlayerBefore: " + "检查网络信息");
        if (CheckNetworkConnect.isWifi(this)) {//表示wifi网络
            setVideoTitleAndPlay(title, video);
        } else {//表示非wifi网络
            AlertDialog.Builder build = new AlertDialog.Builder(this);
            build.setTitle(getResources().getString(R.string.DialogTitle));//标题
            build.setMessage(getResources().getString(R.string.DialogText));
            build.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_NEGATIVE://"取消"
                            Log.i(TAG, "onClick: " + "取消");
                            break;
                    }
                }
            });//取消按钮,不需要监听时候设置为null
            build.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE://确认
                            setVideoTitleAndPlay(title, video);
                            Log.i(TAG, "onClick: " + "确认");
                            break;
                    }
                }
            });//确认按钮
            build.show();

        }
    }

    /**
     * 设置视频的标题和开始播放
     *
     * @param title 视频标题
     * @param video 视频地址
     */
    private void setVideoTitleAndPlay(String title, String video) {
        Log.i(TAG, "setVideoTitleAndPlay: " + title + " 视频地址:" + video);
        if (video == null) {
            return;
        }
//        mMediaController.setVisibility(View.VISIBLE);
        mCourseIcon.setVisibility(View.GONE);
        mVideoView.setVoideUrl(VIDEO_URL);
        mVideoView.requestFocus();
        mVideoView.startPlay();
        if (title != null || !("".equals(title)))
            mVideoView.setVideoTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("videos", "onSaveInstanceState Position=" + mSeekPosition);
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.i("videos", "onRestoreInstanceState Position=" + mSeekPosition);
    }

    //视频全屏 半屏
    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        mSeekPosition = (int)mVideoView.getmPLVideoView().getCurrentPosition();
        Log.i("videos", "onScaleChange: " + mSeekPosition);
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.GONE);
        } else {
            try {
                ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = this.cachedHeight;
                mVideoLayout.setLayoutParams(layoutParams);
                mBottomLayout.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }
        switchTitleBar(!isFullscreen);
    }

    //设置加载TitleBar
    private void switchTitleBar(boolean show) {
        ActionBar supportActionBar = this.getActionBar();
        if (supportActionBar != null) {
            if (show) {
                supportActionBar.show();
            } else {
                supportActionBar.hide();
            }
        }
    }

    @Override
    protected PLPlayView findPlayView() {
        return (PLPlayView) findViewById(R.id.open_video_layout);
    }

    @Override
    protected void onVoidePause() {
        Log.i("videos", "视屏onPause " + (mVideoView != null) + " " + (mVideoView.getmPLVideoView().isPlaying()));
        isLimitTime = false;
//        mVideoView.pause();
        Log.i("videos", "onPause:之前记录视频播放位子mSeekPosition " + mSeekPosition);
        Log.i("videos", "onPause:之前播放时长 " + totalPlayTime);
        totalPlayTime = totalPlayTime + TimeCutTamp.getDuration();
        Log.i("videos", "onPause:总的播放时间" + totalPlayTime);
        mSeekPosition =(int) mVideoView.getmPLVideoView().getCurrentPosition();
        Log.i("videos", "onPause 更新位子信息 mSeekPosition=" + mSeekPosition);
        videoTickling(totalPlayTime, mSeekPosition);
    }

    @Override
    protected void onVoideStart() {
        Log.i("videos", "视频开始播放或恢复播放onStart: " + mVideoView.getmPLVideoView().getCurrentPosition());
        isLimitTime = true;//初始化限时控制器
//        mVideoView.getmPLVideoView().seekTo(mSeekPosition);
        TimeCutTamp.sinkCurrentTimeInLong();//记录当前时间
        limitTime();
    }

    @Override
    protected void screenChange(boolean isFullScreen) {initEventVideo();
        mSeekPosition = (int) mVideoView.getmPLVideoView().getCurrentPosition();
    }

    @Override
    protected boolean onPlayError(int i) {
        Log.i("videos", "onError: " + i + ":" + i);
        ToastUtil.showShortToast(OpenCouseHintActivity.this, "错误代码：" + ";=" + i);
        return false;
    }

    @Override
    protected void onPlayCompletion() {
        totalPlayTime += totalPlayTime + TimeCutTamp.getDuration();
        isTotalPaly = true;////设置该课程视屏完全播放一次
        Log.i("videos", "onCompletion 监听视屏播放完成" + totalPlayTime);
        mSeekPosition = 0;
        mVideoView.getmPLVideoView().pause();
        videoTickling(totalPlayTime, 0);
        //播放完成跳转播放下一个视频
        EventBus.getDefault().post(new ViedoCompleteEvent());
    }

    @Override
    protected void onPlayInfo(int i, int i1) {

    }

    @Override
    protected void onPlayPrepared(int i) {
        //在这个状态下才可以直接得到视屏正确的长度,即时间段
        Log.i("videos", "视屏长度总的播放时长" + mVideoView.getmPLVideoView().getDuration()); //这里并没有的到视屏长度
    }

    @Override
    protected void startToSeek() {
        if (mSeekPosition > 0) {
            mVideoView.getmPLVideoView().seekTo(mSeekPosition);
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "视屏onBackPressed: ");
        if (this.isFullscreen) {
//            mVideoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    //暂停
    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.i("videos", "视屏onPause " + (mVideoView != null) + " " + (mediaPlayer.isPlaying()));
        isLimitTime = false;
//        mVideoView.pause();
        Log.i("videos", "onPause:之前记录视频播放位子mSeekPosition " + mSeekPosition);
        Log.i("videos", "onPause:之前播放时长 " + totalPlayTime);
        totalPlayTime = totalPlayTime + TimeCutTamp.getDuration();
        Log.i("videos", "onPause:总的播放时间" + totalPlayTime);
        mSeekPosition =(int) mVideoView.getmPLVideoView().getCurrentPosition();
        Log.i("videos", "onPause 更新位子信息 mSeekPosition=" + mSeekPosition);
        videoTickling(totalPlayTime, mSeekPosition);
    }

    //视屏启动
    @Override
    public void onStart(MediaPlayer mediaPlayer) { // 视频开始播放或恢复播放
        Log.i("videos", "视频开始播放或恢复播放onStart: " + mediaPlayer.getCurrentPosition());
        isLimitTime = true;//初始化限时控制器
        mVideoView.getmPLVideoView().seekTo(mSeekPosition);
        TimeCutTamp.sinkCurrentTimeInLong();//记录当前时间
        limitTime();
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {// 视频开始缓冲
        // Log.i("videos", "视频开始缓冲onBufferingStart: "+mediaPlayer.getCurrentPosition());
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {// 视频结束缓冲
        // Log.i("videos", "视频结束缓冲onBufferingEnd: 此段视屏总时长"+ mediaPlayer.getDuration());//总的播放时间段
    }


    /**
     * 视屏信息反馈 给网络
     *
     * @param totalPlayTime 用户此次看了多长时间
     * @param mSeekPosition 用户此次看至到什么位子
     */
    public void videoTickling(Long totalPlayTime, int mSeekPosition) {
        Log.i(TAG, "videoTickling: 视频反馈");
        //  Log.i("videos", "videoTickling: " + wares.toString());
//        String url = MyConfig.getURl("learn/saveLearnVideoRecord");
//        pairsList = new ArrayList<>();
//        pairsList.add(new Pairs("endUserId", String.valueOf(endUserId)));//Long 用户ID
//        pairsList.add(new Pairs("courseScheduleId", courseScheduleId));//课程表ID
//        pairsList.add(new Pairs("courseId", couseFuse.getCourseId()));//Long	是	课程id
//        pairsList.add(new Pairs("courwareId", String.valueOf(wares.getId())));//Long	是	课时Id
//        if (isTotalPaly) {//判断该课程视屏没有有完全播放一次
//            Log.i(TAG, "videoTickling: " + "完整播放一次");
//            pairsList.add(new Pairs("playCount", String.valueOf(1)));//Integer	是	完整播放一次
//        } else {
//            Log.i(TAG, "videoTickling: " + "视频没有播放一次");
//            pairsList.add(new Pairs("playCount", String.valueOf(0)));//Integer	是	完整播放一次
//        }
//        pairsList.add(new Pairs("totalPlayTime", String.valueOf(totalPlayTime / 1000)));//Integer	  是	播放总时长
//        pairsList.add(new Pairs("currentPlayTime", String.valueOf(mSeekPosition / 1000)));//Integer	是	当前位置记录
//        demo.doHttpPostVideo(url, pairsList, MyConfig.CODE_POST_VIDEO);
    }


    /**
     * 根据 视频处json串， 获取 人大视频获取url接口
     *
     * @param json 人大视频的Json对象   {"productid":"wis001","videoCode":"6832B598272934F4D923CDF7E38D6962"}
     */
    private void getRenDaPramFromUrl(String json) {
        Log.i(TAG, "解析人大parserRenDaPramFromUrl: " + json);
        try {
            JSONObject object = new JSONObject(json);
            String url = MyConfig.getURl("cmr/getVideokey");
            pairsList = new ArrayList<>();
            productid = object.getString("productid");
            videoCode = object.getString("videoCode");
            pairsList.add(new Pairs("userId", String.valueOf(endUserId)));
            pairsList.add(new Pairs("productid", productid));
            pairsList.add(new Pairs("videoCode", videoCode));
            demo.doHttpGet(url, pairsList, MyConfig.CODE_POST_GETPATH);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "解析人大视频参数: " + e);
        }
    }

    /**
     * 解析 人大视频视频需要的参数 的接口
     *
     * @param msg //{"state":1,"success":true,"message":"","data":{"skey":"si4t4DfyJ2WjoaYMLrz6Ihgs@kgbTt2R"}}
     */
    private void parseRenDaParam(String msg) throws Exception {
        Log.i(TAG, "parseRenDaParam: " + "人大视频参数" + msg);
        JSONObject object = new JSONObject(msg);
        boolean state = object.getBoolean("success");
        if (!state) {
            showNoPlay(object.getString("message"));
            return;
        }
        JSONObject data = (JSONObject) object.get("data");
        String skey = data.getString("skey");
        Log.i(TAG, "parseRenDaParam:" + skey);
        getVideoUrlBySkey(skey);
    }

    private void showNoPlay(String message) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("课程错误提示");//标题
        build.setMessage(message);
        build.setPositiveButton("确认", null);//确认按钮
        build.show();
    }


    /**
     * 获取人大视频参数接口后，再获取网址url的接口
     *
     * @param skey 获取的参数
     */
    private void getVideoUrlBySkey(final String skey) {
        Log.i(TAG, "getVideoUrlBySkey:skey:=" + skey);
        final Long t = Math.round(Math.random() * 100000);
        final String url = "http://mv.cmr.com.cn/play/geturl";
        pairsList = new ArrayList<>();
        pairsList.add(new Pairs("t", String.valueOf(t)));
        pairsList.add(new Pairs("callback", "json_callback_12345"));
        pairsList.add(new Pairs("pid", "wis"));
        pairsList.add(new Pairs("i_uid", String.valueOf(endUserId)));
        pairsList.add(new Pairs("productid", productid));
        pairsList.add(new Pairs("videoCode", videoCode));
        pairsList.add(new Pairs("skey", skey));
        demo.doHttpGet(url, pairsList, MyConfig.CODE_POST_GETURL);
    }


    /**
     * 解析获取人大视频的接口
     *
     * @param msg json对象
     */
    private void cutVideoUrl(final String msg) {
        //返回错信息样本{videoURL:'',error:'未通过验证'}
        Log.i(TAG, "cutVideoUrl:--- " + "获取人大视频网址:" + msg);
        int start = msg.indexOf("(");
        int end = msg.indexOf(")");
        String json = msg.substring(start + 1, end);
        Log.i(TAG, "cutVideoUrl: ---" + json);
        try {
            JSONObject object = new JSONObject(json);
            VIDEO_URL = object.getString("videoURL");
            Log.e(TAG, "parseVideoUrl: " + VIDEO_URL);
            Log.e(TAG, "cutVideoUrl: " + wares.toString());
            String coursewareName = wares.getCoursewareName();
            setVideoPlayerBefore(coursewareName, VIDEO_URL);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "cutVideoUrl: " + e.toString());
        }
    }


}
