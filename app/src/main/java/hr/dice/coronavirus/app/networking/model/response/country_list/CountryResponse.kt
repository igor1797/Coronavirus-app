package hr.dice.coronavirus.app.networking.model.response.country_list

import com.google.gson.annotations.SerializedName
import hr.dice.coronavirus.app.model.country_list.Country
import hr.dice.coronavirus.app.networking.base.DomainMapper

data class CountryResponse(
    @SerializedName("Country")
    val name: String,
    @SerializedName("Slug")
    val slug: String,
    @SerializedName("ISO2")
    val iso2: String
) : DomainMapper<Country> {
    override fun mapToDomain(): Country {
        return Country(name, slug, iso2)
    }
}
