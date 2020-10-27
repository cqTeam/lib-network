package com.cqteam.network

/**
 *
 * @Description:    按照ascii码从大到小排序
 * @Author:         koloces
 * @CreateDate:     2020/7/7 10:13
 */
class SortComparator : Comparator<Map.Entry<String,String>> {
    override fun compare(o1: Map.Entry<String, String>?, o2: Map.Entry<String, String>?): Int {
        if (o1 != null && o2 != null){
            val key1 = o1.key
            val key2 = o2.key
            //compareTo 方法
            //如果指定的数与参数相等返回0。
            //如果指定的数小于参数返回 -1。
            //如果指定的数大于参数返回 1。
            return key1.compareTo(key2)
        }
        if (o1 != null){
            return 1
        }
        if (o2 != null){
            return -1
        }
        return 0
    }
}