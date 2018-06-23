package com.sj.yinjiaoyun.xuexi.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.EmptyEvent;
import com.sj.yinjiaoyun.xuexi.Event.MsgEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.ChatRecordAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.FaceVPAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.ChatPrivateBean;
import com.sj.yinjiaoyun.xuexi.bean.PictureReturnBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.BitmapUtils;
import com.sj.yinjiaoyun.xuexi.utils.DialogUtils;
import com.sj.yinjiaoyun.xuexi.utils.ExpressionUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.widget.DropdownListView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.sj.yinjiaoyun.xuexi.xmppmanager.XmppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends MyBaseActivity implements DropdownListView.OnRefreshListenerHeader, TextWatcher {

    private static final int PICK_PHOTO = 1;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listview)
    DropdownListView listview;
    @BindView(R.id.face_viewpager)
    ViewPager faceViewpager;
    @BindView(R.id.face_dots_container)
    LinearLayout faceDotsContainer;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.input)
    EditText input;
    private LinearLayout chat_face_container;
    //发送者(我)
    private String sender;
    //接受者（别人）
    private String receiver;
    //分页
    private String createTime;
    //每页的数量
    private int row = 20;
    //准备集合存放数据
    private List<ChatPrivateBean.DataBean.MsgPgBean.RowsBean> list = new ArrayList<>();
    //每页显示的表情view
    private List<View> views = new ArrayList<>();
    //表情列表
    private List<String> staticFacesList;
    //表情图标每页6列4行
    private int columns = 6;
    private int size = 4;
    private ViewPager mViewPager;
    private LinearLayout mDotsLayout;
    private LayoutInflater inflater;
    //聊天对象的昵称
    private String realName;
    //聊天对象的LOGO
    private String senderImg;
    private String receiverImg;
    private ChatRecordAdapter chatRecordAdapter;

    //标识是否是发送状态
    private  boolean isText=false;
    //数据的总数
    private int total=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        //注册EventBus
        EventBus.getDefault().register(this);
        init();

    }

    /**
     * 初始化操作
     */
    private void init() {
        inflater = LayoutInflater.from(ChatActivity.this);
        //表情布局
        chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
        //表情
        mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        //表情下小圆点
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
        sender = "5f_" + PreferencesUtils.getSharePreStr(this, "username");
        senderImg=PreferencesUtils.getSharePreStr(this,"userImg");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            receiver = bundle.getString(Const.CHAT_ID);
            realName = bundle.getString(Const.CHAT_NAME);
            receiverImg=bundle.getString(Const.CHAT_LOGO);
        }
        Logger.d("接受者：" + receiver + "  发送者sender:" + sender + " 接受者 realName:" + realName+"接受者的logo："+receiverImg);
        if (!"".equals(realName)) {
            tvTitle.setText(realName );
        } else {
            tvTitle.setText(receiver );
        }
        //初始化表情包的集合
        staticFacesList = ExpressionUtil.initStaticFaces(this);
        //加载适配
        initViewPager();
        //给Edittext添加监听器
        input.addTextChangedListener(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1010)});
        chatRecordAdapter = new ChatRecordAdapter(this, list,receiver);
        listview.setAdapter(chatRecordAdapter);
        //注册listview的下拉刷新
        listview.setOnRefreshListenerHead(this);
        //listvie按下时则关闭对话框
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (chat_face_container.getVisibility() == View.VISIBLE) {
                        chat_face_container.setVisibility(View.GONE);
                    }
                    hideSoftInputView();
                }
                return false;
            }
        });
        //获取请求
        createTime="";
        RequestBean(false);
    }


    /**
     * 账号被挤掉
     * @param emptyEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EmptyEvent emptyEvent){
        if(MyApplication.xmppConnection!=null){
            if(!MyApplication.xmppConnection.isAuthenticated()){
                if(emptyEvent.isLogin){
                    DialogUtils.showNormalDialog(ChatActivity.this);
                }
            }
        }

    }
    /**
     * 回话列表获取消息通知聊天界面刷新
     * @param msgEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MsgEvent msgEvent){
        //私聊记录更新
        if(Message.Type.chat==msgEvent.getType()){
            Logger.d("ChatActivity:message:" + msgEvent.getMsgBody() + "  接收者TO:" + msgEvent.getTo() + "  发送者FROM:" + msgEvent.getFrom());
            if(sender.equals(msgEvent.getFrom())){
                return;
            }
            if(sender.equals(msgEvent.getTo())){
                ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean = new ChatPrivateBean.DataBean.MsgPgBean.RowsBean();
                bean.setCreateTime(new Date().getTime());
                bean.setMsgType(0);
                bean.setMsg(msgEvent.getMsgBody());
                bean.setMsgCount(0);
                bean.setSender(msgEvent.getFrom());
                bean.setReceiver(msgEvent.getTo());
                bean.setMsgName(realName);
                bean.setMsgLogo(receiverImg);
                list.add(bean);
                listview.post(new Runnable() {
                    @Override
                    public void run() {
                        listview.setSelection(chatRecordAdapter.getCount()-1);
                    }
                });
                chatRecordAdapter.notifyDataSetChanged();
                Logger.d("ChatActivity通知私聊界面更新");
            }
        }
        Logger.d("ChatActivity没有新的记录");
    }


    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //让输入框获取焦点
                input.requestFocus();
            }
        }, 100);


    }


    /**
     * 初始化表情
     */
    private void initViewPager() {
        final int pagesize = ExpressionUtil.getPagerCount(staticFacesList.size(), columns, size);
        // 获取页数
        for (int i = 0; i < pagesize; i++) {
            views.add(ExpressionUtil.viewPagerItem(this, i, staticFacesList, columns, size, input));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        mViewPager.setAdapter(mVpAdapter);
        mDotsLayout.getChildAt(0).setSelected(true);
        faceViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<pagesize;i++){
                    mDotsLayout.getChildAt(i).setSelected(false);
                }
                mDotsLayout.getChildAt(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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


    /**
     * 发送请求获取数据
     */
    private void RequestBean(final boolean onLoadMore) {
        String params = "?jid=" + sender + "&withJid=" + receiver + "&msgType=0&createTime=" + createTime + "&rows=" + String.valueOf(row);
        HttpClient.get(this, Api.GET_CHAT_MESSAGE + params, new CallBack<ChatPrivateBean>() {
            @Override
            public void onSuccess(ChatPrivateBean result) {
                if (result == null) {
                    return;
                }
                if (!onLoadMore) {
                    total=result.getData().getMsgPg().getTotal();
                    list.clear();
                }
                if(list.size()<total){
                    list.addAll(result.getData().getMsgPg().getRows());
                }
                Logger.d("聊天记录的数量：" + list.size());
                sort();
                chatRecordAdapter.notifyDataSetChanged();
            }


        });
    }

    /**
     * 保存聊天记录
     * 提交后台服务器保存
     */
    private void SaveChat(String message, String sender, String receiver) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("msg", message);
        hashMap.put("msgType", "0");//私聊
        hashMap.put("system", "1");//android
        HttpClient.post(this, Api.SAVE_CHAT_LOG, hashMap, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result.isSuccess()) {
                    isText=false;
                    send.setText("");
                    send.setBackgroundResource(R.mipmap.img);
                    Logger.d("消息保存服务器成功");
                } else {
                    ToastUtil.showShortToast(ChatActivity.this, result.getMessage());
                }
            }
        });

    }

    @OnClick({R.id.ll_back, R.id.image_face, R.id.input, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.ll_back:
                ClearSessionCount();
                break;
            //显示表情图片
            case R.id.image_face:
                hideSoftInputView();//隐藏软键盘
                if (chat_face_container.getVisibility() == View.GONE) {
                    chat_face_container.setVisibility(View.VISIBLE);
                } else {
                    chat_face_container.setVisibility(View.GONE);
                }
                break;
            //文本框
            case R.id.input:
                if (chat_face_container.getVisibility() == View.VISIBLE) {
                    chat_face_container.setVisibility(View.GONE);
                }

                break;
            //发送按钮
            case R.id.send:
                if(!isText){
                    hideSoftInputView();//隐藏软键盘
                    //启动相机
                    //最大图图片选择数量
                    int maxNum=PhotoPickerActivity.DEFAULT_NUM;
                    Intent intent = new Intent(ChatActivity.this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
                    startActivityForResult(intent, PICK_PHOTO);
                }else{
                    String content = input.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    if(content.length()>1000){
                        ToastUtil.showShortToast(ChatActivity.this,"消息不能超过1000字");
                    }else{
                        //发送消息
                        sendMsgText(content);
                    }

                }
                break;

        }
    }

    /**
     * 清除聊天未读数
     */
    private  void ClearSessionCount(){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("sender",receiver);
        hashMap.put("receiver",sender);
        hashMap.put("msgType","0");
        HttpClient.post(this, Api.CLEAR_SESSION_COUNT, hashMap, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if(result.isSuccess()){
                    Logger.d(result.getMessage());
                    finish();
                }

            }


        });

    }
    /**
     * 发送文本类型的消息
     * @param content
     */
    private void sendMsgText(final String content) {
        if(MyApplication.xmppConnection!=null&&MyApplication.xmppConnection.isAuthenticated()){
            ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean = new ChatPrivateBean.DataBean.MsgPgBean.RowsBean();
            bean.setId(new Date().getTime());
            bean.setCreateTime(new Date().getTime());
            bean.setMsgType(0);
            bean.setMsg(content);
            bean.setMsgCount(0);
            bean.setSender(sender);
            bean.setReceiver(receiver);
            bean.setMsgName(realName);
            bean.setMsgLogo(senderImg);
            Logger.d("新添加的消息记录："+bean.toString());
            list.add(bean);
            listview.post(new Runnable() {
                @Override
                public void run() {
                    listview.setSelection(chatRecordAdapter.getCount()-1);
                }
            });
            chatRecordAdapter.notifyDataSetChanged();
        }else{
            ToastUtil.showShortToast(ChatActivity.this,"登录服务器出错，请重新登录");
        }
        input.setText("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    XmppUtil.sendMessage(MyApplication.xmppConnection, content, sender, receiver);

                } catch (XMPPException e) {
                    e.printStackTrace();
                    ToastUtil.showShortToast(ChatActivity.this, "发送失败");
                }
            }
        }).start();
        //将聊天记录保存服务器中
        if(MyApplication.xmppConnection!=null&&MyApplication.xmppConnection.isAuthenticated()){
            SaveChat(content, sender, receiver);
        }


    }

    /**
     * 查询数据库获取集合按照时间排序
     */
    private void sort(){
        //对集合经行排序
        Collections.sort(list, new Comparator<ChatPrivateBean.DataBean.MsgPgBean.RowsBean>() {
            @Override
            public int compare(ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean, ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean1) {
                long time =bean.getCreateTime();
                long time1=bean1.getCreateTime();
                if (time > time1) {
                    return 1;
                } else if (time < time1) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });
        Logger.d("数据排序完成:"+list.size());
    }
    /**
     * 下拉功能
     */
    @Override
    public void onRefresh() {
        listview.onRefreshCompleteHeader();
        sort();
        if(list.size()==0){
            createTime="";
        }else{
            createTime=String.valueOf(list.get(0).getCreateTime());
        }
        if(total>list.size()){
            RequestBean(true);
        }


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
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideSoftInputView();
            if (chat_face_container.getVisibility() == View.VISIBLE) {
                chat_face_container.setVisibility(View.GONE);
            }else {
                ClearSessionCount();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


  //Edittext的监听器
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Logger.d("输入文本之前的状态");

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Logger.d("输入文字中的状态，"+count+"是一次性输入字符数");
            send.setText("发送");
            send.setBackgroundColor(Color.parseColor("#01af63"));
            isText=true;
    }

    @Override
    public void afterTextChanged(Editable s) {
        Logger.d("输入文字后的状态");
        //如果文本框中没有文字则切换状态
        if("".equals(input.getText().toString())){
            isText=false;
            send.setText("");
            send.setBackgroundResource(R.mipmap.img);
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PHOTO){
            if(resultCode == RESULT_OK){
                showResult(data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT));
            }
        }
    }
    private void showResult(ArrayList<String> paths){
        Logger.d("获取图片的数量："+paths.size()+"  图片的路径："+paths.toString());
        if(paths.size()>0){
            for (int i=0;i<paths.size();i++){
                    UploadPhoto(paths.get(i));

            }

        }

    }

    private void UploadPhoto(final String paths){
        Bitmap bitmap= BitmapUtils.getInstance().display(paths);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] buffer = baos.toByteArray();
        System.out.println("图片的大小："+buffer.length);
        //将图片的字节流数据加密成base64字符输出
        final String photo = Base64.encodeToString(buffer, 0, buffer.length,Base64.DEFAULT).replace("+", "%2B");
        HttpClient.postArray1(Api.UPLOAD_PHOTO,
                "image=" + photo + "&fileName=" + new Date().getTime() + ".png" + "&mimeType=png", new CallBack<StringBuffer>() {
            @Override
            public void onSuccess(StringBuffer result) {
               Logger.d("上传图片返回的URL:"+result.toString());
                Gson gson=new Gson();
                PictureReturnBean bean=gson.fromJson(result.toString(),PictureReturnBean.class);
                if(bean.isSuccess()){
                    Logger.d("上传图片返回的URL："+bean.getData().getUrl());
                    sendMsgText("*$img_"+bean.getData().getUrl()+"_gmi$*");
                }else{
                    ToastUtil.showShortToast(ChatActivity.this,bean.getMessage());
                }
            }

        });
       }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);

    }



}