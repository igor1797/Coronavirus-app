package hr.dice.coronavirus.app.networking

import hr.dice.coronavirus.app.networking.model.response.country_list.CountryResponse
import retrofit2.Response
import retrofit2.http.GET

interface CountryApiService {

    @GET("countries")
    suspend fun getCountryList(): Response<List<CountryResponse>>
}
