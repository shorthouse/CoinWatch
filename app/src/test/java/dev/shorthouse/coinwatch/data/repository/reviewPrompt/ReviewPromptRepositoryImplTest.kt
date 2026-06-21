package dev.shorthouse.coinwatch.data.repository.reviewPrompt

import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.common.TimeProvider
import dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics.ReviewAnalytics
import dev.shorthouse.coinwatch.data.source.local.datastore.reviewanalytics.ReviewAnalyticsLocalDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Duration

class ReviewPromptRepositoryImplTest {

    // Class under test
    private lateinit var reviewPromptRepository: ReviewPromptRepository

    @MockK(relaxUnitFun = true)
    private lateinit var reviewAnalyticsLocalDataSource: ReviewAnalyticsLocalDataSource

    private val nowEpochMillis = 1_700_000_000_000L
    private val cooldownMillis = Duration.ofDays(60).toMillis()
    private val fakeTimeProvider = FakeTimeProvider(currentEpochMillis = nowEpochMillis)

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        reviewPromptRepository = ReviewPromptRepositoryImpl(
            reviewAnalyticsLocalDataSource = reviewAnalyticsLocalDataSource,
            timeProvider = fakeTimeProvider,
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `recordSuccessfulDetailsView delegates to data source`() = runTest {
        // Act
        reviewPromptRepository.recordSuccessfulDetailsView()

        // Assert
        coVerifySequence { reviewAnalyticsLocalDataSource.recordSuccessfulDetailsView() }
    }

    @Test
    fun `recordFavouriteAdded delegates to data source`() = runTest {
        // Act
        reviewPromptRepository.recordFavouriteAdded()

        // Assert
        coVerifySequence { reviewAnalyticsLocalDataSource.recordFavouriteAdded() }
    }

    @Test
    fun `recordReviewPromptAttempted passes current epoch millis to data source`() = runTest {
        // Act
        reviewPromptRepository.recordReviewPromptAttempted()

        // Assert
        coVerifySequence {
            reviewAnalyticsLocalDataSource.recordReviewPromptAttempted(nowEpochMillis)
        }
    }

    @Test
    fun `isReviewPromptEligible returns false when no engagement`() = runTest {
        // Arrange
        stubAnalytics(ReviewAnalytics())

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `isReviewPromptEligible returns true when 3 successful details views`() = runTest {
        // Arrange
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 3,
                hasAddedFavourite = false,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `isReviewPromptEligible returns true when 4 successful details views`() = runTest {
        // Arrange
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 4,
                hasAddedFavourite = false,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `isReviewPromptEligible returns false when 2 successful details views without favourite`() = runTest {
        // Arrange
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 2,
                hasAddedFavourite = false,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `isReviewPromptEligible returns true when 2 successful details views with favourite`() = runTest {
        // Arrange
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 2,
                hasAddedFavourite = true,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `isReviewPromptEligible returns false when 1 successful details view with favourite`() = runTest {
        // Arrange
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 1,
                hasAddedFavourite = true,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `isReviewPromptEligible returns true when last attempt is zero`() = runTest {
        // Arrange
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 3,
                lastReviewPromptAttemptEpochMillis = 0L,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `isReviewPromptEligible returns false when within cooldown`() = runTest {
        // Arrange
        val thirtyDaysAgo = nowEpochMillis - Duration.ofDays(30).toMillis()
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 3,
                lastReviewPromptAttemptEpochMillis = thirtyDaysAgo,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `isReviewPromptEligible returns true when cooldown has elapsed`() = runTest {
        // Arrange
        val sixtyOneDaysAgo = nowEpochMillis - Duration.ofDays(61).toMillis()
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 3,
                lastReviewPromptAttemptEpochMillis = sixtyOneDaysAgo,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `isReviewPromptEligible returns true at exact cooldown boundary`() = runTest {
        // Arrange
        val exactlyAtBoundary = nowEpochMillis - cooldownMillis
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 3,
                lastReviewPromptAttemptEpochMillis = exactlyAtBoundary,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isTrue()
    }

    @Test
    fun `isReviewPromptEligible returns false one millisecond inside cooldown boundary`() = runTest {
        // Arrange
        val justInsideBoundary = nowEpochMillis - cooldownMillis + 1
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 3,
                lastReviewPromptAttemptEpochMillis = justInsideBoundary,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `isReviewPromptEligible returns false when wall clock has skewed backwards past last attempt`() = runTest {
        // Arrange
        val oneDayInFuture = nowEpochMillis + Duration.ofDays(1).toMillis()
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 3,
                lastReviewPromptAttemptEpochMillis = oneDayInFuture,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isFalse()
    }

    @Test
    fun `isReviewPromptEligible returns false when engagement insufficient even if cooldown elapsed`() = runTest {
        // Arrange
        val sixtyOneDaysAgo = nowEpochMillis - Duration.ofDays(61).toMillis()
        stubAnalytics(
            ReviewAnalytics(
                successfulDetailsViewedCount = 1,
                hasAddedFavourite = false,
                lastReviewPromptAttemptEpochMillis = sixtyOneDaysAgo,
            )
        )

        // Act
        val result = reviewPromptRepository.isReviewPromptEligible()

        // Assert
        assertThat(result).isFalse()
    }

    private fun stubAnalytics(analytics: ReviewAnalytics) {
        coEvery { reviewAnalyticsLocalDataSource.getReviewAnalytics() } returns analytics
    }

    private class FakeTimeProvider(
        private val currentEpochMillis: Long,
    ) : TimeProvider {
        override fun elapsedRealtimeMillis(): Long = 0L

        override fun currentEpochMillis(): Long = currentEpochMillis
    }
}
