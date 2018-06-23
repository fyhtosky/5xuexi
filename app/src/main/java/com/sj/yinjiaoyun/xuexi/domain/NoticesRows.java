package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/18.
 * 消息提醒解 析页面
 */
public class NoticesRows {

    long id;
    Byte noticeType;	//是	消息类型 0：提醒 1：通知
    Byte contentType;	//是	提醒通知具体类型  0：讨论组信息  1：答疑  2：批改作业  3：咨询  4：新建讨论组
    String noticeTitle;	//	是	消息标题
    String noticeContent;	//	是	消息标题
    Long sendId;	//	发送人id
    Long receiveId;	//	接收人
    Byte sendType;	//	是	发送人类型 0：前台用户 1：后台用户
    Byte receiveType;	//	是	接收人类型 0：前台用户 1：后台用户
    Byte isRead;	//	是	是否已读 0:未读 1：已读
    long createTime;//穿件时间戳
    long updateTime;//跟新时间戳

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Byte getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Byte noticeType) {
        this.noticeType = noticeType;
    }

    public Byte getContentType() {
        return contentType;
    }

    public void setContentType(Byte contentType) {
        this.contentType = contentType;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Long getSendId() {
        return sendId;
    }

    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public Byte getSendType() {
        return sendType;
    }

    public void setSendType(Byte sendType) {
        this.sendType = sendType;
    }

    public Byte getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(Byte receiveType) {
        this.receiveType = receiveType;
    }

    public Byte getIsRead() {
        return isRead;
    }

    public void setIsRead(Byte isRead) {
        this.isRead = isRead;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
