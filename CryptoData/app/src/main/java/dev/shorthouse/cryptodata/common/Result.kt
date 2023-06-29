package dev.shorthouse.cryptodata.common

sealed class Result<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Result<T>(data)
    class Error<T> : Result<T>()
}

// sealed class NetworkResult<T : Any> {
//    class Success<T : Any>(val data: T) : NetworkResult<T>()
//    class Error<T : Any>(val code: Int, val message: String?) : NetworkResult<T>()
//    class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()
// }
