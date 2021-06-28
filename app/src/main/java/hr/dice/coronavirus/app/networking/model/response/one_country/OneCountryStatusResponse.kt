package hr.dice.coronavirus.app.networking.model.response.one_country

import com.google.gson.annotations.SerializedName
import hr.dice.coronavirus.app.model.one_country.DateStatus
import hr.dice.coronavirus.app.networking.base.DomainMapper

data class OneCountryStatusResponse(
    @SerializedName("Active")
    val active: Int,
    @SerializedName("City")
    val city: String,
    @SerializedName("CityCode")
    val cityCode: String,
    @SerializedName("Province")
    val province: String,
    @SerializedName("Confirmed")
    val confirmed: Int,
    @SerializedName("Country")
    val country: String,
    @SerializedName("CountryCode")
    val countryCode: String,
    @SerializedName("Date")
    val date: String,
    @SerializedName("Deaths")
    val deaths: Int,
    @SerializedName("ID")
    val id: String,
    @SerializedName("Lat")
    val lat: String,
    @SerializedName("Lon")
    val lon: String,
    @SerializedName("Recovered")
    val recovered: Int
) : DomainMapper<DateStatus> {
    override fun mapToDomain(): DateStatus {
        return DateStatus(date, confirmed, active, recovered, deaths)
    }
}
