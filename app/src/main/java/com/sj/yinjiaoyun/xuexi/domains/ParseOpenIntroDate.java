package com.sj.yinjiaoyun.xuexi.domains;

import com.sj.yinjiaoyun.xuexi.domain.OpenCourseVO;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/12/19.
 * 解析公开课详情
 */
public class ParseOpenIntroDate {
    OrderCommentCounterVO orderCommentCounterVO;
    OpenCourseVO openCourse;

    public OrderCommentCounterVO getOrderCommentCounterVO() {
        return orderCommentCounterVO;
    }

    public void setOrderCommentCounterVO(OrderCommentCounterVO orderCommentCounterVO) {
        this.orderCommentCounterVO = orderCommentCounterVO;
    }

    public OpenCourseVO getOpenCourse() {
        return openCourse;
    }

    public void setOpenCourse(OpenCourseVO openCourse) {
        this.openCourse = openCourse;
    }
}
