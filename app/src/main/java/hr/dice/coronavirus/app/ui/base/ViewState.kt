package hr.dice.coronavirus.app.ui.base

import hr.dice.coronavirus.app.networking.base.HttpError

sealed class ViewState <out T>
data class Success<out T>(val data: T) : ViewState<T>()
data class Error(val httpError: HttpError) : ViewState<Nothing>()
object Loading : ViewState<Nothing>()
object NoInternetState : ViewState<Nothing>()

inline fun <T> ViewState<T>.onSuccess(action: (T) -> Unit): ViewState<T> {
    if (this is Success) action(data)
    return this
}
