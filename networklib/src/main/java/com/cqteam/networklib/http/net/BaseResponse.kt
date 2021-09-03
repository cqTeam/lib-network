package com.cqteam.networklib.http.net

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2021/9/3 11:31
 */
abstract class BaseResponse<T> {
    //抽象方法，用户的基类继承该类时，需要重写该方法
    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

    //处理错误情况的逻辑,根据错误码处理不同逻辑
    abstract fun handleError()
}