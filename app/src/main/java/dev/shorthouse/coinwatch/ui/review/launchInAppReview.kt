package dev.shorthouse.coinwatch.ui.review

import android.app.Activity
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.CancellationException

suspend fun launchInAppReview(activity: Activity): Boolean {
    return launchInAppReview(
        activity = activity,
        reviewManager = ReviewManagerFactory.create(activity)
    )
}

internal suspend fun launchInAppReview(
    activity: Activity,
    reviewManager: ReviewManager,
): Boolean {
    return try {
        val reviewInfo = reviewManager.requestReview()
        reviewManager.launchReview(activity, reviewInfo)
        true
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        false
    }
}
