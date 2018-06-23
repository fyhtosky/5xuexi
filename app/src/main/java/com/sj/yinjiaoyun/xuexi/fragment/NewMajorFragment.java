package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.MenuAdapter;
import com.sj.yinjiaoyun.xuexi.db.DbOperatorProductVO;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParseProductForChoose;
import com.sj.yinjiaoyun.xuexi.domain.ParseProductForChooseDate;
import com.sj.yinjiaoyun.xuexi.domain.ProductDirection;
import com.sj.yinjiaoyun.xuexi.domain.SoaProductVOs;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.sj.yinjiaoyun.xuexi.view.ScheduleTitleView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/10.
 * 课程表  专业选择
 */
public class NewMajorFragment extends Fragment implements HttpDemo.HttpCallBack {

    String TAG = "newmajor";
    View convertView;//转化的布局文件
    ScheduleTitleView titleView;//顶部第一条专业名称

    TextView tv_YuanXiao;
    TextView tv_ZhaoSheng;//招生季
    TextView tv_XueZhi;//学制
    TextView tv_Leibie;//教育类别
    TextView tv_Cengci;//层次
    TextView tv_Xuexi;//学习形式
    TextView tv_majorPercent;//专业进度

    ProgressBar progressBar;
    SoaProductVOs soa;//顶部专业解析
    int selectedPosition = 0;//顶部默认选中的位子的下标
    List<SoaProductVOs> productVOList;//我的专业界面
    LayoutInflater inflater;

    PopupWindow pop;
    View menuView;//下拉菜单转化的布局控件
    ListView lv;//下拉菜单pop里面的listview

    View containerFragment;//fragment父
    View containerProgress;//进度条的容器

    String progress;//从课程列表每一个课程进度条的值

    HttpDemo demo;
    List<Pairs> pairsList;
    String endUserId;//用户id
    DbOperatorProductVO dbOperatorProductVO;//专业信息数据库
    Activity content;

    FragmentManager manager;
    FragmentTransaction tran;

    List<ProductDirection> directionList;//方向集合
    FangxFragment fangxFragment;//方向选择页面
    KecFragment kecFragment;//课程选择页面
    TabulationFragment tabFragment;//选完方向 选完课程后的fragment列表显示

    FangxFragment.CallBackFromFangx callBackFromFangx;//从选方向回调的
    KecFragment.CallBackFromKec callBackFromKec;      //从课程回调的
    TabulationFragment.CallBackFromTabulation callBackFromTabu;//从课程列表里面传递过来的（主要用于课程表）

    static Boolean isFirst = true;
    MenuAdapter menuAdapter;
    int flag = 0;//选择方向或选择课程后，却别跳转到那个页面（0，表示没选方向课程；1表示选了方向，跳转课程；2表示选了课程，跳转列表）

    ScrollView scrollView;
    View couseContainer;
    View defaultContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.content = getActivity();
        manager = getChildFragmentManager();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        convertView = inflater.inflate(R.layout.fragment_newmajor, container, false);
        initView(convertView);
        initPopMenu();
        getHeadFormHttp();
        return convertView;
    }


    //初始化控件
    private void initView(View convertView) {
        Log.i(TAG, "initView: ");
        couseContainer = convertView.findViewById(R.id.major_couseContainer);
        defaultContainer=convertView.findViewById(R.id.newmajor_defaultContainer);
        scrollView = (ScrollView) convertView.findViewById(R.id.root_ScrollVIew);
        scrollView.smoothScrollTo(0, 0);//设置至顶
        //上部分 身体列表显示,方向选择，课程选择
        containerFragment = convertView.findViewById(R.id.newmajor_container);
        containerProgress = convertView.findViewById(R.id.report_containerProgress);
        //头部文件
        titleView = (ScheduleTitleView) convertView.findViewById(R.id.major_scheduleTitleView);//顶部第一条
        tv_YuanXiao = (TextView) convertView.findViewById(R.id.report_yuanxiao);
        tv_XueZhi = (TextView) convertView.findViewById(R.id.report_xuezhi);
        tv_ZhaoSheng = (TextView) convertView.findViewById(R.id.report_zhaosheng);
        tv_Leibie = (TextView) convertView.findViewById(R.id.report_leibie);
        tv_Cengci = (TextView) convertView.findViewById(R.id.report_cengci);
        tv_Xuexi = (TextView) convertView.findViewById(R.id.report_xuexi);
        tv_majorPercent = (TextView) convertView.findViewById(R.id.report_majorPercent);
        progressBar = (ProgressBar) convertView.findViewById(R.id.report_micro_progressbar);
        menuView = inflater.inflate(R.layout.fragment_pop_menu, null);
        lv = (ListView) menuView.findViewById(R.id.menu_pop);
    }

    //根据id从网络上获取填充顶部titleView数据（顶部第一条专业切换数据集合）
    private void getHeadFormHttp() {
        Log.i(TAG, "getHeadFormHttp: ====================");
        demo = new HttpDemo(this);
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("product/findMyProductForChoose");
        Log.i(TAG, "getHeadFormHttp: " + "网络请求" + endUserId);
        if(endUserId!=null){
            pairsList.add(new Pairs("endUserId", endUserId));
            demo.doHttpPost(url, pairsList, MyConfig.CODE_POST_MAJOR);
        }
    }



    //初始顶部专业切换数据，设置pop
    private void initEvent(List<SoaProductVOs> productVOList) throws Exception {
        if (productVOList != null) {
            Log.i(TAG, "initEvent: " + "专业的个数 " + productVOList.size());
            //默认设置顶部第一个信息
            soa = productVOList.get(selectedPosition);
        }
        directionList = soa.getSoaEnrollPlanDirectionVO();
        if (isFirst) {
            tran = manager.beginTransaction();
            fangxFragment = new FangxFragment();
            fangxFragment.setFangxDateFromMajor(directionList, callBackFromFangx, String.valueOf(soa.getEnrollPlanId()), endUserId, String.valueOf(soa.getId()));
            tran.add(R.id.newmajor_container, fangxFragment);
//            tran.commit();
            tran.commitAllowingStateLoss();
        }
        setCenterMessage(soa);
        titleView.setTextTitle(soa.getProductName() + "");
        Log.i(TAG, "下表为0的信息:******" + soa.toString());
        titleView.isChoice().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                            containerFragment.setAlpha(0.7f);//pop显示时给列表项设置变灰
                            pop.showAsDropDown(titleView, 0, 0);//这里的view表示在其下显示，后面两个参数表示偏移
                        }
                        titleView.setTextColor(getActivity().getResources().getColor(R.color.colorGreen));//设置主题颜色值
                    } else {
                        // Log.i(TAG, "onCheckedChanged: "+"未选中");
                        containerFragment.setAlpha(1.0f);//pop消失时给列表项设置复原
                        pop.dismiss();
                        titleView.setTextColor(getActivity().getResources().getColor(R.color.colorHomeitem));//设置主题颜色值
                    }
                }
            }
        });
        titleView.clickView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.isChoice().toggle();
            }
        });
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                titleView.isChoice().setChecked(false);
            }
        });
    }


    //设置pop下拉菜单
    private void initPopMenu() {
        menuAdapter = new MenuAdapter(getActivity(), productVOList);
        lv.setAdapter(menuAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemSelected: " + position);
                try {
                    titleView.isChoice().setChecked(false);
                    // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                    MenuAdapter.ViewHolder viewHolder = (MenuAdapter.ViewHolder) view.getTag();
                    viewHolder.cb.toggle();// 把CheckBox的选中状态改为当前状态的反,gridview确保是单一选中
                    soa = productVOList.get(position);
                    selectedPosition = position;
                    titleView.setTextTitle(soa.getProductName() + "");
                    Log.i(TAG, "onItemClick: " + soa.getProductName() + " " + soa.getStatus());
                    setCenterMessage(soa);
                    NewMajorFragment.isScrollViewToTop = true;//此种状态设置其滑动不到顶
                } catch (Exception e) {
                    Log.e(TAG, "onItemClick: " + e.toString());
                }
            }
        });
        pop = new PopupWindow(menuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//第一个参数表示要加载的目标view，后面表示大小
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(getResources().getDrawable(R.color.colorGrayish));
    }


    //设置中间那一部分类型
    private void setCenterMessage(SoaProductVOs soa) {
        if (soa == null) return;
        tv_YuanXiao.setText("" + soa.getCollegeName());//所属院校：
        tv_ZhaoSheng.setText("招生季：" + soa.getEnrollPlanSeason());
        String leibie = String.valueOf(soa.getProductType());
        if (!(leibie.equals(""))) {
            tv_Leibie.setText("教育类别：" + MyConfig.productType().get(leibie));
            tv_XueZhi.setText("学制：" + soa.getProductLearningLength() + MyConfig.productLearningLength().get(leibie));//学制
        }
        String cengci = String.valueOf(soa.getProductGradation());
        Log.i(TAG, "setCenterMessage: ------" + cengci);
        if (cengci.equals("") || cengci.equals("0")) {
            tv_Cengci.setVisibility(View.GONE);
        } else {
            tv_Cengci.setVisibility(View.VISIBLE);
            tv_Cengci.setText("层次：" + MyConfig.productGradation().get(cengci));
        }
        String xuexi = String.valueOf(soa.getLearningModality());
        if (xuexi.equals("") || xuexi == null || xuexi.equals("0")) {
            tv_Xuexi.setVisibility(View.GONE);
        } else {
            Log.i(TAG, "setCenterMessage:--- " + xuexi);
            tv_Xuexi.setVisibility(View.VISIBLE);
            tv_Xuexi.setText("学习形式：" + MyConfig.learningModality().get(xuexi));
        }
        directionList = soa.getSoaEnrollPlanDirectionVO();//方向
        if (soa.getStatus() == 1) {//没选方向
            Log.i(TAG, "setCenterMessage:没选方向 " + soa.getStatus());
            containerProgress.setVisibility(View.GONE);
            Log.i(TAG, "initEvent: " + soa.toString());
            directionList = soa.getSoaEnrollPlanDirectionVO();
            tran = manager.beginTransaction();
            fangxFragment = new FangxFragment();
            fangxFragment.setFangxDateFromMajor(directionList, callBackFromFangx, String.valueOf(soa.getEnrollPlanId()), endUserId, String.valueOf(String.valueOf(soa.getId())));
            tran.replace(R.id.newmajor_container, fangxFragment);
//            tran.commit();
            tran.commitAllowingStateLoss();
        } else if (soa.getStatus() == 2) {//没选课程
            Log.i(TAG, "setCenterMessage:没选课程" + soa.getStatus());
            containerProgress.setVisibility(View.GONE);
            Log.i(TAG, "initEvent: " + soa.toString());
            directionList = soa.getSoaEnrollPlanDirectionVO();
            tran = manager.beginTransaction();
            kecFragment = new KecFragment();
            kecFragment.setKecDateFromMajor(callBackFromKec, endUserId, soa);
            tran.replace(R.id.newmajor_container, kecFragment);
//            tran.commit();
            tran.commitAllowingStateLoss();
        } else if (soa.getStatus() == 3) {//已选方向，已选课程情况
            //进度条容器显示与隐藏
            containerProgress.setVisibility(View.VISIBLE);
            tran = manager.beginTransaction();
            tabFragment = new TabulationFragment();
            tabFragment.setTabDateFromMajor(callBackFromTabu, endUserId, soa.getEnrollPlanId(), soa.getId());
            tran.replace(R.id.newmajor_container, tabFragment);
//            tran.commit();
            tran.commitAllowingStateLoss();
        }
    }


    @Override
    public void setMsg(String msg, int requestCode) {
        Log.i(TAG, "setMsg: " + requestCode + ":" + msg);
        try {
            if (requestCode == MyConfig.CODE_POST_MAJOR) {//获取头部文件信息、、获取个人报名的专业信息
                parserData(msg);
            }
        } catch (Exception e) {
             e.getLocalizedMessage();
        }
    }


    //解析填充顶部的专业数据
    private void parserData(String msg) {
        Log.i(TAG, "解析专业 ---------------" + msg);
        try {
            Gson gson = new Gson();
            ParseProductForChoose parseProduct = gson.fromJson(msg, ParseProductForChoose.class);
            ParseProductForChooseDate data = parseProduct.getData();
            productVOList = data.getSoaProductVOs();
            if (productVOList == null || productVOList.size() == 0) {
                defaultContainer.setVisibility(View.VISIBLE);
                couseContainer.setVisibility(View.GONE);
            }else {
                defaultContainer.setVisibility(View.GONE);
                couseContainer.setVisibility(View.VISIBLE);
                initEvent(productVOList);
                menuAdapter.fresh(productVOList);
                replaceFangxOrKec(flag);
            }
        } catch (Exception e) {
            Log.e(TAG, "parserData: " + e.toString());
        }
    }

    //从课程表传递过来的信息
    public void deliveryMajorForSchedule(int flag) {
        isFirst = false;
        this.flag = flag;
        getHeadFormHttp();//刷新网络数据
    }

    //选完方向 或者 选完课程时候 回调专业刷新网络数据后，再决定替换某个
    public void replaceFangxOrKec(int flag) {
        if (flag == 0) {
            return;
        }
        if (flag == 1) {//选择课程
            Log.i(TAG, "课程表里面: " + "方向已选完，替换方向 显示选择课程");
            containerProgress.setVisibility(View.GONE);
            tran = manager.beginTransaction();
            kecFragment = new KecFragment();
            kecFragment.setKecDateFromMajor(callBackFromKec, endUserId, soa);
            tran.replace(R.id.newmajor_container, kecFragment);
//            tran.commit();
            tran.commitAllowingStateLoss();
        } else if (flag == 2) {//方向选完 课程选完 显示已选的列表
            Log.i(TAG, "课程表里面: " + "方向选完 课程选完 替换课程 显示已选的列表");
            containerProgress.setVisibility(View.VISIBLE);
            tran = manager.beginTransaction();
            tabFragment = new TabulationFragment();
            tabFragment.setTabDateFromMajor(callBackFromTabu, endUserId, soa.getEnrollPlanId(), soa.getId());
            tran.replace(R.id.newmajor_container, tabFragment);
//            tran.commit();
            tran.commitAllowingStateLoss();
        }
    }


    //从课程表传递过来的百分比 （数据来源于课程列表）
    public void deliveryProgressForSchedule(String progress) {
        if (!TextUtils.isEmpty(progress)) {
            tv_majorPercent.setText(progress);
            progressBar.setProgress(MyUtil.transFormation(progress));
        } else {
            this.progress = progress;
            tv_majorPercent.setText("0%");
            progressBar.setProgress(0);
        }
    }

    public void setNewMajorCallBack(FangxFragment.CallBackFromFangx callBackFromFangx,
                                    KecFragment.CallBackFromKec callBackFromKec,
                                    TabulationFragment.CallBackFromTabulation callBackFromTabu,
                                    String endUserId) {
        this.callBackFromFangx = callBackFromFangx;
        this.callBackFromKec = callBackFromKec;
        this.callBackFromTabu = callBackFromTabu;
        this.endUserId = endUserId;
    }


    public static Boolean isScrollViewToTop = true;/*区分scrollView是否滑动置顶,默认为true （除了学历
      课程里面的点击事件跳转到详情页面，在回退到此页面时候不置顶，其余情况均置顶）其余情况包括
      首页4个tab  show和hidden切换  ,课程表里面 学历教育公开课 tab show和hidden切换*/

    /**
     * 设置scrollView置顶
     */
    public void smoothScrollTo() {
        if (scrollView != null) {
            if (isScrollViewToTop) {
                Log.i("showAndHidden", "tabulation  smoothScrollTo");
                scrollView.smoothScrollTo(0, 0);
            }
        }
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            Log.i("onhiddenchanged", "onHiddenChanged: ");
            if(endUserId!=null) {
                Log.i("onhiddenchanged", "onHiddenChanged:学历课程 ");
                getHeadFormHttp();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            /**
             * 解决Fragment相关问题,java-lang-illegalstateexception-activity-has-been-destroyed
             */
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            e.getLocalizedMessage();
        } catch (IllegalAccessException e) {
           e.getLocalizedMessage();
        }
    }
}