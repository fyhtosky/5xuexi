package com.sj.yinjiaoyun.xuexi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.app.MyApplication;
import com.sj.yinjiaoyun.xuexi.db.DbOperatorLogin;
import com.sj.yinjiaoyun.xuexi.domain.College;
import com.sj.yinjiaoyun.xuexi.domain.LoginInfo;
import com.sj.yinjiaoyun.xuexi.domain.Pairs;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.http.HttpDemo;
import com.sj.yinjiaoyun.xuexi.http.MyConfig;
import com.sj.yinjiaoyun.xuexi.loginCommon.ThirdLoginManager;
import com.sj.yinjiaoyun.xuexi.utils.ActiveActUtil;
import com.sj.yinjiaoyun.xuexi.utils.DeleteDataForTable;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;
import com.sj.yinjiaoyun.xuexi.widget.NewEditView;
import com.sj.yinjiaoyun.xuexi.xmppmanager.Const;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends MyBaseActivity implements HttpDemo.HttpCallBack {
    String TAG = "loginaa";
    String account;
    String pwd;
    String img;//用户头像网址
    College college;//院校的类
    public static final String PHONE = "pnone";
    RadioGroup radioGroup;//账号登录  学号登录
    RadioButton rb1;//账号登录
    RadioButton rb2;//学号登录
    View tran_container;//账号、学号切换的动画
    Button btnLogin;//登录
    Button btnZhuce;//注册
    View container;//学校选择 院校容器
    Button btnYuanXiao;//院校  点击后跳转到院校选择页面
    NewEditView etName;
    EditText etPwd;
    TextView tvForget;//忘记密码
    HttpDemo demo;
    DbOperatorLogin db;//保存登录状态的数据库

    //点击关闭软键盘
    private LinearLayout linearLayout;
    //是否显示软键盘
    private boolean isShow = false;
    DeleteDataForTable deleteDataForTable;

    private String url;
    private List<Pairs> pairsList;
    private LoginInfo loginInfo;
    private ThirdLoginManager thirdLoginManager;
    //表示Token是否失效
    private boolean isTokenLost=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        ButterKnife.bind(this);
        thirdLoginManager=new ThirdLoginManager(LoginActivity.this);
        demo = new HttpDemo(this);
        deleteDataForTable = new DeleteDataForTable(this);
        db = new DbOperatorLogin(this);
        loginInfo = db.getLoginEndUserInfo();
        deleteDataForTable.deleteAllData();
        getScreenMiDu();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.getNetworkSatte(LoginActivity.this);
    }

    /**
     * 获取屏幕密度  宽度   设置相应的值
     */
    private void getScreenMiDu() {
        // 获取屏幕密度（方法2）
        DisplayMetrics dm2 = getResources().getDisplayMetrics();

//        float density  = dm2.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
//        int densityDPI = dm2.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）

        int screenWidth2 = dm2.widthPixels;      // 屏幕宽（像素，如：480px）
        int screenHeight2 = dm2.heightPixels;     // 屏幕高（像素，如：800px）

        //设置View这个控件占中高度的0.3
        View view = findViewById(R.id.login_container);
        tran_container = findViewById(R.id.tran_container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                (int) (screenHeight2 * 0.3f + 0.5f));
        view.setLayoutParams(params);

        //设置动画图片的图片宽度
        RelativeLayout.LayoutParams laParams = (RelativeLayout.LayoutParams) tran_container.getLayoutParams();
        laParams.width = screenWidth2 / 4;
        tran_container.setLayoutParams(laParams);
    }

    //初始化控件
    private void init() {
        linearLayout = (LinearLayout) findViewById(R.id.ll_layout);
        radioGroup = (RadioGroup) findViewById(R.id.login_group);
        btnYuanXiao = (Button) findViewById(R.id.login_YuanXiao);
        container = findViewById(R.id.login_container_YuanXiao);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        etName = (NewEditView) findViewById(R.id.login_name);
        etPwd = (EditText) findViewById(R.id.login_password);
        btnLogin = (Button) findViewById(R.id.login_btn);
        tvForget = (TextView) findViewById(R.id.login_forget);
        btnZhuce = (Button) findViewById(R.id.login_regist);
        etPwd.setOnFocusChangeListener(focusListener);
        etName.setOnFocusChangeListener(focusListener);
        initEventForView();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setFocusable(true);
                linearLayout.setFocusableInTouchMode(true);
                linearLayout.requestFocus();
                hideSoftInputView();
            }
        });
        etPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                //显示软键盘
                if (isShow) {
                    //系统自带
//                    showSoftInputView(etPwd);
                } else {
                    //不显示软键盘
                    hideSoftInputView();
                }
            }
        });

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            isTokenLost=bundle.getBoolean(Const.IS_TOKEN_LOST,false);
            if(isTokenLost){
                ToastUtil.showShortToast(MyApplication.getContext(),"你的账号已在别处登录或登录超时");
            }
        }

    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



    //为初始化的的控件设置事件
    private void initEventForView() {
        //设置 学号登录、账号登录 切换问题
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1://账号登录
                        container.setVisibility(View.GONE);
                        btnZhuce.setVisibility(View.VISIBLE);
                        changeAnimation(2);
                        etName.setHintText("手机号/用户名/身份证号");
                        etName.setEditText("");
                        etPwd.setText("");
                        break;
                    case R.id.rb2://学号登录
                        container.setVisibility(View.VISIBLE);
                        btnZhuce.setVisibility(View.GONE);
                        changeAnimation(3);
                        etName.setHintText("学号");
                        etName.setEditText("");
                        etPwd.setText("");
                        break;
                }
            }
        });
        //初始化动画控件
        if (rb1.isChecked()) {
            changeAnimation(2);
        } else if (rb2.isChecked()) {
            changeAnimation(3);
        }
        setLoginHistory();
    }


    View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()) {
                case R.id.login_name://用户名
                    if (hasFocus) {
                        String a = etName.getEditText();
                        if (!(a.equals(""))) {
                            etName.setSelection(a.length());
                        }
                    }
                    break;
                case R.id.login_password://密码
                    if (hasFocus) {
                        etPwd.setText("");
                    }
                    break;
            }
        }
    };


    public void onclick(View view) {
        Intent intent;
        account = etName.getEditText().toString().trim();
        pwd = etPwd.getText().toString();
        switch (view.getId()) {
            case R.id.login_btn://登录
                userLogin();
                break;
            case R.id.login_forget://忘记密码
                //intent = new Intent(this, FindActivity.class);
            //startActivity(intent);
                Intent forIntent = new Intent(this, ForgetPwdActivity.class);
                forIntent.putExtra(PHONE, account);
                startActivity(forIntent);
                break;
            case R.id.login_regist://注册
                //intent = new Intent(this, RegistActivity.class);
                //startActivity(intent);
                startActivity(new Intent(this, JSRegistActivity.class));
                break;
            case R.id.login_YuanXiao://选择院校
                intent = new Intent(this, SelectCollegeActivity.class);
                intent.putExtra("requestCode", 1);
                startActivityForResult(intent, 1);
                break;
        }
    }



    //反馈学校的选择结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case 1:
                Bundle b = data.getExtras();  //data为B中回传的Intent
                if (b != null) {
                    college = b.getParcelable("College");//str即为回传的值"Hello, this is B speaking"
                    if (college != null) {
                        btnYuanXiao.setText(college.getOrganizationName());
                        changeAnimation(3);
                    }

                }
                break;
            default:
                break;
        }

    }

    //用户登录
    private void userLogin() {
        Log.i(TAG, "进入userLogin: ");
        if (rb2.isChecked() && btnYuanXiao.getText().equals("")) {
            Toast.makeText(LoginActivity.this, "请选择院校", Toast.LENGTH_LONG).show();
            return;
        }
        if (account.equals("")) {
            String tishi;
            if (rb1.isChecked()) {
                tishi = "请输入账号";
            } else {
                tishi = "请输入学号";
            }
            Toast.makeText(LoginActivity.this, tishi, Toast.LENGTH_LONG).show();
            return;
        }
        if (pwd.equals("")) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
            return;
        }
        url = MyConfig.getURl("passport/doLogin");
        Logger.d(url);
        pairsList = new ArrayList<>();
        if (rb2.isChecked()) {
            if (college == null) {
                Toast.makeText(LoginActivity.this, "请选择院校", Toast.LENGTH_LONG).show();
                return;
            } else {
                pairsList.add(new Pairs("collegeId", String.valueOf(college.getId())));
            }
        }
        pairsList.add(new Pairs("userName", account));
        pairsList.add(new Pairs("password", pwd));
        pairsList.add(new Pairs("loginSystem", "1"));
        demo.doHttpPostLoading(this, url, pairsList, MyConfig.CODE_POST_LOGIN);
    }

    @Override
    public void setMsg(String msg, int requestCode) {
        parseLogin(msg);
    }

    /**
     * 解析登录返回信息
     *
     * @param msg
     */
    private void parseLogin(String msg) {
        Log.i(TAG, "进入parseLogin登录解析: " + msg);
        try {
            JSONObject object = new JSONObject(msg);
            if (object.getBoolean("success")) {
                JSONObject data = object.getJSONObject("data");
                JSONObject info = (JSONObject) data.get("user");
                Long id = info.getLong("endUserId");
                String password=info.getString("password");
                if (info.get("userImg") == null) {
                    Log.i(TAG, "parseLogin: " + (info.get("userImg").equals("")) + " " + (info.get("userImg") == null));
                } else {
                    img = info.getString("userImg");
                }
                if (id != 0) {
                    int flag;
                    if (rb1.isChecked()) {
                        flag = 1;//记录为账号登录
                    } else {
                        flag = 2;//代表学号登录
                        //存储登录学校的id
                        PreferencesUtils.putSharePre(getApplicationContext(), Const.SCHOOL_ID, college.getId());
                    }
                    String realName = info.getString("realName");
                    loginInfo = new LoginInfo(MyConfig.LOGIN_TRUE, flag + "", id + "", account, pwd, img, realName);
                    Log.i(TAG, "parseLogin:登录成功 " + MyConfig.LOGIN_TRUE + ":" + loginInfo.getEndUserId() + ":" + loginInfo.getParam() + ":" + loginInfo.getPwd() + "图片的url:" + loginInfo.getImage());
                    db.insertLogin(loginInfo);//存对象在数据库
                    /**
                     * 模拟登陆所需要的数据
                     * 1.记录登录方式
                     * 如果是账号登录则存储用户和密码 若果是学号登录则需要存储院校的id和 学号 。密码
                     * 2.记录登录的用户
                     * 3.记录登录密码
                     */

                    //表示登录的方式
                    PreferencesUtils.putSharePre(getApplicationContext(), Const.FLAG, flag);
                    //表示登录的用户
                    PreferencesUtils.putSharePre(getApplicationContext(), Const.LOGIN_NAME, account);
                    //表示登录的MD5密码
                    PreferencesUtils.putSharePre(getApplicationContext(), Const.PASSWORD, password);
                    /**
                     * 1.启动后台登录操作
                     * 2.缓存所需要的数据
                     */

                    PreferencesUtils.putSharePre(getApplicationContext(), "username", loginInfo.getEndUserId());
                    PreferencesUtils.putSharePre(getApplicationContext(), "pwd", pwd);
                    PreferencesUtils.putSharePre(getApplicationContext(), "userImg", loginInfo.getImage());
                    PreferencesUtils.putSharePre(getApplicationContext(), "realName", realName);
                    PreferencesUtils.putSharePre(getApplicationContext(), "Name", info.getString("userName"));
                    //存储缓存
                    PreferencesUtils.putSharePre(getApplicationContext(), Const.TOKEN, info.getString("token"));
                    PreferencesUtils.putSharePre(getApplicationContext(), Const.LOGIN_STATE, true);
                    //跳转操作
                    if (MyApplication.groupsList != null) {
                        MyApplication.groupsList.clear();
                    }
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    MyApplication.isLoginSkip = true;
                    //清除所有Activity
                    ActiveActUtil.getInstance().exit();
                } else {
                    Log.i(TAG, "parseLogin: " + "登录失败");
                }
            } else {
                Log.i(TAG, "parseLogin:" + object.getString("message"));
                Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //设置学号登录和账号登录的历史记录问题
    private void setLoginHistory() {
        String name;
//        String pwd;
        LoginInfo unInfo;
        //获取数据库中未登录用户的集合
        List<LoginInfo> loginInfos = db.getUnLoginEndUserInfo();
        if (loginInfos.size() > 0) {//有离线用户
            Log.i(TAG, "onStart: 数据库未登录用户的个数 " + loginInfos.size());
            unInfo = loginInfos.get(loginInfos.size() - 1);
            Log.i(TAG, "onStart: 未登录状态的账号信息" + unInfo.toString());
            if (unInfo.getFlag().equals("1")) {//账号登录 的历史记录
                rb1.setChecked(true);
                name = unInfo.getParam();
                etName.setEditText(name);
                etName.setSelection(name.length());
            } else {//学号登录 的历史记录
                rb2.setChecked(true);
                etName.setEditText(unInfo.getParam());
                etName.setSelection(unInfo.getParam().length());
            }
        } else {//无离线用户
            etName.setEditText("");
            etPwd.setText("");
        }
    }



    //账号、学号切换时候的动画效果
    private void changeAnimation(int arg0) {
        switch (arg0) {
            case 1://表示动画从左到右
                Animation animationR = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_four_o);
                tran_container.startAnimation(animationR);//开始动画
                animationR.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
            case 2://表示动画初始化（登录进来时候）
                Animation animation3 = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_four_t);
                tran_container.startAnimation(animation3);//开始动画
                animation3.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
            case 3://表示动画初始化（当用户导航回来）
                Animation animationT = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_four_th);
                tran_container.startAnimation(animationT);//开始动画
                animationT.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
            case 4://表示动画从右到左
                Animation animationL = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_four_f);
                tran_container.startAnimation(animationL);//开始动画
                animationL.setFillAfter(true);/* True:图片停在动画结束位置 */
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            //清除所有Activity
            ActiveActUtil.getInstance().exit();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


    @OnClick({R.id.tv_weixin, R.id.tv_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_weixin:
                thirdLoginManager.WeixinLogin();
                break;
            case R.id.tv_qq:
                thirdLoginManager.qqLogin();
                break;
        }
    }






}
