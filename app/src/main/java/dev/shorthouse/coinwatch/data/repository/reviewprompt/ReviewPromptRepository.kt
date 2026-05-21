package dev.shorthouse.coinwatch.data.repository.reviewprompt

interface ReviewPromptRepository {
    suspend fun recordSuccessfulDetailsView()

    suspend fun recordFavouriteAdded()

    suspend fun recordReviewPromptAttempted()

    suspend fun isReviewPromptEligible(): Boolean
}
