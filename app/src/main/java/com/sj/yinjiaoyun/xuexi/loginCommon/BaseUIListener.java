//package com.sj.yinjiaoyun.xuexi.loginCommon;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.UiError;
//
//import org.json.JSONObject;
//
//public abstract class BaseUIListener implements IUiListener {
//	private Context mContext;
//	private String mScope;
//	private boolean mIsCaneled;
//	private static final int ON_COMPLETE = 0;
//	private static final int ON_ERROR = 1;
//	private static final int ON_CANCEL = 2;
//	private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//            case ON_COMPLETE:
//                JSONObject response = (JSONObject)msg.obj;
//				showResult("onComplete", response.toString());
//				break;
//            case ON_ERROR:
//                UiError e = (UiError)msg.obj;
//				showResult("onError", "errorMsg:" + e.errorMessage + "errorDetail:" + e.errorDetail);
//                break;
//            case ON_CANCEL:
//				showResult("onCancel", "");
//                break;
//            }
//        }
//	};
//
//	/**
//	 * 登录成功之后的回调获取相关信息
//	 * @param onCancel
//	 * @param s
//	 */
//	protected abstract void showResult(String onCancel, String s) ;
//
//
//	public BaseUIListener(Context mContext) {
//		super();
//		this.mContext = mContext;
//	}
//
//	public BaseUIListener() {
//	}
//
//	public BaseUIListener(Context mContext, String mScope) {
//		super();
//		this.mContext = mContext;
//		this.mScope = mScope;
//	}
//
//	public void cancel() {
//		mIsCaneled = true;
//	}
//
//
//	@Override
//	public void onComplete(Object response) {
//		if (mIsCaneled) return;
//	    Message msg = mHandler.obtainMessage();
//	    msg.what = ON_COMPLETE;
//	    msg.obj = response;
//	    mHandler.sendMessage(msg);
//	}
//
//	@Override
//	public void onError(UiError e) {
//		if (mIsCaneled) return;
//	    Message msg = mHandler.obtainMessage();
//        msg.what = ON_ERROR;
//        msg.obj = e;
//        mHandler.sendMessage(msg);
//	}
//
//	@Override
//	public void onCancel() {
//		if (mIsCaneled) return;
//	    Message msg = mHandler.obtainMessage();
//        msg.what = ON_CANCEL;
//        mHandler.sendMessage(msg);
//	}
//
//	public Context getmContext() {
//		return mContext;
//	}
//
//	public void setmContext(Context mContext) {
//		this.mContext = mContext;
//	}
//
//}
