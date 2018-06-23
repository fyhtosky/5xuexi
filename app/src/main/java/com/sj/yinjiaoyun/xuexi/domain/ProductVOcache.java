package com.sj.yinjiaoyun.xuexi.domain;

/**
 * Created by ${沈军 961784535@qq.com} on 2016/9/8.
 */
public class ProductVOcache {
    String flag;
    ProductVO vo;

    public ProductVOcache(String flag, ProductVO vo) {
        this.flag = flag;
        this.vo = vo;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ProductVO getVo() {
        return vo;
    }

    public void setVo(ProductVO vo) {
        this.vo = vo;
    }
}
