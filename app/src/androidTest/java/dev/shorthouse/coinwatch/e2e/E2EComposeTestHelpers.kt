package dev.shorthouse.coinwatch.e2e

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.test.core.app.ActivityScenario
import dev.shorthouse.coinwatch.MainActivity

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

/**
 * Use with createEmptyComposeRule() when fake network or DataStore state must be staged before
 * MainActivity is created. Tests that can use default initial state should keep using
 * createAndroidComposeRule<MainActivity>().
 */
fun launchMainActivityAfterPreLaunchSetup(): ActivityScenario<MainActivity> {
    return ActivityScenario.launch(MainActivity::class.java)
}
