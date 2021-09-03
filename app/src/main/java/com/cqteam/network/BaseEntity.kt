package com.cqteam.network

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/8/14 16:14
 */
@Deprecated("")
data class BaseEntity<T> (
    val code: Int,
    val `data`: T,
    val msg: String
)