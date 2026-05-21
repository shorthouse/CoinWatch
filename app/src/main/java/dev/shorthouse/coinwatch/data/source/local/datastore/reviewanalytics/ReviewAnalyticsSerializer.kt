package dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics

import dev.shorthouse.coinwatch.data.source.local.datastore.BasePreferencesSerializer

object ReviewAnalyticsSerializer : BasePreferencesSerializer<ReviewAnalytics>(
    defaultInstance = { ReviewAnalytics() },
    serializer = ReviewAnalytics.serializer(),
)
