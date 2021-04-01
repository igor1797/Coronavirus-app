package hr.dice.coronavirus.app.networking.base

sealed class NetworkResult<out T>
data class SuccessResponse<out T>(val data: T) : NetworkResult<T>()
data class FailureResponse(val httpError: HttpError) : NetworkResult<Nothing>()
object NoInternetConnectionResponse : NetworkResult<Nothing>()

data class HttpError(val throwable: Throwable, val errorCode: Int = 0)

inline fun <T> NetworkResult<T>.onNoInternetConnection(action: () -> Unit): NetworkResult<T> {
    if (this is NoInternetConnectionResponse) action()
    return this
}

inline fun <T> NetworkResult<T>.onSuccess(action: (T) -> Unit): NetworkResult<T> {
    if (this is SuccessResponse) action(data)
    return this
}

inline fun <T> NetworkResult<T>.onFailure(action: (HttpError) -> Unit) {
    if (this is FailureResponse) action(httpError)
}

interface DomainMapper<T> {
    fun mapToDomain(): T
}
