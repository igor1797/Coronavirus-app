package hr.dice.coronavirus.app.common

import hr.dice.coronavirus.app.ui.base.Error
import hr.dice.coronavirus.app.ui.base.Loading
import hr.dice.coronavirus.app.ui.base.Success
import hr.dice.coronavirus.app.ui.base.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

fun <T> makeNetworkRequest(apiCall: suspend () -> Response<T>): Flow<ViewState<T>> {
    return flow {
        emit(Loading)
        try {
            val response: Response<T> = apiCall.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Success(it))
                }
            } else
                emit(Error(response.message()))
        } catch (e: Throwable) {
            emit(Error(e.message))
        }
    }
}
