package com.sj.yinjiaoyun.xuexi.xmppmanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.EmptyEvent;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.utils.MD5;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.StreamError;

import me.xiaopan.android.net.NetworkUtils;


/**
 * @author baiyuliang
 * 监听xmpp连接状态的监听器
 */
public class CheckConnectionListener implements ConnectionListener{

	private static final String TAG ="CheckConnectionListener" ;
	private  String mAccount;
	private  String mPassword;
	public CheckConnectionListener(Context context){
		 mAccount="5f_"+ PreferencesUtils.getSharePreStr(context,"username");
		 mPassword = PreferencesUtils.getSharePreStr(context, "pwd");
	}

	@Override
	public void connectionClosed() {
		Logger.d("TaxiConnectionListener", "连接关闭");
	}




		@Override
	public void connectionClosedOnError(Exception e) {
			Logger.d("TaxiConnectionListener", "连接关闭异常");
			if(e instanceof  XMPPException){
				XMPPException xe = (XMPPException) e;
				final StreamError error = xe.getStreamError();
				String errorCode ;
				if (error!=null) {
					errorCode = error.getCode();// larosn 0930
					Logger.d("TaxiConnectionListener", "连接断开，错误码" + errorCode);
					if (errorCode.equalsIgnoreCase("conflict")) {// 被踢下线
						EventBus.getDefault().post(new EmptyEvent(true));
						return;
					}
				}
			}
            if (!NetworkUtils.isConnectedByState(MyApplication.getContext())) {
                Log.e(TAG,"网络异常");
                return;
            }
			/**
			 * 断线重连
			 */
			ReconnectAsync connAsync = new ReconnectAsync();
			connAsync.execute();

		}

	@Override
	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 断线重新连接
	 */
	public void reconnect() {
		if(MyApplication.xmppConnection!=null){
			MyApplication.xmppConnection.disconnect();
			MyApplication.xmppConnection=null;
		}
		 XMPPConnection conn =XmppConnectionManager.getInstance().init();
		String MDPwd= MD5.getMessageDigest(mPassword.getBytes());
		Logger.d("断线重连");
		try {
			conn.connect();
			conn.login(mAccount, MDPwd);
			conn.addConnectionListener(CheckConnectionListener.this);
			conn.addPacketListener(new MyPacketListener(),null);
			conn.getChatManager().addChatListener(new MyChatManagerListener());
			if(conn.isConnected()){
				Logger.d("重连成功");
				if(conn.isAuthenticated()){
					Logger.d("XMPP身份验证成功！");
					MyApplication.xmppConnection=conn;
				}else{
					Logger.d("XMPP身份验证失败，重新登陆！");
					reconnect();


				}
			}else {
				reconnect();
			}
		} catch (XMPPException e) {
			e.printStackTrace();
			Logger.d("重新连接失败！");
			reconnect();
		}
	}
	public class ReconnectAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			reconnect();
			return null;
		}
	}


}
