package com.cqteam.network

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cqteam.networklib.http.net.HttpResultListener
import com.google.gson.JsonObject
import kotlinx.coroutines.Job

class NetWorkMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BaseNetWork().getAppVersion(object :HttpResultListener<JsonObject>{
            override fun onSuccess(result: JsonObject) {
            }

            override fun getJob(job: Job) {

            }
        })
    }
}