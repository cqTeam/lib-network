
package com.cqteam.networklib.http.net

import android.util.Log
import com.cqteam.networklib.http.okhttp.OkHttpClientProvider
import kotlinx.coroutines.*
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.Serializable

/**
 *
 * @Description:
 * @Author:         koloces
 * @CreateDate:     2021/9/2 17:02
 */
class HttpResult<T> private constructor() : Serializable {

    internal var mValue: Any? = null
    private var netJob: Job? = null
    internal var successBlock: ((value: T) -> Unit)? = null
    internal var failedBlock: ((value: Failure) -> Unit)? = null
    internal var finishedBlock: (() -> Unit)? = null
    internal var progressBlock: ((total : Long,currentProgress : Long) -> Unit)? = null

    internal val isLoading: Boolean get() = mValue == null
    internal val isFinished: Boolean get() = mValue != null
    internal val isSuccess: Boolean get() = mValue != null && mValue !is Failure
    internal val isFailure: Boolean get() = mValue != null && mValue is Failure

    internal companion object {
        internal fun <T> create(): HttpResult<T> = HttpResult()
    }

    internal fun setNetJob(job: Job) {
        netJob = job
    }

    internal fun setValue(data: T) : T {
        mValue = data
        return data
    }

    internal fun onFailed(
        errorCode: Int,
        errorMsg: String,
        exception: Throwable? = null
    ) :Failure{
        mValue = createFailure(errorCode, errorMsg, exception)
        return mValue as Failure
    }

    fun cancel() {
        netJob?.cancel()
    }

    public class Failure(
        val errorCode: Int,
        val errorMsg: String,
        val exception: Throwable?
    ) : Serializable {
        override fun equals(other: Any?): Boolean = other is Failure && exception == other.exception
        override fun hashCode(): Int = exception.hashCode()
        override fun toString(): String = "Failure(errorCode : $errorCode\nerrorMsg : $errorMsg\n$exception)"
    }
}

public suspend inline fun <T> request(
    block: suspend () -> BaseResponse<T>,
    unHandError: Boolean = false
): T? {
    runCatching {
        block()
    }.onSuccess { data ->
        runCatching {
            if (data.isSuccess()) {
                return data.getResponseData()
            } else {
                if (unHandError) {
                    MainScope().launch {
                        data.handleError()
                    }
                }
            }
        }.onFailure {
            Log.e("HttpResult","请求失败-》${it}")
            return null
        }
    }.onFailure {
        Log.e("HttpResult","请求失败-》${it}")
        return null
    }
    return null
}


public fun <T> requestAsync(block: suspend () -> BaseResponse<T>): HttpResult<T> {
    val create = HttpResult.create<T>()
    val job = MainScope().launch(start = CoroutineStart.LAZY) {
        kotlin.runCatching {
            block()
        }.onSuccess { data ->
            if (data.isSuccess()) {
                create.setValue(data.getResponseData())
                create.successBlock?.invoke(data.getResponseData())
            } else {
                val onFailed = create.onFailed(
                    data.getResponseCode(),
                    data.getResponseMsg()
                )
                data.handleError()
                create.failedBlock?.invoke(onFailed)
            }
            create.finishedBlock?.invoke()
        }.onFailure {
            val onFailed = create.onFailed(-999, it.message?:"Exception错误", it)
            create.failedBlock?.invoke(onFailed)
            create.finishedBlock?.invoke()
        }
    }
    create.setNetJob(job)
    job.start()
    return create
}


public suspend fun download(imgUrl:String,saveDir:String,saveName:String) : File? {
    var inputStream: InputStream? = null
    var fileOutputStream: FileOutputStream? = null
    try {
        val client = OkHttpClientProvider.getClient()
        val request = Request.Builder().url(imgUrl).build()
        val response: Response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            Log.e("HttpResult","下载失败-->code:${response.code}")
            return null
        }
        inputStream = response.body?.byteStream()
        val buf: ByteArray = ByteArray(2048)
        var len = 0
        // 存储下载文件的目录
        var file = File(saveDir)
        if (!file.mkdirs()) {
            file.createNewFile()
        }
        val savePath = saveDir + File.separator + saveName
        file = File(savePath)
        //判断文件名是否已存在，若已经存在则取时间戳重新建一个
        if (file.exists()) {
            file = File(saveDir + File.separator + System.currentTimeMillis() + "_" + saveName)
        }
        fileOutputStream = FileOutputStream(file)
        if (inputStream != null) {
            while (inputStream!!.read(buf).also { len = it } != -1) {
                fileOutputStream!!.write(buf, 0, len)
            }
            fileOutputStream!!.flush()
            // 下载完成
            inputStream?.close()
            fileOutputStream?.close()
            return file
        }
    }catch (e:Exception){
        try {
            inputStream?.close()
        }catch (e:Exception){
            e.printStackTrace()
        }

        try {
            fileOutputStream?.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
        Log.e("HttpResult","下载失败-》${e}")
        return null
    }
    return null
}

public fun downloadAsync(imgUrl:String,saveDir:String,saveName:String) : HttpResult<File>{
    val create = HttpResult.create<File>()
    val job = GlobalScope.launch(Dispatchers.IO,start = CoroutineStart.LAZY) {
        var inputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            if (!isActive){
                GlobalScope.launch(Dispatchers.Main) {
                    val onFailed = create.onFailed(-999, "下载取消")
                    create.failedBlock?.invoke(onFailed)
                    create.finishedBlock?.invoke()
                }
                return@launch
            }
            val client = OkHttpClientProvider.getClient()
            val request = Request.Builder().url(imgUrl).build()
            val response: Response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                val onFailed = create.onFailed(-998, "下载失败")
                GlobalScope.launch(Dispatchers.Main) {
                    create.failedBlock?.invoke(onFailed)
                    create.finishedBlock?.invoke()
                }
                return@launch
            }
            inputStream = response.body?.byteStream()
            val total = response.body?.contentLength()
            val buf: ByteArray = ByteArray(2048)
            var len = 0
            // 存储下载文件的目录
            var file = File(saveDir)
            if (!file.mkdirs()) {
                file.createNewFile()
            }
            val savePath = saveDir + File.separator + saveName
            file = File(savePath)
            //判断文件名是否已存在，若已经存在则取时间戳重新建一个
            if (file.exists()) {
                file = File(saveDir + File.separator + System.currentTimeMillis() + "_" + saveName)
            }
            fileOutputStream = FileOutputStream(file)
            var sum: Long = 0
            if (inputStream != null) {
                while (inputStream.read(buf).also { len = it } != -1) {
                    if (isActive) {
                        fileOutputStream.write(buf, 0, len)
                        sum += len
                        total?.let { create.progressBlock?.invoke(it, sum) }
                    } else {
                        break
                    }
                }
                fileOutputStream.flush()
                // 下载完成
                inputStream.close()
                fileOutputStream.close()
                create.setValue(file)
                if (isActive) {
                    GlobalScope.launch(Dispatchers.Main) {
                        create.successBlock?.invoke(file)
                        create.finishedBlock?.invoke()
                    }
                } else {
                    GlobalScope.launch(Dispatchers.Main) {
                        val onFailed = create.onFailed(-999, "下载取消")
                        create.failedBlock?.invoke(onFailed)
                        create.finishedBlock?.invoke()
                    }
                }
            }
        }catch (e:Exception){
            try {
                inputStream?.close()
            }catch (e:Exception){
                e.printStackTrace()
            }

            try {
                fileOutputStream?.close()
            }catch (e:Exception){
                e.printStackTrace()
            }
            val onFailed = create.onFailed(-997, "下载失败",e)
            GlobalScope.launch(Dispatchers.Main) {
                create.failedBlock?.invoke(onFailed)
                create.finishedBlock?.invoke()
            }
        }
    }
    create.setNetJob(job)
    job.start()
    return create
}

internal fun createFailure(
    errorCode: Int,
    errorMsg: String,
    exception: Throwable? = null
): HttpResult.Failure =
    HttpResult.Failure(errorCode, errorMsg, exception)



public fun <T> HttpResult<T>.onFailure(action: (value: HttpResult.Failure) -> Unit): HttpResult<T> {
    if (isFailure) {
        action.invoke(mValue as HttpResult.Failure)
    } else {
        failedBlock = action
    }
    return this
}

public fun <T> HttpResult<T>.onSuccess(action: (value: T) -> Unit): HttpResult<T> {
    if (isSuccess) {
        action.invoke(mValue as T)
    } else {
        successBlock = action
    }
    return this
}

/**
 * 这个回调是在子线程
 */
public fun <T> HttpResult<T>.onProgress(action: (total : Long,currentProgress : Long) -> Unit) : HttpResult<T>{
    progressBlock = action
    return this
}

public inline fun <T> HttpResult<T>.onStart(action: () -> Unit): HttpResult<T> {
    action.invoke()
    return this
}


public fun <T> HttpResult<T>.onFinished(action: () -> Unit): HttpResult<T> {
    if (isFinished) {
        action.invoke()
    } else {
        finishedBlock = action
    }
    return this
}
