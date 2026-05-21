package dev.shorthouse.coinwatch.domain.reviewprompt

import dev.shorthouse.coinwatch.data.repository.reviewprompt.ReviewPromptRepository
import javax.inject.Inject

class RecordFavouriteAddedUseCase @Inject constructor(
    private val reviewPromptRepository: ReviewPromptRepository
) {
    suspend operator fun invoke() {
        return recordFavouriteAdded()
    }

    private suspend fun recordFavouriteAdded() {
        return reviewPromptRepository.recordFavouriteAdded()
    }
}
