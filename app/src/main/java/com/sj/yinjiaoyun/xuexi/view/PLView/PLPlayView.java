package com.sj.yinjiaoyun.xuexi.view.PLView;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.pili.pldroid.player.widget.PLVideoView;

/**
 * 项目名称：xuexi
 * 类描述：七牛播放器
 * 创建人：zhangchunzhe
 * 创建时间：2018/5/24 下午3:41
 * 修改人：zhangchunzhe
 * 修改时间：2018/5/24 下午3:41
 * 修改备注：
 */

public class PLPlayView extends FrameLayout {

    private Context mContext;

    private PLVideoTextureView mPLVideoView;
    //
    private PLMediaController mediaController;
    private VideoPauseCallBack pauseCallBack;
    private VideoStartCallBack startCallBack;
    private ScreenChangeCallback screenChangeCallback;
    private FullSreenBackCallback sreenBackCallback;

    public PLPlayView(Context context) {
        super(context);
        initView(context);
    }

    public PLPlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PLPlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PLPlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    public PLVideoTextureView getmPLVideoView() {
        return mPLVideoView;
    }

    /**
     * @param context
     */
    private void initView(Context context) {
        mContext = context;
        addPlayView();
        addControlerView();
        mediaController.setVisibility(GONE);
        mediaController.setMediaPlayer(mPLVideoView);
    }


    /**
     *
     */
    private void addControlerView() {
        mediaController = new PLMediaController(mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(0, 0, 0, 0);
        addView(mediaController, layoutParams);
    }


    /**
     *
     */
    private void addPlayView() {
        mPLVideoView = new PLVideoTextureView(mContext);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(mPLVideoView, layoutParams);


        //设置屏幕比例为适应屏幕
        mPLVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        AVOptions avOptions = new AVOptions();
        avOptions.setInteger(AVOptions.KEY_VIDEO_DATA_CALLBACK, 1);
        avOptions.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 1000);
        avOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        avOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);// 设置为点播流
        avOptions.setInteger(AVOptions.KEY_MEDIACODEC, 2);
        mPLVideoView.setAVOptions(avOptions);
        mPLVideoView.setOnBufferingUpdateListener(new PLOnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(int i) {

            }
        });
    }


    /**
     * @param onErrorListener
     */
    public void setOnPLError(PLOnErrorListener onErrorListener) {
        if (mPLVideoView != null) {
            mPLVideoView.setOnErrorListener(onErrorListener);
        }
    }


    /**
     * @param onInfoListener
     */
    public void setOnOnInfo(PLOnInfoListener onInfoListener) {
        if (mPLVideoView != null) {
            mPLVideoView.setOnInfoListener(onInfoListener);
        }
    }


    /**
     * @param onOnPrepared
     */
    public void setOnOnPrepared(PLOnPreparedListener onOnPrepared) {
        if (mPLVideoView != null) {
            mPLVideoView.setOnPreparedListener(onOnPrepared);
        }
    }


    /**
     * @param onCompletion
     */
    public void setOnCompletion(PLOnCompletionListener onCompletion) {
        if (mPLVideoView != null) {
            mPLVideoView.setOnCompletionListener(onCompletion);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    /**
     *
     */
    public void startPlay() {
        mediaController.setVisibility(VISIBLE);
        mPLVideoView.start();

        if (startCallBack != null) {
            mediaController.hide();
            startCallBack.onPlayStart();
        }
    }


    /**
     * @param isBuffering
     */
    public void isBufferLoading(boolean isBuffering) {
        if (isBuffering) {
            mediaController.showLoading();
        } else {
            mediaController.hideLoading();
        }
    }


    /**
     *
     */
    public void pausePlay() {
        if (mPLVideoView.isPlaying()) {
            mPLVideoView.pause();
            if (pauseCallBack != null) {
                pauseCallBack.onPlayPause();
            }
        }


    }


    /**
     * @param voideName
     */
    public void setVideoTitle(String voideName) {
        mediaController.setTitle(voideName);
    }

    /**
     * 开始播放视频
     *
     * @param url 播放地址
     */
    public void setVoideUrl(String url) {
        isBufferLoading(true);
        mPLVideoView.setVideoPath(url);
    }

    /**
     *
     */
    public void hiddleControlView() {
        mediaController.setVisibility(GONE);
    }

    /**
     *
     */
    public void showControlView() {
        mediaController.setVisibility(VISIBLE);
    }


    public void startUpdateProgress() {
        mediaController.hide();
    }

    /**
     * '
     *
     * @param isFullScreen
     */
    public void changeScreen(boolean isFullScreen) {
        mediaController.changeFullScreen(isFullScreen);
    }


    public void setPauseCallBack(VideoPauseCallBack pauseCallBack) {
        mediaController.setPauseCallBack(pauseCallBack);
        this.pauseCallBack = pauseCallBack;
    }

    public void setStartCallBack(VideoStartCallBack startCallBack) {
        mediaController.setStartCallBack(startCallBack);
        this.startCallBack = startCallBack;
    }

    public void setScreenChangeCallback(ScreenChangeCallback screenChangeCallback) {
        this.screenChangeCallback = screenChangeCallback;
        mediaController.setScreenChangeCallback(screenChangeCallback);
    }

    /**
     * @param sreenBackCallback
     */
    public void setSreenBackCallback(FullSreenBackCallback sreenBackCallback) {
        this.sreenBackCallback = sreenBackCallback;
        mediaController.setFullSreenBackCallback(sreenBackCallback);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     *
     */
    public interface VideoStartCallBack {
        void onPlayStart();
    }

    /**
     *
     */
    public interface VideoPauseCallBack {
        void onPlayPause();
    }

    /**
     *
     */
    public interface ScreenChangeCallback {
        void onScaleChange(boolean isFullscreen);
    }


    /**
     *
     */
    public interface FullSreenBackCallback {
        void onBack();
    }
}
