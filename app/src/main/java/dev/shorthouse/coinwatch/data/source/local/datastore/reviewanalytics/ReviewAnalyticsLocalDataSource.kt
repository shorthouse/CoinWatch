package dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics

import androidx.datastore.core.DataStore
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import timber.log.Timber

class ReviewAnalyticsLocalDataSource @Inject constructor(
    private val reviewAnalyticsDataStore: DataStore<ReviewAnalytics>,
) {
    val reviewAnalyticsFlow: Flow<ReviewAnalytics> = reviewAnalyticsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading review analytics")
                emit(ReviewAnalytics())
            } else {
                throw exception
            }
        }

    suspend fun getReviewAnalytics(): ReviewAnalytics {
        return reviewAnalyticsFlow.first()
    }

    suspend fun recordSuccessfulDetailsView() {
        reviewAnalyticsDataStore.updateData { currentAnalytics ->
            currentAnalytics.copy(
                successfulDetailsViewedCount = currentAnalytics.successfulDetailsViewedCount + 1
            )
        }
    }

    suspend fun recordFavouriteAdded() {
        reviewAnalyticsDataStore.updateData { currentAnalytics ->
            currentAnalytics.copy(
                hasAddedFavourite = true
            )
        }
    }

    suspend fun recordReviewPromptAttempted(epochMillis: Long) {
        reviewAnalyticsDataStore.updateData { currentAnalytics ->
            currentAnalytics.copy(lastReviewPromptAttemptEpochMillis = epochMillis)
        }
    }
}
