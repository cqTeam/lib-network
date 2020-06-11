package com.cqteam.networklib.http.okhttp

import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.okhttp.cookie.PersistentCookieStore
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 *
 * @Description:    网络请求缓存拦截器
 * @Author:         koloces
 * @CreateDate:     2020/6/10 15:46
 */
internal class CookiesManager : CookieJar {
    private val cooks: PersistentCookieStore = PersistentCookieStore(NetWorkManager.getContent())
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cooks[url]
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isNotEmpty()) {
            for (item in cookies) {
                cooks.add(url, item)
            }
        }
    }
}