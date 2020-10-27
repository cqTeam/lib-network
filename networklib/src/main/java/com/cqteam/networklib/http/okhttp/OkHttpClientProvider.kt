package com.cqteam.networklib.http.okhttp

import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.okhttp.cache.CacheInterceptor
import com.cqteam.networklib.http.utils.NetPrintUtil
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
internal object OkHttpClientProvider {
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
                    NetPrintUtil.print(message)
                }
            })
        //log打印级别，决定了log显示的详细程度
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(PublicParamsInterceptor()) //公共请求
            .addInterceptor(logInterceptor)//添加拦截器
            //设置请求读写的超时时间
            .connectTimeout(NetWorkManager.getConfig().connectTimeout, TimeUnit.SECONDS)
            .writeTimeout(NetWorkManager.getConfig().writeTimeout, TimeUnit.SECONDS)
            .readTimeout(NetWorkManager.getConfig().readTimeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)


        if (NetWorkManager.getConfig().enableCache){
            clientBuilder.cache(Cache(NetWorkManager.getConfig().cacheDirectory, NetWorkManager.getConfig().cacheSize)) //设置缓存
                .addNetworkInterceptor(CacheInterceptor())
        }

        return clientBuilder.build() //自动重试
    }
}