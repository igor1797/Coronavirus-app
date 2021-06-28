package hr.dice.coronavirus.app.networking

import hr.dice.coronavirus.app.networking.model.response.global.GlobalResponse
import hr.dice.coronavirus.app.networking.model.response.one_country.OneCountryStatusResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CoronavirusApiService {

    @GET("summary")
    suspend fun getGlobalData(): Response<GlobalResponse>

    @GET("dayone/country/{country}")
    suspend fun getDayOneAllStatusByCountry(@Path("country") country: String): Response<List<OneCountryStatusResponse>>

    @GET("total/country/{country}")
    suspend fun getTotalStatusByCountry(@Path("country") country: String): Response<List<OneCountryStatusResponse>>
}
