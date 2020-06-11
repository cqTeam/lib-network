package com.cqteam.networklib.http.retrofit

import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.okhttp.OkHttpClientProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/6/10 15:53
 */
object RetrofitUtils {
    private val retorfitMap = HashMap<String, Retrofit>()
    private var mainService: String? = null

    /**
     * 防止可能有多个BaseUr
     * 第一个baseService是住要的,[RetrofitUtils]getRetrofit方法会返回这个主要的serivce的对象
     * @param baseService
     */
    fun putApi(baseService: String?) {
        if (mainService == null) {
            mainService = baseService
        }
        var retrofit = retorfitMap[baseService]
        if (retrofit != null) return
        retrofit = Retrofit.Builder()
            .baseUrl(baseService)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClientProvider.getClient())
            .build()
        retorfitMap[baseService!!] = retrofit
    }

    fun putBaseApi( baseApi : String){
        mainService = baseApi
        putApi(baseApi)
    }

    /**
     * 获取默认的
     * @return
     */
    fun getRetrofit(): Retrofit{
        return getRetrofit(mainService)
    }

    /**
     * 根据baseService获取
     * @param baseService
     * @return
     */
    fun getRetrofit(baseService: String?): Retrofit {
        val retrofit = retorfitMap[baseService]
        if (retrofit == null) {
            NetWorkManager.getConfig().getLogger().log("请先配置Service")
        }
        return retrofit!!
    }
}