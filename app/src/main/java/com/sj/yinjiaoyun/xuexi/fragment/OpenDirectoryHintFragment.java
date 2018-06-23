package com.sj.yinjiaoyun.xuexi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.ViedoCompleteEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.OpenDirectoryAdapter;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.domain.Coursewares;
import com.sj.yinjiaoyun.xuexi.domain.ExpendGroup;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParseCourseWaresDate;
import com.sj.yinjiaoyun.xuexi.domain.ParseCouseWares;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/19.
 * 公开课 目录
 */
public class OpenDirectoryHintFragment extends Fragment  implements HttpDemo.HttpCallBack{

    String TAG="openDirectory";
    View convertView;
    HttpDemo demo;
    ExpandableListView expandableListView;
    OpenDirectoryAdapter adapter;
    OpenDirectoryCallBack callback;
    Coursewares coursewares;//点击item对象
    Long courseId;//课程表id
    TextView tvDefault;
    int flag;//0表示预览页面   2表示学习页面的目录

    //表示用户点击目录的下标
    //父
    private int selectedGroupPosition;
    //字
    private int selectedChildPosition;
    private List<ExpendGroup> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView=inflater.inflate(R.layout.fragment_directory,container,false);
        expandableListView= (ExpandableListView) convertView.findViewById(R.id.directory_expandableListView);
        tvDefault= (TextView) convertView.findViewById(R.id.directory_default);
        getHttpDate();
        return convertView;
    }


    /**
     * 从activity传递过来的数据
     * @param courseScheduleId 课程表id
     */
    public void setDateFromActivity(Long courseScheduleId,OpenDirectoryCallBack callback,int flag){
        Log.i(TAG, "接口9-获取学员课程章节课时信息: "+courseScheduleId);
        this.courseId=courseScheduleId;
        this.callback=callback;
        this.flag=flag;
    }


    /**
     * 根据课程表获取学员课程章节课时信息列表及学习情况
     *  courseScheduleId 课程表id
     */
    public void getHttpDate(){
        demo=new HttpDemo(this);
        String url= MyConfig.getURl("course/findCoursewareByCourseId");
        List<Pairs> pairsList=new ArrayList<>();
        pairsList.add(new Pairs("id",courseId));
        demo.doHttpGetUser(url,pairsList, MyConfig.CODE_POST_SCHEDULE);
    }


    /**
     * 获取学员某个课程章节课时信息中的 视屏的播放时长:
     * @param courseScheduleId 课程表id
     */
    public void getCurrentTime(Long courseScheduleId,Long courseId){
        Log.i(TAG, "获取学员某个课程章节课时信息中的 视屏的播放时长: "+courseScheduleId);
        demo=new HttpDemo(this);
//        String url= MyConfig.getURl("learn/queryLearnVideoTimeByCourseSchedule");
        String url= MyConfig.getURl("learn/findLearnVideoCourware.action");
        List<Pairs> pairsList=new ArrayList<>();
        pairsList.add(new Pairs("courseScheduleId",courseScheduleId));
        pairsList.add(new Pairs("coursewareId",courseId));
        demo.doHttpGetUser(url,pairsList,0);
    }



    @Override
    public void setMsg(String msg, int requestCode) {
        Logger.d("获取视频的信息："+msg);
            if(requestCode==0){//获取某个章节课程时长
                try {
                    JSONObject obj=new JSONObject(msg);
                    if(obj.getBoolean("success")){
                        JSONObject data=obj.getJSONObject("data");
                        JSONObject learnVideo=data.getJSONObject("learnVideo");
                        //当前播放的时间点
                        int currentPlayTime=learnVideo.getInt("currentPlayTime");
                        //持续看了多长时间
                        int totalPlayTime=learnVideo.getInt("totalPlayTime");
                        //播放次数
                        int payCount=learnVideo.getInt("playCount");
                        Log.i(TAG, "setMsg: 课程当前播放的位置"+currentPlayTime+"持续看的时间："+totalPlayTime);
                        callback.deliveryUri(coursewares,currentPlayTime,totalPlayTime,payCount);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "setMsg: "+e.toString());
                    callback.deliveryUri(coursewares,0,0,0);
                }
            }else if(requestCode==MyConfig.CODE_POST_SCHEDULE){
                Log.i(TAG, "setMsg: " + msg);
                try {
                    Gson gson = new Gson();
                    ParseCouseWares parseCouseWares = gson.fromJson(msg, ParseCouseWares.class);
                    ParseCourseWaresDate parseDataCourseWares = parseCouseWares.getData();
                    List<Coursewares> coursewaresList = parseDataCourseWares.getCoursewares();
                     list = changeDate(coursewaresList);
                    //设置adapter
                    setAdapter(list);
                }catch (Exception e){
                    Log.i(TAG, "setMsg: "+e.toString());
                }
            }
    }

    //设置adapter及其各种属性
    private void setAdapter(final List<ExpendGroup> list) {
        Log.i(TAG, "setAdapter: "+list.size());
        if(list.size()==0){
            tvDefault.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
        }else{
            tvDefault.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
        }
        adapter=new OpenDirectoryAdapter(getActivity(),String.valueOf(courseId),list,flag);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null);//设置圆圈为空
        expandableListView.setDivider(null);//设置分割线为空
        //设置首次加载张开
        for (int i = 0; i < list.size(); i++) {
            expandableListView.expandGroup(i);
        }
        //设置ExpandableListView不能收缩
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        // 设置ExpandableListView子项点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                selectedGroupPosition=groupPosition;
                selectedChildPosition=childPosition;
                //拿到子的对象
                coursewares=list.get(groupPosition).getExpendChild().get(childPosition);
//                getCurrentTime(courseId,coursewares.getCourseId());
                callback.deliveryUri(coursewares,0,0,0);
                setItemChecked(groupPosition, childPosition);
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //注册EventBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * 视频播放完成消息的通知播放下一个点击事件的回调
     * @param viedoCompleteEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViedoCompleteEvent viedoCompleteEvent) {
        selectedChildPosition++;
        if(selectedChildPosition<list.get(selectedGroupPosition).getExpendChild().size()){
            //拿到子的对象
            coursewares=list.get(selectedGroupPosition).getExpendChild().get(selectedChildPosition);
//            getCurrentTime(courseId,coursewares.getId());
            setItemChecked(selectedGroupPosition, selectedChildPosition);
        }else{
            coursewares=list.get(selectedGroupPosition+1).getExpendChild().get(0);
//            getCurrentTime(courseId,coursewares.getId());
            setItemChecked(selectedGroupPosition+1, 0);
        }

    }


    /**
     * 子 项的点击时间处理 （设置背景）
     * @param groupPosition
     * @param childPosition
     */
    private void setItemChecked(int groupPosition, int childPosition) {
        if (expandableListView == null) {
            return;
        }
        int numberOfGroupThatIsOpened = 0;
        for (int i = 0; i < groupPosition; i++) {
            if (expandableListView.isGroupExpanded(i)) {
                numberOfGroupThatIsOpened += adapter.getChildrenCount(i);
            }
        }
        int position = numberOfGroupThatIsOpened + groupPosition
                + childPosition + 1;
        System.out.println("groupPosition=" + groupPosition
                + ", childPosition=" + childPosition + ", position="
                + position + ", isItemChecked="
                + expandableListView.isItemChecked(position));
        if (!expandableListView.isItemChecked(position)) {
            expandableListView.setItemChecked(position, true);
        }
    }




    //对课程章节集合进行排序
    private void ranking(List list) {
        Collections.sort(list, new Comparator<Coursewares>(){
            /*
             * int compare(Student o1, Student o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(Coursewares o1, Coursewares o2) {
                //按照课程的章节进行升序排列
                if(o1.getCoursewareOrder() > o2.getCoursewareOrder()){
                    return 1;
                }
                if(o1.getCoursewareOrder().intValue() == o2.getCoursewareOrder().intValue()){
                    return 0;
                }
                return -1;
            }
        });
    }

    public interface OpenDirectoryCallBack{
        void deliveryUri(Coursewares coursewares, int currentTime,int totalPlayTime,int playCount );
    }

    /**
     * //整理解析出来的数据，转化成需要的集合
     * @param coursewaresList  解析出来的集合信息
     * @return 传入expandableListView的集合
     */
    private List<ExpendGroup> changeDate(List<Coursewares> coursewaresList) throws Exception{
        Log.i(TAG, "changeDate: "+"转换集合");
        List<ExpendGroup> groupList=new ArrayList<>();
        ExpendGroup group;
        List<Coursewares> child=new ArrayList<>();//讲的集合
        List<Coursewares> expendGroup = new ArrayList<>();//章的集合
        Long id;
        for(int i=0;i<coursewaresList.size();i++){//开始获得所有章的集合
            Coursewares waresGroup=coursewaresList.get(i);
            if(waresGroup.getIsGroup()==1){//获得所有的组变成一个集合
                expendGroup.add(waresGroup);
            }else if(waresGroup.getIsGroup()==0){
                child.add(waresGroup);
            }
        }
        ranking(expendGroup);//对 章 进行排序
        ranking(child);
        Log.i(TAG, "changeDate:章的长度 "+expendGroup.size()+" -子的长度"+child.size());
        for(int i=0;i<expendGroup.size();i++){//遍历 章 的集合
            group=new ExpendGroup();
            Coursewares coursewares=expendGroup.get(i);
            id=coursewares.getId();//拿到  章的id
            Log.i(TAG, "changeDate:章节 "+id+":"+coursewares.toString());
            List<Coursewares> expendChild=new ArrayList<>();//子的集合
            for(int j=0;j<child.size();j++){//根据章节 id 查找章节下目录
                Coursewares ware=child.get(j);
                if(id.equals(ware.getGroupId())){//获得对应 章节下面的讲
                    Log.i(TAG, "changeDate: 课时----------------------"+ware.toString());
                    if(ware.getCoursewareAuditStatus()==1){// 0：未审核 1：已审核  2：审核不通过
                        expendChild.add(ware);
                    }
                }
            }
            if(expendChild.size()>0){
                group.setGroupName("章节"+(i+1)+" "+coursewares.getCoursewareName());//设置章节
                group.setExpendChild(expendChild);
                groupList.add(group);
            }
        }
        Log.i(TAG, "changeDate: "+groupList.size());
        return groupList;
    }

}
