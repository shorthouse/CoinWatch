package dev.shorthouse.coinwatch.e2e.support

import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText

const val E2E_DEFAULT_TIMEOUT_MILLIS = 5_000L

fun ComposeTestRule.awaitText(
    text: String,
    timeoutMillis: Long = E2E_DEFAULT_TIMEOUT_MILLIS,
    useUnmergedTree: Boolean = false,
) {
    waitUntil(timeoutMillis) {
        onAllNodesWithText(
            text = text,
            useUnmergedTree = useUnmergedTree,
        ).fetchSemanticsNodes().isNotEmpty()
    }
}

fun ComposeTestRule.awaitTextGone(
    text: String,
    timeoutMillis: Long = E2E_DEFAULT_TIMEOUT_MILLIS,
    useUnmergedTree: Boolean = false,
) {
    waitUntil(timeoutMillis) {
        onAllNodesWithText(
            text = text,
            useUnmergedTree = useUnmergedTree,
        ).fetchSemanticsNodes().isEmpty()
    }
}

fun ComposeTestRule.awaitSelectedText(
    text: String,
    timeoutMillis: Long = E2E_DEFAULT_TIMEOUT_MILLIS,
    useUnmergedTree: Boolean = false,
) {
    waitUntil(timeoutMillis) {
        onAllNodes(
            matcher = hasText(text).and(isSelected()),
            useUnmergedTree = useUnmergedTree,
        ).fetchSemanticsNodes().isNotEmpty()
    }
}

fun ComposeTestRule.awaitContentDescription(
    contentDescription: String,
    timeoutMillis: Long = E2E_DEFAULT_TIMEOUT_MILLIS,
    useUnmergedTree: Boolean = false,
) {
    waitUntil(timeoutMillis) {
        onAllNodes(
            matcher = hasContentDescription(contentDescription),
            useUnmergedTree = useUnmergedTree,
        ).fetchSemanticsNodes().isNotEmpty()
    }
}
