package com.cqteam.networklib.http.utils

import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.ThreadUtils

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/10/27 10:34
 */
object NetPrintUtil {
    fun print(msg: String) {
        ThreadUtils.runOnUiThread {
            var msg = msg
            val segmentSize = 3 * 1024
            val length = msg.length.toLong()
            if (length <= segmentSize) { // 长度小于等于限制直接打印
                NetWorkManager.getConfig().getLogger().log(msg)
            } else {
                while (msg!!.length > segmentSize) { // 循环分段打印日志
                    val logContent = msg.substring(0, segmentSize)
                    msg = msg.replace(logContent, "")
                    NetWorkManager.getConfig().getLogger().log(logContent)
                }
                NetWorkManager.getConfig().getLogger().log(msg)// 打印剩余日志
            }
        }
    }
}