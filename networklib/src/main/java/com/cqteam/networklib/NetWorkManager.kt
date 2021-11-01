package com.cqteam.networklib

import android.annotation.SuppressLint
import android.content.Context
import com.cqteam.networklib.http.retrofit.RetrofitUtils

/**
 *
 * @Description:    网络请求管理
 * @Author:         koloces
 * @CreateDate:     2020/6/10 14:43
 */
object NetWorkManager {
    @SuppressLint("StaticFieldLeak")
    private lateinit var mConfig : NetWorkConfig
    private lateinit var baseApi:String

    /**
     * context
     * config
     * ip       请求IP
     */
    fun init(config: NetWorkConfig,baseApi:String){
        mConfig = config
        if (baseApi.isNotEmpty()) {
            NetWorkManager.baseApi = baseApi
        }
    }

    /**
     * context
     * ip       请求IP
     */
    fun init(context : Context,ip:String){
        init(NetWorkConfig.Builder(context).build(),ip)
    }

    fun init(context : Context){
        init(context,"")
    }

    fun getConfig():NetWorkConfig{
        return mConfig
    }

    fun getContent():Context{
        return mConfig.mContext
    }

    /**
     * 清除缓存
     */
    fun cleanCache() :Boolean{
        return NetFileUtils.deleteDir(mConfig.cacheDirectory)
    }


    fun<T> create(clazz: Class<T>):T {
        if (baseApi.trim().isNotEmpty()){
            return RetrofitUtils.getRetrofit(baseApi).create(clazz)
        } else {
            throw Exception("没有添加baseApi,请用带api参数的create方法！！")
        }
    }

    fun <T> create(api:String,clazz: Class<T>):T{
        return RetrofitUtils.getRetrofit(api).create(clazz)
    }
}