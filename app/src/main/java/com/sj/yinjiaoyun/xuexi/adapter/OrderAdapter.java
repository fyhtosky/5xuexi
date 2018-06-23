package com.sj.yinjiaoyun.xuexi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.MainActivity;
import com.sj.yinjiaoyun.xuexi.activity.MicroHintActivity;
import com.sj.yinjiaoyun.xuexi.activity.OpenCouseHintActivity;
import com.sj.yinjiaoyun.xuexi.activity.OpenCouseItemActivity;
import com.sj.yinjiaoyun.xuexi.activity.OrderCommentActivity;
import com.sj.yinjiaoyun.xuexi.activity.PayActivity;
import com.sj.yinjiaoyun.xuexi.domain.MyOrderChild;
import com.sj.yinjiaoyun.xuexi.domain.MyOrderParent;
import com.sj.yinjiaoyun.xuexi.domains.CouseFuse;
import com.sj.yinjiaoyun.xuexi.domains.MicroFuse;
import com.sj.yinjiaoyun.xuexi.pay.SignOrderInfo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.utils.MyUtil;
import com.sj.yinjiaoyun.xuexi.view.DialogOrderHorizontally;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/14.
 * 订单
 */
public class OrderAdapter extends BaseAdapter {

    String TAG = "myorderadapter";
    List<MyOrderParent> list;
    Activity context;
    LayoutInflater inflater;
    Display d;//计算屏幕参数
    String endUserId;
    SignOrderInfo signOrderInfo;
    String orderCode;

    public OrderAdapter(Activity context, List<MyOrderParent> list, Display d, String endUserId) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.d = d;
        this.endUserId = endUserId;
        //dialogAdapter = new DialogOrderAdapter(context, childList);
    }

    public void refresh(List<MyOrderParent> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    MyOrderParent pOrder;
    List<MyOrderChild> childList;

    int flag;//0去支付  1去学习

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        pOrder = list.get(position);
        Byte orderType = pOrder.getOrderType();
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_order, parent,false);
            holder.mCreateTime = (TextView) convertView.findViewById(R.id.item_order_createTime);
            holder.mOrderCode = (TextView) convertView.findViewById(R.id.item_order_orderCode);
            holder.mOrderStatus = (TextView) convertView.findViewById(R.id.item_order_status);
            //公开课
            holder.conatinerOne=convertView.findViewById(R.id.item_order_openConatiner);
            holder.mImage1 = (ImageView) convertView.findViewById(R.id.item_order_open_image);
            holder.mLaber1 = (ImageView) convertView.findViewById(R.id.item_order_open_label);
            holder.mProductName1 = (TextView) convertView.findViewById(R.id.item_order_open_courseName);
            holder.mCollege1 = (TextView) convertView.findViewById(R.id.item_order_open_college);
            holder.mPrice1 = (TextView) convertView.findViewById(R.id.item_order_open_price);
            holder.mPayAcount1 = (TextView) convertView.findViewById(R.id.item_order_open_account);
            holder.mComment1 = (Button) convertView.findViewById(R.id.item_order_open_comment);
            holder.mGoPay1 = (Button) convertView.findViewById(R.id.item_order_open_gopay);
            holder.mGoLearn1 = (Button) convertView.findViewById(R.id.item_order_open_golearn);

            //微专业
            holder.conatinerTwo=convertView.findViewById(R.id.item_order_microConatiner);
            holder.mImage2 = (ImageView) convertView.findViewById(R.id.item_order_micro_image);
            holder.mLaber2 = (ImageView) convertView.findViewById(R.id.item_order_micro_label);
            holder.mProductName2 = (TextView) convertView.findViewById(R.id.item_order_micro_courseName);
            holder.mCollege2 = (TextView) convertView.findViewById(R.id.item_order_micro_college);
            holder.mAttente2 = (TextView) convertView.findViewById(R.id.item_order_micro_attention);
            holder.mPrice2 = (TextView) convertView.findViewById(R.id.item_order_micro_price);
            holder.mPayAcount2 = (TextView) convertView.findViewById(R.id.item_order_micro_account);
            holder.mComment2 = (Button) convertView.findViewById(R.id.item_order_micro_comment);
            holder.mGoPay2 = (Button) convertView.findViewById(R.id.item_order_micro_gopay);
            holder.mGoLearn2 = (Button) convertView.findViewById(R.id.item_order_micro_golearn);
            holder.mRegisterTime2 = (TextView) convertView.findViewById(R.id.item_order_micro_registerTime);
            holder.mTutionWay2 = (TextView) convertView.findViewById(R.id.item_order_micro_tutionWay);
            convertView.setTag(holder);
        }
        holder= (ViewHolder) convertView.getTag();
        try {
            //订单状态
            Byte orderStadus = pOrder.getOrderStatus();
            String text = MyUtil.saveTwoScale(pOrder.getpOrderAmount());
            holder.mOrderCode.setText("订单号：" + pOrder.getOrderCode());
            holder.mCreateTime.setText(MyUtil.getTime(pOrder.getCreateTime(), "yyyy-MM-dd HH:mm"));
            switch (orderType) {
                case 1://公开课
                    if (TextUtils.isEmpty(pOrder.getOrderUrl())) {
                        Picasso.with(context)
                                .load(R.mipmap.error)
                                .placeholder(R.drawable.progressbar_landing)
                                //加载失败中的图片显示
                                .error(R.mipmap.error).into(holder.mImage1);
                    } else {
                        Picasso.with(context)
                                .load(pOrder.getOrderUrl())
                                //加载过程中的图片显示
                                .placeholder(R.drawable.progressbar_landing)
                                //加载失败中的图片显示
                                .error(R.mipmap.error)
                                //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                                .centerCrop()
                                .resize(520, 330).into(holder.mImage1);
                    }
                    holder.conatinerOne.setVisibility(View.VISIBLE);
                    holder.conatinerTwo.setVisibility(View.GONE);
                    holder.mCollege1.setText(pOrder.getCollegeName());
                    holder.mProductName1.setText(pOrder.getCourseName());
                    holder.mLaber1.setImageResource(R.mipmap.tip_publiclass);
                    if (orderStadus == 100) {//待付款
                        holder.mOrderStatus.setText("待付款");
                        holder.mComment1.setVisibility(View.GONE);
                        holder.mGoPay1.setVisibility(View.VISIBLE);
                        holder.mGoLearn1.setVisibility(View.GONE);
                    } else if (orderStadus == 101) {//101：审核中
                        holder.mOrderStatus.setText("已付款");
                        holder.mComment1.setVisibility(View.GONE);
                        holder.mGoPay1.setVisibility(View.GONE);
                        holder.mGoLearn1.setVisibility(View.GONE);
                    }else if (orderStadus == 102) {//101：审核中
                        if(pOrder.getIsAudit()==0){
                            holder.mOrderStatus.setText("系统审核");
                        }else {
                            if("100".equals(pOrder.getQualificationAuditState())){
                                holder.mOrderStatus.setText("待审核");
                            }else if("101".equals(pOrder.getQualificationAuditState())) {
                                holder.mOrderStatus.setText("审核通过");
                            }else if("102".equals(pOrder.getQualificationAuditState())){
                                holder.mOrderStatus.setText("审核不通过");
                            }
                        }
                        holder.mComment1.setVisibility(View.GONE);
                        holder.mGoPay1.setVisibility(View.GONE);
                        holder.mGoLearn1.setVisibility(View.GONE);
                    } else if (orderStadus == 109) {//109:订单超时取消
                        holder.mOrderStatus.setText("订单超时");
                        holder.mComment1.setVisibility(View.GONE);
                        holder.mGoPay1.setVisibility(View.GONE);
                        holder.mGoLearn1.setVisibility(View.GONE);
                    } else if (orderStadus == 110) {//110：已完成；
                        holder.mOrderStatus.setText("已完成");
                        holder.mGoPay1.setVisibility(View.GONE);
                        holder.mGoLearn1.setVisibility(View.VISIBLE);
                        if (pOrder.getIsOrderComment() == 0) {//未评论
                            holder.mComment1.setVisibility(View.VISIBLE);
                        } else {//已评论
                            holder.mComment1.setVisibility(View.GONE);
                        }
                    }
                    if (TextUtils.equals(text, "免费")) {
                        holder.mPrice1.setText("免费");
                        holder.mPrice1.setTextColor(context.getResources().getColor(R.color.colorGreen));
                        holder.mPayAcount1.setVisibility(View.GONE);
                    } else {
                        holder.mPrice1.setText("¥" + text);
                        holder.mPayAcount1.setVisibility(View.VISIBLE);
                        holder.mPrice1.setTextColor(context.getResources().getColor(R.color.colorRed));
                        holder.mPayAcount1.setText(MyUtil.setTextFormatColor(context, "实付金额：" + text, text, R.color.colorRed));
                    }
                    holder.mComment1.setOnClickListener(listener);
                    holder.mGoPay1.setOnClickListener(listener);
                    holder.mImage1.setOnClickListener(listener);
                    holder.mGoLearn1.setOnClickListener(listener);

                    holder.mComment1.setTag(position);
                    holder.mGoPay1.setTag(position);
                    holder.mGoLearn1.setTag(position);
                    holder.mImage1.setTag(position);
                    break;
                case 2:
                    holder.conatinerOne.setVisibility(View.GONE);
                    holder.conatinerTwo.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(pOrder.getOrderUrl())) {
                        Picasso.with(context)
                                .load(R.mipmap.error)
                                .placeholder(R.drawable.progressbar_landing)
                                //加载失败中的图片显示
                                .error(R.mipmap.error).into(holder.mImage2);
                    } else {
                        Picasso.with(context)
                                .load(pOrder.getOrderUrl())
                                //加载过程中的图片显示
                                .placeholder(R.drawable.progressbar_landing)
                                //加载失败中的图片显示
                                .error(R.mipmap.error)
                                //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
                                .centerCrop()
                                .resize(520, 330).into(holder.mImage2);
                    }

                    if (pOrder.getIsFree() == 1) {
                        holder.mPrice2.setText("免费");
                        holder.mPrice2.setTextColor(context.getResources().getColor(R.color.colorGreen));
                        holder.mPayAcount2.setVisibility(View.GONE);
                    } else {
                        holder.mPrice2.setText("¥" + text);
                        holder.mPayAcount2.setVisibility(View.VISIBLE);
                        holder.mPrice2.setTextColor(context.getResources().getColor(R.color.colorRed));
                        holder.mPayAcount2.setText(MyUtil.setTextFormatColor(context, "实付金额：" + text, text, R.color.colorRed));
                    }
                    holder.mCollege2.setText(pOrder.getCollegeName());
                    holder.mProductName2.setText(pOrder.getCourseName());
                    holder.mLaber2.setImageResource(R.mipmap.tip_mirclass);
                    holder.mAttente2.setVisibility(View.GONE);
                    holder.mTutionWay2.setVisibility(View.VISIBLE);
                    Byte tutionWay = pOrder.getTutionWay();
                    holder.mTutionWay2.setText(MyConfig.tutionWay().get(tutionWay + "").toString());
                    if (tutionWay == 0) {//随到随学
                        holder.mRegisterTime2.setVisibility(View.GONE);
                    } else if (tutionWay == 1) {//定期开课
                        holder.mRegisterTime2.setVisibility(View.VISIBLE);
                        holder.mRegisterTime2.setText("报名时间：" + MyUtil.getTime(pOrder.getEnteredStartTime(), "yyyy-MM-dd") + " ～ " + MyUtil.getTime(pOrder.getEnteredEndTime(), "yyyy-MM-dd"));
                    }
                    if (orderStadus == 100) {//待付款
                        holder.mOrderStatus.setText("待付款");
                        holder.mComment2.setVisibility(View.GONE);
                        holder.mGoPay2.setVisibility(View.VISIBLE);
                        holder.mGoLearn2.setVisibility(View.GONE);
                    } else if (orderStadus == 101) {//101：审核中
                        holder.mOrderStatus.setText("已付款");
                        holder.mComment2.setVisibility(View.GONE);
                        holder.mGoPay2.setVisibility(View.GONE);
                        holder.mGoLearn2.setVisibility(View.GONE);
                    } else if (orderStadus == 109) {//109:订单超时取消
                        holder.mOrderStatus.setText("订单超时");
                        holder.mComment2.setVisibility(View.GONE);
                        holder.mGoPay2.setVisibility(View.GONE);
                        holder.mGoLearn2.setVisibility(View.GONE);
                    } else if (orderStadus == 110) {//110：已完成；
                        holder.mOrderStatus.setText("已完成");
                        holder.mGoPay2.setVisibility(View.GONE);
                        holder.mGoLearn2.setVisibility(View.VISIBLE);
                        if (pOrder.getIsOrderComment() == 0) {//未评论
                            holder.mComment2.setVisibility(View.VISIBLE);
                        } else {//已评论
                            holder.mComment2.setVisibility(View.GONE);
                        }
                    }
                    holder.mGoPay2.setOnClickListener(microlistener);
                    holder.mComment2.setOnClickListener(microlistener);
                    holder.mGoLearn2.setOnClickListener(microlistener);
                    holder.mImage2.setOnClickListener(microlistener);
                    holder.mComment2.setTag(position);
                    holder.mGoPay2.setTag(position);
                    holder.mGoLearn2.setTag(position);
                    holder.mImage2.setTag(position);
                    break;
            }
        }catch (Exception e ){
           e.getLocalizedMessage();
        }
        return convertView;
    }



    // 公开课 整合数据为search,传递至课程详情
    private CouseFuse exangeSearch(MyOrderParent parent) {
        Log.i(TAG, "exangeSearch: " + parent.toString());
        CouseFuse search = new CouseFuse();
        try {
            search.setCollegeName(parent.getCollegeName());
            search.setCourseLogo(parent.getOrderUrl());
            Double d = parent.getpOrderAmount();
            search.setPrice(d + "");
            search.setCourseName(parent.getCourseName());
            Byte a = 0;
            if (d>0) {
                a = 0;//收费
            } else {
                a = 1;//免费
            }
            search.setIsFree(a);
            search.setNumber(0);
            search.setCourseId(parent.getCourseId());
            search.setOpencourseId(parent.getObjectId());
            search.setCourseType(parent.getCourseType());
        } catch (Exception e) {
            Log.i(TAG, "exangeSearch: " + e.toString());
        }
        return search;
    }
    String orderId;//订单id
    String objectId;//评论对象id
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            MyOrderParent parent = list.get(position);
            orderId = String.valueOf(parent.getOrderId());
            objectId = String.valueOf(parent.getObjectId());
            Log.i(TAG, "onClick: 公开课id" +objectId+" 位置:"+position +" 订单id"+orderId +" "+parent.toString());
            switch (v.getId()) {
                case R.id.item_order_open_gopay://公开课去支付
                    signOrderInfo = new SignOrderInfo(parent.getOrderCode(), endUserId, parent.getpOrderAmount() + "");
                    Log.e(TAG, "onClick: " + signOrderInfo.toString());
                    Intent intent1 = new Intent(context, PayActivity.class);
                    intent1.putExtra("SignOrderInfo", signOrderInfo);
                    context.startActivity(intent1);
                    break;
                case R.id.item_order_open_image://公开课点击图片点击事件，订单已完成时候去详情，去完成去预览页面
                    Byte orderStatus = parent.getOrderStatus();
                    if (orderStatus == 110) {//110：表示列表里面的item项订单已完成；
                        Intent intent2 = new Intent(context, OpenCouseItemActivity.class);
                        intent2.putExtra("EndUserId", endUserId);
                        intent2.putExtra("CouseFuse", exangeSearch(parent));
                        intent2.putExtra("CourseScheduleId", String.valueOf(parent.getCourseScheduleId()));
                        context.startActivity(intent2);
                    } else {//订单未完成
                        Intent intent3 = new Intent(context, OpenCouseHintActivity.class);
                        intent3.putExtra("EndUserId", endUserId);
                        intent3.putExtra("CouseFuse", exangeSearch(parent));
                        intent3.putExtra("orderStatus", (int) parent.getOrderStatus());//表示预览页面的订单状态
                        intent3.putExtra("orderCode", parent.getOrderCode());
                        context.startActivity(intent3);
                    }
                    break;
                case R.id.item_order_open_golearn://公开课去学习
                    Intent intent4 = new Intent(context, OpenCouseItemActivity.class);
                    intent4.putExtra("EndUserId", endUserId);
                    intent4.putExtra("CouseFuse", exangeSearch(parent));
                    intent4.putExtra("CourseScheduleId", String.valueOf(parent.getCourseScheduleId()));
                    context.startActivity(intent4);
                    break;
                case R.id.item_order_open_comment://公开课去评价
                    Intent intent5 = new Intent(context, OrderCommentActivity.class);
                    intent5.putExtra("endUserId", endUserId);
                    intent5.putExtra("orderId", orderId);//订单id
                    intent5.putExtra("objectId", objectId);//评论对象id
                    intent5.putExtra("commentType",1);////评论类型 0：专业 ，1： 公开课 2：微专业
                    context.startActivityForResult(intent5, 0);
                    break;
            }
        }
    };


    //微专业 整合数据 用于传递
    private MicroFuse exangeMicro(MyOrderParent parent){
        MicroFuse microFuse=new MicroFuse();
        microFuse.setCourseLogo(parent.getOrderUrl());
        microFuse.setMicroId(parent.getObjectId());
        microFuse.setCollegeName(parent.getCollegeName());
        microFuse.setCourseName(parent.getCourseName());
        microFuse.setNumber(parent.getEnterNumber());
        microFuse.setIsFree(parent.getIsFree());
        microFuse.setTutionWay(parent.getTutionWay());
        microFuse.setPrice(null);
        microFuse.setCollegeId(null);
        return microFuse;
    }
    View.OnClickListener microlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            MyOrderParent parent = list.get(position);
            orderId = String.valueOf(parent.getOrderId());
            objectId = String.valueOf(parent.getObjectId());
            orderCode=parent.getOrderCode();
            Log.i(TAG, "onClick: 微专业id" +objectId+":" +orderId+parent.toString());
            switch (v.getId()) {
                case R.id.item_order_micro_gopay://微专业去支付
                    signOrderInfo = new SignOrderInfo(parent.getOrderCode(), endUserId, parent.getpOrderAmount() + "");
                    Log.e(TAG, "onClick: " + signOrderInfo.toString());
                    Intent intent1 = new Intent(context, PayActivity.class);
                    intent1.putExtra("SignOrderInfo", signOrderInfo);
                    context.startActivity(intent1);
                    break;
                case R.id.item_order_micro_image://微专业点击图片点击事件，去详情
                    Log.i("microaaaa", "onClick: "+"微专业点击");
                    Byte orderStatus = parent.getOrderStatus();
                    if (orderStatus == 110) {//110：表示列表里面的item项订单已完成；  去课程表微专业
                        Intent intent2 = new Intent(context, MainActivity.class);
                        intent2.putExtra("indexType", "2");//课程表下的  微专业
                        context.setResult(1, intent2);
                        context.finish();
                    } else {//订单未完成  去详情
                        Intent intent2 = new Intent(context, MicroHintActivity.class);
                        intent2.putExtra("EndUserId", endUserId);//预览
                        intent2.putExtra("MicroFuse", exangeMicro(parent));
                        intent2.putExtra("orderStatus", 109);
                        intent2.putExtra("orderCode",orderCode);
                        context.startActivity(intent2);
                    }
                    break;
                case R.id.item_order_micro_golearn:// 微专业 去学习 跳转到 课程表下的微专业
                    Intent intent4 = new Intent(context, MainActivity.class);
                    intent4.putExtra("indexType", "2");//课程表下的 微专业
                    context.setResult(1, intent4);
                    context.finish();
                    break;
                case R.id.item_order_micro_comment://微专业去评价
                    Intent intent5 = new Intent(context, OrderCommentActivity.class);
                    intent5.putExtra("endUserId", endUserId);
                    intent5.putExtra("orderId", orderId);//订单id
                    intent5.putExtra("objectId", objectId);//评论对象id
                    intent5.putExtra("commentType",2);////评论类型 0：专业 ，1： 公开课 2：微专业
                    context.startActivityForResult(intent5, 0);
                    break;
            }
        }
    };



    class ViewHolder {
        TextView mCreateTime;
        TextView mOrderCode;
        TextView mOrderStatus;
        //公开课
        View conatinerOne;
        ImageView mImage1;
        ImageView mLaber1;
        TextView mProductName1;
        TextView mCollege1;
        TextView mPrice1;
        TextView mPayAcount1;
        Button mComment1;
        Button mGoPay1;
        Button mGoLearn1;

        //微专业
        View conatinerTwo;
        ImageView mImage2;
        ImageView mLaber2;
        TextView mProductName2;
        TextView mCollege2;
        TextView mAttente2;
        TextView mPrice2;
        TextView mPayAcount2;
        Button mComment2;
        Button mGoPay2;
        Button mGoLearn2;
        TextView mRegisterTime2;//微专业 开课时间
        TextView mTutionWay2;//微专业 开课方式
    }












    DialogOrderAdapter dialogAdapter;
    DialogOrderHorizontally dialog;
    static int position;

    //设置Dialog
    private void setDialogs(List<MyOrderChild> list) {
        dialog = new DialogOrderHorizontally(context, list) {
            @Override
            public void onClick(View v) {
                if (dialog != null)
                    dialog.dismiss();
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("result", "onItemClick: " + position);
                if (dialog != null)
                    dialog.dismiss();
            }
        };
        dialog.show();
    }


}
