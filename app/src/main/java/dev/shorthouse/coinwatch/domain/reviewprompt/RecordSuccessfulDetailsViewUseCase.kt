package dev.shorthouse.coinwatch.domain.reviewprompt

import dev.shorthouse.coinwatch.data.repository.reviewprompt.ReviewPromptRepository
import javax.inject.Inject

class RecordSuccessfulDetailsViewUseCase @Inject constructor(
    private val reviewPromptRepository: ReviewPromptRepository
) {
    suspend operator fun invoke() {
        return recordSuccessfulDetailsView()
    }

    private suspend fun recordSuccessfulDetailsView() {
        return reviewPromptRepository.recordSuccessfulDetailsView()
    }
}
