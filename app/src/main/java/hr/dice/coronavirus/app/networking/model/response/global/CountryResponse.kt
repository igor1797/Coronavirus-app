package hr.dice.coronavirus.app.networking.model.response.global

import com.google.gson.annotations.SerializedName
import hr.dice.coronavirus.app.model.global.GlobalCountry
import hr.dice.coronavirus.app.networking.base.DomainMapper

data class CountryResponse(
    @SerializedName("Country")
    val country: String,
    @SerializedName("CountryCode")
    val countryCode: String,
    @SerializedName("Date")
    val date: String,
    @SerializedName("ID")
    val id: String,
    @SerializedName("NewConfirmed")
    val newConfirmed: Int,
    @SerializedName("NewDeaths")
    val newDeaths: Int,
    @SerializedName("NewRecovered")
    val newRecovered: Int,
    @SerializedName("Slug")
    val slug: String,
    @SerializedName("TotalConfirmed")
    val totalConfirmed: Int,
    @SerializedName("TotalDeaths")
    val totalDeaths: Int,
    @SerializedName("TotalRecovered")
    val totalRecovered: Int
) : DomainMapper<GlobalCountry> {
    override fun mapToDomain(): GlobalCountry {
        val totalActive = totalConfirmed - totalRecovered
        return GlobalCountry(country, slug, totalConfirmed, totalActive, totalRecovered, totalDeaths)
    }
}
