package dev.shorthouse.coinwatch.domain.reviewprompt

import dev.shorthouse.coinwatch.data.repository.reviewPrompt.ReviewPromptRepository
import javax.inject.Inject

class IsReviewPromptEligibleUseCase @Inject constructor(
    private val reviewPromptRepository: ReviewPromptRepository,
) {
    suspend operator fun invoke(): Boolean {
        return isReviewPromptEligible()
    }

    private suspend fun isReviewPromptEligible(): Boolean {
        return reviewPromptRepository.isReviewPromptEligible()
    }
}
