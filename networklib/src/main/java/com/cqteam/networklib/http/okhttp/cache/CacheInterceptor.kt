package com.cqteam.networklib.http.okhttp.cache

import com.cqteam.networklib.NetWorkManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * @Description:    缓存拦截器
 * @Author:         koloces
 * @CreateDate:     2020/10/26 15:56
 */
class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var proceed = chain.proceed(request)
        proceed = proceed.newBuilder()
            .removeHeader("pragma")
            .header("Cache-Control","max-age=${NetWorkManager.getConfig().maxAge}")
            .build()
        return proceed
    }
}