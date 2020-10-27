package com.cqteam.network

import android.util.Log

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/10/26 15:04
 */
object Print {
    fun e(msg:String){
        e("Print",msg)
    }
    fun e(tag:String,msg:String){
        Log.e(tag,msg)
    }
}