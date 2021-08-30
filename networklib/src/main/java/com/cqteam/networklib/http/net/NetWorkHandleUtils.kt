package com.cqteam.networklib.http.net

import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.ThreadUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/8/14 16:56
 */
@Deprecated("弃用，项目里自己实现")
object NetWorkHandleUtils {
    fun <T> doJob(call: Call<T>, listener: HttpResultListener<T>?) {
        doJob(call, false, listener)
    }

    /**
     * call 请求
     * isShowLoading 是否显示loading
     * listener 监听
     */
    fun <T> doJob(call: Call<T>, isShowLoading: Boolean, listener: HttpResultListener<T>?) {
        GlobalScope.launch(Dispatchers.Main) {
            if (isShowLoading) {
                ThreadUtils.runOnUiThread {
                    NetWorkManager.getConfig().loadingProvider?.showLoading()
                }
            }
            listener?.onStart()
            val await = withContext(Dispatchers.IO) {
                call.execute()
            }
            if (await.isSuccessful) {
                val result = await.body()
                if (result != null) {
                    listener?.onSuccess(result)
                } else {
                    listener?.onFailed(-99, "body为空")
                }
            } else {
                listener?.onFailed(await.code(), await.message())
            }
            if (isShowLoading) {
                ThreadUtils.runOnUiThread {
                    NetWorkManager.getConfig().loadingProvider?.dismissLoading()
                }
            }
            listener?.onFinish()
        }
    }
}