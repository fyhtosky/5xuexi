package com.sj.yinjiaoyun.xuexi.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 
 * @author 锟斤拷锟斤拷锟斤拷
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "5_xuexi";
	private static final int DB_VERSION = 1;
	private static DBHelper mInstance;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public DBHelper(Context context, int version) {
		super(context, DB_NAME, null, version);
	}
	
	public synchronized static DBHelper getInstance(Context context) { 
		if (mInstance == null) { 
		     mInstance = new DBHelper(context); 
		} 
		return mInstance; 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		/**
		 * 聊天记录
		 */
		String sql_msg = "Create table IF NOT EXISTS " + DBcolumns.TABLE_MSG
				+ "(" + DBcolumns.MSG_ID + " integer primary key autoincrement,"
				+ DBcolumns.MSG_FROM + " text," 
				+ DBcolumns.MSG_TO + " text," 
				+ DBcolumns.MSG_TYPE + " text,"
				+ DBcolumns.MSG_CONTENT + " text,"
				+ DBcolumns.MSG_ISCOMING + " integer,"
				+ DBcolumns.MSG_DATE + " text,"
				+ DBcolumns.MSG_ISREADED + " text);";

		
		/**
		 * 最近联系人
		 */
		String sql_session = "Create table IF NOT EXISTS "
				+ DBcolumns.TABLE_SESSION + "(" + DBcolumns.SESSION_id + " integer primary key AUTOINCREMENT,"
				+ DBcolumns.SESSION_FROM + " text,"
				+ DBcolumns.SESSION_TYPE + " text,"
				+ DBcolumns.SESSION_TIME + " text,"
				+ DBcolumns.SESSION_TO + " text,"
				+ DBcolumns.SESSION_CONTENT + " text,"
				+ DBcolumns.SESSION_ISDISPOSE + " text);";
		/**
		 * 最近回话
		 */
		String sql_msg_row ="Create table IF NOT EXISTS "
				+ DBcolumns.TABLE_ROW + "(" + DBcolumns.TABLE_ROW_ID + " integer primary key AUTOINCREMENT,"
		        + DBcolumns .TABLE_ROW_SENDER + " text,"
		        + DBcolumns .TABLE_ROW_RECEIVER + " text,"
		        + DBcolumns .TABLE_ROW_MSG + " text,"
		        + DBcolumns .TABLE_ROW_SYSTEM + " text,"
		        + DBcolumns .TABLE_ROW_MSG_TYPE + " text,"
		        + DBcolumns .TABLE_ROW_CREATE_TIME + " text,"
		        + DBcolumns .TABLE_ROW_MSG_COUNT + " text,"
		        + DBcolumns.TABLE_ROW_MSG_NAME + " text,"
		        + DBcolumns.TABLE_ROW_MSG_LOGO + " text,"
		        + DBcolumns.TABLE_ROW_SENDER_NAME + " text)";
		
		String sql_notice = "Create table IF NOT EXISTS "
				+ DBcolumns.TABLE_SYS_NOTICE + "(" + DBcolumns.SYS_NOTICE_ID + " integer primary key AUTOINCREMENT,"
				+ DBcolumns.SYS_NOTICE_TYPE + " text,"
				+ DBcolumns.SYS_NOTICE_FROM + " text,"
				+ DBcolumns.SYS_NOTICE_FROM_HEAD + " text,"
				+ DBcolumns.SYS_NOTICE_CONTENT + " text,"
				+ DBcolumns.SYS_NOTICE_ISDISPOSE + " text);";
		
		
		db.execSQL(sql_msg);
		db.execSQL(sql_session);
		db.execSQL(sql_msg_row);
		db.execSQL(sql_notice);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
