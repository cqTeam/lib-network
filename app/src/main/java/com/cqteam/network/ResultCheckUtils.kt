package com.cqteam.network

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/8/14 17:29
 */
object ResultCheckUtils {
    fun check(data: BaseEntity<*>?): Boolean {
        if (data == null) {
            return false
        }
        val code = data.code
        when (code) {
            0 -> {
                return true
            }
            3000->{
//                VToast.toast(data.msg)
                return false
            }
            1001 -> {
                try {
//                    StartUseHelper.show()
//                    VToast.toast("没有查询到用户入驻信息，暂不能使用")
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    return false
                }
            }
        }
        return false
    }
}