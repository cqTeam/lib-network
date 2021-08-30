package com.cqteam.networklib.http.retrofit

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
internal object RetrofitUtils {
    private val retorfitMap = HashMap<String, Retrofit>()

    /**
     * 防止可能有多个BaseUr
     * @param baseService
     */
    private fun putApi(serviceUrl: String) : Retrofit{
        var retrofit = retorfitMap[serviceUrl]
        if (retrofit != null) return retrofit
        retrofit = Retrofit.Builder()
            .baseUrl(serviceUrl)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClientProvider.getClient())
            .build()
        retorfitMap[serviceUrl] = retrofit
        return retrofit
    }


    /**
     * 根据baseService获取
     * @param baseService
     * @return
     */
    fun getRetrofit(baseService: String): Retrofit {
        return retorfitMap[baseService] ?: return putApi(baseService)
    }
}