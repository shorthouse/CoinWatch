package dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics

import kotlinx.serialization.Serializable

@Serializable
data class ReviewAnalytics(
    val successfulDetailsViewedCount: Int = 0,
    val hasAddedFavourite: Boolean = false,
    val lastReviewPromptAttemptEpochMillis: Long = 0,
)
