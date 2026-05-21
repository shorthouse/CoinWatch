package dev.shorthouse.coinwatch.e2e

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.shorthouse.coinwatch.MainActivity
import dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics.ReviewAnalytics
import dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics.ReviewAnalyticsLocalDataSource
import dev.shorthouse.coinwatch.e2e.fixture.Bitcoin
import dev.shorthouse.coinwatch.e2e.support.awaitText
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ReviewAnalyticsE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var reviewAnalyticsLocalDataSource: ReviewAnalyticsLocalDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun when_detailsOpenedOnce_should_recordOneSuccessfulDetailsView() {
        composeTestRule.openBitcoinDetails()

        val analytics = composeTestRule.awaitReviewAnalytics {
            it.successfulDetailsViewedCount == 1
        }
        assertThat(analytics.successfulDetailsViewedCount).isEqualTo(1)
    }

    @Test
    fun when_detailsOpenedTwice_should_recordTwoSuccessfulDetailsViews() {
        composeTestRule.apply {
            openBitcoinDetails()
            onNodeWithContentDescription("Back").performClick()

            awaitText("Market")
            awaitText(Bitcoin.NAME)
            onNodeWithText(Bitcoin.NAME).performClick()
            awaitText("Past day")
        }

        val analytics = composeTestRule.awaitReviewAnalytics {
            it.successfulDetailsViewedCount == 2
        }
        assertThat(analytics.successfulDetailsViewedCount).isEqualTo(2)
    }

    @Test
    fun when_favouriteAddedFromDetails_should_recordFavouriteAdded() {
        composeTestRule.apply {
            openBitcoinDetails()
            onNodeWithContentDescription("Favourite").performClick()
        }

        val analytics = composeTestRule.awaitReviewAnalytics {
            it.hasAddedFavourite
        }
        assertThat(analytics.hasAddedFavourite).isTrue()
    }

    @Test
    fun when_favouriteAddedThenRemoved_should_keepHasAddedFavouriteTrue() {
        composeTestRule.apply {
            openBitcoinDetails()
            onNodeWithContentDescription("Favourite").performClick()
            onNodeWithContentDescription("Favourite").performClick()
        }

        val analytics = composeTestRule.awaitReviewAnalytics {
            it.hasAddedFavourite
        }
        assertThat(analytics.hasAddedFavourite).isTrue()
    }

    @Test
    fun when_freshLaunch_should_haveDefaultAnalytics() {
        val analytics = runBlocking { reviewAnalyticsLocalDataSource.getReviewAnalytics() }

        assertThat(analytics.successfulDetailsViewedCount).isEqualTo(0)
        assertThat(analytics.hasAddedFavourite).isFalse()
        assertThat(analytics.lastReviewPromptAttemptEpochMillis).isEqualTo(0L)
    }

    private fun ComposeTestRule.openBitcoinDetails() {
        awaitText(Bitcoin.NAME)
        onNodeWithText(Bitcoin.NAME).performClick()
        awaitText("Past day")
    }

    private fun ComposeTestRule.awaitReviewAnalytics(
        predicate: (ReviewAnalytics) -> Boolean,
    ): ReviewAnalytics {
        var analytics = runBlocking { reviewAnalyticsLocalDataSource.getReviewAnalytics() }

        waitUntil(timeoutMillis = 2_000) {
            analytics = runBlocking { reviewAnalyticsLocalDataSource.getReviewAnalytics() }
            predicate(analytics)
        }

        return analytics
    }
}
