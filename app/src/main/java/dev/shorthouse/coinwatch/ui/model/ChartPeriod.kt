package dev.shorthouse.coinwatch.ui.model

import androidx.annotation.StringRes
import dev.shorthouse.coinwatch.R

enum class ChartPeriod(
    @StringRes val shortNameId: Int,
    @StringRes val longNameId: Int,
    val stringName: String
) {
    Hour(
        shortNameId = R.string.period_short_name_hour,
        longNameId = R.string.period_long_name_hour,
        stringName = "1h"
    ),
    Day(
        shortNameId = R.string.period_short_name_day,
        longNameId = R.string.period_long_name_day,
        stringName = "24h"
    ),
    Week(
        shortNameId = R.string.period_short_name_week,
        longNameId = R.string.period_long_name_week,
        stringName = "7d"
    ),
    Month(
        shortNameId = R.string.period_short_name_month,
        longNameId = R.string.period_long_name_month,
        stringName = "30d"
    ),
    ThreeMonth(
        shortNameId = R.string.period_short_name_three_month,
        longNameId = R.string.period_long_name_three_month,
        stringName = "3m"
    ),
    Year(
        shortNameId = R.string.period_short_name_year,
        longNameId = R.string.period_long_name_year,
        stringName = "1y"
    ),
    FiveYear(
        shortNameId = R.string.period_short_name_five_year,
        longNameId = R.string.period_long_name_five_year,
        stringName = "5y"
    )
}
