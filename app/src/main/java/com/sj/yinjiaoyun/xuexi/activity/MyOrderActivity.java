package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.OrderAdapter;
import com.sj.yinjiaoyun.xuexi.domain.ItemOrderVO;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.MyOrderChild;
import com.sj.yinjiaoyun.xuexi.domain.MyOrderParent;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ParserOrder;
import com.sj.yinjiaoyun.xuexi.domain.ParserOrderDate;
import com.sj.yinjiaoyun.xuexi.domain.Rows;
import com.sj.yinjiaoyun.xuexi.domain.SoaOrderParameter;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.view.TitleBarView;
import com.sj.yinjiaoyun.xuexi.widget.AutoListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/2.
 * 主页- 我fragment-我的订单
 *
 */
public class MyOrderActivity extends MyBaseActivity implements HttpDemo.HttpCallBack,AutoListView.OnRefreshListener,AutoListView.OnLoadListener
{

    String TAG = "myorder";
    private String endUserId;
    List<Pairs> pairsList;
    HttpDemo demo;

    TitleBarView titleBarView;
    AutoListView listView;
    OrderAdapter adapter;

    List<MyOrderParent> orderList;
    List<MyOrderParent> resultList;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<MyOrderParent> result = (List<MyOrderParent>) msg.obj;
            switch (msg.what) {
                case AutoListView.REFRESH:
                    listView.onRefreshComplete();
                    if (result != null) {
                        orderList.clear();
                        orderList.addAll(result);
                    }
                    break;
                case AutoListView.LOAD:
                    listView.onLoadComplete();
                    orderList.addAll(result);
                    break;
            }
            try {
                if (result == null) {
                    listView.setResultSize(0);
                } else {
                    listView.setResultSize(result.size());
                }
                listView.setDividerHeight(0);
                adapter.refresh(orderList);
                Log.i(TAG, "handleMessage: " + "完成操作");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(TAG, "onItemClick: " + "item项被点击");
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "handleMessage: " + e.toString());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myorder);
        initView();
    }

    private void initView() {
        orderList = new ArrayList<>();
        demo = new HttpDemo(this);
        endUserId=  PreferencesUtils.getSharePreStr(MyOrderActivity.this, "username");
        listView = (AutoListView) findViewById(R.id.myorder_listView);
        titleBarView = (TitleBarView) findViewById(R.id.myorder_titleBarView);
        titleBarView.getBackImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        adapter = new OrderAdapter(this, orderList, d, endUserId);
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(this);
        listView.setOnLoadListener(this);
        listView.setDividerHeight(5);
        loadData(AutoListView.REFRESH);
    }

    @Override
    public void onLoad() {
        loadData(AutoListView.LOAD);
    }

    @Override
    public void onRefresh() {
        loadData(AutoListView.REFRESH);
    }

    Message pullMsg;
    int page = 1;//加载的页数
    Boolean isRefreshSuccess = false;//是否刷新成功

    private void loadData(final int what) {
        // 这里模拟从网络上器获取数据
        if (what == AutoListView.LOAD) {
            Log.i(TAG, "loadData: " + "加载");
            getOrderForHttp(page, 1);//加载
        } else {
            page = 1;
            getOrderForHttp(page, 2);//刷新时候，页码永远为1
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                Log.e(TAG, "---: " + "刷新成功，"+isRefreshSuccess);
                if (isRefreshSuccess) {
                    Log.e(TAG, "---: " + "刷新成功，添加加载数据");
                    pullMsg = handler.obtainMessage();
                    pullMsg.what = what;
                    pullMsg.obj = resultList;
                    handler.sendMessage(pullMsg);
                }
            }
        }).start();
    }

    //发现页面 反馈学校的选择结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case 1:
                Log.i(TAG, "onActivityResult: ");
                loadData(AutoListView.REFRESH);
                break;
            default:
                break;
        }
    }

    /**
     * 获取该用户的订单
     *
     * @param page 订单的页数
     */
    public void getOrderForHttp(int page, int flag) {
        Log.i(TAG, "getOrderForHttp: 页数：" + page);
        pairsList = new ArrayList<>();
        String url = MyConfig.getURl("order/findOrderForPager");
        pairsList.add(new Pairs("endUserId", endUserId));
        pairsList.add(new Pairs("page", page + ""));
        pairsList.add(new Pairs("rows", 10 + ""));//设置默认加载10条
        demo.doHttpPost(url, pairsList, flag);
    }


    @Override
    public void setMsg(String msg, int requestCode) {
      //  Log.i(TAG, "setMsg: " + msg);
        try {
            if (requestCode == 1) {//加载
                parserOrder(msg);
                page = page + 1;
            } else if (requestCode == 2) {//刷新
                parserOrder(msg);
                page = 2;
            }
        } catch (Exception e) {
            Log.e(TAG, "setMsg: " + e.toString());
        }
    }

    //解析订单信息
    private void parserOrder(String msg) throws Exception {
        isRefreshSuccess = true;
        Gson gson = new Gson();
        ParserOrder parserOrder = gson.fromJson(msg, ParserOrder.class);
        ParserOrderDate date = parserOrder.getData();
        SoaOrderParameter soaOrderParameter = date.getSoaOrderParameter();
        List<Rows> rowsList = soaOrderParameter.getRows();
        resultList = exangeData(rowsList);
    }

    //转化学历教育订单结果
    private List<MyOrderParent> exangeData(List<Rows> rowsList) {
        Log.i(TAG, "exangeData: ");
        List<MyOrderParent> listMyOrder = new ArrayList<>();//用来添加封装的订单类结合
        MyOrderParent order;//封装的订单类
//        List<MyOrderChild> childList;//封装的子订单集合
        Rows rows;//原始的订单父
        List<ItemOrderVO> itemOrderVOList;//原始子订单集合
        ItemOrderVO voParent;
        for (int i = 0; i < rowsList.size(); i++) {
            rows = rowsList.get(i);
            voParent = rows.getOrderVO();//父订单
            itemOrderVOList = rows.getItemOrderVOs();//子订单集合
            Byte orderType = rows.getOrderType();
            if (orderType == 0) {// 0学历教育

            } else if (orderType == 1 ) {   //1公开课
                for (int k = 0; k < itemOrderVOList.size(); k++) {
                    order = new MyOrderParent();
                    order.setCollegeName(rows.getCollegeName());
                    order.setOrderUrl(rows.getCourseLogo());
                    order.setCourseName(rows.getCourseName());
                    order.setOrderType(rows.getOrderType());
                    order.setObjectId(rows.getOpenCourseId());
                    //添加审核妈
                    order.setQualificationAuditState(rows.getQualificationAuditState());
                    //审核失败的原因
                    order.setAuditRefuseReason(rows.getAuditRefuseReason());
                    //审核是否通过
                    order.setIsAudit(rows.getIsAudit());

                    //父订单填数据
                    order.setIsOrderComment(voParent.getIsOrderComment());
                    order.setOrderId(voParent.getId());
                    order.setCourseScheduleId(voParent.getCourseScheduleId());//课程表id
                    order.setCourseId(voParent.getCourseId());//课程id
                    order.setCourseType(voParent.getOpenCourseType());//公开课类型(评论页接口用的)
                    order.setOrderCode(voParent.getOrderCode());//父订单号

                    //子订单填数据
                    ItemOrderVO orderVO = itemOrderVOList.get(k);//子订单
                    order.setCreateTime(orderVO.getOrderCreateTime());
                    order.setOrderStatus(orderVO.getOrderStatus());
                    order.setpOrderAmount(orderVO.getOrderAmount());

                    Log.i(TAG, "exangeData公开课: "+order.toString() );
                    listMyOrder.add(order);
                }
            }else if(orderType==2){//微专业订单
                for (int k = 0; k < itemOrderVOList.size(); k++) {
                    order = new MyOrderParent();
                    order.setCollegeName(rows.getCollegeName());
                    order.setOrderUrl(rows.getTrainingLogo());
                    order.setCourseName(rows.getTrainingName());
                    order.setOrderType(rows.getOrderType());
                    order.setIsFree(rows.getIsFree());
                    order.setTutionWay(rows.getTutionWay());
                    order.setEnteredStartTime(rows.getEnteredStartTime());
                    order.setEnteredEndTime(rows.getEnteredEndTime());
                    order.setIsFree(rows.getIsFree());
                    //添加审核妈
                    order.setQualificationAuditState(rows.getQualificationAuditState());
                    //审核失败的原因
                    order.setAuditRefuseReason(rows.getAuditRefuseReason());
                    //审核是否通过
                    order.setIsAudit(rows.getIsAudit());
                    //父订单填数据
                    order.setIsOrderComment(voParent.getIsOrderComment());
                    order.setOrderId(voParent.getId());
                    order.setCourseScheduleId(voParent.getCourseScheduleId());//课程表id
                    order.setCourseId(voParent.getCourseId());//课程id
                    order.setCourseType(voParent.getOpenCourseType());//公开课类型(评论页接口用的)
                    order.setOrderCode(voParent.getOrderCode());//父订单号
                    order.setObjectId(rows.getTrainingId());

                    //子订单填数据
                    ItemOrderVO orderVO = itemOrderVOList.get(k);//子订单
                    order.setCreateTime(orderVO.getOrderCreateTime());
                    order.setOrderStatus(orderVO.getOrderStatus());
                    order.setpOrderAmount(orderVO.getOrderAmount());

                    Log.i(TAG, "exangeData微专业: "+order.toString() );
                    listMyOrder.add(order);
                }
            }
        }
        Log.i(TAG, "exangeData: " + listMyOrder.size());
        return listMyOrder;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
