package dev.shorthouse.coinwatch.ui.assertion

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import com.google.common.truth.Truth.assertThat

fun ComposeTestRule.assertTextContaining(text: String) {
    assertThat(displayedTextsContaining(text)).isNotEmpty()
}

fun ComposeTestRule.assertNoTextContaining(text: String) {
    assertThat(displayedTextsContaining(text)).isEmpty()
}

fun ComposeTestRule.assertCurrencyAfterAmount(amountText: String) {
    val displayedText = displayedTextContaining(amountText)

    assertThat(displayedText.currencyRepresentationIndex())
        .isGreaterThan(displayedText.indexOf(amountText))
}

fun ComposeTestRule.displayedTextContaining(text: String): String {
    return displayedTextsContaining(text).first()
}

fun ComposeTestRule.displayedTextsContaining(text: String): List<String> {
    return onAllNodesWithText(text, substring = true)
        .fetchSemanticsNodes()
        .mapNotNull { node ->
            node.config.getOrNull(SemanticsProperties.Text)
                ?.joinToString(separator = "") { it.text }
        }
}

fun String.currencyRepresentationIndex(): Int {
    return listOf("$", "£", "€", "USD", "GBP", "EUR")
        .map { indexOf(it) }
        .filter { it >= 0 }
        .minOrNull()
        ?: -1
}
