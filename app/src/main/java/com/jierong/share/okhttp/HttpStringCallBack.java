package com.jierong.share.okhttp;

/**
 * @author: qingf
 * @date: 2016/6/3.
 * @desc: String类型接口请求回调
 */
public interface HttpStringCallBack {
	
	/**
     * 请求成功的回调
     * @param result 返回结果
     */
    public void onSuccess(Object result);

    /**
     * 请求失败的回调
     * @param code  错误标识码
     * @param msg   错误描述
     */
    public void onFailure(int code, String msg);
    
//    /**
//	 * 请求前的回调
//	 */
//	public void onBefore();
//    
//    /**
//     * 请求后的回调
//     */
//    public void onAfter();
}
