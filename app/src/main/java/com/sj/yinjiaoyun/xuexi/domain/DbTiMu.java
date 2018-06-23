package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/1.
 * 封装题目答案信息 （仅供数据库使用）
 */
public class DbTiMu {

    String tmId;//题目id
    Integer qid;//编制的题号
    String answer;//答案

    public DbTiMu() {
    }

    public DbTiMu(String tmId, Integer qid, String answer) {
        this.tmId = tmId;
        this.qid = qid;
        this.answer = answer;
    }


    public String getTmId() {
        return tmId;
    }

    public void setTmId(String tmId) {
        this.tmId = tmId;
    }

    public Integer getQid() {
        return qid;
    }

    public void setQid(Integer qid) {
        this.qid = qid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return " 题目DbTiMu{" +
                "tmId='" + tmId + '\'' +
                ",题号 qid='" + qid + '\'' +
                ",答案下标 answer='" + answer + '\'' +
                '}';
    }
}
