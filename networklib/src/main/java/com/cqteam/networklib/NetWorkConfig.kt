package com.cqteam.networklib

import android.content.Context
import android.util.Log
import com.cqteam.networklib.`interface`.LoadingProvider
import com.cqteam.networklib.`interface`.LoggerProvider
import com.cqteam.networklib.`interface`.ParamsProvider
import com.cqteam.networklib.`interface`.ToastProvider
import java.io.File

/**
 *
 * @Description:    配置类
 * @Author:         koloces
 * @CreateDate:     2020/6/10 14:43
 */
class NetWorkConfig private constructor(builder: Builder) {

    /**
     * 是否是debug 控制是否打印
     */
    var isDebug = true

    //请求响应时间
    var connectTimeout = 30L

    //写入响应时间
    var writeTimeout = 30L

    //读取响应时间
    var readTimeout = 30L

    /**
     * 默认的打印，如果外部不提供，内部提自带一个 tag 为 NetWorkHttp 的打印
     * 如果是debug模式自动打印控制台
     */
    private var logger: LoggerProvider

    var paramsProvider: ParamsProvider? = null

    var cacheDirectory: File

    var toastProvider: ToastProvider? = null
    var loadingProvider: LoadingProvider? = null

    //是否缓存
    internal var enableCache = true

    //缓存文件夹大小
    internal var cacheSize: Long = 10 * 1024 * 1024

    //缓存时间
    internal var maxAge: Long = 60

    //是否需要忽略HTTPS证书
    internal var isNeedIgnoreHttps = false;

    lateinit var mContext: Context

    init {
        isDebug = builder.isDebug
        logger = if (builder.logger != null) {
            builder.logger!!
        } else {
            object : LoggerProvider {
                override fun log(message: String) {
                    if (isDebug) {
                        Log.e("NetWorkHttp", message)
                    }
                }
            }
        }
        connectTimeout = builder.connectTimeout
        writeTimeout = builder.writeTimeout
        readTimeout = builder.readTimeout
        paramsProvider = builder.paramsProvider
        cacheDirectory = builder.cacheDirectory!!
        toastProvider = builder.toastProvider
        loadingProvider = builder.loadingProvider
        enableCache = builder.enableCache
        cacheSize = builder.cacheSize
        isNeedIgnoreHttps = builder.isNeedIgnoreHttps
        mContext = builder.mContext
    }


    fun getLogger(): LoggerProvider {
        return logger
    }

    class Builder(context: Context) {
        internal var isDebug = true
        internal var mContext: Context = context
        internal var logger: LoggerProvider? = null

        //请求响应时间
        internal var connectTimeout = 30L

        //写入响应时间
        internal var writeTimeout = 30L

        //读取响应时间
        internal var readTimeout = 30L

        internal var paramsProvider: ParamsProvider? = null

        internal var cacheDirectory: File? = null

        internal var toastProvider: ToastProvider? = null
        internal var loadingProvider: LoadingProvider? = null

        //是否缓存
        internal var enableCache = true

        //缓存文件夹大小
        internal var cacheSize: Long = 10 * 1024 * 1024

        //缓存时间
        internal var maxAge: Int = 60

        //是否需要忽略HTTPS证书
        internal var isNeedIgnoreHttps: Boolean = false

        /**
         * 添加吐司
         */
        fun addToastProvider(toastProvider: ToastProvider?): Builder {
            this.toastProvider = toastProvider
            return this
        }

        /**
         * 是否需要忽略HTTPS证书
         */
        fun isNeedIgnoreHttps(boolean: Boolean) : Builder{
            this.isNeedIgnoreHttps = boolean
            return this
        }

        /**
         * 添加loading
         */
        @Deprecated("弃用，项目里自己实现")
        fun addLoadingProvider(loadingProvider: LoadingProvider?): Builder {
            this.loadingProvider = loadingProvider
            return this
        }

        /**
         * 是否起用缓存(仅仅缓存GET请求)
         */
        fun enableCache(enable: Boolean): Builder {
            this.enableCache = enable
            return this
        }

        /**
         * 缓存文件夹大小（默认 10M = 10 * 1024 *1024）
         */
        fun setCacheSize(size: Long): Builder {
            this.cacheSize = size
            return this
        }

        /**
         * 缓存时间(单位 秒 默认60秒)
         */
        fun setMaxAge(maxAge: Int): Builder {
            this.maxAge = maxAge
            return this
        }

        /**
         * 网络缓存文件夹（默认[Context.getCacheDir]）
         */
        fun addCacheDirectory(cacheDirectory: File): Builder {
            this.cacheDirectory = cacheDirectory
            return this
        }

        /**
         * 请求的公共请求参数
         */
        fun addParamsProvider(provider: ParamsProvider): Builder {
            this.paramsProvider = provider
            return this
        }

        fun addLogger(logger: LoggerProvider): Builder {
            this.logger = logger
            return this
        }

        fun addConnectTimeout(time: Long): Builder {
            connectTimeout = time
            return this
        }

        fun addWriteTimeout(time: Long): Builder {
            writeTimeout = time
            return this
        }

        fun addReadTimeout(time: Long): Builder {
            readTimeout = time
            return this
        }

        /**
         * 默认true
         */
        fun isDebug(isDebug: Boolean): Builder {
            this.isDebug = isDebug
            return this
        }

        fun build(): NetWorkConfig {
            if (cacheDirectory == null) {
                cacheDirectory =
                    File(mContext.applicationContext.cacheDir.absolutePath, "NetWorkCacheFile")
            }
            return NetWorkConfig(this)
        }
    }
}