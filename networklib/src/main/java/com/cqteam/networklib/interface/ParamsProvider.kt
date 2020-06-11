package com.cqteam.networklib.`interface`

/**
 *
 * @Description:    公共请求参数提供者
 * @Author:         koloces
 * @CreateDate:     2020/6/10 15:27
 */
interface ParamsProvider {
    fun bodyParams():HashMap<String,String>?
    fun headerParams():HashMap<String,String>?
}