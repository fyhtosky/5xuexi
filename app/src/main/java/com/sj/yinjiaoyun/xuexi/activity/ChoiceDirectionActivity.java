package com.sj.yinjiaoyun.xuexi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.DirectionGirdAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.DirectionListAdapter;
import com.sj.yinjiaoyun.xuexi.bean.DirectionBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.widget.NewListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择方向的 界面
 */
public class ChoiceDirectionActivity extends AppCompatActivity {

    @BindView(R.id.major_fangx_gridview)
    NewListView majorFangxGridview;
    @BindView(R.id.major_fangx_listView)
    NewListView majorFangxListView;
    //标识的用户id
    private String endUserId="";
    //招生计划的id
    private String enrollPlanId="";
    //专业id
    private String productId="";
    //方向的集合
    private List<DirectionBean.DataBean.EnrollPlanDirectionsBean>list=new ArrayList<>();
    //网格布局的适配器
    private DirectionGirdAdapter girdAdapter;
    //线性布局
    private DirectionListAdapter listAdapter;
    private DirectionBean.DataBean.EnrollPlanDirectionsBean enrollPlanDirectionsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_direction);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        endUserId = PreferencesUtils.getSharePreStr(this,"username");
        //显示方向
        girdAdapter=new DirectionGirdAdapter(ChoiceDirectionActivity.this,list);
        majorFangxGridview.setAdapter(girdAdapter);
        majorFangxGridview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        majorFangxGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enrollPlanDirectionsBean=list.get(position);
                girdAdapter.changeState(position);
            }
        });
        //显示简介
        listAdapter=new DirectionListAdapter(ChoiceDirectionActivity.this,list);
        majorFangxListView.setAdapter(listAdapter);
        majorFangxListView.setDividerHeight(2);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            enrollPlanId=bundle.getString("enrollPlanId");
            productId=bundle.getString("productId");
            requseDirection();
        }

    }

    private void requseDirection() {
        String params="?enrollPlanId="+enrollPlanId;
        HttpClient.get(this, Api.FIND_DIRECTION_ENROLLPLAY + params, new CallBack<DirectionBean>() {
            @Override
            public void onSuccess(DirectionBean result) {
                if(result==null){
                    return;
                }
                if(result.isSuccess()){
                    list.clear();
                    list.addAll(result.getData().getEnrollPlanDirections());
                    girdAdapter.notifyDataSetChanged();
                    listAdapter.notifyDataSetChanged();
                }
            }

        });
    }

    /**
     * 启动Activity
     */
    public static void StartActivity(Context context,String enrollPlanId,String productId){
        Intent intent=new Intent(context,ChoiceDirectionActivity.class);
        intent.putExtra("enrollPlanId",enrollPlanId);
        intent.putExtra("productId",productId);
        context.startActivity(intent);

}
    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                saveDirection();
                break;
        }
    }

    private void saveDirection() {
        if (enrollPlanDirectionsBean == null) {
            ToastUtil.showShortToast(ChoiceDirectionActivity.this, "请选择专业方向");
            return;
        }
        if (!TextUtils.isEmpty(endUserId) && !TextUtils.isEmpty(productId) && !TextUtils.isEmpty(enrollPlanId)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("endUserId", endUserId);
            map.put("productId", productId);
            map.put("enrollPlanId", enrollPlanId);
            map.put("productDirectionId", String.valueOf(enrollPlanDirectionsBean.getProductDirectionId()));
            HttpClient.post(this, Api.ADD_USER_DIRECTION, map, new CallBack<ReturnBean>() {
                @Override
                public void onSuccess(ReturnBean result) {
                    if (result == null) {
                        return;
                    }
                    if (result.isSuccess()) {
                        finish();
                    }
                }
            });
        }
    }
}
