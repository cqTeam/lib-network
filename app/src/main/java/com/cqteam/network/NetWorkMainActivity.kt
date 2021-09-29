package com.cqteam.network

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.net.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class NetWorkMainActivity : AppCompatActivity() {

    private val api = NetWorkManager.create(TestApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startRequest.setOnClickListener {
            start()
        }
        weatherRequest.setOnClickListener {
            weather()
        }
    }

    private fun start() {
        val request = requestAsync {
            api.startApp2()
        }.onStart {
            Print.e("start----->")
        }.onSuccess {
            Print.e("success----->${it}")
            Toast.makeText(this, "成功", Toast.LENGTH_LONG).show()
        }.onFailure {
            Print.e("failure----->${it}")
            Toast.makeText(this, "失败", Toast.LENGTH_LONG).show()
            return@onFailure true
        }.onFinished {
            Print.e("onFinished----->")
        }
    }

    private fun weather() {
//        GlobalScope.launch {
//            val download = download(
////                "https://img0.baidu.com/it/u=3948309700,2415409281&fm=26&fmt=auto&gp=0.jpg",
//                "https://img0.baidu.com/u=3948309700,2415409281&fm=26&fmt=auto&gp=0.jpg",
//                Environment.getExternalStorageDirectory().absolutePath + File.separator + "AAATestDownload",
//                "test.jpg"
//            )
//            if (download == null){
//                Print.e("下载失败")
//            } else {
//                Print.e("下载成功")
//            }
//        }

        val download = downloadAsync(
            "https://img0.baidu.com/it/u=3948309700,2415409281&fm=26&fmt=auto&gp=0.jpg",
//            "https://img0.baidu.com/u=3948309700,2415409281&fm=26&fmt=auto&gp=0.jpg",
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "AAATestDownload",
            "test.jpg"
        ).onStart {
            Print.e("onStart--------->")
            Toast.makeText(this,"下载开始",Toast.LENGTH_SHORT).show()
        }.onSuccess {
            Print.e("onSuccess--------->")
            Toast.makeText(this,"下载成功",Toast.LENGTH_SHORT).show()
        }.onFailure {
            Print.e("onFailure--------->$it")
            Toast.makeText(this,"下载失败",Toast.LENGTH_SHORT).show()
            return@onFailure false
        }.onFinished {
            Print.e("onFinished--------->")
            Toast.makeText(this,"下载结束",Toast.LENGTH_SHORT).show()
        }.onProgress { total, currentProgress ->
            Print.e("onProgress--------->total:$total--currentProgress:$currentProgress")
            runOnUiThread {
                Toast.makeText(this,"下载进度:$currentProgress/$total",Toast.LENGTH_SHORT).show()
            }
        }
        GlobalScope.launch {
            delay(10)
            Print.e("取消下载")
            download.cancel()
        }
    }

    suspend fun getTest(): String {
        return "结果"
    }
}