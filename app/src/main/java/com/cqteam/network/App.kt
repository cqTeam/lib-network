package com.cqteam.network

import android.app.Application
import com.cqteam.networklib.NetWorkManager

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/6/10 16:54
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        NetWorkManager.init(
            this,
            "http://www.zhayuanlier.com/" // 服务器IP
        )
    }
}