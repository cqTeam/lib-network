package com.cqteam.networklib.http.okhttp

import com.cqteam.networklib.NetWorkManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 *
 * @Description:    OkHttpClient提供者 单利模式
 * @Author:         koloces
 * @CreateDate:     2020/6/10 14:43
 */
object OkHttpClientProvider {
    private var mClient: OkHttpClient? = null

    fun getClient() :OkHttpClient {
        if (mClient == null) {
            synchronized(OkHttpClientProvider) {
                if (mClient == null) {
                    mClient =
                        createClient()
                }
            }
        }
        return mClient!!
    }

    private fun createClient():OkHttpClient{


        val logInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    NetWorkManager.getConfig().getLogger().log(message)
                }
            })
        //log打印级别，决定了log显示的详细程度
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder() //添加拦截器
            .addInterceptor(logInterceptor)
            .addInterceptor(PublicParamsInterceptor()) //设置一个自动管理cookies的管理器
            .cookieJar(CookiesManager()) //添加网络连接器
            //设置请求读写的超时时间
            .connectTimeout(NetWorkManager.getConfig().connectTimeout, TimeUnit.SECONDS)
            .writeTimeout(NetWorkManager.getConfig().writeTimeout, TimeUnit.SECONDS)
            .readTimeout(NetWorkManager.getConfig().readTimeout, TimeUnit.SECONDS)
            .cache(Cache(NetWorkManager.getConfig().cacheDirectory, 10 * 1024 * 1024)) //设置缓存
            .retryOnConnectionFailure(true) //自动重试
            .build()
    }
}