package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.Event.ColleageEvent;
import com.sj.yinjiaoyun.xuexi.Event.SwitchTypeEvent;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.MicroHintActivity;
import com.sj.yinjiaoyun.xuexi.activity.OpenCouseHintActivity;
import com.sj.yinjiaoyun.xuexi.activity.OpenCouseItemActivity;
import com.sj.yinjiaoyun.xuexi.activity.SelectCollegeActivity;
import com.sj.yinjiaoyun.xuexi.adapter.FindAdapter;
import com.sj.yinjiaoyun.xuexi.domain.OpenCourseVO;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.SearchData;
import com.sj.yinjiaoyun.xuexi.domains.CouseFuse;
import com.sj.yinjiaoyun.xuexi.domains.Fuse;
import com.sj.yinjiaoyun.xuexi.domains.MicroFuse;
import com.sj.yinjiaoyun.xuexi.domains.ParseOpenIntro;
import com.sj.yinjiaoyun.xuexi.domains.ParseOpenIntroDate;
import com.sj.yinjiaoyun.xuexi.domains.ParseSearch;
import com.sj.yinjiaoyun.xuexi.domains.ParseSearchData;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.DensityUtils;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.StringFormatUtil;
import com.sj.yinjiaoyun.xuexi.view.DeleteEditView;
import com.sj.yinjiaoyun.xuexi.view.FiltrateView;
import com.sj.yinjiaoyun.xuexi.widget.AutoListView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/1.
 * 主Activity - 发现fragment
 */
public class FindFragment extends Fragment implements HttpDemo.HttpCallBack, AutoListView.OnLoadListener, AutoListView.OnRefreshListener{

    String TAG = "findfragemnt";

    Activity activity;
    AutoListView listView;

    String collegeId;//学校id
    private String endUserId;//用户id
    String keyText="";//搜索内容
    int indexType;//搜索类型（）

    TextView tvSearch;
    TextView totalView;
    TextView tvCollegeName;
    ImageView imageView;//院校重置按钮 也就是清除所选院校按钮
    CheckBox cbFiltrate;//筛选结果
    DeleteEditView editText;//自定义编辑框
    StringFormatUtil spanStr;
    int total = 0;

    List<Pairs> pairsList;
    HttpDemo demo;

    List<Fuse> searchList;
    List<Fuse> resultList;
    FindAdapter adapter;
    Fuse fuse;
    CouseFuse couseFuse;
    MicroFuse microFuse;
    private boolean loginState;
    private int isAudit;   //是否需要審核表示

    public static FindFragment newInstance() {
        Bundle args = new Bundle();
        FindFragment fragment = new FindFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<Fuse> result = (List<Fuse>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    searchList.clear();
                    if (result != null) {
                        searchList.addAll(result);
                    }
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    searchList.addAll(result);
                    break;
            }
            spanStr = new StringFormatUtil(getActivity(), "共" + total + "个相关结果",
                    "" + total, R.color.colorGreen).fillColor();
            totalView.setText(spanStr.getResult());
            totalView.setVisibility(View.VISIBLE);
            if (result == null) {
                listView.setResultSize(0);
            } else {
                listView.setResultSize(result.size());
            }
            adapter.onfresh(searchList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        clickItem(position);
                    } catch (Exception e) {
                        Log.e(TAG, "onItemClick:异常 " + e.toString());
                    }
                }
            });
        }
    };


    /**
     * 点击事件
     *
     * @param position
     * @throws Exception
     */
    private void clickItem(int position) throws Exception {
        fuse = searchList.get(position - 2);
        Log.i(TAG, "onItemClick: " + fuse.toString());
        switch (fuse.getIndexType()) {
            case 1://公开课
                couseFuse = exangeCouse(fuse);
                if(!loginState){
                    Intent intent = new Intent(getActivity(), OpenCouseHintActivity.class);
                    Log.i(TAG, "parserIsBuy: " + couseFuse.toString());
                    intent.putExtra("CouseFuse", couseFuse);
                    startActivity(intent);
                }else{
                    getHttpOpenCourseByCourseIdAndType(couseFuse);//公开课先通过接口获取id在判断
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
        couseFuse.setPrice(fuse.getPrice() + "");
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
        MicroFuse microFuse = new MicroFuse();
        microFuse.setMicroId(fuse.getProductId());
        microFuse.setCollegeName(fuse.getCollegeName());
        microFuse.setCourseLogo(fuse.getProductLogoUrl());
        microFuse.setCourseName(fuse.getProductName());
        microFuse.setNumber(fuse.getNumber());
        microFuse.setIndexType(fuse.getIndexType());
        microFuse.setIsFree(fuse.getIsFree());
        if (TextUtils.isEmpty(fuse.getPrice()+"")) {
            microFuse.setPrice(0 + "");
        } else {
            microFuse.setPrice(fuse.getPrice()+"");
        }
        //开课方式
        microFuse.setTutionWay(fuse.getTutionWay());
        microFuse.setCollegeId(""+fuse.getCollegeId());
        Log.i(TAG, "exangeMicro: " + microFuse.toString());
        return microFuse;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        Log.i("showAndHidden", "onCreate: " + "1 findfragment");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_find, container, false);
        filtRate = LayoutInflater.from(activity).inflate(R.layout.fragment_pop_find, null);
        initView(view);
        initPopWindow(filtRate);
        initEvent();
        EventBus.getDefault().register(this);
        return view;
    }


    /**
     * 切换公共课和微专业
     * @param switchTypeEvent
     * 2表示首页过来的删选 微专业    1表示首页过来删选 公开课
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SwitchTypeEvent switchTypeEvent){
        this.indexType = switchTypeEvent.getType();
        flagMold = switchTypeEvent.getType();
        if (flagMold > 0) {//非第一次进入的时候，需要刷新数据
            ftMold.setSelected(flagMold);
        }
        loadData(AutoListView.REFRESH);

    }

    InputMethodManager imm;
    Boolean imageIsDelete = false;
    Boolean isSerch = true;

    public void initView(View view) {
        endUserId= PreferencesUtils.getSharePreStr(getContext(), "username");
        loginState=PreferencesUtils.getSharePreBoolean(getActivity(), Const.LOGIN_STATE);
        searchList = new ArrayList<>();
        totalView = new TextView(activity);
        totalView.setPadding(20, 10, 10, 15);
        spanStr = new StringFormatUtil(activity, "共" + 0 + "个相关结果",
                "" + 0, R.color.colorGreen).fillColor();
        totalView.setBackgroundColor(getResources().getColor(R.color.colorGrayish));
        totalView.setText(spanStr.getResult());
        totalView.setGravity(Gravity.CENTER_VERTICAL);
        totalView.setTextSize(DensityUtils.dp2px(getActivity(),5));

        imageView = view.findViewById(R.id.find_rightIcon);
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText = view.findViewById(R.id.find_edit_search);
        listView =  view.findViewById(R.id.find_ListView);
        tvSearch = view.findViewById(R.id.find_search);
        tvCollegeName = view.findViewById(R.id.find_CollegeName);
        cbFiltrate =view.findViewById(R.id.find_filtrate);
    }


    private void initEvent() {
        tvCollegeName.setOnClickListener(listener);
        imageView.setOnClickListener(listener);
        tvSearch.setOnClickListener(listener);
        listView.addHeaderView(totalView);
        adapter = new FindAdapter(searchList, activity);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        //输入法中搜索的监听
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (isSerch) {
                        isSerch = false;
                        Log.i(TAG, "onEditorAction: " + "键盘搜索");
                        loadData(AutoListView.REFRESH);
                        //键盘消失
                        if(activity.getCurrentFocus().getWindowToken()!=null){
                            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                    }
                    return true;
                }
                return false;
            }
        });
        //清除内容的监听器
        editText.setEditClearListener(new DeleteEditView.EditClearListener() {
            @Override
            public void clearContent() {
                loadData(AutoListView.REFRESH);
            }
        });
        cbFiltrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pop != null) {
                    if (isChecked) {
                        pop.setFocusable(true);
                        if (pop.isShowing()) {
                            //Log.i(TAG, "onCheckedChanged: "+"选中"+"正在显示。设置消失");
                            pop.dismiss();
                        } else {
                            //Log.i(TAG, "onCheckedChanged: "+"选中"+"并显示");
                            pop.showAsDropDown(cbFiltrate, 0, 0);//这里的view表示在其下显示，后面两个参数表示偏移
                            darkenBackground(0.7f);
                        }
                        cbFiltrate.setTextColor(activity.getResources().getColor(R.color.colorGreen));//设置主题颜色值
                    } else {
                        // Log.i(TAG, "onCheckedChanged: "+"未选中");
                        pop.dismiss();
                        cbFiltrate.setTextColor(activity.getResources().getColor(R.color.colorHomeitem));//设置主题颜色值
                    }
                }
            }
        });
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                cbFiltrate.setChecked(false);
                darkenBackground(1f);
            }
        });

        onRefresh();
    }


    PopupWindow pop;
    View filtRate;//筛选布局文件
    Button btnVerify;//筛选确认
    TextView btnReset;//重置筛选
    FiltrateView ftMold;//类型
    FiltrateView ftPrice;//价格
    FiltrateView ftTutionWay;//开课方式
    int flagMold;//筛选结果 类型
    int flagFree;//筛选结果 价格
    int flagTutionWay;//筛选结果 开课方式

    /**
     * 初始化布局文件
     */
    private void initPopWindow(View filtRate) {
        btnVerify = (Button) filtRate.findViewById(R.id.find_pop_verify);
        btnReset = (TextView) filtRate.findViewById(R.id.find_pop_reset);
        ftMold = (FiltrateView) filtRate.findViewById(R.id.find_pop_mold);
        ftPrice = (FiltrateView) filtRate.findViewById(R.id.find_pop_price);
        ftTutionWay = (FiltrateView) filtRate.findViewById(R.id.find_pop_tutionWay);
        pop = new PopupWindow(filtRate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//第一个参数表示要加载的目标view，后面表示大小
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(getResources().getDrawable(R.color.colorGrayish));
        btnVerify.setOnClickListener(listener);
        btnReset.setOnClickListener(listener);
        if (indexType == 1) {//微专业搜索（首页传递过来的按钮）
            flagMold = 1;
            ftMold.setSelected(flagMold);
        } else if (indexType == 2) {//公开课（首页传递过来的按钮）
            flagMold = 2;
            ftMold.setSelected(flagMold);
        }
        ftMold.getGroupView().setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {//类型Group点击事件
                switch (checkedId) {
                    case R.id.view_filtrate_rb0://类型   全部
                        ftTutionWay.setGroupClickable(true);
                        ftTutionWay.setVisibility(View.VISIBLE);
                        break;
                    case R.id.view_filtrate_rb1://类型   微专业
                        ftTutionWay.setGroupClickable(true);
                        ftTutionWay.setVisibility(View.VISIBLE);
                        break;
                    case R.id.view_filtrate_rb2://类型   公安课
                        ftTutionWay.setVisibility(View.GONE);
                        ftTutionWay.setSelected(0);
                        ftTutionWay.setGroupClickable(false);
                        break;
                }
            }
        });
    }



    //下拉加载
    @Override
    public void onLoad() {
        Log.i(TAG, "onLoad: ");
        loadData(AutoListView.LOAD);
    }



    //上拉刷新
    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh: ");
        collegeId="";
        loadData(AutoListView.REFRESH);
    }


    /**
     * 获取网络数据
     *
     * @param page
     * @param requestCode
     */
    private void getHttpDate(int page, int requestCode) {
        Log.i(TAG, "getHttpDate: " + collegeId + " 页数" + page);
        isRefreshSuccess = false;//设置访问网络状态初始化为false
        demo = new HttpDemo(this);
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("search/searchData");
        //院校
        if (!TextUtils.isEmpty(collegeId)) {
            pairsList.add(new Pairs("collegeId", collegeId));
        }
        //类型
        if (flagMold == 1) {//flagMold 表示用户界面选中的项
            pairsList.add(new Pairs("indexType", 2 + "")); //搜索类型   0：专业 ；  1：公开课  2：微专业
        } else if (flagMold == 2) { //公开课
            pairsList.add(new Pairs("indexType", 1 + "")); //搜索类型   0：专业 ；  1：公开课 2：微专业
        }
        //价格
        if (flagFree == 1) {//免费
            pairsList.add(new Pairs("isFree", 1 + ""));//1:免费 0：付费
        } else if (flagFree == 2) {//付费
            pairsList.add(new Pairs("isFree", 0 + ""));//1:免费 0：付费
        }
        //开课方式
        if (flagTutionWay == 1) {
            pairsList.add(new Pairs("tutionWay", 0 + ""));//开课方式 0：随到随学 1:定期开课
        } else if (flagTutionWay == 2) {
            pairsList.add(new Pairs("tutionWay", 1 + ""));//开课方式 0：随到随学 1:定期开课
        }
        //关键字
        keyText = editText.getText().toString();
        if (!(keyText.equals(""))) {
            pairsList.add(new Pairs("key", keyText));
        }
        pairsList.add(new Pairs("page", page + ""));
        pairsList.add(new Pairs("rows", 10 + ""));
        demo.doHttpPostLoading(activity, url, pairsList, requestCode);
    }


    /**
     * 根据课程id和公开课类型查询公开课详细信息   得到公开课id
     *
     * @param
     */
    private void getHttpOpenCourseByCourseIdAndType(CouseFuse couseFuse) {
        demo = new HttpDemo(this);
        String url = MyConfig.getURl("openCourse/findOpenCourseByCourseIdAndType");
        List<Pairs> pairsList = new ArrayList<>();
        pairsList.add(new Pairs("courseId", String.valueOf(couseFuse.getCourseId())));//选课表ID
        pairsList.add(new Pairs("opencourseType", String.valueOf(couseFuse.getCourseType())));//选课表ID
        demo.doHttpGet(url, pairsList, 3);
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
     * 给空间背景设置颜色
     *
     * @param bgcolor
     */
    private void darkenBackground(Float bgcolor) {
        listView.setAlpha(bgcolor);
    }


    Message pullMsg;
    int page = 1;//加载的页数
    Boolean isRefreshSuccess = false;//是否刷新成功

    private void loadData(final int what) {
        // 这里模拟从网络上器获取数据
        if (what == AutoListView.LOAD) {
            getHttpDate(page, 1);//加载
        } else {
            page = 1;
            getHttpDate(page, 2);//刷新时候，页码永远为1
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "run: " + isRefreshSuccess);
                if (isRefreshSuccess) {
                    Log.i(TAG, "---: " + "刷新成功，添加加载数据");
                    pullMsg = handler.obtainMessage();
                    pullMsg.what = what;
                    pullMsg.obj = resultList;
                    handler.sendMessage(pullMsg);
                }
            }
        }).start();
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        try {
            if (requestCode == 1) {//为1是加载 //加载的时候就添加页码
                page = page + 1;
                parserDate(msg);
            } else if (requestCode == 2) {
                page = 2;
                isSerch = true;
                parserDate(msg);
            } else if (requestCode == 3) {//获得公开课id
                parserOpenCourseId(msg);
            } else if (requestCode == 4) {//公开课判断是否购买
                parserOpenIsBuy(msg);
            } else if (requestCode == 5) {//微专业判断是否购买
                parserMicroIsBuy(msg);
            }
        } catch (Exception e) {
            Log.i(TAG, "setMsg: " + e.toString());
        }
    }

    /**
     * 解析刷新数据
     *
     * @param msg
     */
    private void parserDate(String msg) {
         Log.i(TAG, "parserDate: " + msg);
        isRefreshSuccess = true;
        Gson gson = new Gson();
        ParseSearch parserHomeCouse = gson.fromJson(msg, ParseSearch.class);
        SearchData data = parserHomeCouse.getData();
        ParseSearchData searchData = data.getSearchData();
        resultList = searchData.getRows();
        total = searchData.getTotal();
    }



    //解析得到公开课id
    private void parserOpenCourseId(String msg) {
        Gson gson = new Gson();
        ParseOpenIntro parserOpenIntro = gson.fromJson(msg, ParseOpenIntro.class);
        ParseOpenIntroDate data = parserOpenIntro.getData();
        OpenCourseVO opencouse = data.getOpenCourse();
        couseFuse.setOpencourseId(opencouse.getCourseId());
        Log.i(TAG, "parserOpenCourseId: 公开课id " + opencouse.getCourseId());
        isAudit=opencouse.getIsAudit();
        getHttpJudgeCourseIsBug(opencouse.getId(), 1, 4);
    }


    /**
     * 根据公开课和用户id判断用户是否购买
     *
     * @param msg
     */
    private void parserOpenIsBuy(String msg) throws Exception {
        Log.i(TAG, "parserIsBuy:解析是否购买 " + msg);
        JSONObject jsonObject = new JSONObject(msg);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject soaOrderVO = data.getJSONObject("soaOrderVO");
        //定单的状况 100：待付款 101：已付款 102:等待系统审核；109:订单超时取消；110：已完成
        int orderStatus = soaOrderVO.getInt("orderStatus");
        if (orderStatus == 110) {//110 订单完成
            Intent intent = new Intent(activity, OpenCouseItemActivity.class);
            Log.i(TAG, "parserIsBuy: " + couseFuse.toString());
            intent.putExtra("CouseFuse", couseFuse);
            intent.putExtra("CourseScheduleId", String.valueOf(soaOrderVO.getLong("courseScheduleId")));
            startActivity(intent);
        } else {//
            Intent intent = new Intent(activity, OpenCouseHintActivity.class);
            Log.i(TAG, "parserIsBuy: 订单wei完成" + couseFuse.toString());
            intent.putExtra("CouseFuse", couseFuse);
            intent.putExtra("orderStatus", orderStatus);
            intent.putExtra("isAudit",isAudit);
            if (orderStatus == 101 || orderStatus == 100) {//已创建订单，但未完成,未完成时(100,101)存在，无需重复调用生成订单接口.
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
     * 根据微专业和用户id判断用户是否购买
     *
     * @param msg 微专业返回信息
     */
    public void parserMicroIsBuy(String msg) throws Exception {
        Log.i(TAG, "parserMicroIsBuy:解析微专业是否购买 " + msg);
        JSONObject jsonObject = new JSONObject(msg);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject soaOrderVO = data.getJSONObject("soaOrderVO");
        //定单的状况 100：待付款 101：已付款 102:等待系统审核；109:订单超时取消；110：已完成
        int orderStatus = soaOrderVO.getInt("orderStatus");
            Intent intent = new Intent(activity, MicroHintActivity.class);
            Log.i(TAG, "微专业parserIsBuy: " + microFuse.toString());
            intent.putExtra("MicroFuse", microFuse);
            intent.putExtra("EndUserId", endUserId);
            intent.putExtra("orderStatus", orderStatus);
            if (orderStatus == 101 || orderStatus == 100) {//已创建订单，但未完成,未完成时(100,101)存在，无需重复调用生成订单接口
                String orderCode = soaOrderVO.getString("orderCode");
                intent.putExtra("orderCode", orderCode);
                Log.i(TAG, "parserIsBuy: 用户id" + orderCode);
                activity.startActivityForResult(intent,0);
            } else {
                activity.startActivityForResult(intent,0);
            }
    }




    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.find_search://顶部右边搜索
                    loadData(AutoListView.REFRESH);
                    break;
                case R.id.find_CollegeName://选择院校
                    Intent intent1 = new Intent(activity, SelectCollegeActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.find_rightIcon://选择院校右边图标
                    if (imageIsDelete) {
                        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.list_menu_lnor_));
                        collegeId = "";
                        tvCollegeName.setText("");
                        imageIsDelete = false;
                        loadData(AutoListView.REFRESH);
                    }
                    break;
                case R.id.find_pop_reset://pop里面 重置按钮
                    flagMold = 0;
                    flagFree = 0;
                    flagTutionWay = 0;
                    ftMold.setSelected(0);
                    ftPrice.setSelected(0);
                    ftTutionWay.setSelected(0);
                    break;
                case R.id.find_pop_verify://pop里面 确认按钮
                    flagMold = ftMold.getSelected();
                    flagFree = ftPrice.getSelected();
                    flagTutionWay = ftTutionWay.getSelected();
                    loadData(AutoListView.REFRESH);
                    pop.dismiss();
                    break;
            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ColleageEvent colleageEvent){
        this.collegeId = String.valueOf(colleageEvent.getId());
        Logger.d("collegeId阿斯顿爱仕达按时打算达撒旦："+collegeId);
        tvCollegeName.setText(colleageEvent.getOrganizationName());
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.emotionstore_progresscancelbtn));
        imageIsDelete = true;
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
