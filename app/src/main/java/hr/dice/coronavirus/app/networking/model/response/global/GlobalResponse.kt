package hr.dice.coronavirus.app.networking.model.response.global

import com.google.gson.annotations.SerializedName
import hr.dice.coronavirus.app.model.Case
import hr.dice.coronavirus.app.model.CasesStatus
import hr.dice.coronavirus.app.model.global.GlobalStatus
import hr.dice.coronavirus.app.networking.base.DomainMapper

data class GlobalResponse(
    @SerializedName("Countries")
    val countryResponses: List<CountryResponse>,
    @SerializedName("Date")
    val date: String,
    @SerializedName("Global")
    val globalStatusResponse: GlobalStatusResponse,
    @SerializedName("ID")
    val id: String,
    @SerializedName("Message")
    val message: String
) : DomainMapper<GlobalStatus> {
    override fun mapToDomain(): GlobalStatus {
        val totalActiveCases = globalStatusResponse.totalConfirmed - globalStatusResponse.totalRecovered
        val newActiveCases = globalStatusResponse.newConfirmed - globalStatusResponse.newRecovered
        val activeCases = Case(totalActiveCases, newActiveCases)
        val confirmedCases = Case(globalStatusResponse.totalConfirmed, globalStatusResponse.newConfirmed)
        val recoveredCases = Case(globalStatusResponse.totalRecovered, globalStatusResponse.newRecovered)
        val deceasesCases = Case(globalStatusResponse.totalDeaths, globalStatusResponse.newDeaths)
        val casesStatus = CasesStatus(confirmedCases, activeCases, recoveredCases, deceasesCases)
        return GlobalStatus(countryResponses.map { it.mapToDomain() }, casesStatus, date)
    }
}
