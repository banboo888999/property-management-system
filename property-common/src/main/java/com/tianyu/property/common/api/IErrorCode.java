package com.tianyu.property.common.api;

/**
 * API错误码接口
 */
public interface IErrorCode {
    /**
     * 获取错误码
     */
    long getCode();

    /**
     * 获取错误信息
     */
    String getMessage();
}