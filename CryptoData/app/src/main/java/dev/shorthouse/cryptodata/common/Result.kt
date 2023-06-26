package dev.shorthouse.cryptodata.common

sealed class Result<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T> : Result<T>()
}
