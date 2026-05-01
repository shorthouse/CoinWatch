package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.model.CoinLink
import dev.shorthouse.coinwatch.model.CoinLinkType
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class LinksCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_linksProvided_should_displayLabelsAndLaunchIcons() {
        composeTestRule.setContent {
            AppTheme {
                LinksCard(
                    links = defaultLinks,
                    onClickLink = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Website").assertIsDisplayed()
            onNodeWithText("GitHub").assertIsDisplayed()
            onNodeWithText("Reddit").assertIsDisplayed()
            onNodeWithContentDescription("Open Website").assertIsDisplayed()
            onNodeWithContentDescription("Open GitHub").assertIsDisplayed()
            onNodeWithContentDescription("Open Reddit").assertIsDisplayed()
        }
    }

    @Test
    fun when_linkClicked_should_callOnClickLinkWithExpectedUrl() {
        var clickedUrl: String? = null

        composeTestRule.setContent {
            AppTheme {
                LinksCard(
                    links = defaultLinks,
                    onClickLink = { url -> clickedUrl = url }
                )
            }
        }

        composeTestRule.onNodeWithText("GitHub").performClick()

        assertThat(clickedUrl).isEqualTo("https://github.com/ethereum")
    }

    @Test
    fun when_allSupportedLinkTypesProvided_should_displayExpectedLabels() {
        composeTestRule.setContent {
            AppTheme {
                LinksCard(
                    links = persistentListOf(
                        CoinLink(type = CoinLinkType.Website, url = "https://ethereum.org"),
                        CoinLink(type = CoinLinkType.Discord, url = "https://discord.gg/ethereum"),
                        CoinLink(type = CoinLinkType.Facebook, url = "https://facebook.com/ethereum"),
                        CoinLink(type = CoinLinkType.GitHub, url = "https://github.com/ethereum"),
                        CoinLink(type = CoinLinkType.Instagram, url = "https://instagram.com/ethereum"),
                        CoinLink(type = CoinLinkType.Reddit, url = "https://reddit.com/r/ethereum"),
                        CoinLink(type = CoinLinkType.Telegram, url = "https://t.me/ethereum"),
                        CoinLink(type = CoinLinkType.Whitepaper, url = "https://ethereum.org/whitepaper")
                    ),
                    onClickLink = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Website").assertIsDisplayed()
            onNodeWithText("Discord").assertIsDisplayed()
            onNodeWithText("Facebook").assertIsDisplayed()
            onNodeWithText("GitHub").assertIsDisplayed()
            onNodeWithText("Instagram").assertIsDisplayed()
            onNodeWithText("Reddit").assertIsDisplayed()
            onNodeWithText("Telegram").assertIsDisplayed()
            onNodeWithText("Whitepaper").assertIsDisplayed()
        }
    }

    @Test
    fun when_linksAreEmpty_should_notCrashOrDisplayRows() {
        composeTestRule.setContent {
            AppTheme {
                LinksCard(
                    links = persistentListOf(),
                    onClickLink = {}
                )
            }
        }

        composeTestRule.onAllNodesWithText("Website").assertCountEquals(0)
    }

    private val defaultLinks = persistentListOf(
        CoinLink(
            type = CoinLinkType.Website,
            url = "https://ethereum.org",
        ),
        CoinLink(
            type = CoinLinkType.GitHub,
            url = "https://github.com/ethereum",
        ),
        CoinLink(
            type = CoinLinkType.Reddit,
            url = "https://reddit.com/r/ethereum",
        )
    )
}
