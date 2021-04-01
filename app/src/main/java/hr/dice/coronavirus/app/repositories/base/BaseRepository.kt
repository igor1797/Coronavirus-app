package hr.dice.coronavirus.app.repositories.base

import hr.dice.coronavirus.app.networking.GENERAL_NETWORK_ERROR
import hr.dice.coronavirus.app.networking.base.DomainMapper
import hr.dice.coronavirus.app.networking.base.FailureResponse
import hr.dice.coronavirus.app.networking.base.HttpError
import hr.dice.coronavirus.app.networking.base.NetworkResult
import hr.dice.coronavirus.app.networking.base.NoInternetConnectionResponse
import hr.dice.coronavirus.app.networking.base.SuccessResponse
import hr.dice.coronavirus.app.networking.base.onFailure
import hr.dice.coronavirus.app.networking.base.onNoInternetConnection
import hr.dice.coronavirus.app.networking.base.onSuccess
import hr.dice.coronavirus.app.ui.base.Error
import hr.dice.coronavirus.app.ui.base.Loading
import hr.dice.coronavirus.app.ui.base.NoInternetState
import hr.dice.coronavirus.app.ui.base.Success
import hr.dice.coronavirus.app.ui.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {

    protected fun <T : DomainMapper<R>, R> fetchData(apiCall: suspend () -> Response<T>): Flow<ViewState<R>> {
        return flow {
            emit(Loading)
            makeNetworkRequest(apiCall)
                .onNoInternetConnection { emit(NoInternetState) }
                .onSuccess { emit(Success(it.mapToDomain())) }
                .onFailure { emit(Error(it)) }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun <T> makeNetworkRequest(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response: Response<T> = apiCall.invoke()
            if (response.isSuccessful) {
                response.body()?.let {
                    return SuccessResponse(it)
                }
            } else
                return FailureResponse(HttpError(Throwable(response.message()), response.code()))
            return FailureResponse(HttpError(Throwable(GENERAL_NETWORK_ERROR)))
        } catch (throwable: Throwable) {
            return when (throwable) {
                is IOException -> NoInternetConnectionResponse
                else -> FailureResponse(HttpError(throwable))
            }
        }
    }
}
