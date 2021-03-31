package hr.dice.coronavirus.app.repositories.base

import hr.dice.coronavirus.app.common.makeNetworkRequest
import hr.dice.coronavirus.app.common.utils.connectivity.Connectivity
import hr.dice.coronavirus.app.ui.base.NoInternetState
import hr.dice.coronavirus.app.ui.base.Success
import hr.dice.coronavirus.app.ui.base.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

@KoinApiExtension
abstract class BaseRepository : KoinComponent {

    private val connectivity: Connectivity by inject()

    protected fun <T> fetchData(apiCall: suspend () -> Response<T>, transform: ((T) -> Success<T>)? = null): Flow<ViewState<T>> {
        return if (connectivity.isNetworkAvailable()) {
            makeNetworkRequest(apiCall).map { viewState ->
                transform?.let {
                    if (viewState is Success) {
                        transform(viewState.data)
                    }
                }
                viewState
            }.flowOn(Dispatchers.IO)
        } else
            flowOf(NoInternetState)
    }
}
