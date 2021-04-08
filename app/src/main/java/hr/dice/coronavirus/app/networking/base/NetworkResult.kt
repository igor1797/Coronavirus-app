package hr.dice.coronavirus.app.networking.base

sealed class NetworkResult
data class SuccessResponse<out T>(val data: T) : NetworkResult()
data class FailureResponse(val httpError: HttpError) : NetworkResult()
object NoInternetConnectionResponse : NetworkResult()

data class HttpError(val throwable: Throwable, val errorCode: Int = 0)

inline fun NetworkResult.onNoInternetConnection(action: () -> Unit): NetworkResult {
    if (this is NoInternetConnectionResponse) action()
    return this
}

inline fun <T> NetworkResult.onSuccess(action: (T) -> Unit): NetworkResult {
    if (this is SuccessResponse<*>) action(data as T)
    return this
}

inline fun NetworkResult.onFailure(action: (HttpError) -> Unit) {
    if (this is FailureResponse) action(httpError)
}

interface DomainMapper<T> {
    fun mapToDomain(): T
}
