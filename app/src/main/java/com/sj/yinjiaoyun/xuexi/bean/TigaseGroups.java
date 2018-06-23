package com.sj.yinjiaoyun.xuexi.bean;


import java.util.List;

/**
 * Created by wanzhiying on 2017/3/8.
 */
public class TigaseGroups {

   private TigaseGroupVO tigaseGroupVO;
   private List<TigaseGroupVO> childTigaseGroupVOs;

    public TigaseGroupVO getTigaseGroupVO() {
        return tigaseGroupVO;
    }

    public void setTigaseGroupVO(TigaseGroupVO tigaseGroupVO) {
        this.tigaseGroupVO = tigaseGroupVO;
    }

    public List<TigaseGroupVO> getChildTigaseGroupVOs() {
        return childTigaseGroupVOs;
    }

    public void setChildTigaseGroupVOs(List<TigaseGroupVO> childTigaseGroupVOs) {
        this.childTigaseGroupVOs = childTigaseGroupVOs;
    }
}
