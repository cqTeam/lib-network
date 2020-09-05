package com.cqteam.networklib.`interface`

/**
 *
 * @Description:    公共请求参数提供者
 * @Author:         koloces
 * @CreateDate:     2020/6/10 15:27
 */
interface ParamsProvider {
    /**
     * 传入参数为请求所携带的body（可能加密需要用到）
     */
    fun bodyParams(params : HashMap<String,String>):HashMap<String,String>?

    /**
     * 传入参数为请求所携带的body（可能加密需要用到）
     */
    fun headerParams(params : HashMap<String,String>):HashMap<String,String>?
}