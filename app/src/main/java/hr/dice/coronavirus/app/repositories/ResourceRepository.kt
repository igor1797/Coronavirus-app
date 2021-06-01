package hr.dice.coronavirus.app.repositories

import android.app.Application
import hr.dice.coronavirus.app.R

class ResourceRepository(
    private val context: Application
) {
    fun getNowText() = context.getString(R.string.nowText)

    fun getMinutesTimeAgoText(timeAgo: Int) = context.getString(R.string.mAgotext, timeAgo.toString())

    fun getHoursTimeAgoText(timeAgo: Int) = context.getString(R.string.hAgoText, timeAgo.toString())
}
