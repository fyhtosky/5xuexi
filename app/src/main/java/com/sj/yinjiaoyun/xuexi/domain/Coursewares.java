package com.sj.yinjiaoyun.xuexi.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/8/30.
 * 接口9.课程详情-章节课时列表及学习情况
 */
public class Coursewares implements Parcelable {
    Long	id;                         //	是	目录-章节-课时Id
    Long	courseId;                   //	是	目录-章节-课时的课程id
    Byte	isGroup;                    //	是	目录-章节-是否课件组 0:否 1:是
    Long	groupId;                    //  是	目录-章节-课件组ID
    String	coursewareName;             //	是	目录-章节-课时名称
    Long	coursewareTime;             //	是	目录-章节-课时时长
    Integer	coursewareOrder;            //	是	目录-章节-课时顺序
    String	coursewareVideoUrl;         //	是	目录-章节-课时视频地址
    Byte coursewareAuditStatus;         //  是  课件审核状态      0：未审核 1：已审核  2：审核不通过
    Byte isLink;                        //  是  是否为外链地址    0：不是   1：是  （0默认 1：7牛 2：人大）
    boolean	finishLearn;                //	是	目录-章节-课时视频是否学习完
    boolean	isfinish;                   //	是	目录-章节-课时否学习完
    Byte	homeworkState;              //	是	完成作业状态  0：没有作业  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期
    Long	homeworkExamPaperReleaseId; //	不是	作业试卷的
    Byte	examState;                  //	是	完成测验状态  0：没有测验  1：未做   2：已做未批改  3、已做已批改  4：尚未开始考试  5：已过期
    Long	testExamPaperReleaseId;     //		测验试卷的
    Long    learnTime;                  //      学习时长
    Byte    videoState;                  //  视频播放状态（0：没有播放；1：进行中；2播放完成）


    protected Coursewares(Parcel in) {
        coursewareName = in.readString();
        coursewareVideoUrl = in.readString();
        finishLearn = in.readByte() != 0;
        isfinish = in.readByte() != 0;
        homeworkState=in.readByte();
        examState=in.readByte();
        try{
            id=in.readLong();
            homeworkExamPaperReleaseId=in.readLong();
            testExamPaperReleaseId=in.readLong();
        }catch (Exception e){
            Log.i("", "Coursewares: ");
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(coursewareName);
        dest.writeString(coursewareVideoUrl);
        dest.writeByte((byte) (finishLearn ? 1 : 0));
        dest.writeByte((byte) (isfinish ? 1 : 0));
        dest.writeByte(homeworkState);
        dest.writeByte(examState);
        try{
            dest.writeLong(id);
            if(homeworkExamPaperReleaseId!=null)
                dest.writeLong(homeworkExamPaperReleaseId);
            if(testExamPaperReleaseId!=null)
                dest.writeLong(testExamPaperReleaseId);
        }catch (Exception e){
          e.getLocalizedMessage();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Coursewares> CREATOR = new Creator<Coursewares>() {
        @Override
        public Coursewares createFromParcel(Parcel in) {
            return new Coursewares(in);
        }

        @Override
        public Coursewares[] newArray(int size) {
            return new Coursewares[size];
        }
    };

    public Long getLearnTime() {
        return learnTime;
    }

    public void setLearnTime(Long learnTime) {
        this.learnTime = learnTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Byte getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Byte isGroup) {
        this.isGroup = isGroup;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getCoursewareName() {
        return coursewareName;
    }

    public void setCoursewareName(String coursewareName) {
        this.coursewareName = coursewareName;
    }

    public Long getCoursewareTime() {
        return coursewareTime;
    }

    public void setCoursewareTime(Long coursewareTime) {
        this.coursewareTime = coursewareTime;
    }

    public Integer getCoursewareOrder() {
        return coursewareOrder;
    }

    public void setCoursewareOrder(Integer coursewareOrder) {
        this.coursewareOrder = coursewareOrder;
    }

    public String getCoursewareVideoUrl() {
        return coursewareVideoUrl;
    }

    public void setCoursewareVideoUrl(String coursewareVideoUrl) {
        this.coursewareVideoUrl = coursewareVideoUrl;
    }

    public Byte getCoursewareAuditStatus() {
        return coursewareAuditStatus;
    }

    public void setCoursewareAuditStatus(Byte coursewareAuditStatus) {
        this.coursewareAuditStatus = coursewareAuditStatus;
    }

    public Byte getIsLink() {
        return isLink;
    }

    public void setIsLink(Byte isLink) {
        this.isLink = isLink;
    }

    public boolean isFinishLearn() {
        return finishLearn;
    }

    public void setFinishLearn(boolean finishLearn) {
        this.finishLearn = finishLearn;
    }

    public boolean isfinish() {
        return isfinish;
    }

    public void setIsfinish(boolean isfinish) {
        this.isfinish = isfinish;
    }

    public Byte getHomeworkState() {
        return homeworkState;
    }

    public void setHomeworkState(Byte homeworkState) {
        this.homeworkState = homeworkState;
    }

    public Long getHomeworkExamPaperReleaseId() {
        return homeworkExamPaperReleaseId;
    }

    public void setHomeworkExamPaperReleaseId(Long homeworkExamPaperReleaseId) {
        this.homeworkExamPaperReleaseId = homeworkExamPaperReleaseId;
    }

    public Byte getExamState() {
        return examState;
    }

    public void setExamState(Byte examState) {
        this.examState = examState;
    }

    public Long getTestExamPaperReleaseId() {
        return testExamPaperReleaseId;
    }

    public void setTestExamPaperReleaseId(Long testExamPaperReleaseId) {
        this.testExamPaperReleaseId = testExamPaperReleaseId;
    }

    public Byte getVideoState() {
        return videoState;
    }

    public void setVideoState(Byte videoState) {
        this.videoState = videoState;
    }

    @Override
    public String toString() {
        return "Coursewares{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", isGroup=" + isGroup +
                ", groupId=" + groupId +
                ", coursewareName='" + coursewareName + '\'' +
                ", coursewareTime=" + coursewareTime +
                ", coursewareOrder=" + coursewareOrder +
                ", coursewareVideoUrl='" + coursewareVideoUrl + '\'' +
                ", coursewareAuditStatus=" + coursewareAuditStatus +
                ", isLink=" + isLink +
                ", finishLearn=" + finishLearn +
                ", isfinish=" + isfinish +
                ", homeworkState=" + homeworkState +
                ", homeworkExamPaperReleaseId=" + homeworkExamPaperReleaseId +
                ", examState=" + examState +
                ", testExamPaperReleaseId=" + testExamPaperReleaseId +
                ", learnTime=" + learnTime +
                ", videoState=" + videoState +
                '}';
    }
}
