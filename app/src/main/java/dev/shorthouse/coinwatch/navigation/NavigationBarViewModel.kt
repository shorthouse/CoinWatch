package dev.shorthouse.coinwatch.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shorthouse.coinwatch.domain.reviewprompt.IsReviewPromptEligibleUseCase
import dev.shorthouse.coinwatch.domain.reviewprompt.RecordReviewPromptAttemptedUseCase
import javax.inject.Inject

@HiltViewModel
class NavigationBarViewModel @Inject constructor(
    private val isReviewPromptEligibleUseCase: IsReviewPromptEligibleUseCase,
    private val recordReviewPromptAttemptedUseCase: RecordReviewPromptAttemptedUseCase,
) : ViewModel() {

    suspend fun isReviewPromptEligible(): Boolean = isReviewPromptEligibleUseCase()

    suspend fun recordReviewPromptAttempted() = recordReviewPromptAttemptedUseCase()
}
