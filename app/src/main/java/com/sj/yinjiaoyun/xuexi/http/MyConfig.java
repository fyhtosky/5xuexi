package com.sj.yinjiaoyun.xuexi.http;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/19.
 * 常量类
 */
public class MyConfig {


    /**
     *  拼接 接口
     * @param url 核心不同内容的url
     * @return    拼接号的url前缀，不加参数
     */
    public static String getURl(String url){

        String urlHttp=Api.Host+"/api/v2/";//测试环境
        StringBuilder sb=new StringBuilder();
        sb.append(urlHttp);//服务器的前缀
        sb.append(url);
        sb.append(".action");//url的尾部字段
        Log.i("geturl", "getURl: "+sb.toString());
        return sb.toString();
    }



    public static final int SQL_VERSION=4;//数据库版本

    //网络请求码
    public static final int CODE_GET_VERIFICATION=1;//get请求码（验证码校验）
    public static final int CODE_POST_PHONE=2;//post请求码(电话号码，校验)
    public static final int CODE_POST_NAME=3;//post请求码(用户名校验)
    public static final int CODE_POST_REGISTER=4;//post请求码(注册校验)
    public static final int CODE_POST_LOGIN=5;//post请求码(登录校验)
    public static final int CODE_POST_FIND=6;//post请求码(忘记密码接口)
    public static final int CODE_POST_USER=7;//get请求码(获取个人信息请求码)
    public static final int CODE_POST_MAJOR=8;//get请求码(获取个人报名的专业信息)
    public static final int CODE_POST_COURSES=9;//get请求码(教学计划信息)
    public static final int CODE_POST_SCHEDULE=10;//get请求码(据课程表ID获取学员课程章节课时信息列表及学习情况)
    public static final int CODE_GET_COURSEINFO=11;//get请求码（获取课程信息）
    public static final int CODE_GET_SCORE=12;//get请求码（获取学生课程成绩）
    public static final int CODE_POST_VIDEO=13;//post请求码（视屏播放信息反馈）
    public static final int CODE_POST_GETPATH=14;//get请求码（人大获取视频网址需要的参数）
    public static final int CODE_POST_GETURL=15;//get请求码（人大获取视频网址）
    public static final int CODE_GET_UPDATE=16;//get请求码（软件更新）

    //数据库
    public static final String NAME="learn";//数据库的名字
    public static final String SQL_CREATE="create table if not exists";//数据库里面建表时候的提取字符串
    public static final String TB_LOGIN = "tb_logins";//数据库 表名 登录状态
    public static final String LOGIN_TRUE="1";//登录在线状态
    public static final String LOGIN_FALSE="2";//登录不在线状态

    public static final String TB_USERINFO = "tb_userinfo";//数据库表名 用户个人信息储存
    public static final String TB_PRODUCT = "tb_product";//数据库表名   首页我的专业数据缓存
    public static final String TB_INTRO = "tb_intro";//数据库表名   详情页我的分数数据缓存
    public static final String TB_JOBS = "tb_jobs";//数据库表名   我的分数数据缓存
   // 题号的顺序
    public static Map<String,String>timuQid=new HashMap<>();

    /**
     * 专业类型   学年 1:第一学年 2:第二学年 ....', 0的时候不显示
     * @return 学年
     */
    public static Map xueNian(){
        Map<String,String> mapXueNian=new HashMap<>();
        mapXueNian.put("1","第一学期");
        mapXueNian.put("2","第二学期");
        mapXueNian.put("3","第三学期");
        mapXueNian.put("4","第四学期");
        mapXueNian.put("5","第五学期");
        mapXueNian.put("6","第六学期");
        mapXueNian.put("7","第七学期");
        mapXueNian.put("8","第八学期");
        mapXueNian.put("9","第九学期");
        mapXueNian.put("10","第十学期");
        return mapXueNian;
    }

    /**
     * 0:公共基础课,1:专业选修课,2:专业基础课,3:公共选修课,
     * @return  课程类型
     */
    public static Map couseleiXing(){
        Map<String,String> mapLeiXing=new HashMap<>();
        mapLeiXing.put("0","公共基础课");
        mapLeiXing.put("1","专业选修课");
        mapLeiXing.put("2","专业基础课");
        mapLeiXing.put("3","公共选修课");
        return mapLeiXing;
    }

    /**
     * 是	专业学制' (网校   成教   自考   选修)单位：年  ，（培训  考证）单位：周
     * @return
     */
    public static Map productLearningLength(){
        Map<String,String> mapLeiXing=new HashMap<>();
        mapLeiXing.put("0","学期");
        mapLeiXing.put("1","学期");
        mapLeiXing.put("2","学期");
        mapLeiXing.put("3","周");
        mapLeiXing.put("4","周");
        mapLeiXing.put("5","学期");
        mapLeiXing.put("10","周");
        return mapLeiXing;
    }

    /**
     * 教育类别   0：网校 1：成教 2：自考 3：培训 4：考证 5 : 选修
     * @return
     */
    public static Map productType(){
        Map<String,String> mapXueNian=new HashMap<>();
        mapXueNian.put("0","网教");
        mapXueNian.put("1","成教");
        mapXueNian.put("2","自考");
        mapXueNian.put("3","培训");
        mapXueNian.put("4","考证");
        mapXueNian.put("5","统招");
        return mapXueNian;
    }

    /**
     * 教育类别   学年 1:春季 2:秋季 ....', 0的时候不显示
     * @return
     */
    public static Map theSeason(){
        Map<String,String> mapXueNian=new HashMap<>();
        mapXueNian.put("0","春季");
        mapXueNian.put("1","秋季");
        return mapXueNian;
    }

    /**
     *  层次   1:高起专 2:专升本  3 高起本  4 专科 5 本科....', 0的时候不显示
     * @return 集合
     */
    public static Map productGradation(){
        Map<String,String> mapXueNian=new HashMap<>();
        mapXueNian.put("1","高起专");
        mapXueNian.put("2","专升本");
        mapXueNian.put("3","高起本");
        mapXueNian.put("4","专科");
        mapXueNian.put("5","本科");
        return mapXueNian;
    }

    /**
     * 学习形式   1:网络教育 2:业余  3 函授   4 全日制....', 0的时候不显示
     * 2016.12.12修改 网络教育 2:业余 3:函授 4：全日制 5:专科段 6：本科段 7：独立本科段
     * @return 集合
     */
    public static Map learningModality(){
        Map<String,String> mapXueNian=new HashMap<>();
        mapXueNian.put("1","网络教育");
        mapXueNian.put("2","业余");
        mapXueNian.put("3","函授");
        mapXueNian.put("4","全日制");
        mapXueNian.put("5","专科段");
        mapXueNian.put("6","本科段");
        mapXueNian.put("7","独立本科段");
        return mapXueNian;
    }

    /**
     * 支付方式  '0:线上,1:线下支付',
     * @return 集合
     */
    public static Map payMethod(){
        Map<String,String> payMethod=new HashMap<>();
        payMethod.put("0","线上");
        payMethod.put("1","线下支付");
        return payMethod;
    }

    /**
     * 支付方式  '0:分期,1:全额',
     * @return 集合
     */
    public static Map payType(){
        Map<String,String> payMethod=new HashMap<>();
        payMethod.put("0","分期");
        payMethod.put("1","全额");
        return payMethod;
    }

    /**
     * 支付方式  '0:分期,1:全额',
     * @return 集合
     */
    public static Map questionType(){
        Map<String,String> payMethod=new HashMap<>();
        payMethod.put("1","单选题");
        payMethod.put("2","多选题");
        payMethod.put("3","判断题");
        payMethod.put("4","问答题");
        payMethod.put("5","完形填空");
        payMethod.put("6","阅读理解");
        return payMethod;
    }
    /**
     * 题型自动排序
     */
    public static void putTimuQid(String questionType,String qid){
       timuQid.put(questionType,qid);
    }

    public static Map<String, String> getTimuQid() {
        return timuQid;
    }

    /**
     * 微专业开课方式  0：随到随学 1:定期开课
     * @return 集合
     */
    public static Map tutionWay(){
        Map<String,String> tutionMap=new HashMap<>();
        tutionMap.put("0","随到随学");
        tutionMap.put("1","定期开课");
        return tutionMap;
    }


    public static String hint="该功能正在开发中，敬请期待";

}
