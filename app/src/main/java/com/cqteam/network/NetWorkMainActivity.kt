package com.cqteam.network

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cqteam.networklib.NetWorkManager
import com.cqteam.networklib.http.net.*
import kotlinx.android.synthetic.main.activity_main.*

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
    private fun start(){
        requestAsync {
            api.startApp2()
        }.onStart {
            Print.e("start----->")
        }.onSuccess {
            Print.e("success----->${it}")
            Toast.makeText(this,"成功",Toast.LENGTH_LONG).show()
        }.onFailure {
            Print.e("failure----->${it}")
            Toast.makeText(this,"失败",Toast.LENGTH_LONG).show()
            return@onFailure true
        }.onFinished {
            Print.e("onFinished----->")
        }
    }
    private fun weather(){
        NetWorkRequest.request(api.getWeather(),false,{
            Print.e("$it")
        },{errCode, msg ->  },{},{})
    }

    suspend fun getTest():String{
        return "结果"
    }
}