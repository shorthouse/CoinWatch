package dev.shorthouse.cryptodata.common

sealed class Result<T : Any> {
    class Success<T : Any>(val data: T) : Result<T>()
    class Error<T : Any>(val message: String?) : Result<T>()
}
