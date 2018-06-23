package com.sj.yinjiaoyun.xuexi.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.ChatAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.FaceVPAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.db.ChatMsgDao;
import com.sj.yinjiaoyun.xuexi.db.SessionDao;
import com.sj.yinjiaoyun.xuexi.entry.Msg;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.entry.Session;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.ExpressionUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.widget.DropdownListView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.sj.yinjiaoyun.xuexi.xmppmanager.XmppConnectionManager;
import com.sj.yinjiaoyun.xuexi.xmppmanager.XmppUtil;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity2 extends MyBaseActivity implements View.OnClickListener,DropdownListView.OnRefreshListenerHeader {

    private ViewPager mViewPager;
    private LinearLayout mDotsLayout;
    private EditText input;
    private TextView send;
    private DropdownListView mListView;
    private ChatAdapter mLvAdapter;
    private ChatMsgDao msgDao;
    private SessionDao sessionDao;
    private MsgOperReciver msgOperReciver;
    private LinearLayout chat_face_container,chat_add_container ,ll_back;
    private ImageView image_face;//表情图标
    private ImageView image_add;//更多图标

    private TextView tv_title,tv_pic,//图片
            tv_camera,//拍照
            tv_loc;//位置

    //表情图标每页6列4行
    private int columns = 6;
    private int rows = 4;
    //每页显示的表情view
    private List<View> views = new ArrayList<>();
    //表情列表
    private List<String> staticFacesList;
    //消息
    private List<Msg> listMsg=new ArrayList<>();
    private SimpleDateFormat sd;
    private BroadcastReceiver receiver;
    private LayoutInflater inflater;
    private int offset;
    private String I,YOU;//为了好区分，I就是自己，YOU就是对方
   private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           switch (msg.what){
               case 0:
                   offset++;
                  initData();
                   break;
           }
       }
   };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        //监听服务器发送过来的消息
        addListener();
        I= "5f_"+PreferencesUtils.getSharePreStr(this, "username");
//        YOU=getIntent().getStringExtra("from");
        YOU="5f_3494";
        inflater = LayoutInflater.from(ChatActivity2.this);
        sd=new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        //聊天记录本地保存的数据库
        msgDao=new ChatMsgDao(this);
        //聊天记录展示到最近回话列表中
        sessionDao=new SessionDao(this);
        //粘贴复制广播监听
        msgOperReciver=new MsgOperReciver();
        IntentFilter intentFilter=new IntentFilter(Const.ACTION_MSG_OPER);
        registerReceiver(msgOperReciver, intentFilter);
        //初始化表情包的集合
        staticFacesList= ExpressionUtil.initStaticFaces(this);
        //初始化控件
        initViews();
        //初始化表情
        initViewPager();
        //初始化更多选项（即表情图标右侧"+"号内容）
        initAdd();

    }
    /**
     * 查询数据库获取集合按照时间排序
     */
    private void sort(){
        //对集合经行排序
        Collections.sort(listMsg, new Comparator<Msg>() {
            @Override
            public int compare(Msg msg, Msg msg1) {
                long time = 0;
                long time1=0;
                try {
                    time = sd.parse(msg.getDate()).getTime();
                    time1 = sd.parse(msg1.getDate()).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (time > time1) {

                    return 1;
                } else if (time < time1) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });
        Logger.d("数据排序完成:"+listMsg.size());
    }
    /**
     * 添加监听器监服务器发送过来的消息
     */
    private void addListener() {
        new Thread(){
            @Override
            public void run() {
                if(MyApplication.xmppConnection==null || !MyApplication.xmppConnection.isAuthenticated()){
                    MyApplication.xmppConnection=XmppConnectionManager.getInstance().init();
                }
                ChatManager chatmanager = MyApplication.xmppConnection.getChatManager();
                chatmanager.addChatListener(new ChatManagerListener() {
                   @Override
                   public void chatCreated(Chat chat, boolean b) {
                       chat.addMessageListener(new MessageListener() {
                           @Override
                           public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
                               try{
                                   String msgBody=message.getBody();
                                   if(TextUtils.isEmpty(msgBody)){
                                       return;
                                   }
                                   String to=message.getTo().split("@")[0];//接收者
                                   String from=message.getFrom().split("@")[0];//发送者
                                   Logger.d("message:"+msgBody+"  接收者TO:"+to+"  发送者FROM:"+from);
                                   //保存聊天记录到后台
                                   SaveChat(msgBody,from,to);
                                   Msg msg = null;
                                   if(msgBody.startsWith("*$img_")  || msgBody.endsWith("_gmi$*")){

                                   }else{
                                      msg=new Msg();
                                       msg.setToUser(to);//接收者
                                       msg.setFromUser(from);//发送者
                                       msg.setIsComing(0);
                                       msg.setContent(msgBody);
                                       msg.setDate(sd.format(new Date()));
                                       msg.setIsReaded("1");//暂且默认为已读
                                       msg.setType(Const.MSG_TYPE_TEXT);
                                   }
//                                   listMsg.add(msg);
                                   msgDao.insert(msg);
                                   handler.sendEmptyMessage(0);
                           }catch (Exception e){
                               e.printStackTrace();
                               }
                           }


                       });
                   }
               });
            }
        }.start();
    }




    /**
     * 初始化控件
     */
    private void initViews() {
        //标题
        tv_title=(TextView) findViewById(R.id.tv_title);
        tv_title.setText(YOU);
        //返回
        ll_back= (LinearLayout) findViewById(R.id.ll_back);
        mListView = (DropdownListView) findViewById(R.id.message_chat_listview);
        //表情图标
        image_face=(ImageView) findViewById(R.id.image_face);
        //更多图标
        image_add=(ImageView) findViewById(R.id.image_add);
        //表情布局
        chat_face_container=(LinearLayout) findViewById(R.id.chat_face_container);
        //更多
        chat_add_container=(LinearLayout) findViewById(R.id.chat_add_container);

        mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        //适配器
        listMsg.clear();
        mLvAdapter = new ChatAdapter(this, listMsg);
        mListView.setAdapter(mLvAdapter);
        mViewPager.setOnPageChangeListener(new PageChange());
        //表情下小圆点
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
        input = (EditText) findViewById(R.id.input_sms);
        send = (TextView) findViewById(R.id.send_sms);
        input.setOnClickListener(this);
        //返回按钮
        ll_back.setOnClickListener(this);
        //表情按钮
        image_face.setOnClickListener(this);
        //更多按钮
        image_add.setOnClickListener(this);
        // 发送
        send.setOnClickListener(this);

        mListView.setOnRefreshListenerHead(this);
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if(arg1.getAction()==MotionEvent.ACTION_DOWN){
                    if(chat_face_container.getVisibility()==View.VISIBLE){
                        chat_face_container.setVisibility(View.GONE);
                    }
                    if(chat_add_container.getVisibility()==View.VISIBLE){
                        chat_add_container.setVisibility(View.GONE);
                    }
                    hideSoftInputView();
                }
                return false;
            }
        });

    }

    public void initAdd(){
        tv_pic=(TextView) findViewById(R.id.tv_pic);
        tv_camera=(TextView) findViewById(R.id.tv_camera);
        tv_loc=(TextView) findViewById(R.id.tv_loc);

        tv_pic.setOnClickListener(this);
        tv_camera.setOnClickListener(this);
        tv_loc.setOnClickListener(this);

    }

    /**
     * 获取聊天记录的数据
     */
    public void initData(){
        listMsg.clear();
        offset=listMsg.size();
        listMsg.addAll(msgDao.queryMsg(YOU,I,offset));
        listMsg.addAll(msgDao.queryMsg(I,YOU,offset+1));
        sort();
        mLvAdapter.notifyDataSetChanged();
        mListView.setSelection(listMsg.size());



    }

    /**
     * 初始化表情
     */
    private void initViewPager() {
        int pagesize= ExpressionUtil.getPagerCount(staticFacesList.size(),columns,rows);
        // 获取页数
        for (int i = 0; i <pagesize; i++) {
            views.add(ExpressionUtil.viewPagerItem(this, i, staticFacesList,columns, rows, input));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        mViewPager.setAdapter(mVpAdapter);
        mDotsLayout.getChildAt(0).setSelected(true);
    }

    /**
     * 表情页切换时，底部小圆点
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }



    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.send_sms:
                String content=input.getText().toString();
                if(TextUtils.isEmpty(content)){
                    return;
                }
                //发送消息
                sendMsgText(content);
                break;
            case R.id.input_sms:
                if(chat_face_container.getVisibility()==View.VISIBLE){
                    chat_face_container.setVisibility(View.GONE);
                }
                if(chat_add_container.getVisibility()==View.VISIBLE){
                    chat_add_container.setVisibility(View.GONE);
                }
                break;
            case R.id.image_face:
                hideSoftInputView();//隐藏软键盘
                if(chat_add_container.getVisibility()==View.VISIBLE){
                    chat_add_container.setVisibility(View.GONE);
                }
                if(chat_face_container.getVisibility()==View.GONE){
                    chat_face_container.setVisibility(View.VISIBLE);
                }else{
                    chat_face_container.setVisibility(View.GONE);
                }
                break;
            case R.id.image_add:
                hideSoftInputView();//隐藏软键盘
                if(chat_face_container.getVisibility()==View.VISIBLE){
                    chat_face_container.setVisibility(View.GONE);
                }
                if(chat_add_container.getVisibility()==View.GONE){
                    chat_add_container.setVisibility(View.VISIBLE);
                }else{
                    chat_add_container.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_pic://模拟一张图片路径
                sendMsgImg("http://my.csdn.net/uploads/avatar/3/B/9/1_baiyuliang2013.jpg");
                break;
            case R.id.tv_camera://拍照，换个美女图片吧
                sendMsgImg("http://b.hiphotos.baidu.com/image/pic/item/55e736d12f2eb93872b0d889d6628535e4dd6fe8.jpg");
                break;
            case R.id.tv_loc://位置，正常情况下是需要定位的，可以用百度或者高德地图，现设置为北京坐标
                sendMsgLocation("116.404,39.915");
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    /**
     * 执行发送消息 图片类型
     * @param imgpath
     */
    void sendMsgImg(final String imgpath){
        Msg msg=getChatInfoTo(imgpath,Const.MSG_TYPE_IMG);
        msg.setMsgId(msgDao.insert(msg));
        listMsg.add(msg);
        offset=listMsg.size();
        mLvAdapter.notifyDataSetChanged();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XmppUtil.sendMessage(MyApplication.xmppConnection,imgpath, YOU,I);
                } catch (XMPPException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    ToastUtil.showShortToast(ChatActivity2.this, "发送失败");
                    Looper.loop();
                }
            }
        }).start();
        updateSession(Const.MSG_TYPE_TEXT,"[图片]");
    }

    /**
     * 执行发送消息 文本类型
     * @param content
     */
    void sendMsgText(final String content){
        Msg msg=getChatInfoTo(content,Const.MSG_TYPE_TEXT);
        msg.setMsgId(msgDao.insert(msg));
        msgDao.insert(msg);
        listMsg.add(msg);
        offset=listMsg.size();
        mLvAdapter.notifyDataSetChanged();
        input.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XmppUtil.sendMessage(MyApplication.xmppConnection,content, YOU,I);
                } catch (XMPPException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    ToastUtil.showShortToast(ChatActivity2.this, "发送失败");
                    Looper.loop();
                }
            }
        }).start();
        updateSession(Const.MSG_TYPE_TEXT,content);
    }

    /**
     * 执行发送消息 文本类型
     * @param content
     */
    void sendMsgLocation(final String content){
        Msg msg=getChatInfoTo(content,Const.MSG_TYPE_LOCATION);
        msg.setMsgId(msgDao.insert(msg));
        listMsg.add(msg);
        offset=listMsg.size();
        mLvAdapter.notifyDataSetChanged();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XmppUtil.sendMessage(MyApplication.xmppConnection, content, YOU,I);
                } catch (XMPPException e) {
                    e.printStackTrace();
                    Looper.prepare();
                    ToastUtil.showShortToast(ChatActivity2.this, "发送失败");
                    Looper.loop();
                }
            }
        }).start();
        updateSession(Const.MSG_TYPE_TEXT,"[位置]");
        SaveChat(content,I,YOU);
    }
    /**
     * 保存聊天记录
     * 提交后台服务器保存
     */
    private void SaveChat(String message,String sender,String receiver){
        HashMap<String,String>hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("msg",message);
        hashMap.put("msgType","0");
        hashMap.put("system","1");
        HttpClient.post(this, Api.SAVE_CHAT_LOG, hashMap, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if(result.isSuccess()){
                    Logger.d("消息发送f服务器成功");
                }else{
                    ToastUtil.showShortToast(ChatActivity2.this,result.getMessage());
                }
            }
        });

    }
    /**
     * 发送的信息
     *  from为收到的消息，to为自己发送的消息
     * @param message =>
     * @return
     */
    private Msg getChatInfoTo(String message,String msgtype) {
        String time=sd.format(new Date());
        Msg msg = new Msg();
        msg.setFromUser(YOU);
        msg.setToUser(I);
        msg.setType(msgtype);
        msg.setIsComing(1);
        msg.setContent(message);
        msg.setDate(time);
        return msg;
    }

    /**
     * 更新最近会话列表中
     * @param type
     * @param content
     */
    void updateSession(String type,String content){
       Session session=new Session();
        session.setFrom(YOU);
        session.setTo(I);
        session.setNotReadCount("");//未读消息数量
        session.setContent(content);
        session.setTime(sd.format(new Date()));
        session.setType(type);
        if(sessionDao.isContent(YOU, I)){
            sessionDao.updateSession(session);
        }else{
            sessionDao.insertSession(session);
        }
//        //发送广播，通知消息界面更新
//        Intent intent=new Intent(Const.ACTION_ADDFRIEND);
//        sendBroadcast(intent);

    }


    /**
     * 表情页改变时，dots效果也要跟着改变
     * */
    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(arg0).setSelected(true);
        }
    }

    /**
     * 下拉加载更多
     */
    @Override
    public void onRefresh() {
        List<Msg> list=msgDao.queryMsg(YOU,I,offset);
        if(list.size()<=0){
            mListView.setSelection(0);
            mListView.onRefreshCompleteHeader();
            return;
        }
        listMsg.addAll(0,list);
        offset=listMsg.size();
        mListView.onRefreshCompleteHeader();
        mLvAdapter.notifyDataSetChanged();
        mListView.setSelection(list.size());
    }

    /**
     * 弹出输入法窗口
     */
    private void showSoftInputView(final View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 接收消息记录操作广播：删除复制
     * @author baiyuliang
     */
    private class MsgOperReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type", 0);
            final int position=intent.getIntExtra("position", 0);
            if(listMsg.size()<=0){
                return;
            }
            final Msg msg=listMsg.get(position);
            switch (type) {
                case 1://聊天记录操作
                    Builder bd = new AlertDialog.Builder(ChatActivity2.this);
                    String[] items;
                    if(msg.getType().equals(Const.MSG_TYPE_TEXT)){
                        items =  new String[]{"删除记录","删除全部记录","复制文字"};
                    }else{
                        items =  new String[]{"删除记录","删除全部记录"};
                    }
                    bd.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            switch (arg1) {
                                case 0://删除
                                    listMsg.remove(position);
                                    offset=listMsg.size();
                                    mLvAdapter.notifyDataSetChanged();
                                    msgDao.deleteMsgById(msg.getMsgId());
                                    break;
                                case 1://删除全部
//                                    listMsg.removeAll(listMsg);
                                    listMsg.clear();
                                    offset=listMsg.size();
                                    mLvAdapter.notifyDataSetChanged();
                                    msgDao.deleteTableData();
                                    break;
                                case 2://复制
                                    /**
                                     * 粘贴复制
                                     */
                                    ClipboardManager cmb = (ClipboardManager) ChatActivity2.this.getSystemService(ChatActivity.CLIPBOARD_SERVICE);
                                    cmb.setText(msg.getContent());
                                    Toast.makeText(getApplicationContext(), "已复制到剪切板", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                    bd.show();
                    break;
            }

        }
    }



    @Override
    protected void onResume() {
        //初始化数据
        initData();
       handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //让输入框获取焦点
                input.requestFocus();
            }
        }, 100);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(msgOperReciver);
    }

    /**
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideSoftInputView();
            if(chat_face_container.getVisibility()==View.VISIBLE){
                chat_face_container.setVisibility(View.GONE);
            }else if(chat_add_container.getVisibility()==View.VISIBLE){
                chat_add_container.setVisibility(View.GONE);
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
