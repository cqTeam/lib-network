package com.cqteam.networklib.http.okhttp

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.webkit.WebSettings
import androidx.annotation.RequiresPermission
import com.cqteam.networklib.NetWorkManager
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.*

/**
 *
 * @Description:    自定义公共请求体的拦截器
 * @Author:         koloces
 * @CreateDate:     2020/6/10 15:25
 */
internal class PublicParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!isNetworkReachable(NetWorkManager.getContent())!!) {
            NetWorkManager.getConfig().getLogger().log("暂无网络可用")
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE) //无网络时只从缓存中读取
                .build()
        }
        val RequestBuilder = request.newBuilder()
        var build: Request
        val authorization = RequestBuilder
            .method(request.method, request.body)
            .removeHeader("User-Agent")
            .addHeader("User-Agent", getUserAgent())

        //添加公共请求头
        var headerParams = NetWorkManager.getConfig().paramsProvider?.headerParams()
        if (headerParams == null) {
            headerParams = HashMap()
        }
        val headerIterator: Iterator<Map.Entry<String, String>> = headerParams.entries.iterator()
        while (headerIterator.hasNext()) {
            val next = headerIterator.next()
            val key = next.key
            val value = next.value
            authorization.addHeader(key, value)
        }

        //添加公共请求体
        var bodyParams = NetWorkManager.getConfig().paramsProvider?.bodyParams()
        if (bodyParams == null) {
            bodyParams = HashMap()
        }
        val bodyIterator: Iterator<Map.Entry<String, String>> = bodyParams.entries.iterator()
        val bodyBuilder = request.url.newBuilder()
        while (bodyIterator.hasNext()) {
            val next = bodyIterator.next()
            val key = next.key
            val value = next.value
            bodyBuilder.addQueryParameter(key, value)
        }

        val httpUrl = bodyBuilder.build()
        build = authorization.url(httpUrl).build()
        return chain.proceed(build)
    }

    private fun getUserAgent(): String {
        var userAgent: String? = ""
        userAgent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                WebSettings.getDefaultUserAgent(NetWorkManager.getContent())
            } catch (e: Exception) {
                System.getProperty("http.agent")
            }
        } else {
            System.getProperty("http.agent")
        }
        val sb = StringBuffer()
        var i = 0
        val length = userAgent!!.length
        while (i < length) {
            val c = userAgent[i]
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", c.toInt()))
            } else {
                sb.append(c)
            }
            i++
        }
        return sb.toString()
    }

    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetworkReachable(context: Context): Boolean? {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val current = cm.activeNetworkInfo ?: return false
        return current.isAvailable
    }
}