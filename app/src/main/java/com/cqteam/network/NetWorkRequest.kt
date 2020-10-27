package com.cqteam.network

import com.cqteam.networklib.http.net.HttpResultListener
import com.cqteam.networklib.http.net.NetWorkHandleUtils
import kotlinx.coroutines.Job
import retrofit2.Call

/**
 *
 * @Description:    的网络请求
 * @Author:         koloces
 * @CreateDate:     2020/9/1 10:40
 */
object NetWorkRequest {

    fun<T> request(call: Call<BaseEntity<T>>
                   ,success:((data : T) -> Unit)? = null
                   ,failed:((errCode: Int, msg: String?) -> Unit)? = null
                   ,start:(() -> Unit)? = null
                   ,finish:(() -> Unit)? = null
                   ,getJob:((job: Job) -> Unit)? = null
    ){
        request(call,false,success,failed,start,finish,getJob)
    }

    /**
     * 通用网络请求
     */
    fun<T> request(call: Call<BaseEntity<T>>
                   ,isShowLoading: Boolean = false
                   ,success:((data : T) -> Unit)? = null
                   ,failed:((errCode: Int, msg: String?) -> Unit)? = null
                   ,start:(() -> Unit)? = null
                   ,finish:(() -> Unit)? = null
                   ,getJob:((job: Job) -> Unit)? = null
    ){
        NetWorkHandleUtils.doJob(call,isShowLoading,object : HttpResultListener<BaseEntity<T>>{
            override fun getJob(job: Job) {
                getJob?.invoke(job)
            }

            override fun onSuccess(result: BaseEntity<T>) {
                if (ResultCheckUtils.check(result)) {
                    success?.invoke(result.data)
                } else {
                    failed?.invoke(result.code, result.msg)
                }
            }

            override fun onFailed(errCode: Int, msg: String?) {
                super.onFailed(errCode, msg)
                failed?.invoke(errCode,msg)
            }

            override fun onStart() {
                super.onStart()
                start?.invoke()
            }

            override fun onFinish() {
                super.onFinish()
                finish?.invoke()
            }
        })
    }
}