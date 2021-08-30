package com.cqteam.network

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cqteam.networklib.NetWorkManager
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
        NetWorkRequest.request(api.startApp(),false,{
            Print.e("$it")
        },{errCode, msg ->  },{},{})
    }
    private fun weather(){
        NetWorkRequest.request(api.getWeather(),false,{
            Print.e("$it")
        },{errCode, msg ->  },{},{})
    }
}