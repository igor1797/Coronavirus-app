package hr.dice.coronavirus.app.ui.base

sealed class ViewState <out T>
data class Success<out T>(val data: T) : ViewState<T>()
data class Error(val message: String? = null) : ViewState<Nothing>()
object Loading : ViewState<Nothing>()
object NoInternetState : ViewState<Nothing>()
