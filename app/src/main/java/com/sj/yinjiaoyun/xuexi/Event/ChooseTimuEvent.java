package com.sj.yinjiaoyun.xuexi.Event;

import com.sj.yinjiaoyun.xuexi.domain.TiMu;

/**
 * Created by ${沈军 961784535@qq.com} on 2017/10/19.
 */
public class ChooseTimuEvent {
    private TiMu timu;

    public ChooseTimuEvent(TiMu timu) {
        this.timu = timu;
    }

    public TiMu getTimu() {
        return timu;
    }
}
