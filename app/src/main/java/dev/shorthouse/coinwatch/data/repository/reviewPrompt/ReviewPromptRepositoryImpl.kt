package dev.shorthouse.coinwatch.data.repository.reviewPrompt

import dev.shorthouse.coinwatch.common.TimeProvider
import dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics.ReviewAnalytics
import dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics.ReviewAnalyticsLocalDataSource
import java.time.Duration
import javax.inject.Inject

class ReviewPromptRepositoryImpl @Inject constructor(
    private val reviewAnalyticsLocalDataSource: ReviewAnalyticsLocalDataSource,
    private val timeProvider: TimeProvider,
) : ReviewPromptRepository {
    override suspend fun recordSuccessfulDetailsView() {
        reviewAnalyticsLocalDataSource.recordSuccessfulDetailsView()
    }

    override suspend fun recordFavouriteAdded() {
        reviewAnalyticsLocalDataSource.recordFavouriteAdded()
    }

    override suspend fun recordReviewPromptAttempted() {
        reviewAnalyticsLocalDataSource.recordReviewPromptAttempted(
            timeProvider.currentEpochMillis()
        )
    }

    override suspend fun isReviewPromptEligible(): Boolean {
        val reviewAnalytics = reviewAnalyticsLocalDataSource.getReviewAnalytics()

        return reviewAnalytics.hasEnoughPositiveEngagement() &&
            reviewAnalytics.isOutsideReviewPromptCooldown(
                nowEpochMillis = timeProvider.currentEpochMillis()
            )
    }

    private fun ReviewAnalytics.hasEnoughPositiveEngagement(): Boolean {
        return successfulDetailsViewedCount >= SUCCESSFUL_DETAILS_VIEWS_ELIGIBILITY_THRESHOLD ||
            (successfulDetailsViewedCount >= SUCCESSFUL_DETAILS_VIEWS_WITH_FAVOURITE_ELIGIBILITY_THRESHOLD &&
                hasAddedFavourite)
    }

    private fun ReviewAnalytics.isOutsideReviewPromptCooldown(nowEpochMillis: Long): Boolean {
        if (lastReviewPromptAttemptEpochMillis == 0L) return true

        return nowEpochMillis - lastReviewPromptAttemptEpochMillis >= REVIEW_PROMPT_COOLDOWN_MILLIS
    }

    private companion object {
        const val SUCCESSFUL_DETAILS_VIEWS_ELIGIBILITY_THRESHOLD = 3
        const val SUCCESSFUL_DETAILS_VIEWS_WITH_FAVOURITE_ELIGIBILITY_THRESHOLD = 2
        val REVIEW_PROMPT_COOLDOWN_MILLIS: Long = Duration.ofDays(60).toMillis()
    }
}
