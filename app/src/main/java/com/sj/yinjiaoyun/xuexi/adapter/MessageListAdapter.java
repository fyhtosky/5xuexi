package com.sj.yinjiaoyun.xuexi.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.activity.ChatActivity;
import com.sj.yinjiaoyun.xuexi.activity.GroupChatActivity;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.bean.GroupinfoBean;
import com.sj.yinjiaoyun.xuexi.bean.RecentChatBean;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroupVO;
import com.sj.yinjiaoyun.xuexi.bean.TigaseGroups;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.DensityUtils;
import com.sj.yinjiaoyun.xuexi.utils.ExpressionUtil;
import com.sj.yinjiaoyun.xuexi.utils.PicassoUtils;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.TimeRender;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;

import java.util.List;

/**
 * 最近聊天回话列表Adapter
 */

public class MessageListAdapter extends BaseAdapter {
	private Context mContext;
	List<RecentChatBean.DataBean.MsgPgBean.RowsBean> list;
	private String userid;
	private String realName;
	private String uesrName;
	private String  name;
	private  ForegroundColorSpan span;


	public MessageListAdapter(Context context, List<RecentChatBean.DataBean.MsgPgBean.RowsBean>list) {
		this.mContext = context;
		this.list=list;
		userid="5f_"+ PreferencesUtils.getSharePreStr(mContext, "username");
		realName=PreferencesUtils.getSharePreStr(mContext,"realName");
		 uesrName=PreferencesUtils.getSharePreStr(mContext,"Name");
		if(!"null".equals(realName)){
			name=realName;
		}else {
			name=uesrName;
		}
		 span=new ForegroundColorSpan(Color.RED);


	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<RecentChatBean.DataBean.MsgPgBean.RowsBean> list){
		this.list = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.fragment_news_item,null);
			holder = new Holder();
			holder.item= (RelativeLayout) convertView.findViewById(R.id.item);
			holder.iv = (ImageView) convertView.findViewById(R.id.user_head);
			holder.ivmessageno= (ImageView) convertView.findViewById(R.id.iv_message_no);
			holder.num= (TextView) convertView.findViewById(R.id.num);
			holder.tv_name = (TextView) convertView.findViewById(R.id.user_name);
			holder.tv_content = (TextView) convertView.findViewById(R.id.content);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final RecentChatBean.DataBean.MsgPgBean.RowsBean rowsBean = list.get(position);
		//设置未读消息数量复用默认状态
		holder.num.setVisibility(View.INVISIBLE);
		//设置免打扰标示的默认状态
		holder.ivmessageno.setVisibility(View.INVISIBLE);
		//设置图片的默认状态
		holder.iv.setImageResource(R.mipmap.kechen);
		//设置消息内容的默认状态
		holder.tv_content.setText("");
		//设置控件的宽高复用
		ViewGroup.LayoutParams p=holder.num.getLayoutParams();
		p.width= ViewGroup.LayoutParams.WRAP_CONTENT;
		p.height=ViewGroup.LayoutParams.WRAP_CONTENT;
		holder.num.setLayoutParams(p);
		//消息名字
		holder.tv_name.setText(rowsBean.getMsgName());
		//显示时间
		holder.tv_time.setText(TimeRender.FormatString(rowsBean.getCreateTime()));
		if(rowsBean.getMsgType()==0){//私聊
			/**
			 * 设置个人头像
			 */
			if(rowsBean.getMsgLogo()!=null){
				if(!TextUtils.isEmpty(rowsBean.getMsgLogo())){
					PicassoUtils.LoadPathCorners(mContext,rowsBean.getMsgLogo(),holder.iv);
				}else{
					PicassoUtils.LoadCorners(mContext,R.mipmap.default_userhead,holder.iv);
				}
			}else{
				PicassoUtils.LoadCorners(mContext,R.mipmap.default_userhead,holder.iv);
			}
			/**
			 * 设置消息的数量
			 */
			//显示消息的数量
			if(0==rowsBean.getMsgCount()){
				holder.num.setVisibility(View.INVISIBLE);
			}else{
				holder.num.setVisibility(View.VISIBLE);
				if(rowsBean.getMsgCount()>99){
					holder.num.setText("99+");
				}else{
					holder.num.setText(""+rowsBean.getMsgCount());
				}
			}
		}else{//群聊
			if(MyApplication.groupsList!=null &&MyApplication.groupsList.size()>0){
				for (TigaseGroups tigaseGroups:MyApplication.groupsList){
					if(rowsBean.getSender().equals(tigaseGroups.getTigaseGroupVO().getGroupId())){
						/**
                         * 加载群的图片
						 */
						if(tigaseGroups.getTigaseGroupVO().getType()==0){//招生专业类型
							PicassoUtils.LoadCorners(mContext,R.mipmap.xueli,holder.iv);
						}else if(tigaseGroups.getTigaseGroupVO().getType()==1){
							PicassoUtils.LoadCorners(mContext,R.mipmap.gongkaike,holder.iv);
						}else if(tigaseGroups.getTigaseGroupVO().getType()==2){
							PicassoUtils.LoadCorners(mContext,R.mipmap.weizhuanye,holder.iv);
						}
                          /**
                           * 开启了免打扰
						   */
						if(1==tigaseGroups.getTigaseGroupVO().getIsNotDisturb()){
							//免打扰标示可见
							holder.ivmessageno.setVisibility(View.VISIBLE);
							//不显示消息的数量
							if(0==rowsBean.getMsgCount()){
								holder.num.setVisibility(View.INVISIBLE);
							}else{
								holder.num.setVisibility(View.VISIBLE);
								holder.num.setText(" ");
								//动态设置控件的宽高
								ViewGroup.LayoutParams lp=holder.num.getLayoutParams();
								lp.width= DensityUtils.dp2px(mContext,12);
								lp.height=DensityUtils.dp2px(mContext,12);
								holder.num.setLayoutParams(lp);
							}
						}else{
							//未开启免打扰
							holder.ivmessageno.setVisibility(View.INVISIBLE);
							//显示消息数据量
							if(0==rowsBean.getMsgCount()){
								holder.num.setVisibility(View.INVISIBLE);
							}else{
								holder.num.setVisibility(View.VISIBLE);
								if(rowsBean.getMsgCount()>99){
									holder.num.setText("99+");
								}else{
									holder.num.setText(""+rowsBean.getMsgCount());
								}
							}
						}
					}else{
						if(tigaseGroups.getChildTigaseGroupVOs()!=null){
							if(tigaseGroups.getChildTigaseGroupVOs().size()>0){
								for (TigaseGroupVO tigaseGroupVO:tigaseGroups.getChildTigaseGroupVOs()){
									/**
                                     * 加载群的图片
									 */
									if(rowsBean.getSender().equals(tigaseGroupVO.getGroupId())) {
										PicassoUtils.LoadCorners(mContext, R.mipmap.kechen, holder.iv);
										/**
                                         * 开启了免打扰
										 */
										if(1==tigaseGroupVO.getIsNotDisturb()){
											//显示免打扰的标示
											holder.ivmessageno.setVisibility(View.VISIBLE);
											//不显示消息的数量
											if(0==rowsBean.getMsgCount()){
												holder.num.setVisibility(View.INVISIBLE);
											}else{
												holder.num.setVisibility(View.VISIBLE);
												holder.num.setText(" ");
												//动态设置控件的宽高
												ViewGroup.LayoutParams lp=holder.num.getLayoutParams();
												lp.width= DensityUtils.dp2px(mContext,12);
												lp.height=DensityUtils.dp2px(mContext,12);
												holder.num.setLayoutParams(lp);

											}
										}else{
											//未开启免打扰
											holder.ivmessageno.setVisibility(View.INVISIBLE);
											//显示消息数据量
											if(0==rowsBean.getMsgCount()){
												holder.num.setVisibility(View.INVISIBLE);
											}else{
												holder.num.setVisibility(View.VISIBLE);
												if(rowsBean.getMsgCount()>99){
													holder.num.setText("99+");
												}else{
													holder.num.setText(""+rowsBean.getMsgCount());
												}
											}
										}
									}
								}
							}

						}
					}
				}
				//如果群的信息本地没有则获取网络数据
			}else{
				getGroupInfo(mContext,rowsBean.getSender(),holder.iv);
				holder.ivmessageno.setVisibility(View.INVISIBLE);
			}
		}


		//显示消息内容
      if(rowsBean.getMsg()!=null){
		  //图片类型
		  if(rowsBean.getMsg().startsWith("*$img_")&& rowsBean.getMsg().endsWith("_gmi$*")){
			  if(rowsBean.getSenderName()!=null && !"".equals(rowsBean.getSenderName())){
				  holder.tv_content.setText(rowsBean.getSenderName()+":[图片]");
			  }else{
				  holder.tv_content.setText("[图片]");
			  }

		  }else {
			  //@功能
			  if(rowsBean.getMsg().contains("_!@_")){
				  if(rowsBean.getSenderName()!=null && !"".equals(rowsBean.getSenderName())){
					  String content=ExpressionUtil.RecursiveQuery(mContext,rowsBean.getSenderName()+":"+rowsBean.getMsg(),0);
					  if(rowsBean.getMsg().contains(userid) || rowsBean.getMsg().contains("_5xuexi_all_")){
						  if(rowsBean.getMsgCount()==0){
							  holder.tv_content.setText(content);
						  }else{
							  SpannableStringBuilder builder=new SpannableStringBuilder("[有人@我]"+content);
							  builder.setSpan(span,0,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
							  holder.tv_content.setText(builder);
							  notifyDataSetChanged();
						  }
					  }else{
						  holder.tv_content.setText(content);
					  }
				  }else{
					  holder.tv_content.setText(ExpressionUtil.prase(mContext,holder.tv_content,ExpressionUtil.RecursiveQuery(mContext,rowsBean.getMsg(),0)));
				  }

			  }else{
				  //表情类型及文本类型
				  if(rowsBean.getSenderName()!=null && !"".equals(rowsBean.getSenderName())){
					  holder.tv_content.setText(ExpressionUtil.prase(mContext,holder.tv_content,rowsBean.getSenderName()+":"+rowsBean.getMsg()));
				  }else{
					  holder.tv_content.setText(ExpressionUtil.prase(mContext,holder.tv_content,rowsBean.getMsg()));
				  }

			  }

		  }
	  }else{
		  holder.tv_content.setText("");
	  }


		//设置监听器
		holder.item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转操作(0=私聊1=群聊)
				if(rowsBean.getMsgType()==0){
					Intent intent=new Intent(mContext, ChatActivity.class);
					intent.putExtra(Const.CHAT_ID,rowsBean.getSender());
					intent.putExtra(Const.CHAT_NAME,rowsBean.getMsgName());
					intent.putExtra(Const.CHAT_LOGO,rowsBean.getMsgLogo());
					mContext.startActivity(intent);
				}else{
					Intent intent=new Intent(mContext, GroupChatActivity.class);
					intent.putExtra(Const.GROUP_ID,rowsBean.getSender());
					intent.putExtra(Const.GROUP_NAME,rowsBean.getMsgName());
					mContext.startActivity(intent);
				}
                 //清除未读的消息
//				rowsBean.setMsgCount(0);
//				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	class Holder {
		RelativeLayout item;
		ImageView iv ,ivmessageno;
		TextView num;
		TextView tv_name;
		TextView tv_content;
		TextView tv_time;
	}



	/**
	 * 获取群信息的类型
	 * @param context
	 * @param groupJid
     * @param iv
     */
  private void getGroupInfo(Context context, String groupJid, final ImageView iv){
	  String params="?groupJid="+groupJid;
	  HttpClient.get(context, Api.GET_GROUP_INFO+params, new CallBack<GroupinfoBean>() {
		  @Override
		  public void onSuccess(GroupinfoBean result) {
			  if(result==null){
				  return;
			  }
			  //islef不是子节点则
			  if(0==result.getData().getGroupChat().getIsLef()){
				  int type=result.getData().getGroupChat().getType();
				  if(type==0){
					  PicassoUtils.LoadCorners(mContext, R.mipmap.xueli, iv);
				  }else if(type==1){
					  PicassoUtils.LoadCorners(mContext, R.mipmap.gongkaike, iv);
				  }else if(type==2){
					  PicassoUtils.LoadCorners(mContext, R.mipmap.weizhuanye, iv);
				  }
			  }else{//子节点
				  PicassoUtils.LoadCorners(mContext, R.mipmap.kechen, iv);
			  }


		  }
	  });
  }

}
