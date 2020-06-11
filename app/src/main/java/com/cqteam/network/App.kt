package com.cqteam.network

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.cqteam.networklib.NetWorkConfig
import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.`interface`.LoggerProvider
import com.cqteam.networklib.`interface`.ToastProvider

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/6/10 16:54
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = NetWorkConfig.Builder(this)
            .isDebug(true)
            .addLogger(object : LoggerProvider {
                override fun log(message: String) {
                    Log.e("-----", message)
                }
            })
            .addToastProvider(object : ToastProvider {
                override fun toast(str: String?) {
                    Toast.makeText(this@App, str, Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        NetWorkManager.init(
            config,
            "http://www.zhayuanlier.com/"
        )

    }
}