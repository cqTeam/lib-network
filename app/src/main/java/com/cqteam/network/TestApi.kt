package com.cqteam.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/10/26 15:11
 */
interface TestApi {
    @FormUrlEncoded
    @POST("start")
    fun startApp(@Field("device_no") device_no: String = "XHFH0010000221"): Call<BaseEntity<StartEntity>>


    @GET("weather")
    fun getWeather(): Call<BaseEntity<WeatherEntity>>

    @FormUrlEncoded
    @POST("start")
    suspend fun startApp2(@Field("device_no") device_no: String = "XHFH0010000221"): APIResponse<StartEntity>

}