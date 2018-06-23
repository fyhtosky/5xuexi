package com.sj.yinjiaoyun.xuexi.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.GroEvent;
import com.sj.yinjiaoyun.xuexi.Event.MsgEvent;
import com.sj.yinjiaoyun.xuexi.Event.NoticeEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.MessageListAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.GroupinfoBean;
import com.sj.yinjiaoyun.xuexi.bean.PersonalInfoBean;
import com.sj.yinjiaoyun.xuexi.bean.RecentChatBean;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroupVO;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.bean.User;
import com.sj.yinjiaoyun.xuexi.greedao.gen.UserDao;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.ExpressionUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by wanzhiying on 2017/3/2.
 * 好友列表展示
 */
public class MessageListFragment extends Fragment {



    @BindView(R.id.lv_news)
    ListView mCustomListView;
    private Context mContext;
    private MessageListAdapter adapter;
    //"5f_3376"
    private String userid;
    private int rows=15;
    private int total;
    //准备集合存放最近回话的数据源
    //获取未知群的信息
    private String BusinessName;
    private String createTime;
    private List<RecentChatBean.DataBean.MsgPgBean.RowsBean>list=new ArrayList<>();
    //获取未读消息数量之和
    private int messageCount=0;
    //操作数据库对象
    private UserDao userDao;
    private Query query;
    //查询数据库返回的集合
    private List userinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_message_list, container, false);
        //注册EventBus
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);
        init();
        return view;
    }
    private void init() {
        //获取操作数据库对象
        userDao=MyApplication.getInstances().getmDaoSession().getUserDao();
        mContext = getActivity();
        //获取userid
        Logger.d("Userid:"+PreferencesUtils.getSharePreStr(mContext, "username"));
        userid="5f_"+PreferencesUtils.getSharePreStr(mContext, "username");
        //显示回话聊天
        adapter = new MessageListAdapter(mContext, list);
        mCustomListView.setAdapter(adapter);
        //给listView添加监听器
        mCustomListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_IDLE){
                    if(total>list.size()&& list.size()>0){
                        initData(true);
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        messageCount=0;
        //获取数据
        createTime="";
        initData(false);
    }

    /**
     * 获取会话列表的消息数量
     */
    private void  getNoticeCount(){
        new Thread(MessageRun).start();

    }
Runnable MessageRun=new Runnable() {
    @Override
    public void run() {
        if(list.size()>0){
            messageCount=0;
            for(int i=0;i<list.size();i++){
                getNotDisturb(list.get(i));

            }
        }else{
            messageCount=0;
        }
        EventBus.getDefault().post(new NoticeEvent(messageCount));
    }
};
    /**
     * 判断群聊消息是否设置免打扰否则消息数不显示
     * @param rowsBean

     */
    private void getNotDisturb(RecentChatBean.DataBean.MsgPgBean.RowsBean rowsBean) {
        if (1==rowsBean.getMsgType()) {
            if (MyApplication.groupsList != null && MyApplication.groupsList.size() > 0) {
                for (TigaseGroups tigaseGroups : MyApplication.groupsList) {
                    //判断父群
                    if (rowsBean.getSender().equals(tigaseGroups.getTigaseGroupVO().getGroupId())) {
                        //未设置免打扰了
                        if (0== tigaseGroups.getTigaseGroupVO().getIsNotDisturb()) {
                            messageCount += rowsBean.getMsgCount();
                        }
                    } else if (tigaseGroups.getChildTigaseGroupVOs() != null) {
                        if (tigaseGroups.getChildTigaseGroupVOs().size() > 0) {
                            for (TigaseGroupVO tigaseGroupVO : tigaseGroups.getChildTigaseGroupVOs()) {
                                if (rowsBean.getSender().equals(tigaseGroupVO.getGroupId())) {
                                    //未设置免打扰了
                                    if (0== tigaseGroupVO.getIsNotDisturb()) {
                                        messageCount += rowsBean.getMsgCount();
                                    }

                                }
                            }
                        }

                    }

                }
            }
        }else {
            //私聊
            messageCount += rowsBean.getMsgCount();

        }
    }

    /**
     * 更具jie判断是否是设置免打扰
     * @param sender
     */
    private void getNotDisturb(int type,String sender) {
        if(type==1){
            if (MyApplication.groupsList != null && MyApplication.groupsList.size() > 0) {
                for (TigaseGroups tigaseGroups : MyApplication.groupsList) {
                    //判断父群
                    if (sender.equals(tigaseGroups.getTigaseGroupVO().getGroupId())) {
                        //未设置免打扰了
                        if (0== tigaseGroups.getTigaseGroupVO().getIsNotDisturb()) {
                            messageCount +=1;
                        }
                    } else if (tigaseGroups.getChildTigaseGroupVOs() != null) {
                        if (tigaseGroups.getChildTigaseGroupVOs().size() > 0) {
                            for (TigaseGroupVO tigaseGroupVO : tigaseGroups.getChildTigaseGroupVOs()) {
                                if (sender.equals(tigaseGroupVO.getGroupId())) {
                                    //未设置免打扰了
                                    if (0== tigaseGroupVO.getIsNotDisturb()) {
                                        messageCount += 1;
                                    }

                                }
                            }
                        }

                    }

                }
            }
        }else{
            messageCount +=1;
        }
        EventBus.getDefault().post(new NoticeEvent(messageCount));

    }
    /**
     * 获取会话数据
     * @param isLoadMore
     */
    private void initData(final boolean isLoadMore) {
        String params="?receiver="+userid+"&createTime="+createTime+"&rows="+String.valueOf(rows);
        HttpClient.get(this, Api.GET_RECENT_CHAT_LIST+params, new CallBack<RecentChatBean>() {
            @Override
            public void onSuccess(RecentChatBean result) {
                if(result==null){
                    return;
                }
                if(200==result.getState()){
                    messageCount=0;
                    if(!isLoadMore){
                        total=result.getData().getMsgPg().getTotal();
                        list.clear();

                    }

                    if(total>list.size()){
                        sort();
                        list.addAll(result.getData().getMsgPg().getRows());
                    }
                    createTime=String.valueOf(list.get((list.size()-1)).getCreateTime());
                    adapter.notifyDataSetChanged();
                    Logger.d("最近回话的列表的数量："+list.size());
                    getNoticeCount();
                }

            }
        });
    }
    /**
     * 查询数据库获取集合按照时间排序
     */
    private void sort(){
        //对集合经行排序
        Collections.sort(list, new Comparator<RecentChatBean.DataBean.MsgPgBean.RowsBean>() {
            @Override
            public int compare(RecentChatBean.DataBean.MsgPgBean.RowsBean bean, RecentChatBean.DataBean.MsgPgBean.RowsBean bean1) {
                long time =bean.getCreateTime();
                long time1=bean1.getCreateTime();
                if (time > time1) {
                    return -1;
                } else if (time < time1) {
                    return 1;
                } else {
                    return 0;
                }

            }
        });
        Logger.d("数据排序完成:"+list.size());
    }
    /**
     * 会话列表获取群聊消息通知聊天界面刷新
     * @param groEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GroEvent groEvent){
        Logger.d("MessageListFragment:群聊:" + groEvent.getMsgBody() + "  接收者TO:" + groEvent.getTo() + "  发送者FROM:" + groEvent.getFrom() + "TYpe:" + groEvent.getType());
        String from = groEvent.getFrom().split("@")[0];

//    前台账户的格式     "5p_304@muc.139.196.155.123/5f_3477";
//    后台账户    "5p_304@muc.139.196.155.123/5b_163@139.196.155.123/webim";
        String str;
        if(groEvent.getFrom().split("/")[1] .contains("@")){
            str=groEvent.getFrom().split("/")[1].split("@")[0];
        }else{
            str = groEvent.getFrom().split("/")[1];
        }
        String msgBody = groEvent.getMsgBody();
        if (TextUtils.isEmpty(msgBody)) {
            return;
        }
        //设置查询条件查询一条数据
        query=userDao.queryBuilder().where(UserDao.Properties.UserId.eq(str)).build();
        userinfo=query.list();
        //群聊记录更新
        if(org.jivesoftware.smack.packet.Message.Type.groupchat==groEvent.getType()) {
            Logger.d("MessageListFragment:群聊:" + groEvent.getMsgBody() + "  接收者TO:" + groEvent.getTo() + "  发送者FROM:" + groEvent.getFrom() + "TYpe:" + groEvent.getType());
            RecentChatBean.DataBean.MsgPgBean.RowsBean bean;
                int j = 0;
                if (list.size() > 0) {
                    boolean t = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (from.equals(list.get(i).getSender())) {
                            Logger.d("groEvent：群"+from+"本地群："+list.get(i).getSender());
                            j = i;
                            t = true;
                            break;
                        }
                    }

                    if (!t) {
                        Logger.d("【群聊】发送人不存在最近的消息列表");
                        getGroupInfo(from, groEvent);
                    } else {
                        bean = list.get(j);
                        Logger.d("MessageListFragment:群聊:" + bean.getSender());
//                    list.remove(list.lastIndexOf(bean));
                        list.remove(bean);
                        //先去数据库查，如果数据库不存在择获取网络请求将相关信息存入数据库
                        getInfoname(str, bean, groEvent);
                        Logger.d("【群聊】发送人存在最近消息列表");

                    }

                }else{
                    getGroupInfo(from, groEvent);
                }


              //不是我发送的则提示消息更新
             if(!userid.equals(str)){
                 getNotDisturb(1,from);
             }


        }

    }

    /**
     *
     * 根据群的id赋值给群名复制给MsgName
     * @param groupJid
     * @param groEvent
     */
    private void getGroupInfo(final String groupJid, final GroEvent groEvent){
        String str ;
        if(groEvent.getFrom().split("/")[1] .contains("@")){
            str=groEvent.getFrom().split("/")[1].split("@")[0];
        }else{
            str = groEvent.getFrom().split("/")[1];
        }
        String params="?groupJid="+groupJid;
        final String finalStr = str;
        HttpClient.get(this, Api.GET_GROUP_INFO + params, new CallBack<GroupinfoBean>() {
            @Override
            public void onSuccess(GroupinfoBean result) {
                if(result==null){
                    return;
                }
                Logger.d(result.getData().getGroupChat().toString());
                BusinessName=result.getData().getGroupChat().getBusinessName();
                RecentChatBean.DataBean.MsgPgBean.RowsBean rowsBean=new RecentChatBean.DataBean.MsgPgBean.RowsBean();
                rowsBean.setReceiver(groEvent.getTo());
                rowsBean.setSender(groupJid);
                rowsBean.setCreateTime(new Date().getTime());
                //群聊
                rowsBean.setMsgType(1);
                rowsBean.setMsg(groEvent.getMsgBody());
               if(finalStr.equals(userid)){
                   rowsBean.setMsgCount(0);
               }else{
                   rowsBean.setMsgCount(1);
               }
                rowsBean.setMsgName(BusinessName);
                //获取群成员的信息
                getInfoname(finalStr,rowsBean,groEvent);

            }
        });

    }
    /**
     * 根据群成员的id获取发送者的名称复制给SenderName
     * @param jeid
     * @param bean
     * @param groEvent
     */
    private void getInfoname(final String jeid, final RecentChatBean.DataBean.MsgPgBean.RowsBean bean, final GroEvent groEvent){
        final String from = groEvent.getFrom().split("@")[0];
        //先去数据库查，如果数据库不存在择获取网络请求将相关信息存入数据库
        if(userinfo.size()>0){
            User user= (User) userinfo.get(userinfo.size()-1);
            Logger.d("查询本地数据库："+userinfo.size()+"userid="+user.getUserId());
            RecentChatBean.DataBean.MsgPgBean.RowsBean rowsBean=new RecentChatBean.DataBean.MsgPgBean.RowsBean();
            rowsBean.setReceiver(groEvent.getTo());
            rowsBean.setSender(from);
            rowsBean.setCreateTime(new Date().getTime());
            //群聊
            rowsBean.setMsgType(1);
            rowsBean.setMsg(groEvent.getMsgBody());
            if(jeid.equals(userid)){
                rowsBean.setMsgCount(0);
            }else{
                rowsBean.setMsgCount(bean.getMsgCount()+1);
            }
            rowsBean.setMsgName(bean.getMsgName());
            rowsBean.setMsgLogo(bean.getMsgLogo());
            /**
             * 从本地数据库中取值赋值
             */
            rowsBean.setSenderName(user.getMsgName());
            list.add(0,rowsBean);
            adapter.updateListView(list);
            return;
        }
        String params="?jid="+jeid;
        HttpClient.get(this, Api.GET_PERSONAL_INFO + params, new CallBack<PersonalInfoBean>() {
            @Override
            public void onSuccess(PersonalInfoBean result) {
                if(result==null){
                    return;
                }
                RecentChatBean.DataBean.MsgPgBean.RowsBean rowsBean=new  RecentChatBean.DataBean.MsgPgBean.RowsBean();
                rowsBean.setSender(from);
                rowsBean.setReceiver(groEvent.getTo());
                rowsBean.setCreateTime(new Date().getTime());
                if(jeid.equals(userid)){
                    rowsBean.setMsgCount(0);
                }else{
                    rowsBean.setMsgCount(bean.getMsgCount()+1);
                }
                rowsBean.setMsgLogo(bean.getMsgLogo());
                rowsBean.setSenderName(result.getData().getUser().getMsgName());
                rowsBean.setMsgName(bean.getMsgName());
                //群聊
                rowsBean.setMsgType(1);
                rowsBean.setMsg(groEvent.getMsgBody());
                list.add(0,rowsBean);
                adapter.updateListView(list);
                Logger.d("网络请求："+rowsBean.toString());
                //将更新数据库中
                User mUser=new User(result.getData().getUser().getJid(),result.getData().getUser().getMsgName(),result.getData().getUser().getMsgLogo());
                userDao.insert(mUser);
                Logger.d("插入数据库："+mUser.getUserId());
            }
        });

    }


    /**
     * 回话列表获取私聊消息通知聊天界面刷新
     * @param msgEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onMessageEvent(MsgEvent msgEvent){
        if(org.jivesoftware.smack.packet.Message.Type.chat==msgEvent.getType()){
            Logger.d("MessageListFragment:私聊:" + msgEvent.getMsgBody() + "  接收者TO:" + msgEvent.getTo() + "  发送者FROM:" + msgEvent.getFrom());
            RecentChatBean.DataBean.MsgPgBean.RowsBean bean ;
            int j = 0;
            if(list.size()>0){
                boolean t = false;
                for (int i=0;i<list.size();i++){
                    if(msgEvent.getFrom().equals(list.get(i).getSender())){
                        j = i;
                        t = true;
                        break;
                    }
                }
                if(!t){
                    //如果发送消息的人不存在消息列表中中
                    getInfo(msgEvent);
                }else{
                    bean=list.get(j);
                    Logger.d("MessageListFragment:onMessageEvent:"+bean.getSender());
                    list.remove(bean);
                    bean.setSender(msgEvent.getFrom());
                    bean.setReceiver(msgEvent.getTo());
                    bean.setCreateTime(new Date().getTime());
                    bean.setMsgCount(bean.getMsgCount()+1);
                    //私聊
                    bean.setMsgType(0);
                    bean.setMsg(msgEvent.getMsgBody());
                    list.add(0,bean);
                    adapter.updateListView(list);
                }

            }else {
                Logger.d("【私聊】发送人不存在最近的消息列表");
                //如果没有消息列表
                getInfo(msgEvent);
            }
            //私聊发送消息更新消息数量
            getNotDisturb(0,msgEvent.getFrom());
        }
    }
    /**
     * 私聊获取未知发送人的信息
     * 根据jeid获取个人信息
     */

    private void getInfo( final MsgEvent message){
        //设置查询条件查询一条数据
        query=userDao.queryBuilder().where(UserDao.Properties.UserId.eq(message.getFrom())).limit(1).build();
        userinfo=query.list();
        //先去数据库查，如果数据库不存在择获取网络请求将相关信息存入数据库
        if(userinfo.size()>0){
            User user= (User) userinfo.get(userinfo.size()-1);
            Logger.d("查询本地数据库："+userinfo.size()+"userid="+user.getUserId());
            RecentChatBean.DataBean.MsgPgBean.RowsBean rowsBean=new RecentChatBean.DataBean.MsgPgBean.RowsBean();
            rowsBean.setReceiver(message.getTo());
            rowsBean.setSender(message.getFrom());
            rowsBean.setCreateTime(new Date().getTime());
            //群聊
            rowsBean.setMsgType(1);
            rowsBean.setMsg(message.getMsgBody());
            if(message.getFrom().equals(userid)){
                rowsBean.setMsgCount(0);
            }else{
                rowsBean.setMsgCount(1);
            }
            /**
             * 本地数据库赋值
             */
            rowsBean.setMsgName(user.getMsgName());
            rowsBean.setMsgLogo(user.getMsgLogo());
            list.add(0,rowsBean);
            adapter.updateListView(list);
            return;
        }
        String params="?jid="+message.getFrom();
        HttpClient.get(this, Api.GET_PERSONAL_INFO + params, new CallBack<PersonalInfoBean>() {
            @Override
            public void onSuccess(PersonalInfoBean result) {
                if(result==null){
                    return;
                }
                Logger.d("网络请求："+userinfo.size());
                RecentChatBean.DataBean.MsgPgBean.RowsBean bean=new RecentChatBean.DataBean.MsgPgBean.RowsBean();
                bean.setReceiver(message.getTo());
                bean.setCreateTime(new Date().getTime());
                bean.setMsgCount(1);
                bean.setMsgType(0);
                //私聊
                bean.setSender(message.getFrom());
                if(message.getMsgBody().contains("_!@_")){
                    bean.setMsg(ExpressionUtil.RecursiveQuery(getActivity(),message.getMsgBody(),0));
                }else{
                    bean.setMsg(message.getMsgBody());
                }
                Logger.d("发送人的logo:"+result.getData().getUser().getMsgLogo()+"发送人的MsgName："+result.getData().getUser().getMsgName()+bean.getMsg());
                /**
                 * 网络请求赋值
                 */
                bean.setMsgName(result.getData().getUser().getMsgName());
                bean.setMsgLogo(result.getData().getUser().getMsgLogo());
                list.add(0,bean);
                adapter.notifyDataSetChanged();
                //将其更新数据库中
                User mUser=new User(result.getData().getUser().getJid(),result.getData().getUser().getMsgName(),result.getData().getUser().getMsgLogo());
                userDao.insert(mUser);
                Logger.d("插入数据库："+mUser.getUserId()+mUser.getMsgName()+mUser.getMsgLogo());
            }
        });

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        //反注册EventBus
        EventBus.getDefault().unregister(this);
    }
}
