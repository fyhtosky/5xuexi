package com.sj.yinjiaoyun.xuexi.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.Event.ViedoCompleteEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.FragmentAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.VideoAddressBean;
import com.sj.yinjiaoyun.xuexi.domain.Coursewares;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domains.CouseFuse;
import com.sj.yinjiaoyun.xuexi.fragment.EvaluateFragment;
import com.sj.yinjiaoyun.xuexi.fragment.OpenDirectoryHintFragment;
import com.sj.yinjiaoyun.xuexi.fragment.OpenDirectoryItemFragment;
import com.sj.yinjiaoyun.xuexi.fragment.OpenIntroFragment;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.CXAESUtil;
import com.sj.yinjiaoyun.xuexi.utils.CheckNetworkConnect;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.TimeCutTamp;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.view.PLView.PLPlayView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.universalvideoview.UniversalVideoView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/15.
 * 公开课 课程 详情
 * 订单完成状态下的公开课详情界面
 */
public class OpenCouseItemActivity extends PlayBaseActivity implements UniversalVideoView.VideoViewCallback,
        HttpDemo.HttpCallBack, OpenDirectoryHintFragment.OpenDirectoryCallBack, OpenDirectoryItemFragment.OpenDirectoryCallBack,
        OpenIntroFragment.OpenIntroHttpCallBack {

    /////////////////////////////视频控件的设置/////////////////////////////////////////////////////
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static int mSeekPosition;//当前视屏播放的当前位置
    String TAG = "opencouseItem";
    Coursewares wares;//课程详情-章节课时列表及学习情况
    CouseFuse search;
    String courseScheduleId;//课程表id
    Intent intent;
    LayoutInflater inflater;
    ViewPager viewPager;
    RadioGroup group;//简介 目录 评论
    FragmentAdapter fragmentAdapter;
    List<Fragment> listFragment;
    OpenIntroFragment openIntroFragment;//简介
    OpenDirectoryItemFragment openDirectoryFragment;//目录
    EvaluateFragment openEvaluateFragment;//评论
    View flashContainer;//下划线的父
    HttpDemo demo;
    List<Pairs> pairsList;//网络请求时候传递参数
    String productid;//解析人大视频url解析出来的变量
    String videoCode;//解析人大视频url解析出来的变量
    //    UniversalVideoView mVideoView;//视频播放控件
//    UniversalMediaController mMediaController;//视频控制控件
    TextView mViewTitle;//视屏没播放时的标题
    View mBottomLayout;//非视频播放纵容其的其他的所有的控件容器
    View mVideoLayout;// <!--视屏播放器总的容器-->
    long countTotalTime;//单个该节视频或者当前播放视频的时长 ,即视屏长度总的播放时长
    RadioButton rb1;
    RadioButton rb2;
    Long id;//公开课id
    private String endUserId;//用户id
    private String VIDEO_URL;//视频播放地址
    private Boolean isTotalPaly = false;//当前视屏播放的总时间段
    private long totalPlayTime = 0;//记录用户观看视屏用了多长时间
    //    private int cachedHeight;
    private boolean isFullscreen;//是否是满屏
    private int currentTime;//pc段该视频播放的位子
    //视频播放次数
    private int playCount = 0;
    private boolean isFullPlay = false;

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
        return R.layout.layout_opencouse;
    }

    //////////////////////Activity的生命周期////////////////////////////////////////////////////////////
    //数据准备
    private void init() {
        inflater = LayoutInflater.from(this);
        demo = new HttpDemo(this);
        intent = getIntent();
        endUserId = PreferencesUtils.getSharePreStr(OpenCouseItemActivity.this, "username");
        search = intent.getParcelableExtra("CouseFuse");
        courseScheduleId = intent.getStringExtra("CourseScheduleId");//课程表id
        Log.i(TAG, "onCreate:用户id： " + endUserId + ":" + courseScheduleId);
        // Log.i(TAG, "onCreate:专业信息： "+search.toString());
    }

    @Override
    protected PLPlayView findPlayView() {
        return (PLPlayView) findViewById(R.id.open_video_layout);
    }

    @Override
    protected void onVoidePause() {
        Log.i("videos", "视屏onPause " + (mVideoView != null) + " " + (mVideoView.getmPLVideoView().isPlaying()));
//        mVideoView.pause();


        Log.i("videos", "onPause:获取用去上次观看此视频播放位子mSeekPosition " + mSeekPosition);
        Log.i("videos", "onPause:用户在记录之前看了多长时间totalPlayTime " + totalPlayTime);
        long currentPlayTotal = TimeCutTamp.getDuration();
        totalPlayTime = totalPlayTime + currentPlayTotal;
        Log.e("videos", "onPause:已观看时间段totalPlayTime" + totalPlayTime);
        mSeekPosition = (int) mVideoView.getmPLVideoView().getCurrentPosition();
        Log.i("videos", "onPause 更新位子信息 mSeekPosition=" + mSeekPosition);
        currentTime = (int) (mSeekPosition - totalPlayTime);
        videoTickling(totalPlayTime, mSeekPosition, currentPlayTotal);
    }

    @Override
    protected void onVoideStart() {
        Log.i("videos", "视频开始播放或恢复播放onStart: " + mVideoView.getmPLVideoView().getCurrentPosition());
//        if (mSeekPosition != 0) {
//            mVideoView.getmPLVideoView().seekTo(mSeekPosition);
//        }
        TimeCutTamp.sinkCurrentTimeInLong();//记录当前时间
    }

    @Override
    protected void screenChange(boolean isFullScreen) {

    }

    @Override
    protected boolean onPlayError(int i) {
        Log.i("videos", "onError: " + ":" + i);
        ToastUtil.showShortToast(OpenCouseItemActivity.this, "错误代码：;=" + i);
        return false;
    }

    @Override
    protected void onPlayCompletion() {
        long currentPlayTotal = TimeCutTamp.getDuration();
        totalPlayTime = totalPlayTime + currentPlayTotal;
        isTotalPaly = true;////设置该课程视屏完全播放一次
        Log.i("videos", "onCompletion 监听视屏播放完成" + totalPlayTime);
        mSeekPosition = 0;
        mVideoView.pausePlay();

        videoTickling(totalPlayTime, mSeekPosition, currentPlayTotal);
        if(playCount>0){
            openDirectoryFragment.refreshProgressData(2);
        }

//        播放完成跳转播放下一个视频
        EventBus.getDefault().post(new ViedoCompleteEvent());
    }

    @Override
    protected void onPlayInfo(int i, int i1) {

    }

    @Override
    protected void onPlayPrepared(int i) {
        //在这个状态下才可以直接得到视屏正确的长度,即时间段
        Log.i("videos", "视屏长度总的播放时长" + mVideoView.getmPLVideoView().getDuration()); //这里并没有的到视屏长度
        countTotalTime = mVideoView.getmPLVideoView().getDuration();
    }

    @Override
    protected void startToSeek() {
        openDirectoryFragment.refreshProgressData(1);
        if (mSeekPosition > 0) {
            mVideoView.getmPLVideoView().seekTo(mSeekPosition);
        }
    }

    private void initView() {
        flashContainer = findViewById(R.id.opencouse_underLineC);
        group = (RadioGroup) findViewById(R.id.open_item_head_group);
        rb1 = (RadioButton) findViewById(R.id.open_rb1);
        rb2 = (RadioButton) findViewById(R.id.open_rb2);
        viewPager = (ViewPager) findViewById(R.id.item_opencourse_viewpager);
        mViewTitle = (TextView) findViewById(R.id.open_view_title);
        mVideoLayout = findViewById(R.id.open_video_layout);
        mBottomLayout = findViewById(R.id.open_bottom_layout);
//        mVideoView = (UniversalVideoView) findViewById(R.id.open_videoView);
//        mMediaController = (UniversalMediaController) findViewById(R.id.open_media_controller);
        setUnderLine();//设置下划线
    }

    //非视频控件的事件绑定
    private void initEventView() {
        createFragmentData();
        Log.i(TAG, "initEventView: " + listFragment.size());
        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), listFragment);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(1);
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
        openIntroFragment.setDateFromActivity(search, this);//选课表id

        openDirectoryFragment = new OpenDirectoryItemFragment();
        openDirectoryFragment.setDateFromActivity(courseScheduleId, OpenCouseItemActivity.this, 2);

        openEvaluateFragment = new EvaluateFragment();
        openEvaluateFragment.setDateFromActivity(search.getOpencourseId(), 1);//公开课id

        listFragment.add(openIntroFragment);
        listFragment.add(openDirectoryFragment);
        listFragment.add(openEvaluateFragment);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Activty onRestart: " + mSeekPosition);
        mVideoView.startPlay();
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

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy:homeitem " + endUserId);
        super.onDestroy();
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
        changeAnimation(1);
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
        this.wares = coursewares;
        this.currentTime = currentTime;
        this.playCount = playCount;
        Log.i(TAG, "deliveryUri: 用户观看位子" + currentTime);
        Log.i(TAG, "deliveryUri: " + coursewares.toString());

        mVideoView.pausePlay();//设置正在播放的视频暂停
        mSeekPosition = (currentTime * 1000);//把该课程正在播放的位子设置为pc段时候的位子
        this.totalPlayTime = (totalPlayTime * 1000);
        isTotalPaly = false;//设置该课程视屏没有完全播放一次
        try {
            if (coursewares.getIsLink() == null) {
//                this.VIDEO_URL = coursewares.getCoursewareVideoUrl();
                Log.i(TAG, "为7牛或者默认本地的视屏: " + VIDEO_URL);
//                setVideoPlayerBefore(coursewares.getCoursewareName(), VIDEO_URL);
                getVideoAddress(coursewares);
            } else if (coursewares.getIsLink() == 2) {//为外链   人大视屏网址
                Log.i(TAG, "人大视屏: " + coursewares.getCoursewareVideoUrl() + ":");
                getRenDaPramFromUrl(coursewares.getCoursewareVideoUrl());//获取人大视频参数接口
            } else {//为7牛或者默认本地的视屏
//                this.VIDEO_URL = coursewares.getCoursewareVideoUrl();
//                Log.i(TAG, "为7牛或者默认本地的视屏: " + VIDEO_URL);
//                setVideoPlayerBefore(coursewares.getCoursewareName(), VIDEO_URL);
                getVideoAddress(coursewares);
            }
        } catch (Exception e) {
            Log.e(TAG, "deliveryUri: " + e.toString());
        }
    }

    /**
     * 获取视频的地址
     *
     * @param coursewares
     */
    private void getVideoAddress(final Coursewares coursewares) {
        Long time = new Date().getTime();
        String Token = PreferencesUtils.getSharePreStr(MyApplication.getContext(), Const.TOKEN);
        String Text = endUserId + "_" + coursewares.getId() + "_" + time + "_1" + Token;
        String key = CXAESUtil.encryptString(Api.AESKEY, Text);
        com.orhanobut.logger.Logger.d("明文：" + Text);
        com.orhanobut.logger.Logger.d("密文：" + key);
        String params = Api.GET_VIDEO_ADDRESS + "?courseId=" + coursewares.getCourseId() + "&cursewareId=" + coursewares.getId() + "&courseScheduleId=" + courseScheduleId + "&time=" + time + "&key=" + key + "&platform=1";
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
        this.id = id;
        mViewTitle.setText(courseName);//设置视屏中的标题
        if (openEvaluateFragment != null) {
            openEvaluateFragment.setDateFromActivity(id, 1);
        }
        Log.i(TAG, "deliveryIdAndName: " + courseName);
    }


    ////////////////////////网络数据//////////////////////////////////////////////////////////////////
    @Override
    public void setMsg(String msg, int requestCode) {
        if (requestCode == MyConfig.CODE_POST_GETPATH) {//人大获取视频前需要的参数接口
            parseRenDaParam(msg);
        } else if (requestCode == MyConfig.CODE_POST_GETURL) {//获取人大视频参数接口后，再获取视频网址的接口返回信息
            Log.i(TAG, "setMsg: " + "人大获取视频网址返回信息");
            cutVideoUrl(msg);
        } else if (requestCode == MyConfig.CODE_POST_VIDEO) {
            try {
                Log.i(TAG, "setMsg:视频反馈信息 " + msg);
                JSONObject obj = new JSONObject(msg);
                if (obj.getBoolean("success")) {
                    Log.i("videos", "setMsg: " + "=====视屏反馈成功");
                    TimeCutTamp.sinkCurrentTimeInLong();//记录当前时间

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    ////////////////////////////////视频的操作///////////////////////////////////////////////////////////////

    //关于视频控件事件的绑定
    private void initEventVideo() {
        setVideoAreaSize();
        ////
        if (VIDEO_URL == null) {
            Log.i(TAG, "initVideo: 视频网址为空");
            mVideoView.hiddleControlView();
            mViewTitle.setVisibility(View.VISIBLE);
        } else {
            try {
                mViewTitle.setVisibility(View.GONE);
                setVideoPlayerBefore(wares.getCoursewareName(), VIDEO_URL);
            } catch (Exception e) {
                Log.i(TAG, "initEventVideo: " + e.toString());
            }
        }
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
                    mViewTitle.setVisibility(View.GONE);
                    mVideoView.setVoideUrl(VIDEO_URL);
                    mVideoView.startPlay();
                } else {
                    Log.i(TAG, "设置视频大小: 视频网址为空：" + null);
                    mViewTitle.setVisibility(View.VISIBLE);
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
        mVideoView.hiddleControlView();
        mViewTitle.setVisibility(View.GONE);
        mPlayUrl = video;
        mVideoView.setVoideUrl(video);
        mVideoView.requestFocus();
        mVideoView.startPlay();
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

    /**
     * 视频屏幕切换全屏半屏
     *
     * @param isFullscreen
     */
    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        mSeekPosition = (int) mVideoView.getmPLVideoView().getCurrentPosition();
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
//        Log.i("videos", "视屏onPause " + (mVideoView != null) + " " + (mediaPlayer.isPlaying()));
////        mVideoView.pause();
//        Log.i("videos", "onPause:获取用去上次观看此视频播放位子mSeekPosition " + mSeekPosition);
//        Log.i("videos", "onPause:用户在记录之前看了多长时间totalPlayTime " + totalPlayTime);
//        long currentPlayTotal = TimeCutTamp.getDuration();
//        totalPlayTime = totalPlayTime + currentPlayTotal;
//        Log.e("videos", "onPause:已观看时间段totalPlayTime" + totalPlayTime);
//        mSeekPosition = (int) mVideoView.getmPLVideoView().getCurrentPosition();
//        Log.i("videos", "onPause 更新位子信息 mSeekPosition=" + mSeekPosition);
//        currentTime = (int) (mSeekPosition - totalPlayTime);
//        videoTickling(totalPlayTime, mSeekPosition, currentPlayTotal);
    }

    //视屏启动
    @Override
    public void onStart(MediaPlayer mediaPlayer) { // 视频开始播放或恢复播放
        Log.i("videos", "视频开始播放或恢复播放onStart: " + mediaPlayer.getCurrentPosition());
//        mVideoView.getmPLVideoView().seekTo(mSeekPosition);
//        TimeCutTamp.sinkCurrentTimeInLong();//记录当前时间
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
    public void videoTickling(Long totalPlayTime, int mSeekPosition, long currentPlayTotal) {
        Log.i(TAG, "videoTickling: 视频反馈");
        Log.i(TAG, "videoTickling: " + wares.toString());
        String url = MyConfig.getURl("learn/saveLearnVideoRecord");
        pairsList = new ArrayList<>();
        pairsList.add(new Pairs("endUserId", String.valueOf(endUserId)));//Long 用户ID
        pairsList.add(new Pairs("courseScheduleId", courseScheduleId));//   课程表ID
        pairsList.add(new Pairs("courseId", String.valueOf(search.getCourseId())));  //Long	是	课程id
        pairsList.add(new Pairs("courwareId", String.valueOf(wares.getId())));  //Long	是	课时Id
        if (playCount == 0) {
            if (((double) totalPlayTime / (double) countTotalTime) >= 0.5) {
                pairsList.add(new Pairs("playCount", String.valueOf(1)));//Integer	是	完整播放一次
                playCount += 1;
            } else {
                pairsList.add(new Pairs("playCount", String.valueOf(0)));//Integer	是	完整播放一次
            }
        } else {
            if (totalPlayTime - (countTotalTime * playCount) > countTotalTime) {
                pairsList.add(new Pairs("playCount", String.valueOf(1)));//Integer	是	完整播放一次
                playCount += 1;
            } else {
                pairsList.add(new Pairs("playCount", String.valueOf(0)));//Integer	是	完整播放一次
            }
        }
        pairsList.add(new Pairs("totalPlayTime", String.valueOf(currentPlayTotal / 1000)));//Integer	是	播放总时长
        pairsList.add(new Pairs("currentPlayTime", String.valueOf(mSeekPosition / 1000)));//Integer	是	当前位置记录
        demo.doHttpPostVideo(url, pairsList, MyConfig.CODE_POST_VIDEO);
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
    private void parseRenDaParam(String msg) {
        Log.i(TAG, "parseRenDaParam: " + "人大视频参数" + msg);
        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "解析人大视频参数" + e);
        }
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
            Log.i(TAG, "parseVideoUrl: " + VIDEO_URL);
            String coursewareName = wares.getCoursewareName();
            setVideoPlayerBefore(coursewareName, VIDEO_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}


