package com.sj.yinjiaoyun.xuexi.bean;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/20.
 * 课程的实体类
 */
public class CoureseBean {


    /**
     * state : null
     * success : true
     * message :
     * data : {"courseSchedules":[{"id":2044,"courseCode":null,"courseName":"小学数学","courseLogoUrl":"http://139.196.255.175/group1/M00/00/0B/i8T_r1hXrw-AMsyMAACV-1L-h60611.jpg","coursePercent":"0%","totalLearnTime":86,"teacherName":"刘子橙","totalScore":0,"courseCredit":2,"courseId":144},{"id":2045,"courseCode":null,"courseName":"小学美术","courseLogoUrl":"http://139.196.255.175/group1/M00/00/0B/i8T_r1hXrw-AMsyMAACV-1L-h60611.jpg","coursePercent":"0%","totalLearnTime":168,"teacherName":"刘子橘","totalScore":0,"courseCredit":4,"courseId":145}],"status":4}
     */

    private Object state;
    private boolean success;
    private String message;
    private DataBean data;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * courseSchedules : [{"id":2044,"courseCode":null,"courseName":"小学数学","courseLogoUrl":"http://139.196.255.175/group1/M00/00/0B/i8T_r1hXrw-AMsyMAACV-1L-h60611.jpg","coursePercent":"0%","totalLearnTime":86,"teacherName":"刘子橙","totalScore":0,"courseCredit":2,"courseId":144},{"id":2045,"courseCode":null,"courseName":"小学美术","courseLogoUrl":"http://139.196.255.175/group1/M00/00/0B/i8T_r1hXrw-AMsyMAACV-1L-h60611.jpg","coursePercent":"0%","totalLearnTime":168,"teacherName":"刘子橘","totalScore":0,"courseCredit":4,"courseId":145}]
         * status : 4
         */

        private int status;
        private List<CourseSchedulesBean> courseSchedules;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<CourseSchedulesBean> getCourseSchedules() {
            return courseSchedules;
        }

        public void setCourseSchedules(List<CourseSchedulesBean> courseSchedules) {
            this.courseSchedules = courseSchedules;
        }

        public static class CourseSchedulesBean {
            /**
             * id : 2044
             * courseCode : null
             * courseName : 小学数学
             * courseLogoUrl : http://139.196.255.175/group1/M00/00/0B/i8T_r1hXrw-AMsyMAACV-1L-h60611.jpg
             * coursePercent : 0%
             * totalLearnTime : 86
             * teacherName : 刘子橙
             * totalScore : 0
             * courseCredit : 2
             * courseId : 144
             */

            private int id;
            private Object courseCode;
            private String courseName;
            private String courseLogoUrl;
            private String coursePercent;
            private int totalLearnTime;
            private String teacherName;
            private int totalScore;
            private int courseCredit;
            private int courseId;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getCourseCode() {
                return courseCode;
            }

            public void setCourseCode(Object courseCode) {
                this.courseCode = courseCode;
            }

            public String getCourseName() {
                return courseName;
            }

            public void setCourseName(String courseName) {
                this.courseName = courseName;
            }

            public String getCourseLogoUrl() {
                return courseLogoUrl;
            }

            public void setCourseLogoUrl(String courseLogoUrl) {
                this.courseLogoUrl = courseLogoUrl;
            }

            public String getCoursePercent() {
                return coursePercent;
            }

            public void setCoursePercent(String coursePercent) {
                this.coursePercent = coursePercent;
            }

            public int getTotalLearnTime() {
                return totalLearnTime;
            }

            public void setTotalLearnTime(int totalLearnTime) {
                this.totalLearnTime = totalLearnTime;
            }

            public String getTeacherName() {
                return teacherName;
            }

            public void setTeacherName(String teacherName) {
                this.teacherName = teacherName;
            }

            public int getTotalScore() {
                return totalScore;
            }

            public void setTotalScore(int totalScore) {
                this.totalScore = totalScore;
            }

            public int getCourseCredit() {
                return courseCredit;
            }

            public void setCourseCredit(int courseCredit) {
                this.courseCredit = courseCredit;
            }

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }
        }
    }
}
