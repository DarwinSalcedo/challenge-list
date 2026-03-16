package com.events.challenge.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.events.challenge.presentation.ItemUiState
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ComponentsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingView_displaysLoadingTextAndIndicator() {
        composeTestRule.setContent {
            LoadingView(innerPadding = PaddingValues(0.dp))
        }

        composeTestRule.onNodeWithText("Loading items...").assertIsDisplayed()
    }

    @Test
    fun errorView_displaysErrorMessageAndRetryButton() {
        val errorMessage = "Something went extremely wrong!"
        var retryClicked = false

        composeTestRule.setContent {
            ErrorView(
                innerPadding = PaddingValues(0.dp),
                state = ItemUiState.Error(errorMessage),
                onRetry = { retryClicked = true }
            )
        }

        composeTestRule.onNodeWithText("Error: $errorMessage").assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()
        
        assertTrue(retryClicked)
    }

    @Test
    fun itemView_displaysTitleAndDescriptionAndHandlesClicks() {
        val testTitle = "Item"
        val testDescription = "This is a description."
        var itemClicked = false

        composeTestRule.setContent {
            ItemView(
                title = testTitle,
                description = testDescription,
                onClick = { itemClicked = true }
            )
        }

        composeTestRule.onNodeWithText(testTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(testDescription).assertIsDisplayed()

        composeTestRule.onNodeWithText(testTitle).performClick()

        assertTrue(itemClicked)
    }
}
