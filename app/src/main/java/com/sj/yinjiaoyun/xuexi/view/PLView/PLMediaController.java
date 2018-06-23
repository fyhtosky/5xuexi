package com.sj.yinjiaoyun.xuexi.view.PLView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pili.pldroid.player.IMediaController;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;
import com.sj.yinjiaoyun.xuexi.R;

import java.util.Formatter;
import java.util.Locale;


/**
 * 项目名称：xuexi
 * 类描述：七牛控制器
 * 创建人：zhangchunzhe
 * 创建时间：2018/5/24 下午3:41
 * 修改人：zhangchunzhe
 * 修改时间：2018/5/24 下午3:41
 * 修改备注：
 */
public class PLMediaController extends FrameLayout implements IMediaController {

    private static final String TAG = "PLMediaController";
    private static final int sDefaultTimeout = 3000;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSE = 2;
    private static final int STATE_LOADING = 3;
    private static final int STATE_ERROR = 4;
    private static final int STATE_COMPLETE = 5;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int SHOW_LOADING = 3;
    private static final int HIDE_LOADING = 4;
    //    private boolean mFullscreenEnabled = false;
    private static final int SHOW_ERROR = 5;
    private static final int HIDE_ERROR = 6;
    private static final int SHOW_COMPLETE = 7;
    private static final int HIDE_COMPLETE = 8;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    boolean handled = false;
    private Context mContext;
    private PLVideoView mPLVideoView1;
    private PLVideoTextureView mPLVideoView;
    private View mRootView;
    private ProgressBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private TextView mTitle;
    private boolean mShowing = true;
    private boolean mDragging;
    private boolean mScalable = false;
    private boolean mIsFullScreen = false;
    private int mState = STATE_LOADING;
    private ImageButton mTurnButton;// 开启暂停按钮
    private ImageButton mScaleButton; // 全屏按钮
    private View mBackButton;// 返回按钮
    private ViewGroup loadingLayout;
    private ViewGroup errorLayout;
    private View mTitleLayout;
    private View mControlLayout;
    private View mCenterPlayButton;
    private PLPlayView.VideoPauseCallBack pauseCallBack;
    private PLPlayView.VideoStartCallBack startCallBack;
    private PLPlayView.ScreenChangeCallback screenChangeCallback;
    private PLPlayView.FullSreenBackCallback fullSreenBackCallback;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT: //1
                    hide();
                    break;
                case SHOW_PROGRESS: //2
                    pos = setProgress();
                    if (!mDragging && mShowing && mPLVideoView != null && mPLVideoView.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
                case SHOW_LOADING: //3
                    show();
                    Log.d("---","接收到显示加载进度条的消息");
                    showCenterView(R.id.loading_layout_pl);
                    break;
                case SHOW_COMPLETE: //7
                    showCenterView(R.id.center_play_btn_pl);
                    break;
                case SHOW_ERROR: //5
                    show();
                    showCenterView(R.id.error_layout_pl);
                    break;
                case HIDE_LOADING: //4
                case HIDE_ERROR: //6
                case HIDE_COMPLETE: //8
                    hide();
                    Log.d("---","接收到隐藏加载进度的消息");
                    hideCenterView();
                    break;
            }
        }
    };
    //如果正在显示,则使之消失
    private OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mShowing) {
                    hide();
                    handled = true;
                    return true;
                }
            }
            return false;
        }
    };
    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mPLVideoView != null) {
                doPauseResume();
                show(sDefaultTimeout);
            }
        }
    };
    private View.OnClickListener mScaleListener = new View.OnClickListener() {
        public void onClick(View v) {
            mIsFullScreen = !mIsFullScreen;
            doScreenChange();
            updateScaleButton();
            updateBackButton();

        }
    };
    //仅全屏时才有返回按钮
    private View.OnClickListener mBackListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mIsFullScreen) {
                mIsFullScreen = false;
                doBack();
                updateScaleButton();
                updateBackButton();

//                mPLVideoView.setFullscreen(false);
            }

        }
    };
    private View.OnClickListener mCenterPlayListener = new View.OnClickListener() {
        public void onClick(View v) {
            hideCenterView();
            doStart();
            mPLVideoView.start();
        }
    };


    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        int newPosition = 0;

        boolean change = false;

        public void onStartTrackingTouch(SeekBar bar) {
            if (mPLVideoView == null) {
                return;
            }
            show(3600000);

            mDragging = true;
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (mPLVideoView == null || !fromuser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPLVideoView.getDuration();
            long newposition = (duration * progress) / 1000L;
            newPosition = (int) newposition;
            change = true;
        }

        public void onStopTrackingTouch(SeekBar bar) {
            if (mPLVideoView == null) {
                return;
            }
            if (change) {
                mPLVideoView.seekTo(newPosition);
                if (mCurrentTime != null) {
                    mCurrentTime.setText(stringForTime(newPosition));
                }
            }
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            mShowing = true;
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    public PLMediaController(@NonNull Context context) {
        super(context);
        init(context);
    }


    public PLMediaController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public PLMediaController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PLMediaController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mScalable = true;
        initView();

    }

    private void initView() {
        addRootView();
        initControllerView(mRootView);
    }

    private void addRootView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.pl_player_controller, null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(0, 0, 0, 0);
        addView(mRootView, layoutParams);
        mRootView.setOnTouchListener(mTouchListener);
    }

    private void initControllerView(View v) {
        mTitleLayout = v.findViewById(R.id.title_part_pl);
        mControlLayout = v.findViewById(R.id.control_layout_pl);
        loadingLayout = (ViewGroup) v.findViewById(R.id.loading_layout_pl);
        errorLayout = (ViewGroup) v.findViewById(R.id.error_layout_pl);
        mTurnButton = (ImageButton) v.findViewById(R.id.turn_button_pl);
        mScaleButton = (ImageButton) v.findViewById(R.id.scale_button_pl);
        mCenterPlayButton = v.findViewById(R.id.center_play_btn_pl);
        mBackButton = v.findViewById(R.id.back_btn_pl);

        if (mTurnButton != null) {
            mTurnButton.requestFocus();
            mTurnButton.setOnClickListener(mPauseListener);
        }

        if (mScalable) {
            if (mScaleButton != null) {
                mScaleButton.setVisibility(VISIBLE);
                mScaleButton.setOnClickListener(mScaleListener);
            }
        } else {
            if (mScaleButton != null) {
                mScaleButton.setVisibility(GONE);
            }
        }

        if (mCenterPlayButton != null) {//重新开始播放
            mCenterPlayButton.setOnClickListener(mCenterPlayListener);
        }

        if (mBackButton != null) {//返回按钮仅在全屏状态下可见
            mBackButton.setOnClickListener(mBackListener);
        }

        View bar = v.findViewById(R.id.seekbar_pl);
        mProgress = (ProgressBar) bar;
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }

        mEndTime = (TextView) v.findViewById(R.id.duration_pl);
        mCurrentTime = (TextView) v.findViewById(R.id.has_played_pl);
        mTitle = (TextView) v.findViewById(R.id.title_pl);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }


    public void setMediaPlayer(PLVideoView plVideoView) {
        this.mPLVideoView1 = plVideoView;
        updatePausePlay();
    }


    public void setMediaPlayer(PLVideoTextureView plVideoView) {
        this.mPLVideoView = plVideoView;
        updatePausePlay();
    }

    @Override
    public void setMediaPlayer(MediaPlayerControl mediaPlayerControl) {

    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons() {
        try {
            if (mTurnButton != null && mPLVideoView != null && !mPLVideoView.canPause()) {
                mTurnButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show
     *                the controller until hide() is called.
     */
    public void show(int timeout) {//只负责上下两条bar的显示,不负责中央loading,error,playBtn的显示.
        if (!mShowing) {
            setProgress();
            if (mTurnButton != null) {
                mTurnButton.requestFocus();
            }
            disableUnsupportedButtons();
            mShowing = true;
        }
        updatePausePlay();
        updateBackButton();

        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        if (mTitleLayout.getVisibility() != VISIBLE) {
            mTitleLayout.setVisibility(VISIBLE);
        }
        if (mControlLayout.getVisibility() != VISIBLE) {
            mControlLayout.setVisibility(VISIBLE);
        }

        // cause the progress bar to be updated even if mShowing
        // was already true. This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    public void hide() {//只负责上下两条bar的隐藏,不负责中央loading,error,playBtn的隐藏
        if (mShowing) {
            mHandler.removeMessages(SHOW_PROGRESS);
            mTitleLayout.setVisibility(GONE);
            mControlLayout.setVisibility(GONE);
            mShowing = false;
        }
    }


    /**
     * @param resId
     */
    private void showCenterView(int resId) {
        if (resId == R.id.loading_layout_pl) {
            if (loadingLayout.getVisibility() != VISIBLE) {
                loadingLayout.setVisibility(VISIBLE);
            }
            if (mCenterPlayButton.getVisibility() == VISIBLE) {
                mCenterPlayButton.setVisibility(GONE);
            }
            if (errorLayout.getVisibility() == VISIBLE) {
                errorLayout.setVisibility(GONE);
            }
        } else if (resId == R.id.center_play_btn) {
            if (mCenterPlayButton.getVisibility() != VISIBLE) {
                mCenterPlayButton.setVisibility(VISIBLE);
            }
            if (loadingLayout.getVisibility() == VISIBLE) {
                loadingLayout.setVisibility(GONE);
            }
            if (errorLayout.getVisibility() == VISIBLE) {
                errorLayout.setVisibility(GONE);
            }

        } else if (resId == R.id.error_layout) {
            if (errorLayout.getVisibility() != VISIBLE) {
                errorLayout.setVisibility(VISIBLE);
            }
            if (mCenterPlayButton.getVisibility() == VISIBLE) {
                mCenterPlayButton.setVisibility(GONE);
            }
            if (loadingLayout.getVisibility() == VISIBLE) {
                loadingLayout.setVisibility(GONE);
            }

        }
    }

    public void changeFullScreen(boolean isFullScreen) {
        this.mIsFullScreen = isFullScreen;
        doScreenChange();
    }

    private void hideCenterView() {
        if (mCenterPlayButton.getVisibility() == VISIBLE) {
            mCenterPlayButton.setVisibility(GONE);
        }
        if (errorLayout.getVisibility() == VISIBLE) {
            errorLayout.setVisibility(GONE);
        }
        if (loadingLayout.getVisibility() == VISIBLE) {
            loadingLayout.setVisibility(GONE);
        }
    }

    public void reset() {
        mCurrentTime.setText("00:00");
        mEndTime.setText("00:00");
        mProgress.setProgress(0);
        mTurnButton.setImageResource(R.drawable.uvv_player_player_btn);
        setVisibility(View.VISIBLE);
        hideLoading();
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mPLVideoView == null || mDragging) {
            return 0;
        }
        int position = (int) mPLVideoView.getCurrentPosition();
        int duration = (int) mPLVideoView.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPLVideoView.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                show(0); // show until hide is called
                handled = false;
                break;
            case MotionEvent.ACTION_UP:
                if (!handled) {
                    handled = false;
                    show(sDefaultTimeout); // start timeout
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(sDefaultTimeout);
                if (mTurnButton != null) {
                    mTurnButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mPLVideoView.isPlaying()) {
                mPLVideoView.start();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mPLVideoView.isPlaying()) {
                mPLVideoView.pause();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
                || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    private void updatePausePlay() {
        if (mPLVideoView != null && mPLVideoView.isPlaying()) {
            mTurnButton.setImageResource(com.universalvideoview.R.drawable.uvv_stop_btn);
//            mCenterPlayButton.setVisibility(GONE);
        } else {
            mTurnButton.setImageResource(com.universalvideoview.R.drawable.uvv_player_player_btn);
//            mCenterPlayButton.setVisibility(VISIBLE);
        }
    }

    void updateScaleButton() {
        if (mIsFullScreen) {
            mScaleButton.setImageResource(com.universalvideoview.R.drawable.uvv_star_zoom_in);
        } else {
            mScaleButton.setImageResource(com.universalvideoview.R.drawable.uvv_player_scale_btn);
        }
    }

    void toggleButtons(boolean isFullScreen) {
        mIsFullScreen = isFullScreen;
        updateScaleButton();
        updateBackButton();
    }

    void updateBackButton() {
        mBackButton.setVisibility(mIsFullScreen ? View.VISIBLE : View.INVISIBLE);
    }


    boolean isFullScreen() {
        return mIsFullScreen;
    }


    /**
     *
     */
    private void doPauseResume() {
        if (mPLVideoView.isPlaying()) {
            mPLVideoView.pause();
            doPause();
        } else {

            mPLVideoView.start();
            doStart();
        }
        updatePausePlay();
    }

    @Override
    public void setEnabled(boolean enabled) {
//        super.setEnabled(enabled);
        if (mTurnButton != null) {
            mTurnButton.setEnabled(enabled);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        if (mScalable) {
            mScaleButton.setEnabled(enabled);
        }
        mBackButton.setEnabled(true);// 全屏状态下右上角的返回键总是可用.
    }

    @Override
    public void setAnchorView(View view) {

    }


    public void showLoading() {
        mHandler.sendEmptyMessage(SHOW_LOADING);
    }

    public void hideLoading() {
        mHandler.sendEmptyMessage(HIDE_LOADING);
    }

    public void showError() {
        mHandler.sendEmptyMessage(SHOW_ERROR);
    }

    public void hideError() {
        mHandler.sendEmptyMessage(HIDE_ERROR);
    }

    public void showComplete() {
        mHandler.sendEmptyMessage(SHOW_COMPLETE);
    }

    public void hideComplete() {
        mHandler.sendEmptyMessage(HIDE_COMPLETE);
    }

    public void setTitle(String titile) {
        mTitle.setText(titile);
    }


    public void setOnErrorView(int resId) {
        errorLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(resId, errorLayout, true);
    }

    public void setOnErrorView(View onErrorView) {
        errorLayout.removeAllViews();
        errorLayout.addView(onErrorView);
    }

    public void setOnLoadingView(int resId) {
        loadingLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(resId, loadingLayout, true);
    }

    public void setOnLoadingView(View onLoadingView) {
        loadingLayout.removeAllViews();
        loadingLayout.addView(onLoadingView);
    }

    public void setOnErrorViewClick(View.OnClickListener onClickListener) {
        errorLayout.setOnClickListener(onClickListener);
    }

    public void setPauseCallBack(PLPlayView.VideoPauseCallBack pauseCallBack) {
        this.pauseCallBack = pauseCallBack;
    }

    public void setStartCallBack(PLPlayView.VideoStartCallBack startCallBack) {
        this.startCallBack = startCallBack;
    }


    public void setScreenChangeCallback(PLPlayView.ScreenChangeCallback screenChangeCallback) {
        this.screenChangeCallback = screenChangeCallback;
    }


    public void setFullSreenBackCallback(PLPlayView.FullSreenBackCallback fullSreenBackCallback) {
        this.fullSreenBackCallback = fullSreenBackCallback;
    }

    /**
     *
     */
    private void doPause() {
        if (pauseCallBack != null) {
            pauseCallBack.onPlayPause();
        }
    }

    /**
     *
     */
    private void doStart() {
        if (startCallBack != null) {
            startCallBack.onPlayStart();
        }
    }


    /**
     *
     */
    private void doScreenChange() {
        if (screenChangeCallback != null) {
            screenChangeCallback.onScaleChange(mIsFullScreen);
        }
    }


    private void doBack() {
        if (fullSreenBackCallback != null) {
            fullSreenBackCallback.onBack();
        }
    }
}
