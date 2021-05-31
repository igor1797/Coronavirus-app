package hr.dice.coronavirus.app.networking.model.response.news_list

import hr.dice.coronavirus.app.model.news_list.Pagination
import hr.dice.coronavirus.app.networking.base.DomainMapper

data class PaginationResponse(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val total: Int
) : DomainMapper<Pagination> {
    override fun mapToDomain(): Pagination {
        return Pagination(count, limit, offset, total)
    }
}
