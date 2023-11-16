package dev.shorthouse.coinwatch.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CurrencyPound
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import dev.shorthouse.coinwatch.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class AppBottomSheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_appBottomSheetDisplayed_should_showExpectedContent() {
        composeTestRule.setContent {
            AppTheme {
                AppBottomSheet(
                    title = "Title",
                    onDismissRequest = {},
                    sheetState = rememberModalBottomSheetState(),
                    content = {}
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("Title").assertIsDisplayed()
        }
    }

    @Test
    fun when_appBottomSheetHasContent_should_showContent() {
        composeTestRule.setContent {
            AppTheme {
                AppBottomSheet(
                    title = "",
                    onDismissRequest = {},
                    sheetState = rememberModalBottomSheetState(),
                    content = {
                        BottomSheetOption(
                            icon = Icons.Rounded.AttachMoney,
                            label = "USD",
                            isSelected = true,
                            onSelected = {}
                        )
                        BottomSheetOption(
                            icon = Icons.Rounded.CurrencyPound,
                            label = "GBP",
                            isSelected = false,
                            onSelected = {}
                        )
                    }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("USD").assertIsDisplayed().assertIsSelected()
            onNodeWithText("GBP").assertIsDisplayed().assertIsNotSelected()
        }
    }

    @Test
    fun when_clickingBottomSheetOption_should_callOnSelected() {
        var onSelectedCalled = false

        composeTestRule.setContent {
            AppTheme {
                AppBottomSheet(
                    title = "",
                    onDismissRequest = {},
                    sheetState = rememberModalBottomSheetState(),
                    content = {
                        BottomSheetOption(
                            icon = Icons.Rounded.AttachMoney,
                            label = "USD",
                            isSelected = true,
                            onSelected = { onSelectedCalled = true }
                        )
                    }
                )
            }
        }

        composeTestRule.apply {
            onNodeWithText("USD").performClick()
        }

        assertThat(onSelectedCalled).isTrue()
    }
}
