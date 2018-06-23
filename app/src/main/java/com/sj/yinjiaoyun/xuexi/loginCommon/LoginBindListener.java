package com.sj.yinjiaoyun.xuexi.loginCommon;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * 作者：VanHua on 2018/5/14 15:39
 * <p>
 * 邮箱：Van_Hua@qq.com
 */
public interface LoginBindListener {
    void doComplete(SHARE_MEDIA share_media, int i, Map<String, String> map);
}
