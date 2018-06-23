package com.sj.yinjiaoyun.xuexi.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.Event.ViedoCompleteEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.ScheduleDirectoryAdapter;
import com.sj.yinjiaoyun.xuexi.domain.Coursewares;
import com.sj.yinjiaoyun.xuexi.domain.ExpendGroup;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParseCourseWaresDate;
import com.sj.yinjiaoyun.xuexi.domain.ParseCouseWares;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;

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
 * Created by ${沈军 961784535@qq.com} on 2016/8/29.
 * 学历课程 。微专业  视屏学习页 目录  下面fragment对应  --目录
 * 接口9    /api/v1/course/findCoursewareByCourseId.action
 */
public class DirectoryFragment extends Fragment implements HttpDemo.HttpCallBack {

    String TAG = "fragmentdirectory";
    View convertView;
    HttpDemo demo;
    ExpandableListView expandableListView;
    ScheduleDirectoryAdapter adapter;
    Context context;
    FragmentCallBack callback;
    Coursewares coursewares;//点击item对象
    String courseScheduleId;//课程表id

    long totalTime = 0;//item项里面成绩的中总时长
    Boolean isHaveTeacher = true;
    //表示用户点击目录的下标
    //父
    private int selectedGroupPosition;
    //字
    private int selectedChildPosition;
    private List<ExpendGroup> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demo = new HttpDemo(this);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("directory", "onCreateView: " + "DirectoryFragment");
        convertView = inflater.inflate(R.layout.fragment_directory, container, false);
        expandableListView = (ExpandableListView) convertView.findViewById(R.id.directory_expandableListView);
        return convertView;
    }


    @Override
    public void onResume() {
        super.onResume();
//
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
     *
     * @param viedoCompleteEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViedoCompleteEvent viedoCompleteEvent) {
        selectedChildPosition++;
        if (selectedChildPosition < list.get(selectedGroupPosition).getExpendChild().size()) {
            //拿到子的对象
            coursewares = list.get(selectedGroupPosition).getExpendChild().get(selectedChildPosition);
            getCurrentTime(courseScheduleId, coursewares.getId());
            setItemChecked(selectedGroupPosition, selectedChildPosition);
        } else {
            coursewares = list.get(selectedGroupPosition + 1).getExpendChild().get(0);
            getCurrentTime(courseScheduleId, coursewares.getId());
            setItemChecked(selectedGroupPosition + 1, 0);
        }

    }

    /**
     * 从activity传递过来的数据
     *
     * @param courseScheduleId 课程表id
     */
    public void setDateFromActivity(String courseScheduleId, FragmentCallBack callback) {
        this.courseScheduleId = courseScheduleId;
        this.callback = callback;
        getHttpDate();
    }

    /**
     * (微专业没有授课老师是没有答疑考试的 ，其他课程不收此限制)
     *
     * @param isHaveTeacher true 表示有 ，false表示没有
     */
    public void setIsHaveTeacherFromActivity(Boolean isHaveTeacher) {
        this.isHaveTeacher = isHaveTeacher;
    }


    //做完作业后刷新
    public void setOnfreshFromActivity() {
        getHttpDate();
    }

    /**
     * 根据课程表获取学员课程章节课时信息列表及学习情况
     * courseScheduleId 课程表id
     */
    public void getHttpDate() {
        demo = new HttpDemo(this);
        String url = MyConfig.getURl("course/findCoursewareByCourseScheduleId");
        List<Pairs> pairsList = new ArrayList<>();
        pairsList.add(new Pairs("id", courseScheduleId));
        demo.doHttpGet(url, pairsList, MyConfig.CODE_POST_SCHEDULE);
    }


    /**
     * 获取学员某个课程章节课时信息中的 视屏的播放时长:
     *
     * @param courseScheduleId 课程表id
     */
    public void getCurrentTime(String courseScheduleId, Long courseId) {
        Log.i(TAG, "获取学员某个课程章节课时信息中的 视屏的播放时长: " + courseScheduleId);
        demo = new HttpDemo(this);
//        String url= MyConfig.getURl("learn/queryLearnVideoTimeByCourseSchedule");
        String url = MyConfig.getURl("learn/findLearnVideoCourware.action");
        List<Pairs> pairsList = new ArrayList<>();
        pairsList.add(new Pairs("courseScheduleId", courseScheduleId));
        pairsList.add(new Pairs("coursewareId", String.valueOf(courseId)));
        demo.doHttpGet(url, pairsList, 0);
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: " + msg);
        try {
            if (requestCode == 0) {//获取某个章节课程时长
                try {
                    JSONObject obj = new JSONObject(msg);
                    if (obj.getBoolean("success")) {
                        JSONObject data = obj.getJSONObject("data");
                        JSONObject learnVideo = data.getJSONObject("learnVideo");
                        //当前播放的时间点
                        int currentPlayTime = learnVideo.getInt("currentPlayTime");
                        //持续看了多长时间
                        int totalPlayTime = learnVideo.getInt("totalPlayTime");
                        //播放次数
                        int payCount = learnVideo.getInt("playCount");
                        Log.i(TAG, "setMsg: 课程当前播放的位置" + currentPlayTime + "持续看的时间：" + totalPlayTime);
                        callback.deliveryUri(coursewares, currentPlayTime, totalPlayTime, payCount);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "setMsg: " + e.toString());
                    callback.deliveryUri(coursewares, 0, 0, 0);
                }
            } else {
                Gson gson = new Gson();
                ParseCouseWares parseCouseWares = gson.fromJson(msg, ParseCouseWares.class);
                ParseCourseWaresDate parseDataCourseWares = parseCouseWares.getData();
                List<Coursewares> coursewaresList = parseDataCourseWares.getCoursewares();
                Log.i(TAG, "setMsg: " + coursewaresList.size());
                list = changeDate(coursewaresList);
                //设置adapter
                setAdapter(list);
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }


    /**
     *
     */
    public void refreshProgressData(int state) {
        byte s = (byte)state;
        byte s1 = coursewares.getVideoState().byteValue();
        if(s > s1){
            coursewares.setVideoState(s);
            adapter.notifyDataSetChanged();
        }
    }


    //设置adapter及其各种属性
    private void setAdapter(final List<ExpendGroup> list) {
        Log.i(TAG, "setAdapter: " + list.size());
        adapter = new ScheduleDirectoryAdapter(context, list, courseScheduleId, isHaveTeacher);
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
                selectedGroupPosition = groupPosition;
                selectedChildPosition = childPosition;
                //拿到子的对象
                coursewares = list.get(groupPosition).getExpendChild().get(childPosition);
                getCurrentTime(courseScheduleId, coursewares.getId());
                setItemChecked(groupPosition, childPosition);
                return true;
            }
        });

    }

    /**
     * 子 项的点击时间处理 （设置背景）
     *
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
        Collections.sort(list, new Comparator<Coursewares>() {
            /*
             * int compare(Student o1, Student o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            public int compare(Coursewares o1, Coursewares o2) {
                //按照课程的章节进行升序排列
                if (o1.getCoursewareOrder() > o2.getCoursewareOrder()) {
                    return 1;
                }
                if (o1.getCoursewareOrder().intValue() == o2.getCoursewareOrder().intValue()) {
                    return 0;
                }
                return -1;
            }
        });
    }

    /**
     * //整理解析出来的数据，转化成需要的集合
     *
     * @param coursewaresList 解析出来的集合信息
     * @return 传入expandableListView的集合
     */
    private List<ExpendGroup> changeDate(List<Coursewares> coursewaresList) {
        Log.i(TAG, "changeDate: " + "转换集合");
        totalTime = 0;
        List<ExpendGroup> groupList = new ArrayList<>();
        ExpendGroup group;
        List<Coursewares> child = new ArrayList<>();//讲的集合
        List<Coursewares> expendGroup = new ArrayList<>();//章的集合
        Long id;
        for (int i = 0; i < coursewaresList.size(); i++) {//开始获得所有章的集合
            Coursewares waresGroup = coursewaresList.get(i);
            if (waresGroup.getIsGroup() == 1) {//获得所有的组变成一个集合
                expendGroup.add(waresGroup);
            } else if (waresGroup.getIsGroup() == 0) {
                child.add(waresGroup);
            }
        }
        ranking(expendGroup);//对 章 进行排序
        ranking(child);
        Log.i(TAG, "changeDate:章的长度 " + expendGroup.size() + " -子的长度" + child.size());
        for (int i = 0; i < expendGroup.size(); i++) {//遍历 章 的集合
            group = new ExpendGroup();
            Coursewares coursewares = expendGroup.get(i);
            id = coursewares.getId();//拿到  章的id
            //  Log.i(TAG, "changeDate:章节 "+id+":"+coursewares.toString());
            List<Coursewares> expendChild = new ArrayList<>();//子的集合
            for (int j = 0; j < child.size(); j++) {//根据章节 id 查找章节下目录
                Coursewares ware = child.get(j);
                if (id.equals(ware.getGroupId())) {//获得对应 章节下面的讲
                    //     Log.i(TAG, "changeDate: 课时----------------------"+ware.toString());
                    if (ware.getCoursewareAuditStatus() == 1) {// 0：未审核 1：已审核  2：审核不通过
                        expendChild.add(ware);
                        if (ware.getLearnTime() != null)
                            totalTime += ware.getLearnTime();
                    }
                }
            }
            if (expendChild.size() > 0) {
                group.setGroupName("章节" + (i + 1) + " " + coursewares.getCoursewareName());//设置章节
                group.setExpendChild(expendChild);
                groupList.add(group);
            }
        }
        callback.deliverTime(totalTime);
        Log.i(TAG, "changeDate: " + totalTime);
        //  Log.i(TAG, "changeDate: 返回集合的大小-==========="+groupList.size());
        return groupList;
    }

    public interface FragmentCallBack {
        void deliveryUri(Coursewares coursewares, int currentTime, int totalPlayTime, int playCount);

        void deliverTime(long totalTime);//传递总播放时长
    }

}
