package dev.shorthouse.coinwatch.ui.screen.details.component

import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.junit.Rule
import org.junit.Test

class AboutCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_coinDetailsProvided_should_displayExpectedAboutValues() {
        composeTestRule.setContent {
            AppTheme {
                AboutCard(
                    description = defaultDescription,
                    tags = defaultTags,
                    listedDate = defaultListedDate
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .assertIsDisplayed()
            onNodeWithText("Tags").assertIsDisplayed()
            onNodeWithText("smart-contracts").assertIsDisplayed()
            onNodeWithText("staking").assertIsDisplayed()
            onNodeWithText("layer-1").assertIsDisplayed()
            onNodeWithText("Listed Date").assertIsDisplayed()
            onNodeWithText("7 Aug 2015").assertIsDisplayed()
        }
    }

    @Test
    fun when_descriptionAndTagsAreEmpty_should_onlyDisplayListedDate() {
        composeTestRule.setContent {
            AppTheme {
                AboutCard(
                    description = "",
                    tags = persistentListOf(),
                    listedDate = defaultListedDate
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Ethereum is a decentralized blockchain with smart contract functionality.")
                .assertDoesNotExist()
            onNodeWithText("Tags").assertDoesNotExist()
            onNodeWithText("Listed Date").assertIsDisplayed()
            onNodeWithText("7 Aug 2015").assertIsDisplayed()
        }
    }

    @Test
    fun when_descriptionIsLong_should_expandAndCollapseWithChevron() {
        val longDescription = """
            Ethereum is a decentralized blockchain with smart contract functionality. It enables developers to build
            applications that run without a central authority. The network supports token standards, decentralized finance,
            games, identity systems, and other programmable financial tools across a global settlement layer.
        """.trimIndent().replace("\n", " ")

        composeTestRule.setContent {
            AppTheme {
                AboutCard(
                    description = longDescription,
                    tags = defaultTags,
                    listedDate = defaultListedDate,
                    modifier = Modifier.width(220.dp)
                )
            }
        }

        composeTestRule.waitForIdle()

        val collapsedHeight = composeTestRule.onNodeWithText(longDescription)
            .getUnclippedBoundsInRoot()
            .let { bounds -> bounds.bottom - bounds.top }

        composeTestRule.onNodeWithContentDescription("Expand about description")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitForIdle()

        val expandedHeight = composeTestRule.onNodeWithText(longDescription)
            .getUnclippedBoundsInRoot()
            .let { bounds -> bounds.bottom - bounds.top }

        assertThat(expandedHeight).isGreaterThan(collapsedHeight)

        composeTestRule.onNodeWithContentDescription("Collapse about description")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithContentDescription("Expand about description")
            .assertIsDisplayed()
    }

    @Test
    fun when_tagsWrap_should_keepAllTagsVisible() {
        val tags = persistentListOf(
            "decentralized-finance",
            "zero-knowledge",
            "smart-contracts",
            "staking"
        )

        composeTestRule.setContent {
            AppTheme {
                AboutCard(
                    description = defaultDescription,
                    tags = tags,
                    listedDate = defaultListedDate,
                    modifier = Modifier.width(180.dp)
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("decentralized-finance").assertIsDisplayed()
            onNodeWithText("staking").assertIsDisplayed()
        }

        val firstTagTop = composeTestRule.onNodeWithText("decentralized-finance")
            .getUnclippedBoundsInRoot()
            .top
        val lastTagTop = composeTestRule.onNodeWithText("staking")
            .getUnclippedBoundsInRoot()
            .top

        assertThat(lastTagTop).isGreaterThan(firstTagTop)
    }

    @Test
    fun when_aboutCardDisplayed_should_showContentInExpectedOrder() {
        composeTestRule.setContent {
            AppTheme {
                AboutCard(
                    description = defaultDescription,
                    tags = defaultTags,
                    listedDate = defaultListedDate
                )
            }
        }

        val labels = listOf(
            "Ethereum is a decentralized blockchain with smart contract functionality.",
            "Tags",
            "Listed Date"
        )
        val labelTops = labels.map { label ->
            composeTestRule.onNodeWithText(label)
                .getUnclippedBoundsInRoot()
                .top
        }

        assertThat(labelTops).isEqualTo(labelTops.sorted())
    }

    private val defaultDescription =
        "Ethereum is a decentralized blockchain with smart contract functionality."
    private val defaultTags: ImmutableList<String> = persistentListOf(
        "smart-contracts",
        "staking",
        "layer-1"
    )
    private val defaultListedDate = "7 Aug 2015"
}
