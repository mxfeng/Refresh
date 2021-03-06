package com.jierong.share.mvp.model;

import com.jierong.share.okhttp.HttpStringCallBack;

/**
 * 周排行榜数据模型
 */
public interface IPhbWeekModel extends ModelListener {

    /**
     * 查询周排行班数据
     * @param uid           用户id
     * @param token         用户token
     * @param mCallBack     结果回调
     */
    public void loadData(String uid, String token, HttpStringCallBack mCallBack);

}
