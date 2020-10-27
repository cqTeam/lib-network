package com.cqteam.networklib

import java.io.File

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/10/27 11:21
 */
internal object NetFileUtils {
    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }
}