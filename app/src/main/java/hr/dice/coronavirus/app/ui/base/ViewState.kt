package hr.dice.coronavirus.app.ui.base

import hr.dice.coronavirus.app.networking.base.HttpError

sealed class ViewState
data class Success<out T>(val data: T) : ViewState()
data class Error(val httpError: HttpError) : ViewState()
object Loading : ViewState()
object NoInternetState : ViewState()
object EmptyState : ViewState()

inline fun <T> ViewState.onSuccess(action: (T) -> Unit): ViewState {
    if (this is Success<*>) action(data as T)
    return this
}
