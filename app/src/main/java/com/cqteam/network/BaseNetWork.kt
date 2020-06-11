package com.cqteam.network

import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.net.HttpResultListener
import com.cqteam.networklib.http.net.NetWorkHandle
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

class BaseNetWork : NetWorkHandle() {
    private var service = NetWorkManager.create(Api::class.java)

    interface Api{
        @GET("/common/getAppVersion")
        fun getAppVersion(): Call<JsonObject>
    }


    fun getAppVersion(listener : HttpResultListener<JsonObject>?) {
        val call  = service.getAppVersion()
        doJob(call,listener)
    }

    override fun <T> handleResult(result: T, listener: HttpResultListener<T>?) {

    }
}