package com.sj.yinjiaoyun.xuexi.bean;

import java.util.List;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/9/22.
 * 选修课程的实体类
 */
public class OptionalCourseBean {

    /**
     * state : null
     * success : true
     * message :
     * data : {"needSelectCourses":[{"id":982,"productId":null,"collegeId":null,"teachingGroupId":null,"enrollPlanDirectionId":null,"productDirectionId":null,"enrolmentId":122,"courseId":null,"courseCredit":null,"suggestTime":null,"courseStartTime":null,"courseAttribute":1,"theFewYear":1,"operatorRoleId":null,"courseName":null,"enrollmentPlanName":null,"productDirectionName":null,"productName":null,"teacherName":null,"collegeName":null,"courseLogoUrl":null,"courseScheduleId":null},{"id":986,"productId":null,"collegeId":null,"teachingGroupId":null,"enrollPlanDirectionId":null,"productDirectionId":null,"enrolmentId":122,"courseId":null,"courseCredit":null,"suggestTime":null,"courseStartTime":null,"courseAttribute":1,"theFewYear":1,"operatorRoleId":null,"courseName":null,"enrollmentPlanName":null,"productDirectionName":null,"productName":null,"teacherName":null,"collegeName":null,"courseLogoUrl":null,"courseScheduleId":null}]}
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
        private List<NeedSelectCoursesBean> needSelectCourses;

        public List<NeedSelectCoursesBean> getNeedSelectCourses() {
            return needSelectCourses;
        }

        public void setNeedSelectCourses(List<NeedSelectCoursesBean> needSelectCourses) {
            this.needSelectCourses = needSelectCourses;
        }

        public static class NeedSelectCoursesBean {
            /**
             * id : 982
             * productId : null
             * collegeId : null
             * teachingGroupId : null
             * enrollPlanDirectionId : null
             * productDirectionId : null
             * enrolmentId : 122
             * courseId : null
             * courseCredit : null
             * suggestTime : null
             * courseStartTime : null
             * courseAttribute : 1
             * theFewYear : 1
             * operatorRoleId : null
             * courseName : null
             * enrollmentPlanName : null
             * productDirectionName : null
             * productName : null
             * teacherName : null
             * collegeName : null
             * courseLogoUrl : null
             * courseScheduleId : null
             */

            private int id;
            private Object productId;
            private Object collegeId;
            private Object teachingGroupId;
            private Object enrollPlanDirectionId;
            private Object productDirectionId;
            private int enrolmentId;
            private Object courseId;
            private Object courseCredit;
            private Object suggestTime;
            private Object courseStartTime;
            private int courseAttribute;
            private int theFewYear;
            private Object operatorRoleId;
            private String courseName;
            private Object enrollmentPlanName;
            private Object productDirectionName;
            private Object productName;
            private Object teacherName;
            private Object collegeName;
            private Object courseLogoUrl;
            private Object courseScheduleId;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public Object getProductId() {
                return productId;
            }

            public void setProductId(Object productId) {
                this.productId = productId;
            }

            public Object getCollegeId() {
                return collegeId;
            }

            public void setCollegeId(Object collegeId) {
                this.collegeId = collegeId;
            }

            public Object getTeachingGroupId() {
                return teachingGroupId;
            }

            public void setTeachingGroupId(Object teachingGroupId) {
                this.teachingGroupId = teachingGroupId;
            }

            public Object getEnrollPlanDirectionId() {
                return enrollPlanDirectionId;
            }

            public void setEnrollPlanDirectionId(Object enrollPlanDirectionId) {
                this.enrollPlanDirectionId = enrollPlanDirectionId;
            }

            public Object getProductDirectionId() {
                return productDirectionId;
            }

            public void setProductDirectionId(Object productDirectionId) {
                this.productDirectionId = productDirectionId;
            }

            public int getEnrolmentId() {
                return enrolmentId;
            }

            public void setEnrolmentId(int enrolmentId) {
                this.enrolmentId = enrolmentId;
            }

            public Object getCourseId() {
                return courseId;
            }

            public void setCourseId(Object courseId) {
                this.courseId = courseId;
            }

            public Object getCourseCredit() {
                return courseCredit;
            }

            public void setCourseCredit(Object courseCredit) {
                this.courseCredit = courseCredit;
            }

            public Object getSuggestTime() {
                return suggestTime;
            }

            public void setSuggestTime(Object suggestTime) {
                this.suggestTime = suggestTime;
            }

            public Object getCourseStartTime() {
                return courseStartTime;
            }

            public void setCourseStartTime(Object courseStartTime) {
                this.courseStartTime = courseStartTime;
            }

            public int getCourseAttribute() {
                return courseAttribute;
            }

            public void setCourseAttribute(int courseAttribute) {
                this.courseAttribute = courseAttribute;
            }

            public int getTheFewYear() {
                return theFewYear;
            }

            public void setTheFewYear(int theFewYear) {
                this.theFewYear = theFewYear;
            }

            public Object getOperatorRoleId() {
                return operatorRoleId;
            }

            public void setOperatorRoleId(Object operatorRoleId) {
                this.operatorRoleId = operatorRoleId;
            }

            public String getCourseName() {
                return courseName;
            }

            public void setCourseName(String courseName) {
                this.courseName = courseName;
            }

            public Object getEnrollmentPlanName() {
                return enrollmentPlanName;
            }

            public void setEnrollmentPlanName(Object enrollmentPlanName) {
                this.enrollmentPlanName = enrollmentPlanName;
            }

            public Object getProductDirectionName() {
                return productDirectionName;
            }

            public void setProductDirectionName(Object productDirectionName) {
                this.productDirectionName = productDirectionName;
            }

            public Object getProductName() {
                return productName;
            }

            public void setProductName(Object productName) {
                this.productName = productName;
            }

            public Object getTeacherName() {
                return teacherName;
            }

            public void setTeacherName(Object teacherName) {
                this.teacherName = teacherName;
            }

            public Object getCollegeName() {
                return collegeName;
            }

            public void setCollegeName(Object collegeName) {
                this.collegeName = collegeName;
            }

            public Object getCourseLogoUrl() {
                return courseLogoUrl;
            }

            public void setCourseLogoUrl(Object courseLogoUrl) {
                this.courseLogoUrl = courseLogoUrl;
            }

            public Object getCourseScheduleId() {
                return courseScheduleId;
            }

            public void setCourseScheduleId(Object courseScheduleId) {
                this.courseScheduleId = courseScheduleId;
            }
        }
    }
}
