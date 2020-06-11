package com.cqteam.networklib

import android.content.Context
import com.cqteam.networklib.http.retrofit.RetrofitUtils
import retrofit2.Retrofit

/**
 *
 * @Description:    网络请求管理
 * @Author:         koloces
 * @CreateDate:     2020/6/10 14:43
 */
object NetWorkManager {
    private lateinit var mContext : Context
    private lateinit var mConfig : NetWorkConfig

    /**
     * context
     * config
     * ip       请求IP
     */
    fun init(config: NetWorkConfig,ip:String){
        mContext = config.mContext
        mConfig = config
        if (ip.isNotEmpty()) {
            putBaseApi(ip)
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
        return mContext
    }

    /**
     * 添加网络请求ip（如果项目中需要多个，则可以添加多个）
     */
    fun putApi(api : String){
        RetrofitUtils.putApi(api)
    }

    /**
     * 添加网络请求ip
     */
    fun putBaseApi(api : String){
        RetrofitUtils.putBaseApi(api)
    }

    /**
     * 获取Retrofit实体
     */
    fun getRetrofit(): Retrofit{
        return RetrofitUtils.getRetrofit()
    }
    /**
     * 根据ip获取Retrofit实体
     */
    fun getRetrofit(service: String?): Retrofit{
        return RetrofitUtils.getRetrofit(service)
    }

    fun<T> create(clazz: Class<T>):T {
        return RetrofitUtils.getRetrofit().create(clazz)
    }

    fun <T> create(api:String,clazz: Class<T>):T{
        return RetrofitUtils.getRetrofit(api).create(clazz)
    }
}