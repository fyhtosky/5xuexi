package com.sj.yinjiaoyun.xuexi.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.SortAdapter;
import com.sj.yinjiaoyun.xuexi.domain.College;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.domain.ParserYuanXiao;
import com.sj.yinjiaoyun.xuexi.domain.ParserYuanXiaoData;
import com.sj.yinjiaoyun.xuexi.entry.CharacterParser;
import com.sj.yinjiaoyun.xuexi.entry.PinyinComparator;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.view.ClearEditText;
import com.sj.yinjiaoyun.xuexi.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 名片夹
 * 
 * bug：一个名字只选了第一个电话号码；另外，如果重名的话只显示一个
 * 
 * @author WangPeng
 * 类似qq通讯录的效果
 * 
 */
public class ContactsFragment extends Fragment implements HttpDemo.HttpCallBack{
	String TAG="aaaa";
	private Context mContext;
	private ListView sortListView;
	private List<College> SourceDateList;//设置adapter的集合（数据已经过处理）
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;

	HttpDemo demo;
	List<College> collegeList;//网络请求返回的集合（数据未经过处理）

	ContactsFragemntCallBack callBack;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	View headView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
		headView = inflater.inflate(R.layout.head_fragment_contacts,null);
		findView(rootView,headView);
		init();
		return rootView;
	}




	/**
	 * 传递值
	 * @param context
     */
	public void setActivity(Context context){
		this.callBack= (ContactsFragemntCallBack) context;
	}

	private void findView(View rootView,View headView) {
		sideBar = (SideBar) rootView.findViewById(R.id.sidrbar);
		dialog = (TextView) rootView.findViewById(R.id.dialog);
		sortListView = (ListView) rootView.findViewById(R.id.country_lvcountry);
		mClearEditText = (ClearEditText)headView.findViewById(R.id.filter_edit);
	}

	/**
	 * 初始化
	 */
	private void init() {
		//输入法的对象初始化
		final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
        //实力化拼音排序类
		pinyinComparator = new PinyinComparator();
		sideBar.setTextView(dialog);
		// 设置右侧字母导航触摸监听
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@SuppressLint("NewApi")
			@Override
			public void onTouchingLetterChanged(String s) {
				try {
					//该字母首次出现的位置
					int position = adapter.getPositionForSection(s.charAt(0));
					if (position != -1) {
						sortListView.setSelection(position);//根据首字母设置listView的位置
					}
					if(imm.isActive()){//表示输入法正在显示
						imm.hideSoftInputFromWindow(mClearEditText.getWindowToken(), 0);//设置其影藏
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		//设置listview的子项点击事件
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(TAG, "onItemClick: "+"点击事件"+collegeList.get(position-1).toString());//加头了要减1
				Logger.d("setOnItemClickListener"+collegeList.get(position-1).getId());
				College college=SourceDateList.get(position-1);
				callBack.deliverValue(college);
			}
		});
		new AsyncTaskConstact().execute();
	}



	/**
	 * //从网络上获取所有院校的接口
	 */
	public void getHttpDataToYuanXian(){
		Log.i(TAG, "getHttpDataToYuanXian: ");
		demo=new HttpDemo(this);
		String url= MyConfig.getURl("college/findAllActivityCollege");
		demo.doHttpGet(url,null,0);
	}

	@Override
	public void setMsg(String msg, int requestCode) {
		parserYuanXiaoData(msg);
	}

	/**
	 *  解析获取所有院校的信息
	 * @param msg   json串    {"state":200,"success":true,"message":null,"data":{"college":[
	 *              {"id":1,"organizationName":"湖北经济学院"},{"id":2,"organizationName":"重庆大学"},
	 *              {"id":10,"organizationName":"武汉大学"},{"id":18,"organizationName":"武汉理工大学"},
	 *              {"id":21,"organizationName":"湖北工业大学"},{"id":41,"organizationName":"武汉职业技术学院"},
	 *              {"id":85,"organizationName":"武汉轻工大学"},{"id":99,"organizationName":"华中科技大学"},
	 *              {"id":105,"organizationName":"重庆城市管理职业学院"},
	 *              {"id":114,"organizationName":"武汉科技大学"},
	 *              {"id":123,"organizationName":"南京大学"},{"id":163,"organizationName":"武汉生物工程学院"}]}}
	 */
	public void parserYuanXiaoData(String msg){
		Log.i(TAG, "parserYuanXiaoData: "+msg);
		Gson gson=new Gson();
		ParserYuanXiao parserYuanXiao=gson.fromJson(msg, ParserYuanXiao.class);
		if(parserYuanXiao.getSuccess()==false){
			return;
		}
		ParserYuanXiaoData data=parserYuanXiao.getData();
		collegeList=data.getCollege();
		Log.i(TAG, "parserYuanXiaoData: "+collegeList.size());
		SourceDateList = filledData(collegeList);

		sideBar.setVisibility(View.VISIBLE);
		// 根据a-z进行排序源数据，非a-z排在最后面
		Collections.sort(SourceDateList, pinyinComparator);
		// 非a-z排在最前面，a-z排序源数据在其后面，
		SourceDateList=orderList(SourceDateList);

		//给listView添加头文件
		sortListView.addHeaderView(headView);

		//排序完成设置Adapter
		adapter = new SortAdapter(mContext, SourceDateList);
		sortListView.setAdapter(adapter);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 重新进行排序  把特殊字符的排在字母的顺序前面
	 * @param list   需要排序的集合
	 * @return  拍好的集合
	 */
	private List<College> orderList(List<College> list){
		List<College> data=new ArrayList<>();
		College college;
		String sortString;
		for(int i=0;i<list.size();i++){
			college=list.get(i);
			sortString=college.getFirstSpell();
			if(!(sortString.matches("[A-Z]"))){//首拼音不是英文字母的情况
				college.setFirstSpell("#");//设置首拼音不是字母的设置为#，方便输入框查找
				data.add(college);
			}
		}
		for(int i=0;i<list.size();i++){
			college=list.get(i);
			sortString=college.getFirstSpell();
			if(sortString.matches("[A-Z]")){//首拼音是英文字母的情况
				data.add(college);
			}
		}
		return data;
	}


	//往StudentIdLoginAvtivity里面传递数据，作为结果返回给登录页面
	public interface ContactsFragemntCallBack{
		void  deliverValue(College college);
	}


	/**
	 * 为ListView填充数据  (把获取的数据简单处理后填充adapter)
	 *
	 * @param date
	 * @return
	 */
	public List<College> filledData(List<College> date) {
		Log.i(TAG, "filledData: date "+date.size());
		List<College> mSortList = new ArrayList<>();
		for (int i = 0; i < date.size(); i++) {
			College college=date.get(i);
			College sortModel = new College();

			sortModel.setOrganizationName(college.getOrganizationName());
			// 汉字转换成拼音
			/*String pinyin = characterParser.getSelling(college.getOrganizationName());
			String sortString = pinyin.substring(0, 1).toUpperCase();*/

			String sortString=college.getFirstSpell();
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setFirstSpell(sortString.toUpperCase());
			} else {
				sortModel.setFirstSpell("#");
			}
			sortModel.setId(college.getId());
			sortModel.setCollegeLogo(college.getCollegeLogo());
			mSortList.add(sortModel);
		}
		return mSortList;
	}


	/**
	 * 根据输入框中的值来过滤（匹配对应的）数据并更新ListView
	 * @param filterStr  输入框中的lisrView
	 */
	private void filterData(String filterStr) {
		List<College> filterDateList = new ArrayList<>();
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			String name;//名字
			String firstSpell;//首字母
			for (College sortModel : SourceDateList) {
				name = sortModel.getOrganizationName();//名字
				firstSpell = sortModel.getFirstSpell();//首字母
				if (name.contains(filterStr)  //名字匹配
						|| characterParser.getSelling(name).startsWith(filterStr) //拼音匹配
						|| firstSpell.equals(filterStr)){ //首字母匹配
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z及特殊符号进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(orderList(filterDateList));

	}


	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////

	private class AsyncTaskConstact extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			int result ;
			result = 1;
			return result;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == 1) {
				getHttpDataToYuanXian();
			}
		}
	}


}
