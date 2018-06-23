package com.sj.yinjiaoyun.xuexi.Event;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/4/14.
 */
public class ColleageEvent {
    private Long id;
    private String OrganizationName;

    public ColleageEvent(Long id, String organizationName) {
        this.id = id;
        OrganizationName = organizationName;
    }

    public Long getId() {
        return id;
    }

    public String getOrganizationName() {
        return OrganizationName;
    }
}
