package com.sj.yinjiaoyun.xuexi.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.fragment.MessageDaYiFragment;
import com.sj.yinjiaoyun.xuexi.fragment.MessageTaolunFragment;
import com.sj.yinjiaoyun.xuexi.fragment.MessageZiXunFragment;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/2.
 * 主页- 我fragment-我的消息页面
 */
public class MyMessageActivity extends MyBaseActivity  {

    private static String TAG="message";
    RadioGroup group;

    TextView tvTiJiao;//全部标为已读
    View icUnderLine;//下划线


    Intent intent;
    String endUserId;

    int contentType=3; // 0：讨论组信息  1：答疑  2：批改作业  3：咨询  4：新建讨论组   （默认值为咨询）
    FragmentManager manager;
    FragmentTransaction tran;

    MessageZiXunFragment messageZiXunFragment;//咨询
    MessageTaolunFragment messageTaolunFragment;//讨论组
    MessageDaYiFragment messageDaYiFragment;//答疑
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mymessage);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
        initEvent();
    }

    private void init() {
        endUserId=  PreferencesUtils.getSharePreStr(MyMessageActivity.this, "username");
        intent=getIntent();
        manager=getFragmentManager();
        createFragment();
        Log.i(TAG, "init: "+endUserId);
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            String type=printBundle(bundle).get("type");
            if(!TextUtils.isEmpty(type)) {
                contentType = Integer.parseInt(type);
            }
            Log.e(TAG, "推送过来的消息: "+"Title : " + title + "  " + "Content : " + content +" "+ " type "+ type +" "+" contentType "+ contentType);
        }
    }

    //创建fragemnt
    private void createFragment() {
        messageZiXunFragment=new MessageZiXunFragment();
        messageZiXunFragment.setDateFromMessageActivity(endUserId);

        messageTaolunFragment=new MessageTaolunFragment();
        messageTaolunFragment.setDateFromMessageActivity(endUserId);

        messageDaYiFragment=new MessageDaYiFragment();
        messageDaYiFragment.setDateFromMessageActivity(endUserId);
    }

    /**
     * // 打印所有的 intent extra 数据
     * @param bundle  通知里面产地过来的值
     * @return
     */
    private  Map<String,String> printBundle(Bundle bundle) {
        Map<String,String> map=new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:").append(key).append(", value: [").append(myKey).append(" - ").append(json.optString(myKey)).append("]");
                        map.put(myKey,json.optString(myKey));//加入map集合
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getString(key));
            }
        }
        Log.i(TAG, "printBundle: "+sb.toString());
        return map;
    }

    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    private void initView() {
        group= (RadioGroup) findViewById(R.id.message_container);
        rb1= (RadioButton) findViewById(R.id.message_rb1);
        rb2= (RadioButton) findViewById(R.id.message_rb2);
        rb3= (RadioButton) findViewById(R.id.message_rb3);
        tvTiJiao= (TextView) findViewById(R.id.message_tijiao);
        icUnderLine=findViewById(R.id.message_underLine);
        getScreenMiDu();//设置下划线
        if(contentType==0 || contentType==4){//讨论组  新建讨论组
            rb2.setChecked(true);
            showFragment(messageTaolunFragment);
            changeAnimation(2);
        }else if(contentType==1){// 1：答疑
            rb3.setChecked(true);
            showFragment(messageDaYiFragment);
            changeAnimation(3);
        }else if(contentType==3){//咨询
            rb1.setChecked(true);
            showFragment(messageZiXunFragment);
            changeAnimation(1);
        }
    }

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    Fragment currentFragment=new Fragment();
    private void showFragment(Fragment fragment){
        FragmentTransaction tran = manager.beginTransaction();
        //如果之前没有添加过
        if(!fragment.isAdded()){
            tran
                    .hide(currentFragment)
                    .add(R.id.message_fragment,fragment,"1");  //第三个参数为添加当前的fragment时绑定一个tag
        }else{
            tran
                    .hide(currentFragment)
                    .show(fragment);
        }
        currentFragment = fragment;
        tran.commit();
    }


    public void onclick(View view){
        switch(view.getId()) {
            case R.id.message_back://返回
                finish();
                break;
        }
    }

    /**
     * 获取屏幕密度  宽度   设置相应的值  设置下划线
     */
    private void getScreenMiDu(){
        // 获取屏幕密度（方法2）
        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        int screenWidth2  = dm2.widthPixels;      // 屏幕宽（像素，如：480px）
//        int screenHeight2 = dm2.heightPixels;     // 屏幕高（像素，如：800px）

        //设置动画图片的图片宽度
        LinearLayout.LayoutParams laParams = (LinearLayout.LayoutParams) icUnderLine.getLayoutParams();
        laParams.width = (screenWidth2/3);
        icUnderLine.setLayoutParams(laParams);
        changeAnimation(1);
    }

    //设置下环线动画
    //账号、学号切换时候的动画效果
    private void changeAnimation(int arg0){
        switch(arg0){
            case 1://表示动画从右到左
                Animation animationL = AnimationUtils.loadAnimation(MyMessageActivity.this, R.anim.translate_three_o);
                icUnderLine.startAnimation(animationL);//开始动画
                animationL.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
            case 2://表示动画从左到右
                Animation animationR = AnimationUtils.loadAnimation(MyMessageActivity.this, R.anim.translate_three_t);
                icUnderLine.startAnimation(animationR);//开始动画
                animationR.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
            case 3://表示动画初始化（登录进来时候）
                Animation animation3 = AnimationUtils.loadAnimation(MyMessageActivity.this, R.anim.translate_three_th);
                icUnderLine.startAnimation(animation3);//开始动画
                animation3.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
        }

    }


    private void initEvent() {
        tvTiJiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setAllIsRead();
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.message_rb1://咨询
                        contentType=3;
                        showFragment(messageZiXunFragment);
                        changeAnimation(1);
                        Log.i(TAG, "onCheckedChanged: 咨询");
                        break;
                    case R.id.message_rb2://讨论组
                        contentType=4;
                        showFragment(messageTaolunFragment);
                        changeAnimation(2);
                        Log.i(TAG, "onCheckedChanged:讨论组 ");
                        break;
                    case R.id.message_rb3://答疑
                        contentType=1;
                        showFragment(messageDaYiFragment);
                        changeAnimation(3);
                        Log.i(TAG, "onCheckedChanged: 答疑");
                        break;
                }
            }
        });


    }


    //设置全部标为已读
    private void setAllIsRead() {
        try {
            if (messageZiXunFragment != null && !(messageZiXunFragment.isHidden())) {
                Log.i(TAG, "setAllIsRead: " + "咨询正在显示");
                messageZiXunFragment.setAllIsReadToHttp();
            } else if (messageTaolunFragment != null && !(messageTaolunFragment.isHidden())) {
                Log.i(TAG, "setAllIsRead: " + "讨论组正在显示");
                messageTaolunFragment.setAllIsReadToHttp();
            } else if (messageDaYiFragment != null && !(messageDaYiFragment.isHidden())) {
                Log.i(TAG, "setAllIsRead: " + "答疑正在显示");
                messageDaYiFragment.setAllIsReadToHttp();
            }
        }catch (Exception e){
            Log.e(TAG, "setAllIsRead: "+e.toString());
        }
    }

}
