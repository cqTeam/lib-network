package com.cqteam.networklib.http.net

import android.util.Log
import kotlinx.coroutines.*
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
    internal var failedBlock: ((value: Failure) -> Boolean)? = null
    internal var finishedBlock: (() -> Unit)? = null

    internal val isLoading: Boolean get() = mValue == null
    internal val isFinished: Boolean get() = mValue != null
    internal val isSuccess: Boolean get() = mValue != null && mValue !is Failure
    internal val isFailure: Boolean get() = mValue != null && mValue is Failure

    public companion object {
        public fun <T> create(): HttpResult<T> = HttpResult()
    }

    internal fun setNetJob(job: Job) {
        netJob = job
    }

    internal fun setValue(data: T) {
        mValue = data
    }

    internal fun onFailed(
        errorCode: Int,
        errorMsg: String,
        exception: Exception?
    ) {
        mValue = createFailure(errorCode, errorMsg, exception)
    }

    fun cancelJob() {
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

/**
 * @param unHandError true 则不执行[BaseResponse.handleError]方法,默认false
 */
public suspend fun <T> request(
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
            return null
        }
    }.onFailure {
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
                create.successBlock?.invoke(data.getResponseData())
            } else {
                val invoke: Boolean? = create.failedBlock?.invoke(
                    createFailure(
                        data.getResponseCode(),
                        data.getResponseMsg()
                    )
                )
                if (invoke != null && !invoke) {
                    data.handleError()
                }
            }
            create.finishedBlock?.invoke()
        }.onFailure {
            create.failedBlock?.invoke(createFailure(-999, "Exception错误", it))
            create.finishedBlock?.invoke()
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


/**
 * @param action 返回为 true 说明拦截，则不执行[BaseResponse.handleError]方法
 */
public fun <T> HttpResult<T>.onFailure(action: (value: HttpResult.Failure) -> Boolean): HttpResult<T> {
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
