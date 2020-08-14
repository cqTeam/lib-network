package com.cqteam.networklib.http.net
import com.cqteam.networklib.NetWorkManager
import kotlinx.coroutines.*
import retrofit2.Call

/**
 * 每个项目里应该都不一样，这里统一处理错误操作，如登录失效等操作
 */
@Deprecated("过时不好用")
abstract class NetWorkHandle {

    protected fun<T> doJob(call : Call<T>, listener : HttpResultListener<T>?){
        doJob(call,false,listener)
    }
    /**
     * call 请求
     * isShowLoading 是否显示loading
     * listener 监听
     */
    protected fun<T> doJob(call : Call<T>, isShowLoading : Boolean, listener : HttpResultListener<T>?){
        GlobalScope.launch(Dispatchers.Main) {
            if (isShowLoading){
                NetWorkManager.getConfig().loadingProvider?.showLoading()
            }
            listener?.onStart()
            val await = withContext(Dispatchers.IO) {
                call.execute()
            }
            listener?.onFinish()
            if (isShowLoading){
                NetWorkManager.getConfig().loadingProvider?.dismissLoading()
            }
            if (await.isSuccessful) {
                val result = await.body()
                handleResult(result as T,listener)
            } else {
                listener?.onFailed(await.code(),await.message())
            }
        }
    }

    /**
     * 统一处理结果的方法，最好在项目当中新建一个base
     */
    abstract fun<T> handleResult(result : T,listener : HttpResultListener<T>?)
}