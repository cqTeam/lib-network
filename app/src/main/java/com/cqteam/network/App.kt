package com.cqteam.network

import android.app.Application
import android.widget.Toast
import com.cqteam.networklib.NetWorkConfig
import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.`interface`.LoggerProvider
import com.cqteam.networklib.`interface`.ParamsProvider
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2020/6/10 16:54
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val build = NetWorkConfig.Builder(this)
            .isDebug(true)
            .enableCache(true)//是否起用缓存 仅对GET有效
            .addCacheDirectory(this.cacheDir)//添加缓存文件夹
            .setMaxAge(60)//缓存时间 秒
            .setCacheSize(10 * 1024 * 1024)//设置缓存文件夹大小 10M
            .addLogger(object : LoggerProvider {
                override fun log(message: String) {
                    Print.e("NetHttp", message)
                    if ("暂无网络可用" == message){
                        Toast.makeText(this@App,message,Toast.LENGTH_LONG).show()
                    }
                }
            })
            .addParamsProvider(object : ParamsProvider {
                override fun bodyParams(params: HashMap<String, String>): HashMap<String, String>? {
                    return null
                }

                override fun headerParams(params: HashMap<String, String>): HashMap<String, String>? {
                    var token = "mhQK46rFWKamsGug"
                    Print.e("token:$token")
                    val randomStr = "123456"
                    val time = System.currentTimeMillis().toString().substring(0, 10)
                    val map = LinkedHashMap<String, String>()
                    map["authorization"] = token
                    map["version"] = "2.1.0"
                    map["timeStamp"] = time
                    map["enterpriseKey"] = "3bc0d2b5a818e60b3b12fd18dd348c32"
                    map["randomStr"] = randomStr
                    map["deviceNo"] = "XHFH0010000221"
                    val infoIds: List<Map.Entry<String, String>> =
                        ArrayList<Map.Entry<String, String>>(map.entries)
                    Collections.sort(infoIds, SortComparator())
                    val sb = StringBuilder()
                    for (infoId in infoIds) {
                        sb.append("&")
                        sb.append(infoId.key)
                        sb.append("=")
                        sb.append(infoId.value)
                    }
                    if (sb.isNotEmpty()) {
                        sb.delete(0, 1)
                    }
                    sb.append("62c5ba37c9dff22860f502ec1c410850")
                    val sgin = MD5(sb.toString().toLowerCase())
                    map.remove("authorization")
                    map["Authorization"] = token
                    map["Content-Type"] = "application/json"
                    map["signature"] = sgin
                    return map
                }
            }).build()
        NetWorkManager.init(
            build,
            "http://test.hyzl.aitravit.com/api/hotelbox/" // 服务器IP
        )
    }

    fun MD5(str : String) : String{
        try {
            //获取md5加密对象
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            //对字符串加密，返回字节数组
            val digest:ByteArray = instance.digest(str.toByteArray())
            var sb : StringBuffer = StringBuffer()
            for (b in digest) {
                //获取低八位有效值
                var i :Int = b.toInt() and 0xff
                //将整数转化为16进制
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    //如果是一位的话，补0
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

}