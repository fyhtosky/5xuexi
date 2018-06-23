package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/11/10.
 * 课程表 顶部专业解析
 */
public class ProductDirection {

    long productDirectionId;//方向id
    String productDirectionDesc;//方向简介
    String productDirectionName;//方向名称

    public ProductDirection(long productDirectionId, String productDirectionDesc, String productDirectionName) {
        this.productDirectionId = productDirectionId;
        this.productDirectionDesc = productDirectionDesc;
        this.productDirectionName = productDirectionName;
    }

    public long getProductDirectionId() {
        return productDirectionId;
    }

    public void setProductDirectionId(long productDirectionId) {
        this.productDirectionId = productDirectionId;
    }

    public String getProductDirectionDesc() {
        return productDirectionDesc;
    }

    public void setProductDirectionDesc(String productDirectionDesc) {
        this.productDirectionDesc = productDirectionDesc;
    }

    public String getProductDirectionName() {
        return productDirectionName;
    }

    public void setProductDirectionName(String productDirectionName) {
        this.productDirectionName = productDirectionName;
    }
}
