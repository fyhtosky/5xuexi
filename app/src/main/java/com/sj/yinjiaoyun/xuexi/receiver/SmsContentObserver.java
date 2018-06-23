package com.sj.yinjiaoyun.xuexi.receiver;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.sj.yinjiaoyun.xuexi.Event.SmsEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/5/22.
 */
public class SmsContentObserver extends ContentObserver {
    private static final String TAG = "SmsContentObserver";
    //发送验证码的号码
    private static final String VERIFY_CODE_FROM = "106905111506";
    //6位纯数字验证码
    private static final String PATTERN_CODER = "(?<![0-9])([0-9]{6})(?![0-9])";
    private Context mContext;
    private Handler mHandler;


    public SmsContentObserver(Context context, Handler handler) {
        super(handler);
        this.mContext = context;
        this.mHandler = handler;
    }


    @Override
    public void onChange(boolean selfChange) {
//        Uri inBoxUri = Uri.parse("content://sms/inbox");
        Uri inBoxUri= Uri.parse("content://sms");
        Cursor c = mContext.getContentResolver().query(inBoxUri, null,
                null, null, "date desc");
        if (c != null) {
            //只取最新的一条短信
            if (c.moveToNext()) {
                String number = c.getString(c.getColumnIndex("address"));//手机号
                Log.d(TAG, "number:"+number);
                String body = c.getString(c.getColumnIndex("body"));
                Log.d(TAG, "body:"+body);
                if (number.equals(VERIFY_CODE_FROM)) {
                    String verifyCode = patternCode(body);
//                    Message msg = Message.obtain();
//                    msg.what = Const.MSG_RECEIVED_CODE;
//                    msg.obj = verifyCode;
//                    mHandler.sendMessage(msg);
                    if(verifyCode!=null){
                        EventBus.getDefault().post(new SmsEvent(verifyCode));
                    }
                }
            }
            c.close();
        }
    }

    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(PATTERN_CODER);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
