package com.sj.yinjiaoyun.xuexi.activity;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.PLOnSeekCompleteListener;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.view.PLView.PLPlayView;

/**
 * 项目名称：xuexi
 * 类描述：
 * 创建人：zhangchunzhe
 * 创建时间：2018/5/25 下午2:03
 * 修改人：zhangchunzhe
 * 修改时间：2018/5/25 下午2:03
 * 修改备注：
 */
public abstract class PlayBaseActivity extends MyBaseActivity {

    protected PLPlayView mVideoView;
    protected boolean isFullscreen;
    protected int cachedHeight;
    protected String mPlayUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID());
        mVideoView = findPlayView();
        if (mVideoView == null) {
            try {
                throw new Exception("播放器控件为空，请实现findPlayView方法");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        init();
    }

    protected abstract int layoutID();

    // 初始化七牛播放器
    private void init() {
        mVideoView.setPauseCallBack(new PLPlayView.VideoPauseCallBack() {
            @Override
            public void onPlayPause() {
                if (mPlayUrl == null) return;
                onVoidePause();
            }
        });

        mVideoView.setStartCallBack(new PLPlayView.VideoStartCallBack() {
            @Override
            public void onPlayStart() {
                if (mPlayUrl == null) return;
                onVoideStart();
            }
        });

        mVideoView.setScreenChangeCallback(new PLPlayView.ScreenChangeCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
                onScreenChange(isFullscreen);

            }
        });

        mVideoView.setSreenBackCallback(new PLPlayView.FullSreenBackCallback() {
            @Override
            public void onBack() {
                mVideoView.changeScreen(false);
            }
        });
        mVideoView.getmPLVideoView().setOnSeekCompleteListener(new PLOnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {

            }
        });

        //监听播放完成的事件。
        mVideoView.setOnCompletion(new PLOnCompletionListener() {
            @Override
            public void onCompletion() {
                onPlayCompletion();
            }
        });
        //监听播放发生错误时候的事件。
        mVideoView.setOnPLError(new PLOnErrorListener() {
            @Override
            public boolean onError(int extra) {
                //  发生错误隐藏进度条
                mVideoView.isBufferLoading(false);

                switch (extra) {
                    case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                        ToastUtil.showLongToast(PlayBaseActivity.this, "文件或网络异常");
                        break;
                    case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                        ToastUtil.showLongToast(PlayBaseActivity.this, "打开播放器失败");
                        break;
                    case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                        ToastUtil.showLongToast(PlayBaseActivity.this, "拖动播放进度失败");
                        break;
                    default:
                        ToastUtil.showLongToast(PlayBaseActivity.this, "播放失败,位置错误");
                        break;
                }
                return onPlayError(extra);
            }
        });
        mVideoView.setOnOnPrepared(new PLOnPreparedListener() {

            @Override
            public void onPrepared(int i) {
                mVideoView.startUpdateProgress();
                onPlayPrepared(i);
            }
        });

        mVideoView.setOnOnInfo(new PLOnInfoListener() {
            @Override
            public void onInfo(int i, int i1) {

                Log.i("setOnOnInfo", "OnInfo, what = " + i + ", extra = " + i1);
                switch (i) {
                    case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
                        break;
                    case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                        mVideoView.isBufferLoading(false);
                        break;
                    case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
                        // 视频首次渲染开始
                        startToSeek();
                        break;
                    case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
                        break;
                    case PLOnInfoListener.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                        break;
                    case PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                        break;
                    case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
                        break;
                    case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
                        break;
                    case PLOnInfoListener.MEDIA_INFO_METADATA:
                        break;
                    case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:
                    case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS:
                        break;
                    case PLOnInfoListener.MEDIA_INFO_CONNECTED:
                        break;
                    case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                        break;
                    default:
                        break;
                }
                onPlayInfo(i, i1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFullscreen) {
            mVideoView.changeScreen(false);
        } else {
            super.onBackPressed();
        }
    }

    private void onScreenChange(boolean isFullScreen) {
        this.isFullscreen = isFullScreen;

        ViewGroup group = (ViewGroup) mVideoView.getParent();
        if (isFullscreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoView.setLayoutParams(layoutParams);
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i) != mVideoView) {
                    group.getChildAt(i).setVisibility(View.GONE);
                }
            }

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            try {
                ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = cachedHeight;
                mVideoView.setLayoutParams(layoutParams);

                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i) != mVideoView) {
                        group.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }
        switchTitleBar(!isFullscreen);
        screenChange(isFullScreen);
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


    protected abstract PLPlayView findPlayView();

    protected abstract void onVoidePause();

    protected abstract void onVoideStart();

    protected abstract void screenChange(boolean isFullScreen);


    /**
     * 监听播放发生错误时候的事件。
     *
     * @param i
     * @return
     */
    protected abstract boolean onPlayError(int i);

    /**
     * //监听播放完成的事件。
     */
    protected abstract void onPlayCompletion();

    /**
     * @param i
     * @param i1
     */
    protected abstract void onPlayInfo(int i, int i1);

    /**
     * @param i
     */
    protected abstract void onPlayPrepared(int i);


    /**
     *  首次渲染视频画面的时候，去SeekTo
     */
    protected abstract void startToSeek();


}
