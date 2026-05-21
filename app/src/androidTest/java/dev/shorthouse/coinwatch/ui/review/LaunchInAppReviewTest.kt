package dev.shorthouse.coinwatch.ui.review

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.testing.FakeReviewManager
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LaunchInAppReviewTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun whenFakeReviewManagerCompletes_shouldReturnTrue() = runBlocking {
        val reviewManager = FakeReviewManager(composeTestRule.activity)

        val result = launchInAppReview(
            activity = composeTestRule.activity,
            reviewManager = reviewManager
        )

        assertThat(result).isTrue()
    }

    @Test
    fun whenRequestReviewFails_shouldReturnFalse() = runBlocking {
        val reviewManager = FailingRequestReviewManager()

        val result = launchInAppReview(
            activity = composeTestRule.activity,
            reviewManager = reviewManager
        )

        assertThat(result).isFalse()
    }

    @Test
    fun whenLaunchReviewFails_shouldReturnFalse() = runBlocking {
        val reviewManager = FailingLaunchReviewManager(composeTestRule.activity)

        val result = launchInAppReview(
            activity = composeTestRule.activity,
            reviewManager = reviewManager
        )

        assertThat(result).isFalse()
    }

    private class FailingRequestReviewManager : ReviewManager {
        override fun requestReviewFlow(): Task<ReviewInfo> {
            return Tasks.forException(RuntimeException("Review request failed"))
        }

        override fun launchReviewFlow(
            activity: Activity,
            reviewInfo: ReviewInfo,
        ): Task<Void> {
            return Tasks.forResult(null)
        }
    }

    private class FailingLaunchReviewManager(
        context: Activity,
    ) : FakeReviewManager(context) {
        override fun launchReviewFlow(
            activity: Activity,
            reviewInfo: ReviewInfo,
        ): Task<Void> {
            return Tasks.forException(RuntimeException("Review launch failed"))
        }
    }
}
