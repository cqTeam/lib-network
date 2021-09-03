package com.cqteam.network

import com.cqteam.networklib.http.net.BaseResponse

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2021/9/3 11:51
 */
class APIResponse<T>(val code:Int,val msg:String,val data:T,val isAlertMsg : Boolean) : BaseResponse<T>() {
    override fun isSuccess() = code == 0

    override fun getResponseData() = data

    override fun getResponseCode() = code

    override fun getResponseMsg() = msg

    override fun handleError() {
        if (code == 0){
            Print.e("0的错误")
        }
    }
}