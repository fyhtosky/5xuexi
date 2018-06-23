package com.sj.yinjiaoyun.xuexi.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.NewGridAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.NewListAdapter;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.domain.ProductDirection;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.StringFormatUtil;
import com.sj.yinjiaoyun.xuexi.widget.NewGridView;
import com.sj.yinjiaoyun.xuexi.widget.NewListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/10.
 *  课程表  方向选择导航
 */
public class FangxFragment extends Fragment implements HttpDemo.HttpCallBack{

    String TAG="fangx";
    String enrollPlanId;//招生计划id
    String productId;//专业id
    String endUserId;//用户id
    CallBackFromFangx callBack;
    List<ProductDirection> directionList;//方向集合集合
    ProductDirection productDirection;//单选选中的方向

    List<Pairs> pairsList;
    HttpDemo demo;
    View view;
    View viewContainer;

    Button btnSure;//确认选择
    NewGridView gridView;
    NewListView listView;
    NewGridAdapter gridAdapter;
    NewListAdapter listAdapter;
    TextView textViewFangx;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_fangx,container,false);
        initView(view);
        initEvent();
        return view;
    }

    private void initEvent() {
        if(directionList==null){
            viewContainer.setVisibility(View.GONE);
            return;
        }
        viewContainer.setVisibility(View.VISIBLE);
        StringFormatUtil spanStr = new StringFormatUtil(getActivity(), "请选择专业方向（方向一旦选定不得更改）",
                "（方向一旦选定不得更改）", R.color.colorRed).fillColor();
        textViewFangx.setText(spanStr.getResult());

        demo=new HttpDemo(this);
        listAdapter =new NewListAdapter(getActivity(),directionList);
        listView.setAdapter(listAdapter);
        listView.setDividerHeight(1);//设置listView分割线1

        gridAdapter=new NewGridAdapter(directionList,getActivity());
        gridView.setAdapter(gridAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productDirection = directionList.get(position);
            }
        });

        callBack.smoothScrollToTop();//设置scrollView 专业头部和列表从上至下置顶
        btnSure.setText("移动端此功能尚未开放，请于网页端操作");
        btnSure.setBackgroundResource(R.drawable.btn_sure_gray);
//        btnSure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // callBack.replaceFragment(1);
//                sendQueRenToHttp();
//            }
//        });
    }


    //选定 方向确认 按钮的操作
    public void sendQueRenToHttp(){
        int position = gridView .getCheckedItemPosition();     // 即获取选中位置
        if(position<0){
            return;
        }
        if(productDirection==null){
            Toast.makeText(getActivity(),"请选择方向",Toast.LENGTH_LONG).show();
            return;
        }
        pairsList=new ArrayList<>();
        String url= MyConfig.getURl("product/addEndUserDirection");
        pairsList.add(new Pairs("endUserId",endUserId));//学生ID
        pairsList.add(new Pairs("productId",productId));//专业id
        pairsList.add(new Pairs("enrollPlanId",enrollPlanId));//招生计划id
        pairsList.add(new Pairs("productDirectionId",String.valueOf(productDirection.getProductDirectionId())));//方向id
        demo.doHttpPostLoading(getActivity(),url,pairsList,0);
    }


    public void initView(View v){
        textViewFangx= (TextView) v.findViewById(R.id.fangx_fangxiang);
        viewContainer=v.findViewById(R.id.containerFangx);
        gridView= (NewGridView) v.findViewById(R.id.major_fangx_gridview);
        listView= (NewListView) v.findViewById(R.id.major_fangx_listView);
        btnSure= (Button) v.findViewById(R.id.major_fangx_queren);
    }


    /**
     *  从专业传过来的方向数据
     * @param directionList  方向集合
     * @param callBack       回调的activity
     * @param enrollPlanId   招生计划id
     * @param endUserId      用户id
     * @param productId      专业id
     */
    public void setFangxDateFromMajor(List<ProductDirection> directionList, CallBackFromFangx callBack, String enrollPlanId, String endUserId, String productId){
        this.callBack=callBack;
        this.productId=productId;
        this.endUserId=endUserId;
        this.enrollPlanId=enrollPlanId;
        // Log.i(TAG, "setDirectionDate: ");
        this.directionList=directionList;
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(msg);
            boolean isSuccess=jsonObject.getBoolean("success");
            if(isSuccess){
                Log.i(TAG, "setMsg: "+"方向选择成功");
                callBack.replaceFragment(1);
            }else{
                String message=jsonObject.getString("message");
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface CallBackFromFangx{
        void replaceFragment(int flag);
        void smoothScrollToTop();
    }

}
