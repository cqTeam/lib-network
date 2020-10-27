package com.cqteam.networklib.http

import android.os.Handler
import android.os.Looper

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/10/27 10:37
 */
internal object ThreadUtils {
    private val mainThread :Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    fun isUiThread() : Boolean{
        return Looper.myLooper() == Looper.getMainLooper()
    }

    fun runOnUiThread(callback:()->Unit){
        if (isUiThread()){
            callback.invoke()
            return
        }
        mainThread.post(callback)
    }
}