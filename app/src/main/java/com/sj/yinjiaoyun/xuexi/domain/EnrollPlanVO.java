package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by wanzhiying on 2017/3/7.
 * 学历教育招生计划
 */
public class EnrollPlanVO {
    private Long id;
    private Long theYear;
    private Long theSeason;
    private String  enrollmentPlanName;

    public String getEnrollmentPlanName() {
        return enrollmentPlanName;
    }

    public void setEnrollmentPlanName(String enrollmentPlanName) {
        this.enrollmentPlanName = enrollmentPlanName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheYear() {
        return theYear;
    }

    public void setTheYear(Long theYear) {
        this.theYear = theYear;
    }

    public Long getTheSeason() {
        return theSeason;
    }

    public void setTheSeason(Long theSeason) {
        this.theSeason = theSeason;
    }
}
