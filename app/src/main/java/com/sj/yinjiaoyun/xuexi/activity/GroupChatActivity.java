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
import com.sj.yinjiaoyun.xuexi.Event.GroEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.FaceVPAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.GroupChatAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.ChatPrivateBean;
import com.sj.yinjiaoyun.xuexi.bean.MultiChatBean;
import com.sj.yinjiaoyun.xuexi.bean.PersonalInfoBean;
import com.sj.yinjiaoyun.xuexi.bean.PictureReturnBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroupVO;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.bean.User;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.greedao.gen.UserDao;
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
import org.greenrobot.greendao.query.Query;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

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

public class GroupChatActivity extends MyBaseActivity implements DropdownListView.OnRefreshListenerHeader, TextWatcher {

    private static final int PICK_PHOTO = 1;
    private static final int CHOOSE_REPLY = 0;
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
    @BindView(R.id.iv_message_no)
    ImageView ivMessageNo;
    private LinearLayout chat_face_container;
    //发送者(我)
    private String sender;
    private String senderName;
    //聊天对象的LOGO
    private String senderImg;
    //接受者（群）
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
    private String realName;
    private GroupChatAdapter groupChatAdapter;

    //标识是否是发送状态
    private boolean isText = false;
    //数据的总数
    private int total = 0;
    //获取未知群的信息
    private String BusinessName;
    private String BusinessImg;
    //群聊对象
    private MultiUserChat multiUserChat;
    private String chooseContent;
    private String jid;

    //操作数据库对象
    private UserDao userDao;
    private Query query;
    //查询数据库返回的集合
    private List userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.bind(this);
        init();


    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        //获取操作数据库对象
        userDao=MyApplication.getInstances().getmDaoSession().getUserDao();
        sender = "5f_" + PreferencesUtils.getSharePreStr(this, "username");
        String realname = PreferencesUtils.getSharePreStr(GroupChatActivity.this, "realName");
        String uesrname = PreferencesUtils.getSharePreStr(GroupChatActivity.this, "Name");
        Logger.d("GroupChatActivity:" + realname + "==" + uesrname);
        if (!"null".equals(realname)) {
            senderName = realname;
        } else {
            senderName = uesrname;
        }
        Logger.d("GroupChatActivity:" + realName + "==" + uesrname);
        senderImg = PreferencesUtils.getSharePreStr(this, "userImg");
        Logger.d("获取个人信息：" + sender + "url;" + senderImg);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            receiver = bundle.getString(Const.GROUP_ID, "");
            realName = bundle.getString(Const.GROUP_NAME, "");

        }
        Logger.d("receiver：" + receiver + "  sender:" + sender + "  realName:" + realName);
        if (!"".equals(realName)) {
            tvTitle.setText(realName);
        } else {
            tvTitle.setText(receiver);
        }
        //添加群聊的信息
        if (MyApplication.xmppConnection != null && MyApplication.xmppConnection.isAuthenticated()) {
            if (MyApplication.list != null) {
                for (MultiChatBean bean : MyApplication.list) {
                    if (receiver.equals(bean.getGroupId())) {
                            multiUserChat = bean.getMultiUserChat();
                    }
                    }
            }
            if(multiUserChat==null){
                //群聊管理器
                multiUserChat=XmppUtil.joinMultiUserChat(sender,receiver);
            }
        }
        inflater = LayoutInflater.from(GroupChatActivity.this);
        //表情布局
        chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
        //表情
        mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        //表情下小圆点
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
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


        //初始化表情包的集合
        staticFacesList = ExpressionUtil.initStaticFaces(this);
        //加载适配
        initViewPager();
        groupChatAdapter = new GroupChatAdapter(this, list, receiver);
        listview.setAdapter(groupChatAdapter);
        //给Edittext添加监听器
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1010)});
        input.addTextChangedListener(this);
        //获取请求
        createTime = "";
        RequestBean(false);

    }

    /**
     * 设置免打扰标示
     */
  private void  setIsNotDisturb(){
      //设置免打扰
      if(MyApplication.groupsList!=null &&MyApplication.groupsList.size()>0){
          for (TigaseGroups tigaseGroups:MyApplication.groupsList){
              if(receiver.equals(tigaseGroups.getTigaseGroupVO().getGroupId())){
                  //开启了免打扰
                  if(1==tigaseGroups.getTigaseGroupVO().getIsNotDisturb()){
                      ivMessageNo.setVisibility(View.VISIBLE);
                  }else{
                      //未开启免打扰
                      ivMessageNo.setVisibility(View.GONE);
                  }
              }else{
                  if(tigaseGroups.getChildTigaseGroupVOs()!=null){
                      if(tigaseGroups.getChildTigaseGroupVOs().size()>0){
                          for (TigaseGroupVO tigaseGroupVO:tigaseGroups.getChildTigaseGroupVOs()){
                              if(receiver.equals(tigaseGroupVO.getGroupId())){
                                  //开启了免打扰
                                  if(1==tigaseGroupVO.getIsNotDisturb()){
                                      ivMessageNo.setVisibility(View.VISIBLE);
                                  }else{
                                      //未开启免打扰
                                      ivMessageNo.setVisibility(View.GONE);
                                  }
                              }
                          }
                      }
                  }
              }
          }
      }else{
          ivMessageNo.setVisibility(View.GONE);
      }
  }

    /**
     * 账号被挤掉
     *
     * @param emptyEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EmptyEvent emptyEvent) {
        if (MyApplication.xmppConnection != null) {
            if (!MyApplication.xmppConnection.isAuthenticated()) {
                if (emptyEvent.isLogin) {
                    DialogUtils.showNormalDialog(GroupChatActivity.this);
                }
            }
        }

    }

    /**
     * 回话列表获取群聊消息通知聊天界面刷新
     *
     * @param groEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GroEvent groEvent) {
        //私聊记录更新
        if (Message.Type.groupchat == groEvent.getType()) {
            Logger.d("GroupChatActivity:message群聊:" + groEvent.getMsgBody() + "  接收者TO:" + groEvent.getTo() + "  发送者FROM:" + groEvent.getFrom());
            //群id
            String from = groEvent.getFrom().split("@")[0];
            //群的实际发送者
            String str;
            if (groEvent.getFrom().split("/")[1] != null) {
                str=groEvent.getFrom().split("/")[1].split("@")[0];
            } else {
                str=groEvent.getFrom().split("/")[0].split("@")[0];
            }
            //是发送到这个群里的
            if (!receiver.equals(from)) {
                return;
            }
            //是我发的
            if (sender.equals(str)) {
                return;
            }
            getPersonInfo(groEvent);
        }

    }



    /**
     * 根据jeid获取群的成员个人的用户名及图像logo
     * @param groEvent
     */
    private void getPersonInfo( final GroEvent groEvent) {
        String str;
        if (groEvent.getFrom().split("/")[1] != null) {
            str=groEvent.getFrom().split("/")[1].split("@")[0];
        } else {
            str=groEvent.getFrom().split("/")[0].split("@")[0];
        }
        //设置查询条件查询一条数据
        query=userDao.queryBuilder().where(UserDao.Properties.UserId.eq(str)).build();
        userinfo=query.list();
        //先去数据库查，如果数据库不存在择获取网络请求将相关信息存入数据库
        if(userinfo.size()>0){
            User user= (User) userinfo.get(userinfo.size()-1);
            Logger.d("查询本地数据库："+userinfo.size()+"userid="+user.getUserId());
            ChatPrivateBean.DataBean.MsgPgBean.RowsBean rowsBean = new ChatPrivateBean.DataBean.MsgPgBean.RowsBean();
                    rowsBean.setSender(user.getUserId());
                    rowsBean.setReceiver(groEvent.getTo());
                    rowsBean.setCreateTime(new Date().getTime());
                    rowsBean.setMsgCount(0);
                    rowsBean.setMsgLogo(user.getMsgLogo());
                    rowsBean.setMsgName(user.getMsgName());
                    rowsBean.setSenderName(user.getMsgName());
                    //群聊
                    rowsBean.setMsgType(1);
//                    if (groEvent.getMsgBody().contains("_!@_")) {
//                        rowsBean.setMsg(ExpressionUtil.RecursiveQuery(GroupChatActivity.this, groEvent.getMsgBody(), 0));
//                    } else {
//                        rowsBean.setMsg(groEvent.getMsgBody());
//                    }
                    rowsBean.setMsg(groEvent.getMsgBody());
                    Logger.d("监听群聊的消息记录：" + rowsBean.toString() + "集合的size：" + list.size());
                    list.add(rowsBean);
                    listview.post(new Runnable() {
                        @Override
                        public void run() {
                            listview.setSelection(groupChatAdapter.getCount()-1);
                        }
                    });
                    groupChatAdapter.notifyDataSetChanged();
            return;
        }
        final String finalStr = str;
        String params = "?jid=" + finalStr;
        HttpClient.get(this, Api.GET_PERSONAL_INFO + params, new CallBack<PersonalInfoBean>() {
            @Override
            public void onSuccess(PersonalInfoBean result) {
                if (result == null) {
                    return;
                }
                Logger.d(result.getData().getUser().toString());
                BusinessName = result.getData().getUser().getMsgName();
                BusinessImg = result.getData().getUser().getMsgLogo();
                ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean = new ChatPrivateBean.DataBean.MsgPgBean.RowsBean();
                bean.setSender(finalStr);
                bean.setReceiver(groEvent.getTo());
                bean.setCreateTime(new Date().getTime());
                bean.setMsgCount(0);
                bean.setMsgLogo(BusinessImg);
                bean.setMsgName(BusinessName);
                bean.setSenderName(BusinessName);
                //群聊
                bean.setMsgType(1);
                bean.setMsg(groEvent.getMsgBody());
                //将获取的消息添加到集合
                list.add(bean);
                listview.post(new Runnable() {
                    @Override
                    public void run() {
                        listview.setSelection(groupChatAdapter.getCount()-1);
                    }
                });
                groupChatAdapter.notifyDataSetChanged();
                //将其插入数据库中
                User mUser=new User(result.getData().getUser().getJid(),BusinessName,BusinessImg);
                userDao.insert(mUser);
                Logger.d("插入数据库："+mUser.getUserId()+mUser.getMsgName()+mUser.getMsgLogo());
            }
        });

    }

    /**
     * 发送请求获取数据
     */
    private void RequestBean(final boolean onLoadMore) {
        String params = "?jid=" + sender + "&withJid=" + receiver + "&msgType=1&createTime=" + createTime + "&rows=" + String.valueOf(row);
        HttpClient.get(this, Api.GET_CHAT_MESSAGE + params, new CallBack<ChatPrivateBean>() {
            @Override
            public void onSuccess(ChatPrivateBean result) {
                if (result == null) {
                    return;
                }
                if (!onLoadMore) {
                    total = result.getData().getMsgPg().getTotal();
                    list.clear();
                }
                if (list.size() < total) {
                    list.addAll(result.getData().getMsgPg().getRows());
                }
                Logger.d("聊天记录的数量：" + list.size());
                sort();
                groupChatAdapter.notifyDataSetChanged();
            }


        });
    }

    /**
     * 查询数据库获取集合按照时间排序
     */
    private void sort() {
        //对集合经行排序
        Collections.sort(list, new Comparator<ChatPrivateBean.DataBean.MsgPgBean.RowsBean>() {
            @Override
            public int compare(ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean, ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean1) {
                long time = bean.getCreateTime();
                long time1 = bean1.getCreateTime();
                if (time > time1) {
                    return 1;
                } else if (time < time1) {
                    return -1;
                } else {
                    return 0;
                }

            }
        });
        Logger.d("数据排序完成:" + list.size());
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
                for (int i = 0; i < pagesize; i++) {
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
     *
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

    @Override
    protected void onResume() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //让输入框获取焦点
                input.requestFocus();
            }
        }, 100);
        setIsNotDisturb();
        super.onResume();

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
            } else {
                ClearSessionCount();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.ll_back, R.id.iv_group_fo, R.id.image_face, R.id.input, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                ClearSessionCount();
                break;
            //群主信息
            case R.id.iv_group_fo:
                Intent inten = new Intent(GroupChatActivity.this, GroupInforActivity.class);
                inten.putExtra(Const.GROUP_ID, receiver);
                startActivity(inten);
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
                if (!isText) {
                    hideSoftInputView();//隐藏软键盘
                    //启动相机
                    //最大图图片选择数量
                    int maxNum = PhotoPickerActivity.DEFAULT_NUM;
                    Intent intent = new Intent(GroupChatActivity.this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
                    startActivityForResult(intent, PICK_PHOTO);
                } else {
                    String content = input.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        return;
                    }
                    if(content.length()>1000){
                        ToastUtil.showShortToast(GroupChatActivity.this,"消息不能超过1000字");
                    }else {
                        String text = content.replace("@" + chooseContent, "#@_" + jid + "_!@_" + chooseContent) + "_@#";
                        //发送消息
                        if (text.contains("_!@_")) {
                            Logger.d("sendMsgText@:" + text);
                            sendMsgText(text);
                        } else {
                            sendMsgText(content);
                        }
                    }
                }

                break;

        }
    }

    /**
     * 清除聊天未读数
     */
    private void ClearSessionCount() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", receiver);
        hashMap.put("receiver", sender);
        hashMap.put("msgType", "1");
        HttpClient.post(this, Api.CLEAR_SESSION_COUNT, hashMap, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result.isSuccess()) {
                    Logger.d(result.getMessage());
                    finish();
                }
            }


        });

    }

    /**
     * 发送文本类型的消息
     *
     * @param content
     */
    private void sendMsgText(final String content) {
        if (MyApplication.xmppConnection != null && multiUserChat!=null && MyApplication.xmppConnection.isAuthenticated()) {
            ChatPrivateBean.DataBean.MsgPgBean.RowsBean bean = new ChatPrivateBean.DataBean.MsgPgBean.RowsBean();
            bean.setId(new Date().getTime());
            bean.setCreateTime(new Date().getTime());
            bean.setMsgType(1);
            bean.setMsg(content);
            bean.setMsgCount(0);
            bean.setSender(sender);
            bean.setReceiver(receiver);
            bean.setMsgName(senderName);
            bean.setMsgLogo(senderImg);
            bean.setSenderName(senderName);
            Logger.d("新添加的消息记录：" + bean.toString()+multiUserChat);
            list.add(bean);
            groupChatAdapter.notifyDataSetChanged();
            listview.setSelection(list.size());
            input.setText("");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        XmppUtil.sendGroupMessage(multiUserChat, content, sender, receiver);
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            //将聊天记录保存服务器中
            SaveChat(content, sender, receiver);
        } else {
            ToastUtil.showShortToast(GroupChatActivity.this, "登录服务器出错，请重新登录");
        }




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
        hashMap.put("msgType", "1");//群聊
        hashMap.put("system", "1");//android
        HttpClient.post(this, Api.SAVE_CHAT_LOG, hashMap, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result.isSuccess()) {
                    isText = false;
                    send.setText("");
                    send.setBackgroundResource(R.mipmap.img);
                    Logger.d("消息保存服务器成功");
                } else {
                    ToastUtil.showShortToast(GroupChatActivity.this, result.getMessage());
                }
            }
        });

    }

    /**
     * 下拉功能
     */
    @Override
    public void onRefresh() {
        sort();
        listview.onRefreshCompleteHeader();
        if (list.size() == 0) {
            createTime = "";
        } else {
            createTime = String.valueOf(list.get(0).getCreateTime());
        }

        if (total > list.size()) {
            RequestBean(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Logger.d("输入文本之前的状态");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Logger.d("输入文字中的状态，count是一次性输入字符数" + s);
        if (input.getText().toString().endsWith("@")) {
            Intent intent = new Intent(GroupChatActivity.this, ChooseReplyActivity.class);
            intent.putExtra(Const.GROUP_ID, receiver);
            startActivityForResult(intent, GroupChatActivity.CHOOSE_REPLY);
        } else {
            send.setText("发送");
            send.setBackgroundColor(Color.parseColor("#01af63"));
            isText = true;
        }


    }

    @Override
    public void afterTextChanged(Editable s) {
        Logger.d("输入文字后的状态");
        if ("".equals(input.getText().toString())) {
            isText = false;
            send.setText("");
            send.setBackgroundResource(R.mipmap.img);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                showResult(data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT));
            }
        } else if (requestCode == CHOOSE_REPLY) {
            if (data != null) {
                chooseContent = data.getStringExtra(Const.CHOOSE_REPLY);
                jid = data.getStringExtra(Const.CHOOSE_JID);
//                String content="#@_"+jid+"_!@_"+chooseContent+"_@#";
//                input.getText().toString().subSequence(0,input.getText().toString().length()-1);
//                input.setText(input.getText().toString().subSequence(0,input.getText().toString().length()-1));
                input.setText(input.getText().append(chooseContent).toString());
                input.setSelection(input.getText().length());
//                input.setText(input.getText().append("#@_"+jid+"_!@_"+chooseContent+"_@#").toString());


            }

        }
    }

    private void showResult(ArrayList<String> paths) {
        Logger.d("获取图片的数量：" + paths.size() + "  图片的路径：" + paths.toString());
        if (paths.size() > 0) {
            for (int i = 0; i < paths.size(); i++) {
                Logger.d("for:" + i);
                UploadPhoto(paths.get(i));
            }
        }
    }

    private void UploadPhoto(final String paths) {
                Bitmap bitmap = BitmapUtils.getInstance().display(paths);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] buffer = baos.toByteArray();
                System.out.println("图片的大小：" + buffer.length);
                //将图片的字节流数据加密成base64字符输出
                final String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT).replace("+", "%2B");
                HttpClient.postArray1(Api.UPLOAD_PHOTO,
                        "image=" + photo + "&fileName=" + new Date().getTime() + ".png" + "&mimeType=png", new CallBack<StringBuffer>() {
                            @Override
                            public void onSuccess(StringBuffer result) {
                                Logger.d("上传图片返回的URL:" + result.toString());
                                Gson gson = new Gson();
                                PictureReturnBean bean = gson.fromJson(result.toString(), PictureReturnBean.class);
                                if (bean.isSuccess()) {
                                    Logger.d("上传图片返回的URL：" + bean.getData().getUrl());
                                    sendMsgText("*$img_" + bean.getData().getUrl() + "_gmi$*");
                                } else {
                                    ToastUtil.showShortToast(GroupChatActivity.this, bean.getMessage());
                                }
                            }

                        });
            }







}
