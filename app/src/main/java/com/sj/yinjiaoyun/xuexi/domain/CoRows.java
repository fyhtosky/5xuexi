package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/29.
 * 专业课  公开课 详情 里面的评论接口
 * 最内层
 */
public class CoRows {

    Long id;//评价id
    Long orderId;//订单id
    Long objectId;//专业id
    Long commentType;//评价对象类型 0：专业 1公开课
    Long endUserId;//用户id
    Boolean anonymous; //是否匿名
    Byte stars;//星级评价（5级）
    String content;//评论内容
    Long createBy;//
    String createTime;//
    String updateBy;//
    String updateTime;//
    String endUserName;//名称
    String endUserAvatar;//头像

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Long getCommentType() {
        return commentType;
    }

    public void setCommentType(Long commentType) {
        this.commentType = commentType;
    }

    public Long getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(Long endUserId) {
        this.endUserId = endUserId;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Byte getStars() {
        return stars;
    }

    public void setStars(Byte stars) {
        this.stars = stars;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getEndUserName() {
        return endUserName;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
    }

    public String getEndUserAvatar() {
        return endUserAvatar;
    }

    public void setEndUserAvatar(String endUserAvatar) {
        this.endUserAvatar = endUserAvatar;
    }

    @Override
    public String toString() {
        return "CoRows{" +
                ", anonymous=" + anonymous +
                ", stars=" + stars +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", endUserName='" + endUserName + '\'' +
                ", endUserAvatar='" + endUserAvatar + '\'' +
                '}';
    }
}
