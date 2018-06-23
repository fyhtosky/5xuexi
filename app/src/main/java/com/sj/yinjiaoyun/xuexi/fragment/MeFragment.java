package com.sj.yinjiaoyun.xuexi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.LoginActivity;
import com.sj.yinjiaoyun.xuexi.activity.MyBindActivity;
import com.sj.yinjiaoyun.xuexi.activity.MyMeActivity;
import com.sj.yinjiaoyun.xuexi.activity.MyMessageActivity;
import com.sj.yinjiaoyun.xuexi.activity.MyOpinionActivity;
import com.sj.yinjiaoyun.xuexi.activity.MyOrderActivity;
import com.sj.yinjiaoyun.xuexi.activity.MySafeActivity;
import com.sj.yinjiaoyun.xuexi.activity.MySchoolRollActivity;
import com.sj.yinjiaoyun.xuexi.activity.MySetActivity;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.view.MeView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/1.
 * 主Activity - 我的Fragment
 *
 */
public class MeFragment extends Fragment implements View.OnClickListener{

    String TAG="mememe";

    private View mContainer;
    private ImageView userHead;
    private TextView userName;
    private MeView mAccount;
    private MeView mBind;
    private MeView mOrder;
    private RelativeLayout mMessage;
    private TextView tvMessageHint;//消息条数提示信息
    private MeView mOpinion;
    private MeView mSet;
    private MeView mSchool;

    private String endUserId;//登录用户id
    private int noticeCount;//用户未读消息数
    private Activity activity;
    private String senderImg="";
    //标示用户是否登录
    private boolean loginState;
    private   Transformation transformation = new RoundedTransformationBuilder()
           .borderColor(R.color.colorWrite)
           .borderWidthDp(0.5f)
           .cornerRadiusDp(2)
           .oval(false)
           .build();

    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        Log.i("showAndHidden", "onCreate: "+"1 mefragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_me,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initEvent();
    }

    private void setDateRequest() {
        String params = "?id=" + endUserId;
        HttpClient.get(this, Api.GET_USER_INFO + params, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                if(result.isSuccess()){
                    if(result.getData().getUser().getUserImg()!=null){
                        //展示图片
                        senderImg=result.getData().getUser().getUserImg();
                        Logger.d("图片的url:"+senderImg);
                    }
                    if (!TextUtils.isEmpty(senderImg)) {
                        Picasso.with(getActivity())
                                .load(senderImg.trim())
                                .transform(transformation)
                                .error(R.mipmap.default_userhead)
                                .into(userHead);
                    } else {
                        Picasso.with(getActivity())
                                .load(R.mipmap.default_userhead)
                                .into(userHead);
                    }
                    //显示用户名
                    if(result.getData().getUser().getRealName()!=null){
                        userName.setText(result.getData().getUser().getRealName());
                    } else if(result.getData().getUser().getNickName()!=null){
                        userName.setText(result.getData().getUser().getNickName());
                    } else if(result.getData().getUser().getUserName()!=null){
                        userName.setText(result.getData().getUser().getUserName());
                    }else {
                        userName.setText("");
                    }
                }
            }


        });
    }

    private void initView(View view) {
        endUserId=  PreferencesUtils.getSharePreStr(MyApplication.getContext(), "username");
        loginState=PreferencesUtils.getSharePreBoolean(MyApplication.getContext(), Const.LOGIN_STATE);
        mContainer=view.findViewById(R.id.fragment_me_container);
        userHead=  view.findViewById(R.id.fragment_me_icon);
        userName= view.findViewById(R.id.fragment_me_text);
        mAccount=  view.findViewById(R.id.fragment_me_account);
        mBind=view.findViewById(R.id.fragment_me_bind);
        mOrder=  view.findViewById(R.id.fragment_me_order);
        mMessage= view.findViewById(R.id.fragment_me_message);
        tvMessageHint=  view.findViewById(R.id.fragment_me_message_hint);
        mOpinion=  view.findViewById(R.id.fragment_me_opinion);
        mSet=  view.findViewById(R.id.fragment_me_set);
        mSchool= view.findViewById(R.id.fragment_me_status);
        if(noticeCount==0){
            tvMessageHint.setVisibility(View.GONE);
        }else{
            tvMessageHint.setVisibility(View.VISIBLE);
            tvMessageHint.setText(noticeCount+"");
        }
        mContainer.setOnClickListener(this);
        mOpinion.setOnClickListener(this);
        mAccount.setOnClickListener(this);
        mBind.setOnClickListener(this);
        mOrder.setOnClickListener(this);
        mMessage.setOnClickListener(this);
        mSet.setOnClickListener(this);
        mSchool.setOnClickListener(this);

    }

    /**
     * @param noticeCount   用户未读消息条数
     */
    public void setDateForActivity(int noticeCount){
        this.noticeCount=noticeCount;
            Log.i(TAG, "meFragment setDateForActivity: "+noticeCount);
            if(tvMessageHint!=null){
                if(noticeCount==0){
                    tvMessageHint.setVisibility(View.GONE);
                }else {
                    tvMessageHint.setVisibility(View.VISIBLE);
                    tvMessageHint.setText(noticeCount + "");
                }
                Log.i(TAG, "致值: ");
            }

    }

    private void initEvent(){
        senderImg= PreferencesUtils.getSharePreStr(getActivity(),"userImg");
        //未登录
        if(!loginState){
            Picasso.with(getActivity())
                    .load(R.mipmap.tu)
                    .into(userHead);

            userName.setText("点击登录");
            return;
        }else {

            setDateRequest();
        }


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fragment_me_container://头像用户信息
                if(!loginState){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MyMeActivity.class));
                break;
            case R.id.fragment_me_account://账户安全
                if(!loginState){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MySafeActivity.class));
                break;
            case R.id.fragment_me_bind://账户绑定
                if(!loginState){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MyBindActivity.class));
                break;
            case R.id.fragment_me_status://学籍信息
                if(!loginState){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MySchoolRollActivity.class));
                break;
            case R.id.fragment_me_order://我的订单
                if(!loginState){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                activity.startActivityForResult(new Intent(getActivity(), MyOrderActivity.class),0);
                break;
            case R.id.fragment_me_message://我的消息
                if(!loginState){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;
            case R.id.fragment_me_opinion://意见反馈
                startActivity(new Intent(getActivity(), MyOpinionActivity.class));
                break;
            case R.id.fragment_me_set://设置
                startActivity(new Intent(getActivity(), MySetActivity.class));
                break;
        }
    }

}
