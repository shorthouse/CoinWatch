package dev.shorthouse.coinwatch.e2e.support

import androidx.test.core.app.ActivityScenario
import dev.shorthouse.coinwatch.MainActivity

/**
 * Use with createEmptyComposeRule() when fake network or DataStore state must be staged before
 * MainActivity is created. Tests that can use default initial state should keep using
 * createAndroidComposeRule<MainActivity>().
 */
fun launchMainActivityAfterPreLaunchSetup(): ActivityScenario<MainActivity> {
    return ActivityScenario.launch(MainActivity::class.java)
}
