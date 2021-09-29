package com.cqteam.networklib.http.okhttp

import android.os.Build
import androidx.annotation.RequiresApi
import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.okhttp.cache.CacheInterceptor
import com.cqteam.networklib.http.utils.LongPrintUtil
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.net.Socket
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLEngine
import javax.net.ssl.X509ExtendedTrustManager
import javax.net.ssl.X509TrustManager

/**
 *
 * @Description:    OkHttpClient提供者 单利模式
 * @Author:         koloces
 * @CreateDate:     2020/6/10 14:43
 */
internal object OkHttpClientProvider {
    private var mClient: OkHttpClient? = null

    fun getClient(): OkHttpClient {
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

    private fun createClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                LongPrintUtil.print(message)
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

        if (NetWorkManager.getConfig().isNeedIgnoreHttps) {

            var manager: X509TrustManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                manager = object : X509ExtendedTrustManager() {
                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?,
                        socket: Socket?
                    ) {
                    }

                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?,
                        engine: SSLEngine?
                    ) {
                    }

                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?,
                        socket: Socket?
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?,
                        engine: SSLEngine?
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                }
            } else {
                manager = object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {}

                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?
                    ) {}

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            }

            clientBuilder
                .sslSocketFactory(
                    SSLSocketClient.getSSLSocketFactory(),manager
                )
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
        }

        if (NetWorkManager.getConfig().enableCache) {
            clientBuilder.cache(
                Cache(
                    NetWorkManager.getConfig().cacheDirectory,
                    NetWorkManager.getConfig().cacheSize
                )
            ) //设置缓存
                .addNetworkInterceptor(CacheInterceptor())
        }

        return clientBuilder.build() //自动重试
    }
}