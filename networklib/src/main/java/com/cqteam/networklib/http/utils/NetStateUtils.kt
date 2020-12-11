package com.cqteam.networklib.http.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/12/11 10:35
 */
object NetStateUtils {
    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetworkReachable(context: Context): Boolean? {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val current = cm.activeNetworkInfo ?: return false

        //网络是否有效
        var isNetEffective = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            isNetEffective = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
        return current.isAvailable && isNetEffective
    }
}