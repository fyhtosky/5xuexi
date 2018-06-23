package com.sj.yinjiaoyun.xuexi.manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.ApkBean;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * app内置自动更新工具类
 * 检查版本号 是否需要更新
 */
public class UpdateManager {

	private Context mContext;
    String TAG="UpDate";
	private HttpDemo demo;

	//提示语
	private String updateMsg = "有最新的软件包哦，亲快下载吧~";

	//返回的安装包url
	private String apkUrl;

	private Dialog noticeDialog;

	private Dialog downloadDialog;
	 /* 下载包安装路径 */
//    private static final String savePath =  MyApplication.getContext().getExternalCacheDir().toString()+"/";
//	private static final String saveFileName = savePath + "5xuexi.apk";

	private  String savePath ;
	private  String saveFileName;

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;


	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private Handler mHandler = new Handler(Looper.getMainLooper()){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DOWN_UPDATE://正在下载
					mProgress.setProgress(progress);
					break;
				case DOWN_OVER://下载完成
					installApk();
//					uninstallAPK();//安装之后卸载
					break;
				default:
					break;
			}
		}
	};

	public UpdateManager(Context context) {
		this.mContext =context;
		savePath =  getDiskCacheDir(context).toString()+"/";
		saveFileName = savePath + "5xuexi.apk";

	}
	public String getDiskCacheDir(Context context) {
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = Environment.getExternalStorageDirectory().getPath();
		} else {
			cachePath = context.getExternalCacheDir().getPath();
		}
		return cachePath;
	}


	//外部接口让主Activity调用
	private void checkUpdateInfo(String apkUrl){
		Log.i(TAG, "checkUpdateInfo:更新apk "+apkUrl);
		this.apkUrl=apkUrl;
		showNoticeDialog(apkUrl);
	}

	//获取网络接口版本号
	public void checkHttpVersion(HttpDemo.HttpCallBack callback){
		mContext= (Context) callback;
		demo=new HttpDemo(callback);
        String upHttpUrl= MyConfig.getURl("app/getAppVersion");
		List<Pairs> pairsList=new ArrayList<>();
		pairsList.add(new Pairs("phoneType","android"));
		pairsList.add(new Pairs("app","5xuexi"));
		demo.doHttpGet(upHttpUrl,pairsList,MyConfig.CODE_GET_UPDATE);
	}



    //提示框  下载 或者 以后再说
	public void showNoticeDialog(final String apkUrl){
		this.apkUrl=apkUrl;
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setMessage(updateMsg);
		builder.setCancelable(false);
		builder.setPositiveButton("下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog(apkUrl);
			}
		});
//		builder.setNegativeButton("以后再说", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	//提示框（下载完成后） 版本是否更新
	public void showDownloadDialog(String apkUrl){
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("软件版本更新");
		builder.setCancelable(false);

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);

		builder.setView(v);
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				interceptFlag = true;
//			}
//		});
		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk(apkUrl);
	}

	/**
	 * 另开线程 下载apk
	 */
	public void downloadApk(String apkUrl){
		Log.i(TAG, "downloadApk: ");
		com.orhanobut.logger.Logger.d(apkUrl);
		if(apkUrl!=null && !TextUtils.isEmpty(apkUrl)){
			downLoadThread = new Thread(mdownApkRunnable);
			downLoadThread.start();
		}

	}

	//下载安装包
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				Log.i(TAG, "run: "+apkUrl);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if(!file.exists()){
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do{
					int numread = is.read(buf);
					count += numread;
					progress =(int)(((float)count / length) * 100);
					//更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if(numread <= 0){
						//下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf,0,numread);
				}while(!interceptFlag);//点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	};



	/**
	 * 安装apk
	 * @param
	 */
	private void installApk(){
		Log.i(TAG, "installApk: ");
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		//版本更新之后出现引导页
		PreferencesUtils.putSharePre(mContext, Const.ISFIRST,false);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		if(Build.VERSION.SDK_INT>=24){
			// Android7.0及以上版本
			Uri uri = FileProvider.getUriForFile(mContext, "com.sj.yinjiaoyun.xuexi.fileProvider", apkfile);
			intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
		}else {
			// Android7.0以下版本
			intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		mContext.startActivity(intent);
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//		mContext.startActivity(i);
	}



	/**
	 * 强制卸载某个包名（丢失jks的旧版本app）的程序
	 * @param
     */
	public void uninstallAPK(){
		Log.i(TAG, "uninstallAPK: ");
		String packageName="com.sj.yinjiaoyun.learn";//以前前版本包名
		PackageManager pageManage = mContext.getPackageManager();
		List<PackageInfo> packages = pageManage.getInstalledPackages(0);
		for(int i=0;i<packages.size();i++) {
			PackageInfo packageInfo = packages.get(i);
			String pagName = packageInfo.packageName;
			if (pagName.equals(packageName)) {//判断该应用是否存在
				Log.i(TAG, "uninstallAPK: " + "卸载包名：" + packageName);
				Uri uri = Uri.parse("package:" + packageName);
				Intent intent = new Intent(Intent.ACTION_DELETE, uri);
				mContext.startActivity(intent);
			}
		}

	}


    //获取当前app的版本号
	public int getNewVersion(Context context){
		int versionCode=0;
		try {
//			versionCode=context.getPackageManager().getPackageInfo("com.sj.yinjiaoyun.xuexi",0).versionCode;
			versionCode=context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * //比对本地版本和网络版本比对，然后判断是否需要跟新
	 * @param versionHttp 网络版本号
	 * @param apkUrl     更新路径
	 * @param flag       是否需要提示 1需要 2不需要
	 */
	public void checkIsUpdate(int versionHttp,String apkUrl,int flag){
		int versionLocation=getNewVersion(mContext);
		Log.i(TAG, "判断版本号checkIsUpdate: 网络versionHttp："+versionHttp+" 本地versionLocation:"+versionLocation+" : "+apkUrl);
		if(versionHttp > versionLocation){
			checkUpdateInfo(apkUrl);
		}else{
			if(flag==1){
				Toast.makeText(mContext, "当前版本已是最新版本啦", Toast.LENGTH_SHORT).show();
			}
		}
	}


	//返回app的版本名称
	public String getVersionName(){
		String name="";
		try {
//			name=mContext.getPackageManager().getPackageInfo("com.sj.yinjiaoyun.xuexi",0).versionName;
			name=mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "getAppName: app版本名称"+name);
		return name;
	}

}
