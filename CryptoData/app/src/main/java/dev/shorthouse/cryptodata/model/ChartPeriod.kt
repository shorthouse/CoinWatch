package dev.shorthouse.cryptodata.model

import androidx.annotation.StringRes
import dev.shorthouse.cryptodata.R

enum class ChartPeriod(
    @StringRes val shortNameId: Int,
    @StringRes val longNameId: Int,
    val stringName: String
) {
    Day(
        shortNameId = R.string.period_short_name_day,
        longNameId = R.string.period_long_name_day,
        stringName = "1"
    ),
    Week(
        shortNameId = R.string.period_short_name_week,
        longNameId = R.string.period_long_name_week,
        stringName = "7"
    ),
    Month(
        shortNameId = R.string.period_short_name_month,
        longNameId = R.string.period_long_name_month,
        stringName = "30"
    ),
    SixMonth(
        shortNameId = R.string.period_short_name_six_month,
        longNameId = R.string.period_long_name_six_month,
        stringName = "180"
    ),
    Year(
        shortNameId = R.string.period_short_name_year,
        longNameId = R.string.period_long_name_year,
        stringName = "365"
    ),
    Max(
        shortNameId = R.string.period_short_name_max,
        longNameId = R.string.period_long_name_max,
        stringName = "max"
    )
}
