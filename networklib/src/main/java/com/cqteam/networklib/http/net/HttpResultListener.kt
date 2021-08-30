package com.cqteam.networklib.http.net
import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.ThreadUtils
import kotlinx.coroutines.Job

@Deprecated("弃用，项目里自己实现")
interface HttpResultListener <T> {
    /**
     * 需要显示做操作时重写该方法
     * 例如网络请求需要显示loading
     */
    fun onStart() {
    }

    /**
     * 请求成功
     * @param result
     */
    fun onSuccess(result: T)

    /**
     * 请求失败
     * @param msg 返回失败信息
     * -999是我自定义的错误,不用管直接看msg
     */
    fun onFailed(errCode: Int, msg: String?) {
        ThreadUtils.runOnUiThread {
            NetWorkManager.getConfig().toastProvider?.toast(msg)
        }
    }

    /**
     * 同 onStart();
     */
    fun onFinish() {
    }

    /**
     * 获取网络请求的job以方便注销,防止内存泄漏
     * @param job
     */
    fun getJob(job : Job)
}