package com.sj.yinjiaoyun.xuexi.utils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.FaceGVAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析消息内容的工具类
 */
public class ExpressionUtil {
	
	public static SpannableStringBuilder prase(Context mContext,final TextView gifTextView,String content) {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		InputStream is = null;
		String regex = "\\*#emo_(\\d{3})#\\*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
			try {
				String num ="g/"+ tempText.replace("*#","").replace("#*","")+".gif";
				/**
				 * 如果open这里不抛异常说明存在gif，则显示对应的gif
				 * 否则说明gif找不到，则显示png
				 * */
				is = mContext.getAssets().open(num);
				//显示动态图
//				sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(is,new AnimatedGifDrawable.UpdateListener() {
//							@Override
//							public void update() {
//								gifTextView.postInvalidate();
//							}
//						})), m.start(), m.end(),
//						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				//显示静态图
//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inSampleSize = 2;
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(is,null, options);
				// 调用上面定义的方法计算inSampleSize值
				options.inSampleSize = calculateInSampleSize(options, 30, 30);
				// 使用获取到的inSampleSize值再次解析图片
				options.inJustDecodeBounds = false;
				sb.setSpan(
						new ImageSpan(mContext, BitmapFactory
								.decodeStream(is,null,options)), m.start()
								,m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//资源释放
				if(!BitmapFactory.decodeStream(is).isRecycled()){
					BitmapFactory.decodeStream(is).recycle();
				}
				is.close();
			} catch (Exception e) {
				String png ="g/"+tempText.replace("*#","").replace("#*","")+".gif";
				try {
					//设置图片大小
//					BitmapFactory.Options options = new BitmapFactory.Options();
//					options.inSampleSize = 2;
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(is,null, options);
					// 调用上面定义的方法计算inSampleSize值
					options.inSampleSize = calculateInSampleSize(options, 30, 30);
					// 使用获取到的inSampleSize值再次解析图片
					options.inJustDecodeBounds = false;
					sb.setSpan(
							new ImageSpan(mContext, BitmapFactory.decodeStream(
									mContext.getAssets().open(png),null,options)), m.start(), m.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					//资源释放
					if(! BitmapFactory.decodeStream(mContext.getAssets().open(png)).isRecycled()){
						BitmapFactory.decodeStream(mContext.getAssets().open(png)).recycle();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return sb;
	}
	public static SpannableStringBuilder praseSample(Context mContext,String content) {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		InputStream is;
		String regex = "\\*#emo_(\\d{3})#\\*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
			try {
				String num ="g/"+ tempText.replace("*#","").replace("#*","")+".gif";
				/**
				 * 如果open这里不抛异常说明存在gif，则显示对应的gif
				 * 否则说明gif找不到，则显示png
				 * */
				is = mContext.getAssets().open(num);
				//显示静态图
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				sb.setSpan(
						new ImageSpan(mContext, BitmapFactory
								.decodeStream(is,null,options)), m.start()
						,m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				//资源释放
				if(!BitmapFactory.decodeStream(is).isRecycled()){
					BitmapFactory.decodeStream(is).recycle();
				}
				is.close();
			} catch (Exception e) {
				String png ="g/"+tempText.replace("*#","").replace("#*","")+".gif";
				try {
					//设置图片大小
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 1;
					sb.setSpan(
							new ImageSpan(mContext, BitmapFactory.decodeStream(
									mContext.getAssets().open(png),null,options)), m.start(), m.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					//资源释放
					if(! BitmapFactory.decodeStream(mContext.getAssets().open(png)).isRecycled()){
						BitmapFactory.decodeStream(mContext.getAssets().open(png)).recycle();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return sb;
	}
	public static SpannableStringBuilder getFace(Context mContext,String gif) {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		try {
			/**
			 * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
			 * 所以这里对这个tempText值做特殊处理
			 * 格式：*#emo_052#*，以方便判斷當前圖片是哪一個
			 * */
			String tempText = "*#" + gif.replace("g/","").replace(".gif","") + "#*";
			sb.append(tempText);
			sb.setSpan(
					new ImageSpan(mContext, BitmapFactory
							.decodeStream(mContext.getAssets().open(gif))), sb.length()
							- tempText.length(), sb.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			//资源释放
			if( !BitmapFactory.decodeStream(mContext.getAssets().open(gif)).isRecycled()){
				BitmapFactory.decodeStream(mContext.getAssets().open(gif)).recycle();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}
	
	/**
	 * 向输入框里添加表情
	 * */
	public static void insert(EditText input,CharSequence text) {

		int iCursorStart = Selection.getSelectionStart((input.getText()));
		int iCursorEnd = Selection.getSelectionEnd((input.getText()));
		if (iCursorStart != iCursorEnd) {
			( input.getText()).replace(iCursorStart, iCursorEnd, "");
		}
		int iCursor = Selection.getSelectionEnd((input.getText()));
		( input.getText()).insert(iCursor, text);
	}


	/**
	 * 删除图标执行事件
	 * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
	 * */
	public static void delete(EditText input) {
		if (input.getText().length() != 0) {
			int iCursorEnd = Selection.getSelectionEnd(input.getText());
			int iCursorStart = Selection.getSelectionStart(input.getText());
			if (iCursorEnd > 0) {
				if (iCursorEnd == iCursorStart) {
					if (isDeletePng(input,iCursorEnd)) {
						String st = "*#emo_000#*";
						( input.getText()).delete(
								iCursorEnd - st.length(), iCursorEnd);
					} else {
						( input.getText()).delete(iCursorEnd - 1,
								iCursorEnd);
					}
				} else {
					(input.getText()).delete(iCursorStart,
							iCursorEnd);
				}
			}
		}
	}

	/**
	 * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
	 * **/
	public static boolean  isDeletePng(EditText input,int cursor) {
		String st = "*#emo_000#*";
		String content = input.getText().toString().substring(0, cursor);
		if (content.length() >= st.length()) {
			String checkStr = content.substring(content.length() - st.length(),content.length());
			Logger.d(checkStr);
			String regex = "\\*#emo_(\\d{3})#\\*";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(checkStr);
			return m.matches();
		}
		return false;
	}
	
	public static View viewPagerItem(final Context context,int position,List<String> staticFacesList,int columns,int rows,final EditText editText) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
		/**
		 * 因为每一页末尾都有一个删除图标，
		 * 所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
		 *
		 * */
		List<String> subList = new ArrayList<>();
		subList.addAll(staticFacesList
				.subList(position * (columns * rows - 1),
						(columns * rows - 1) * (position + 1) > staticFacesList
								.size() ? staticFacesList.size() : (columns
								* rows - 1)
								* (position + 1)));
		/**
		 * 末尾添加删除图标
		 * */
		subList.add("emo_del.png");
		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, context);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		// 单击表情执行的操作
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				try {
					String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
					Logger.d("表情的字符串："+png);
					if (!png.contains("_del")) {// 如果不是删除图标
						ExpressionUtil.insert(editText,ExpressionUtil.getFace(context,png));
					} else {
						ExpressionUtil.delete(editText);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		return gridview;
	}
	
	/**
	 * 根据表情数量以及GridView设置的行数和列数计算Pager数量
	 * @return
	 */
	public  static int getPagerCount(int listsize,int columns,int rows ) {
		return listsize % (columns * rows - 1) == 0 ? listsize / (columns * rows - 1): listsize / (columns * rows - 1) + 1;
	}
	
	/**
	 * 初始化表情列表staticFacesList
	 * 加载数据源
	 */
	public static  List<String> initStaticFaces(Context context) {
		List<String> facesList=null;
		try {
			facesList = new ArrayList<>();
			String[] faces = context.getAssets().list("g");
			//将Assets中的表情名称转为字符串一一添加进staticFacesList
//			for (String s:faces){
//				facesList.add(s);
//			}
			Collections.addAll(facesList, faces);
			//去掉删除图片
			facesList.remove("emo_del.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return facesList;
	}
	/**
	 * 检索String是否还有#@_
	 * @param content
	 * @param index
	 * @return
	 */
	public static String RecursiveQuery(Context context,String content,int index){
		String start="#@_";
		String end="_@#";
        String msg;
		int startindex=content.indexOf(start,index);
		int endindex=content.indexOf(end,startindex);
		if(startindex!=-1&& endindex!=-1 && endindex>startindex+start.length()){
			msg=content.substring(endindex+3,content.length());
			content=content.substring(0,startindex)+content.substring(content.indexOf("_!@_"),endindex);
			content=content.replace("_!@_","@").replace(end,"");
			return content+RecursiveQuery(context,msg,0);
		}else{
			return  content;
		}
	}



	private static int calculateInSampleSize(BitmapFactory.Options options,
									  int reqWidth, int reqHeight) {
		// 源图片的宽度
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		// int min = Math.min(width, height);
		// int maxReq = Math.max(reqWidth, reqHeight);

		// if(min > maxReq) {
		//     inSampleSize = Math.round((float) min / (float) maxReq);
		// }

		// 通过之前的计算方法，在加载类似400*4000这种长图时会内存溢出
		if (width > reqWidth || height > reqHeight) {
			int widthRadio = Math.round(width * 1.0f / reqWidth);
			int heightRadio = Math.round(height * 1.0f / reqHeight);

			inSampleSize = Math.max(widthRadio, heightRadio);
		}

		return inSampleSize;
	}
}
