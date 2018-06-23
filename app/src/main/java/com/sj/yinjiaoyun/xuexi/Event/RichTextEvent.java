package com.sj.yinjiaoyun.xuexi.Event;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/11/22.
 * 富文本表示事件传递
 */
public class RichTextEvent {
    private int id;
    private String content;

    public RichTextEvent(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
