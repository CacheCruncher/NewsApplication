package com.experiment.newsapplication.data


sealed class APIResult<T>(val data: T? = null, val error: Throwable? = null) {
    class Success<T>(data: T) : APIResult<T>(data)
    class Loading<T>(data: T? = null) : APIResult<T>(data)
    class Error<T>(data: T? = null, error: Throwable) : APIResult<T>(data, error)
}
