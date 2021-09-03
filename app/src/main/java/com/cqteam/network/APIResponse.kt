package com.cqteam.network

import com.cqteam.networklib.http.net.BaseResponse

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2021/9/3 11:51
 */
class APIResponse<T>(val code: Int, val msg: String, val data: T, val isAlertMsg: Boolean) :
    BaseResponse<T>() {
    override fun isSuccess(): Boolean = code == 0

    override fun getResponseData(): T = data

    override fun getResponseCode(): Int = code

    override fun getResponseMsg(): String = msg

    override fun handleError() {
        if (code == 1) {
            Print.e("1的错误")
        }
    }
}