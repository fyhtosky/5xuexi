package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.SwitchTypeEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.LoginActivity;
import com.sj.yinjiaoyun.xuexi.activity.MicroHintActivity;
import com.sj.yinjiaoyun.xuexi.activity.OpenCouseHintActivity;
import com.sj.yinjiaoyun.xuexi.activity.OpenCouseItemActivity;
import com.sj.yinjiaoyun.xuexi.adapter.HomeAdapter;
import com.sj.yinjiaoyun.xuexi.domain.OpenCourse;
import com.sj.yinjiaoyun.xuexi.domain.OpenCourseVO;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParserHome;
import com.sj.yinjiaoyun.xuexi.domain.ParserHomeDate;
import com.sj.yinjiaoyun.xuexi.domain.Training;
import com.sj.yinjiaoyun.xuexi.domains.CouseFuse;
import com.sj.yinjiaoyun.xuexi.domains.Fuse;
import com.sj.yinjiaoyun.xuexi.domains.MicroFuse;
import com.sj.yinjiaoyun.xuexi.domains.ParseOpenIntro;
import com.sj.yinjiaoyun.xuexi.domains.ParseOpenIntroDate;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.ImageHolderLocalView;
import com.sj.yinjiaoyun.xuexi.utils.ImageHolderNetworkView;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/1.
 * 主Activity -首页fragment
 */
public class HomeFragment extends Fragment implements HttpDemo.HttpCallBack {

    String TAG = "homefragment";
    List<Pairs> pairsList;
    HttpDemo demo;
    Map<String, String> map;
    List<Fuse> fuseList;//公开课 和 微专业 融合后的总集合
    CouseFuse couseFuse;//公开课  处理数据后接收数据的封装类
    MicroFuse microFuse;//微专业  处理数据后接收数据的封装类
    private String endUserId;
    ListView listView;
    HomeAdapter adapter;

    TextView rbMajor;//学历专业
    TextView rbMicrMajor;//微专业
    TextView rbOpenCourse;//公开课


    ConvenientBanner banner;
    Activity activity;
    //标示用户是否登录
    private boolean loginState;
    private int isAudit;   //是否需要審核表示
    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        View head = inflater.inflate(R.layout.head_home, null);
        initView(view, head);
        setBannerSize();
        getHttpDate();
        return view;
    }

    /**
     * 这是轮播图大小
     */
    private void setBannerSize() {
        // 获取屏幕密度（方法2）
        DisplayMetrics dm = getResources().getDisplayMetrics();
//        float density = dm2.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
//        int densityDPI = dm2.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）
//        int screenWidth2 = dm2.widthPixels;      // 屏幕宽（像素，如：480px）
        int screenHeight = dm.heightPixels;     // 屏幕高（像素，如：800px）
        //设置动画图片的图片宽度
        LinearLayout.LayoutParams laParams = (LinearLayout.LayoutParams) banner.getLayoutParams();
        laParams.height = screenHeight / 4;
        banner.setLayoutParams(laParams);
    }


    private void initView(View view, View head) {
        endUserId= PreferencesUtils.getSharePreStr(getContext(), "username");
        loginState=PreferencesUtils.getSharePreBoolean(getActivity(), Const.LOGIN_STATE);
        banner =  head.findViewById(R.id.head_home_convenientBanner);
        rbMajor = head.findViewById(R.id.head_home_rb1);
        rbMicrMajor = head.findViewById(R.id.head_home_rb2);
        rbOpenCourse =  head.findViewById(R.id.head_home_rb3);
        listView = view.findViewById(R.id.home_listView);
        rbMajor.setOnClickListener(listener);
        rbMicrMajor.setOnClickListener(listener);
        rbOpenCourse.setOnClickListener(listener);

        listView.addHeaderView(head);
        adapter = new HomeAdapter(fuseList, getActivity());
        listView.setAdapter(adapter);
        listView.setDividerHeight(1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    clickItem(position);
            }
        });
    }

    /**
     * 列表项   点击时间的操作
     *
     * @param position 点击的位置
     */
    private void clickItem(int position) {
        Log.i(TAG, "onItemClick: ");
        if (position != 9) {//第9条为推荐公开课
            Fuse fuse = fuseList.get(position - 1);
            switch (fuse.getIndexType()) {
                case 1://公开课
                    couseFuse = exangeCouse(fuse);
                    //未登录
                    if(!loginState){
                        Intent intent = new Intent(getActivity(), OpenCouseHintActivity.class);
                        Log.i(TAG, "parserIsBuy: " + couseFuse.toString());
                        intent.putExtra("CouseFuse", couseFuse);
                        startActivity(intent);
                    }else{
                        getOpenCourseByCourseIdAndType(couseFuse);//公开课先通过接口获取id在判断
                    }

                    break;
                case 2://微专业
                    microFuse = exangeMicro(fuse);
                    if(!loginState){
                        Intent intent = new Intent(getActivity(), MicroHintActivity.class);
                        Log.i(TAG, "parserIsBuy: " + microFuse.toString());
                        intent.putExtra("MicroFuse", microFuse);
                        startActivity(intent);
                    }else{
                        getHttpJudgeCourseIsBug(microFuse.getMicroId(), 2, 5);
                    }

                    break;
            }
        }
    }

    /**
     * 公开课 转化数据
     *
     * @param fuse 综合类
     * @return
     */
    private CouseFuse exangeCouse(Fuse fuse) {
        CouseFuse couseFuse = new CouseFuse();
        couseFuse.setCollegeName(fuse.getCollegeName());
        couseFuse.setCourseLogo(fuse.getCourseLogo());
        couseFuse.setCourseName(fuse.getCourseName());
        couseFuse.setNumber(fuse.getNumber());
        couseFuse.setCourseId(fuse.getCourseId());
        couseFuse.setOpencourseId(fuse.getProductId());//公开课id
        couseFuse.setIndexType(fuse.getIndexType());
        couseFuse.setPrice(String.valueOf(fuse.getPrice()));
        couseFuse.setIsFree(fuse.getIsFree());
        couseFuse.setCourseType(fuse.getCourseType());
        Log.i(TAG, "exangeCouse: " + couseFuse.toString());
        return couseFuse;
    }

    /**
     * 微专业 转化数据
     *
     * @param fuse 综合类
     * @return
     */
    private MicroFuse exangeMicro(Fuse fuse) {
        Log.i(TAG, "exangeMicro: " + fuse.toString());
        MicroFuse microFuse = new MicroFuse();
        microFuse.setMicroId(fuse.getProductId());
        microFuse.setCollegeName(fuse.getCollegeName());
        microFuse.setCourseLogo(fuse.getCourseLogo());
        microFuse.setCourseName(fuse.getCourseName());
        microFuse.setNumber(fuse.getNumber());
        microFuse.setIndexType(fuse.getIndexType());
        microFuse.setPrice(String.valueOf(fuse.getPrice()));
        microFuse.setIsFree(fuse.getIsFree());
        microFuse.setCollegeId(String.valueOf(fuse.getCollegeId()));
        microFuse.setTutionWay(fuse.getTutionWay());
        return microFuse;
    }


    Boolean isFirstJudge = true;
    /**
     * 根据课程id和公开课类型查询公开课详细信息   得到公开课id
     *
     * @param
     */
    private void getOpenCourseByCourseIdAndType(CouseFuse search) {
        Log.i(TAG, "getOpenCourseByCourseIdAndType: " + isFirstJudge);
        if (isFirstJudge) {
            isFirstJudge = false;
            demo = new HttpDemo(this);
            String url = MyConfig.getURl("openCourse/findOpenCourseByCourseIdAndType");
            List<Pairs> pairsList = new ArrayList<>();
            pairsList.add(new Pairs("courseId", String.valueOf(search.getCourseId())));//选课表ID
            pairsList.add(new Pairs("opencourseType", String.valueOf(search.getCourseType())));//公开课类型
            demo.doHttpGet(url, pairsList, 3);
        }
    }

    /**
     * 解析得到公开课id  然后通过公开课id判断用户是否购买
     *
     * @param msg
     */
    private void parserOpenCourseId(String msg) {
        Gson gson = new Gson();
        ParseOpenIntro parserOpenIntro = gson.fromJson(msg, ParseOpenIntro.class);
        ParseOpenIntroDate data = parserOpenIntro.getData();
        OpenCourseVO opencouse = data.getOpenCourse();
        Log.i(TAG, "parserOpenCourseId: 公开课id "+opencouse.getCourseId());
        isAudit=opencouse.getIsAudit();
        getHttpJudgeCourseIsBug(opencouse.getId(), 1, 4);
    }


    /**
     * 根据业务id和用户id访问网络（根据结果判断用户是否购买）
     *
     * @param objectId    业务id 专业id or 公开课id or 微专业id
     * @param objectType  业务类型 0： 专业，1：公开课   2 微专业
     * @param requestCode 网络请求码区分   4表示公开课请求  5表示微专业请求
     */
    private void getHttpJudgeCourseIsBug(Long objectId, int objectType, int requestCode) {
        if (objectType == 1 && couseFuse == null) {
            return;
        }
        if (objectType == 2 && microFuse == null) {
            return;
        }
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("order/findOrderByEndUserIdAndObjectIdAndType");
        pairsList.add(new Pairs("endUserId", endUserId));
        pairsList.add(new Pairs("objectId", String.valueOf(objectId)));//业务id 专业id or 公开课id or 微专业id
        pairsList.add(new Pairs("objectType", String.valueOf(objectType)));//业务类型 0： 专业，1：公开课   2 微专业
        demo.doHttpGet(url, pairsList, requestCode);
    }

    /**
     * 解析根据业务id和用户id访问网络返回的结果，判断用户是否购买
     *
     * @param msg 公开课数据
     */
    private void parserJudgeCourseIsBuy(String msg) throws Exception {
        Log.i(TAG, "parserIsBuy:解析公开课是否购买 " + msg);
        JSONObject jsonObject = new JSONObject(msg);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject soaOrderVO = data.getJSONObject("soaOrderVO");
        //定单的状况 100：待付款 101：已付款 102:等待系统审核；109:订单超时取消；110：已完成
        int orderStatus = soaOrderVO.getInt("orderStatus");
        if (orderStatus == 110) {//110 订单完成
            Intent intent = new Intent(getActivity(), OpenCouseItemActivity.class);
            Log.i(TAG, "parserIsBuy: " + " 110" + couseFuse.toString());
            intent.putExtra("CouseFuse", couseFuse);
            intent.putExtra("CourseScheduleId", String.valueOf(soaOrderVO.getLong("courseScheduleId")));
            getActivity().startActivity(intent);
        } else {
            //订单为完成状态
            Intent intent = new Intent(getActivity(), OpenCouseHintActivity.class);
            Log.i(TAG, "parserIsBuy: " + couseFuse.toString());
            intent.putExtra("CouseFuse", couseFuse);
            intent.putExtra("orderStatus", orderStatus);
            intent.putExtra("isAudit",isAudit);
            if (orderStatus == 101 || orderStatus == 100) {//已创建订单，但未完成,未完成时(100,101)存在，无需重复调用生成订单接口
                String orderCode = soaOrderVO.getString("orderCode");
                intent.putExtra("orderCode", orderCode);
                Log.i(TAG, "parserIsBuy: 用户id" + orderCode);
                startActivity(intent);
            } else {
                startActivity(intent);
            }
        }
    }

    /**
     * 解析根据业务id和用户id访问网络返回的结果，判断用户是否购买
     *
     * @param msg 微专业数据
     */
    private void parserMicroIsBuy(String msg) throws Exception {
        Log.i(TAG, "parserMicroIsBuy:解析微专业是否购买 " + msg);
        JSONObject jsonObject = new JSONObject(msg);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject soaOrderVO = data.getJSONObject("soaOrderVO");
        //定单的状况 100：待付款 101：已付款 102:等待系统审核；109:订单超时取消；110：已完成
        int orderStatus = soaOrderVO.getInt("orderStatus");
        Intent intent = new Intent(getActivity(), MicroHintActivity.class);
        Log.i(TAG, "parserIsBuy: " + microFuse.toString());
        intent.putExtra("MicroFuse", microFuse);
        intent.putExtra("orderStatus", orderStatus);
        if (orderStatus == 101 || orderStatus == 100) {//已创建订单，但未完成,未完成时(100,101)存在，无需重复调用生成订单接口
            String orderCode = soaOrderVO.getString("orderCode");
            intent.putExtra("orderCode", orderCode);
            Log.i(TAG, "parserIsBuy: 用户id" + orderCode);
            activity.startActivityForResult(intent, 0);
        } else {
            activity.startActivityForResult(intent, 0);
        }
    }


    /**
     * 获取首页的基础数据（轮播图数据，公开课数据，微专业数据）
     */
    private void getHttpDate() {
        demo = new HttpDemo(this);
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("home/queryHomeAd");
        pairsList.add(new Pairs("courseNum", 8 + ""));
        demo.doHttpGet(url, pairsList, 0);
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.head_home_rb1://课程表   专业
                    if(!loginState){
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }
                    EventBus.getDefault().post(new SwitchTypeEvent(0));
                    break;
                case R.id.head_home_rb2://微专业
                    EventBus.getDefault().post(new SwitchTypeEvent(1));
                    break;
                case R.id.head_home_rb3://公开课
                    EventBus.getDefault().post(new SwitchTypeEvent(2));
                    break;
            }
        }
    };




    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: "+msg);
        try {
            isFirstJudge = true;
            if (requestCode == 0) {//首页基础数据
                parserDate(msg);
            } else if (requestCode == 3) {//根据课程id和公开课类型查询公开课详细信息   得到公开课id
                parserOpenCourseId(msg);
            } else if (requestCode == 4) {//根据公开课id和用户id访问网络（判断用户是否购买）
                parserJudgeCourseIsBuy(msg);
            } else if (requestCode == 5) {//根据微专业id和用户id访问网络（判断用户是否购买）
                parserMicroIsBuy(msg);
            }
        } catch (Exception e) {
            Log.e(TAG, "setMsg: " + e.toString());
        }
    }


    /**
     * 解析首页基础数据
     *
     * @param msg
     */
    private void parserDate(String msg) throws Exception {
        Logger.d(TAG+"===="+msg);
        Gson gson = new Gson();
        ParserHome parserHomeCouse = gson.fromJson(msg, ParserHome.class);
        ParserHomeDate data = parserHomeCouse.getData();
        map = data.getCarouselPhoto();
        fuseList = exangeOpenCouseAndTrainingIntoFuse(data.getOpenCourse(), data.getTraining());
        adapter.onfresh(fuseList);
        setLunBo(map);
    }

    /**
     * 把 公开课集合 和 微专业集合 转化 指定封装类集合 的数据
     *
     * @param couseList 公开课集合
     * @param tranList  微专业集合
     */
    private List<Fuse> exangeOpenCouseAndTrainingIntoFuse(List<OpenCourse> couseList, List<Training> tranList) {
        List<Fuse> fuseList = new ArrayList<>();
        Fuse fuse;
        Training tran;
        for (int i = 0; i < tranList.size(); i++) {//微专业
            fuse = new Fuse();
            tran = tranList.get(i);
            fuse.setIndexType(2);
            fuse.setCourseLogo(tran.getTrainingLogo());
            fuse.setCourseName(tran.getTrainingName());
            fuse.setCollegeName(tran.getCollegeName());
            fuse.setIsFree(tran.getIsFree());//是否免费
            fuse.setMinPrice(tran.getMinPrice());
            fuse.setMaxPrice(tran.getMaxPrice());
            fuse.setNumber(tran.getEnterNumber());
            fuse.setTutionWay(tran.getTutionWay());
            fuse.setCollegeId(tran.getCollegeId());//院校id

            fuse.setProductId(tran.getId());// 微专业id
            fuseList.add(fuse);
        }

        //推荐公开课
        fuse = new Fuse();
        fuse.setIndexType(3);
        fuseList.add(fuse);

        OpenCourse openCouse;
        for (int i = 0; i < couseList.size(); i++) {//公开课
            fuse = new Fuse();
            openCouse = couseList.get(i);
            fuse.setIndexType(1);//公开课
            fuse.setCourseLogo(openCouse.getCourseLogo());
            fuse.setCourseName(openCouse.getCourseName());
            fuse.setCollegeName(openCouse.getCollegeName());
            fuse.setNumber(openCouse.getNumber());
            Double price = openCouse.getPrice();
            fuse.setPrice(price);
            Byte a ;
            if (price > 0) {
                a = 0;
            } else {
                a = 1;
            }
            fuse.setIsFree(a);
            fuse.setCourseId(openCouse.getCourseId());//课程id
            fuse.setProductId(openCouse.getId());// //公开课id
            fuse.setCourseType(openCouse.getOpenCourseType());//公开课类型
            fuseList.add(fuse);
        }
        Log.i(TAG, "exangeOpenCouseAndTrainingIntoFuse: " + fuseList.size());
        return fuseList;
    }


    List<String> imagesList;
    List<Integer> imgs;//本地图片

    /**
     * 轮播设置
     *
     * @param map
     * @throws Exception
     */
    public void setLunBo(Map<String, String> map) {
        if (map == null || map.size() == 0) {//设置本地默认轮播图片
            imgs = new ArrayList<>();
            imgs.add(R.mipmap.banner1);
            banner.setPages(new CBViewHolderCreator() {
                @Override
                public Object createHolder() {
                    return new ImageHolderLocalView();
                }
            }, imgs).setPageIndicator(new int[]{R.mipmap.banner, R.mipmap.banner_active});
        } else {//设置网络认轮播图片
            imagesList = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Log.i(TAG, "saveLibrary: " + "Key = " + entry.getKey() + ", Value = " + entry.getValue());
                imagesList.add(entry.getKey());
            }
            banner.setPages(new CBViewHolderCreator<ImageHolderNetworkView>() {
                @Override
                public ImageHolderNetworkView createHolder() {
                    return new ImageHolderNetworkView();
                }
            }, imagesList).setPageIndicator(new int[]{R.mipmap.banner, R.mipmap.banner_active});
        }
    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        banner.startTurning(5000);
    }

    //停止翻页
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //停止翻页
        banner.stopTurning();
    }


    public interface CallBackFromHome {
        void handOver(int flag);
    }


}
